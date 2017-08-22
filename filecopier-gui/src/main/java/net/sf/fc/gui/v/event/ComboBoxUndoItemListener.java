package net.sf.fc.gui.v.event;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.undo.UndoManager;

import net.sf.fc.gui.v.edit.ComboBoxUndoableEdit;

/**
 * ItemListener for JComboBoxes that creates a ComboBoxUndoableEdit when the JComboBox's selected item is
 * changed.
 * @author David Armstrong
 *
 */
public class ComboBoxUndoItemListener implements ItemListener {

    private final JComboBox<?> comboBox;
    private final UndoManager undoManager;
    private Object oldValue;
    private Object newValue;

    public ComboBoxUndoItemListener(JComboBox<?> comboBox, UndoManager undoManager) {
        this.comboBox = comboBox;
        this.undoManager = undoManager;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(ItemEvent.DESELECTED == e.getStateChange()) {
            oldValue = e.getItem();
        } else if(ItemEvent.SELECTED == e.getStateChange()) {
            newValue = e.getItem();
            undoManager.addEdit(new ComboBoxUndoableEdit(comboBox, this, oldValue, newValue));
        }
    }

}
