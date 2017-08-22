package net.sf.fc.gui.v.copy.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.undo.UndoManager;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import net.sf.fc.gui.c.copy.CopyController;
import net.sf.fc.gui.c.copy.CopyProperty;
import net.sf.fc.gui.v.View;
import net.sf.fc.gui.v.edit.XMLTreeModelInsertUndoableEdit;
import net.sf.fc.gui.v.edit.XMLTreeModelRemoveUndoableEdit;
import net.sf.fc.gui.v.tree.CopyScriptXMLTreeNode;
import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.gui.v.tree.XMLTreeNode;
import net.sf.fc.gui.v.util.JTreeUtil;
import net.sf.fc.script.gen.copy.Type;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.util.CopyScriptXmlUtil;

@SuppressWarnings("serial")
public class PathsPopupMenu extends CopyScriptPopupMenu implements View {

    private final CopyController copyController;
    private final List<JMenuItem> menuItems;
    private final String parentName;
    private final String pathType;
    private final MenuItemData mnuItmDataAdd;
    private final MenuItemData mnuItmDataRemove;
    private final MenuItemData mnuItmDataAddOptions;
    private boolean getLastPath = false;
    private boolean getOptions = false;

    public PathsPopupMenu(CopyController copyController, Type type, JTree tree, UndoManager undoManager) {
        super(tree, undoManager);
        this.copyController = copyController;
        if(type == null) {
            parentName = "src";
            pathType = "source";
            mnuItmDataAdd = MenuItemData.ADD_SRC_PATH;
            mnuItmDataRemove = MenuItemData.REMOVE_SRC_PATHS;
            mnuItmDataAddOptions = MenuItemData.ADD_SRC_OPTIONS;
        } else {
            parentName = "dst";
            pathType = "destination";
            mnuItmDataAdd = type.equals(Type.DIRECTORY) ? MenuItemData.ADD_DST_DIR_PATH : MenuItemData.ADD_DST_FILE_PATH;
            mnuItmDataRemove = MenuItemData.REMOVE_DST_PATHS;
            mnuItmDataAddOptions = MenuItemData.ADD_DST_OPTIONS;
        }
        menuItems = createMenuItems();
        for(JMenuItem item : menuItems) {
            add(item);
        }

        copyController.addView(this);
    }

    public PathsPopupMenu(CopyController copyController, JTree tree, UndoManager undoManager) {
        this(copyController, null, tree, undoManager);
    }

    public void setTreeNode(XMLTreeNode treeNode) {
        if(!isNodePath(treeNode)) {
            throw new IllegalArgumentException("XMLTreeNode must be a " + pathType + " path.");
        }
        this.treeNode = treeNode;

        // See if we should make the remove menu item visible. If the path has no siblings, the item should not be visible.
        XMLTreeNode parentNode = treeNode.getParent();
        List<XMLTreeNode> children = parentNode.children();
        int ct = 0;
        for(XMLTreeNode child : children) {
            if("paths".equals(child.toString())) ct++;
        }
        menuItems.get(mnuItmDataRemove.getIdx()).setVisible(ct > 1);

        // See if we already have an options element. If we do, we should not show the add options menu item.
        boolean showOptions = true;
        for(XMLTreeNode child : treeNode.children()) {
            if("options".equals(child.toString())) {
                showOptions = false;
                break;
            }
        }
        menuItems.get(mnuItmDataAddOptions.getIdx()).setVisible(showOptions);
    }

    private boolean isNodePath(XMLTreeNode treeNode) {
        return treeNode.getNode() instanceof Element &&
                treeNode.toString().equals("paths") &&
                treeNode.getParent().toString().equals(parentName);
    }

    private List<JMenuItem> createMenuItems() {
        List<JMenuItem> menuItems = new ArrayList<>();
        menuItems.add(createAddDstPathItem());
        menuItems.add(createRemoveDstPathsItem());
        menuItems.add(createAddOptionsItem());
        return menuItems;
    }

    private JMenuItem createAddDstPathItem() {
        JMenuItem menuItem = createMenuItem(mnuItmDataAdd);
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Set getLastPath to true. modelPropertyChange will only update the tree if this value is true. This view
                // will get the LastSrcDirPath/LastDstDirPath model property change event if the value was set in the edit
                // panel and in that case, we don't want to update the tree because it has already been updated.
                getLastPath = true;
                if("src".equals(parentName)) {
                    copyController.retrieveLastSrcDirPath();
                } else {
                    copyController.retrieveLastDstDirPath();
                }
            }
        });

        return menuItem;
    }

    private JMenuItem createRemoveDstPathsItem() {
        JMenuItem menuItem = createMenuItem(mnuItmDataRemove);
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                XMLTreeNode parent = treeNode.getParent();
                int idx = 0;
                for(XMLTreeNode child : parent.children()) {
                    if(child == treeNode) break;
                    idx++;
                }
                treeModel.removeNodeFromParent(treeNode);

                // Select the parent.
                TreePath path = new TreePath(parent.getPath());
                tree.setSelectionPath(path);

                undoManager.addEdit(new XMLTreeModelRemoveUndoableEdit(tree, parent, treeNode, idx));
            }
        });

        return menuItem;
    }

    private JMenuItem createAddOptionsItem() {
        JMenuItem menuItem = createMenuItem(mnuItmDataAddOptions);
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getOptions = true;
                copyController.retrieveDefaultOptions();
            }
        });

        return menuItem;
    }


    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        try {
            CopyProperty cpProperty = CopyProperty.valueOf(evt.getPropertyName());
            switch(cpProperty) {
            case LastSrcDirPath:
            case LastDstDirPath:
                if(getLastPath) {
                    getLastPath = false;
                    Node node = treeNode.getNode();

                    Node pathXmlNode = CopyScriptXmlUtil.createPathElement(node.getOwnerDocument(), (String)evt.getNewValue());
                    XMLTreeNode pathTreeNode = new CopyScriptXMLTreeNode(treeNode, pathXmlNode);

                    int idx = treeNode.getChildCount();
                    treeModel.insertNodeInto(pathTreeNode, treeNode, idx);

                    TreePath path = new TreePath(pathTreeNode.getPath());
                    JTreeUtil.expandAll(tree, path);
                    // Select the path value so the user can quickly change it.
                    TreePath valPath = new TreePath(pathTreeNode.getChildAt(0).getPath());
                    tree.setSelectionPath(valPath);
                    tree.scrollPathToVisible(valPath);

                    undoManager.addEdit(new XMLTreeModelInsertUndoableEdit(tree, treeNode, pathTreeNode, idx));
                }
                break;

            case DefaultOptions:
            case CachedDefaultOptions:
                if(getOptions) {
                    getOptions = false;

                    Node node = treeNode.getNode();
                    Node optXmlNode = CopyScriptXmlUtil.createOptionsElement(node.getOwnerDocument(), (OptionsScript)evt.getNewValue());
                    XMLTreeNode optTreeNode = new CopyScriptXMLTreeNode(treeNode, optXmlNode);

                    treeModel.insertNodeInto(optTreeNode, treeNode, 0);

                    TreePath path = new TreePath(optTreeNode.getPath());
                    JTreeUtil.expandAll(tree, path);
                    tree.setSelectionPath(path);
                    tree.scrollPathToVisible(path);

                    undoManager.addEdit(new XMLTreeModelInsertUndoableEdit(tree, treeNode, optTreeNode, 0));
                }
                break;

            case DefaultOptionsFailure:
                copyController.retrieveCachedDefaultOptions();
                break;

            default:
                break;
            }
        } catch(IllegalArgumentException ignored) {}
    }
}
