package net.sf.fc.gui.v.copy.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.undo.UndoManager;

import net.sf.fc.gui.v.edit.XMLTreeModelRemoveUndoableEdit;
import net.sf.fc.gui.v.tree.XMLTreeNode;

@SuppressWarnings("serial")
public class OptionsPopupMenu extends CopyScriptPopupMenu {

    public OptionsPopupMenu(JTree tree, UndoManager undoManager, boolean isSrc) {
        super(tree, undoManager);
        add(createRemoveOptionsItem(isSrc));
    }

    private JMenuItem createRemoveOptionsItem(boolean isSrc) {
        JMenuItem mnuItm = createMenuItem(isSrc ? MenuItemData.REMOVE_SRC_OPTIONS : MenuItemData.REMOVE_DST_OPTIONS);
        mnuItm.addActionListener(new ActionListener() {

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

        return mnuItm;
    }

}
