package net.sf.fc.gui.v.copy.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import net.sf.fc.gui.v.copy.menu.CopyScriptPopupMenu;
import net.sf.fc.gui.v.tree.XMLTreeNode;

public class CopyScriptPopupListener extends MouseAdapter {

    private final CopyScriptPopupMenu popupMenu;

    public CopyScriptPopupListener(CopyScriptPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if(e.isPopupTrigger()) {
            JTree tree = (JTree)e.getSource();
            int x = e.getX();
            int y = e.getY();
            // Set selPath to the path at the mouse's location.
            TreePath selPath = tree.getPathForLocation(x, y);
            // See if selPath is selected.
            if(!tree.isPathSelected(selPath)) {
                // Since selPath is not selected, select it if it is not null. If it is null, select the root node.
                tree.setSelectionPath(selPath != null ? selPath : new TreePath(tree.getModel().getRoot()));
            }
            // The popup menu will be for the path on which the mouse is, or the root if the mouse was not on a path.
            //popupMenu.show(e.getComponent(), x, y);
            XMLTreeNode treeNode = (XMLTreeNode)selPath.getLastPathComponent();
            popupMenu.setTreeNode(treeNode);
            popupMenu.show(e.getComponent(), x, y);
//            if(xmlNode.getNode() instanceof Element) {
//                if(xmlNode.toString().equals("path")) {
//                    XMLTreeNode xmlParent = xmlNode.getParent();
//                    if(xmlParent.toString().equals("dst")) {
//                        CopyScriptPopupMenu popupMenu = new PathsPopupMenu(Type.DIRECTORY, xmlNode);
//                        popupMenu.show(e.getComponent(), x, y);
//                    }
//                }
//            }
        }
    }
}


