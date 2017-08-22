package net.sf.fc.gui.v.edit;

import javax.swing.ButtonModel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

import net.sf.fc.gui.c.FlagChanger;

@SuppressWarnings("serial")
//public class CheckBoxUndoableEdit extends BasicUndoableEdit {
public class CheckBoxUndoableEdit extends AbstractUndoableEdit {
    private final ButtonModel btnModel;
    private final boolean selected;
    private final FlagChanger flagChanger;

    public CheckBoxUndoableEdit(ButtonModel btnModel, boolean selected, FlagChanger flagChanger) {
        this.btnModel = btnModel;
        this.selected = selected;
        this.flagChanger = flagChanger;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        btnModel.setSelected(!selected);
        flagChanger.changeFlag(!selected);
    }

    @Override
    public void redo() throws CannotUndoException {
        super.redo();
        btnModel.setSelected(selected);
        flagChanger.changeFlag(selected);
    }

}
