package net.sf.fc.gui.v.edit;

import javax.swing.undo.AbstractUndoableEdit;

@SuppressWarnings("serial")
public class BasicUndoableEdit extends AbstractUndoableEdit {

    @Override
    public boolean canUndo() { return true; }
    @Override
    public boolean canRedo() { return true; }

}
