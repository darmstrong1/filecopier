package net.sf.fc.gui.v.copy.menu;

import javax.swing.JTree;
import javax.swing.undo.UndoManager;

@SuppressWarnings("serial")
public class NoPopupMenu extends CopyScriptPopupMenu {

    public NoPopupMenu(JTree tree, UndoManager undoManager) {
        super(tree, undoManager);
    }

}
