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

import net.sf.fc.cfg.RequestFactory;
import net.sf.fc.gui.c.copy.CopyController;
import net.sf.fc.gui.c.copy.CopyProperty;
import net.sf.fc.gui.v.View;
import net.sf.fc.gui.v.edit.XMLTreeModelInsertUndoableEdit;
import net.sf.fc.gui.v.tree.CopyScriptXMLTreeNode;
import net.sf.fc.gui.v.tree.XMLTreeNode;
import net.sf.fc.gui.v.util.JTreeUtil;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.util.CopyScriptXmlUtil;

import org.w3c.dom.Node;

@SuppressWarnings("serial")
public class DstOrSrcPopupMenu extends CopyScriptPopupMenu implements View {

    private final List<JMenuItem> menuItems;
    private final CopyController copyController;
    private final String parentName;
    private final String pathType;
    private final MenuItemData mnuItmData;
    private boolean getOptions = false;
    private OptionsScript defaultOptions;
    private boolean getPath = false;

    public DstOrSrcPopupMenu(CopyController copyController, JTree tree, UndoManager undoManager, boolean isSrc) {
        super(tree, undoManager);
        this.copyController = copyController;
        if(isSrc) {
            parentName = "src";
            pathType = "source";
            mnuItmData = MenuItemData.ADD_SRC_PATHS;
        } else {
            parentName = "dst";
            pathType = "destination";
            mnuItmData = MenuItemData.ADD_DST_PATHS;
        }
        menuItems = createMenuItems();
        for(JMenuItem item : menuItems) {
            add(item);
        }
        copyController.addView(this);
    }

    private List<JMenuItem> createMenuItems() {
        List<JMenuItem> menuItems = new ArrayList<>();
        menuItems.add(createAddDstPathsItem());
        //menuItems.add(createMenuItem(MenuItemData.REMOVE_DST_PATH));
        return menuItems;
    }

    @Override
    public void setTreeNode(XMLTreeNode treeNode) {
        super.setTreeNode(treeNode);
        if(!parentName.equals(treeNode.toString())) {
            throw new IllegalArgumentException("XMLTreeNode must be a " + pathType + " value path.");
        }
    }

    private JMenuItem createAddDstPathsItem() {
        JMenuItem item = createMenuItem(mnuItmData);
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                getOptions = true;
                copyController.retrieveDefaultOptions();

                getPath = true;
                if("source".equals(pathType)) {
                    copyController.retrieveLastSrcDirPath();
                } else {
                    copyController.retrieveLastDstDirPath();
                }
            }
        });

        return item;
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        try {
            CopyProperty cpProperty = CopyProperty.valueOf(evt.getPropertyName());
            switch(cpProperty) {
            case DefaultOptions:
            case CachedDefaultOptions:
                if(getOptions) {
                    getOptions = false;
                    defaultOptions = (OptionsScript)evt.getNewValue();
                }
                break;

            case DefaultOptionsFailure:
                copyController.retrieveCachedDefaultOptions();
                break;

            case LastSrcDirPath:
            case LastDstDirPath:
                if(getPath == true) {
                    getPath = false;

                    Node node = treeNode.getNode();
                    Node pathsXmlNode = CopyScriptXmlUtil.createPathsElement(node.getOwnerDocument(), (String)evt.getNewValue(), defaultOptions);
                    XMLTreeNode pathsTreeNode = new CopyScriptXMLTreeNode(treeNode, pathsXmlNode);

                    int idx = treeNode.getChildCount();
                    treeModel.insertNodeInto(pathsTreeNode, treeNode, idx);

                    TreePath path = new TreePath(pathsTreeNode.getPath());
                    JTreeUtil.setExpandScrollTo(tree, path);

                    undoManager.addEdit(new XMLTreeModelInsertUndoableEdit(tree, treeNode, pathsTreeNode, idx));
                }
            }

        } catch(IllegalArgumentException ignored){}
    }
}
