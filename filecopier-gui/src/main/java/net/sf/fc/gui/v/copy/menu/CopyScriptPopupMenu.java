package net.sf.fc.gui.v.copy.menu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.undo.UndoManager;

import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.gui.v.tree.XMLTreeNode;

@SuppressWarnings("serial")
public class CopyScriptPopupMenu extends JPopupMenu {

    protected XMLTreeNode treeNode;
    protected final JTree tree;
    protected final XMLTreeModel treeModel;
    protected final UndoManager undoManager;

    public CopyScriptPopupMenu(JTree tree, UndoManager undoManager) {
        this.tree = tree;
        this.treeModel = (XMLTreeModel)tree.getModel();
        this.undoManager = undoManager;
    }

    public void setTreeNode(XMLTreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public JMenuItem createMenuItem(MenuItemData mnItmDta) {
        if(mnItmDta == null) throw new IllegalArgumentException("MenuItemData object cannot be null");
        JMenuItem menuItem = new JMenuItem(mnItmDta.getText());
        menuItem.setActionCommand(mnItmDta.getCmd());
        menuItem.setMnemonic(mnItmDta.getMnemonic());
        menuItem.setToolTipText(mnItmDta.getToolTip());
        return menuItem;
    }

    public void changeMenuItem(JMenuItem mnItm, MenuItemData mnItmDta) {
        if(mnItm == null) throw new IllegalArgumentException("JMenuItem object cannot be null");
        if(mnItmDta == null) throw new IllegalArgumentException("MenuItemData object cannot be null");
        mnItm.setText(mnItmDta.getText());
        mnItm.setActionCommand(mnItmDta.getCmd());
        mnItm.setMnemonic(mnItmDta.getMnemonic());
        mnItm.setToolTipText(mnItmDta.getToolTip());
    }
}
