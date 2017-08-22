package net.sf.fc.gui.v.copy.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.undo.UndoManager;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import net.sf.fc.gui.v.edit.XMLTreeModelRemoveUndoableEdit;
import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.gui.v.tree.XMLTreeNode;
import net.sf.fc.script.gen.copy.Type;

@SuppressWarnings("serial")
public class PathPopupMenu extends CopyScriptPopupMenu {

    private final JMenuItem menuItem;
    private final String parentName;
    private final String pathType;
    private final MenuItemData mnuItmData;

    public PathPopupMenu(Type type, JTree tree, UndoManager undoManager) {
        super(tree, undoManager);
        if(type == null) {
            parentName = "src";
            pathType = "source";
            mnuItmData = MenuItemData.REMOVE_SRC_PATH;
        } else {
            parentName = "dst";
            pathType = "destination";
            mnuItmData = type.equals(Type.DIRECTORY) ? MenuItemData.REMOVE_DST_DIR_PATH : MenuItemData.REMOVE_DST_FILE_PATH;
        }
        menuItem = createRemovePathItem();
        add(menuItem);
    }

    public PathPopupMenu(JTree tree, UndoManager undoManager) {
        this(null, tree, undoManager);
    }

    private JMenuItem createRemovePathItem() {
        JMenuItem menuItem = createMenuItem(mnuItmData);
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
                tree.scrollPathToVisible(path);

                undoManager.addEdit(new XMLTreeModelRemoveUndoableEdit(tree, parent, treeNode, idx));
            }
        });
        return menuItem;
    }

    public void setTreeNode(XMLTreeNode treeNode) {
        if(!isNodePathVal(treeNode)) {
            throw new IllegalArgumentException("XMLTreeNode must be a " + pathType + " value path.");
        }
        this.treeNode = treeNode;

        // See if we should make the menu item visible. If the path has no siblings, the item should not be visible.
        XMLTreeNode parent = treeNode.getParent();
        int pathCt = 0;
        for(XMLTreeNode child : parent.children()) {
            if("path".equals(child.toString())) pathCt++;
        }
        menuItem.setVisible(pathCt > 1);
    }

    private boolean isNodePathVal(XMLTreeNode treeNode) {
        XMLTreeNode parent = treeNode.getParent();
        return treeNode.getNode() instanceof Element &&
                treeNode.toString().equals("path") &&
                parent.toString().equals("paths") &&
                parent.getParent().toString().equals(parentName);
    }
}
