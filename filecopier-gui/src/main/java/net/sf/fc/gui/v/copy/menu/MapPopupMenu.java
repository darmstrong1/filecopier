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
import javax.xml.bind.JAXBException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import net.sf.fc.cfg.RequestFactory;
import net.sf.fc.gui.c.copy.CopyController;
import net.sf.fc.gui.c.copy.CopyProperty;
import net.sf.fc.gui.v.View;
import net.sf.fc.gui.v.edit.XMLTreeModelInsertUndoableEdit;
import net.sf.fc.gui.v.edit.XMLTreeModelRemoveUndoableEdit;
import net.sf.fc.gui.v.tree.CopyScriptXMLTreeNode;
import net.sf.fc.gui.v.tree.XMLTreeNode;
import net.sf.fc.gui.v.util.JTreeUtil;
import net.sf.fc.script.gen.copy.Type;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.util.CopyScriptXmlUtil;

@SuppressWarnings("serial")
public class MapPopupMenu extends CopyScriptPopupMenu implements View {

    private final CopyController copyController;
    private final List<JMenuItem> menuItems;
    private Type type;
    private boolean getLastPath = false;

    public MapPopupMenu(CopyController copyController, JTree tree, UndoManager undoManager) {
        super(tree, undoManager);
        this.copyController = copyController;
        menuItems = createMenuItems();
        for(JMenuItem item : menuItems) {
            add(item);
        }

        copyController.addView(this);
    }

    private List<JMenuItem> createMenuItems() {
        List<JMenuItem> menuItems = new ArrayList<>();
        menuItems.add(createAddDstMnuItm());
        menuItems.add(createRemoveMnuItm());
        return menuItems;
    }

    private JMenuItem createAddDstMnuItm() {
        JMenuItem mnItm = createMenuItem(MenuItemData.ADD_DIR_DSTS);
        mnItm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getLastPath = true;
                copyController.retrieveLastDstDirPath();
            }
        });
        return mnItm;
    }

    private JMenuItem createRemoveMnuItm() {
        JMenuItem mnItm = createMenuItem(MenuItemData.REMOVE_MAP);
        mnItm.addActionListener(new ActionListener() {

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
                tree.scrollPathToVisible(path);

                undoManager.addEdit(new XMLTreeModelRemoveUndoableEdit(tree, parent, treeNode, idx));
            }
        });
        return mnItm;
    }

    public void setTreeNode(XMLTreeNode treeNode) {
        if(!isNodeMap(treeNode)) {
            throw new IllegalArgumentException("XMLTreeNode must be a map path.");
        }
        this.treeNode = treeNode;

        // See if we should make the add dsts item visible. If it already has two dsts, it should not be visible.
        List<XMLTreeNode> children = treeNode.children();
        int dstCt = 0;
        for(XMLTreeNode child : children) {
            if("dst".equals(child.toString())) {
                dstCt++;
                if(dstCt == 1) {
                    List<XMLTreeNode> dstChildren = child.children();
                    for(XMLTreeNode dstChild : dstChildren) {
                        if("type".equals(dstChild.toString())) {
                            XMLTreeNode t = dstChild.getChildAt(0);
                            type = "Directory".equals(String.valueOf(t)) ? Type.DIRECTORY : Type.FILE;
                        }
                    }
                }
            }

        }
        JMenuItem addItm = menuItems.get(0);
        addItm.setVisible(dstCt == 1);
        if(dstCt == 1) {
            if(Type.DIRECTORY.equals(type)) {
                changeMenuItem(addItm, MenuItemData.ADD_FILE_DSTS);
                // Make type Type.FILE since that is what we will want to create.
                type = Type.FILE;
            } else {
                changeMenuItem(addItm, MenuItemData.ADD_DIR_DSTS);
                type = Type.DIRECTORY;
            }
        }

        // See if we should make the remove item visible. If this is the only map, it should not be visible.
        XMLTreeNode parent = treeNode.getParent();
        children = parent.children();
        int mapCt = 0;
        for(XMLTreeNode child : children) {
            if("map".equals(child.toString())) mapCt++;
        }
        menuItems.get(1).setVisible(mapCt > 1);
    }

    private boolean isNodeMap(XMLTreeNode treeNode) {
        return treeNode.getNode() instanceof Element &&
                treeNode.toString().equals("map");
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if(getLastPath) {
            getLastPath = false;
            try {
                CopyProperty cpProperty = CopyProperty.valueOf(evt.getPropertyName());
                switch(cpProperty) {
                case LastDstDirPath:
                    Node node = treeNode.getNode();

                    Node dstNode = CopyScriptXmlUtil.createDstElement(node.getOwnerDocument(), type, (String)evt.getNewValue());
                    XMLTreeNode dstTreeNode = new CopyScriptXMLTreeNode(treeNode, dstNode);
                    int idx = treeNode.getChildCount();
                    treeModel.insertNodeInto(dstTreeNode, treeNode, idx);

                    TreePath path = new TreePath(dstTreeNode.getPath());
                    JTreeUtil.setExpandScrollTo(tree, path);

                    undoManager.addEdit(new XMLTreeModelInsertUndoableEdit(tree, treeNode, dstTreeNode, idx));
                    break;
                }

            } catch(IllegalArgumentException ignored){}
        }
    }

}
