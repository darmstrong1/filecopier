package net.sf.fc.gui.v.edit;

import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

@SuppressWarnings("serial")
//public class ComboBoxUndoableEdit extends BasicUndoableEdit {
public class ComboBoxUndoableEdit extends AbstractUndoableEdit {

    private final JComboBox<?> comboBox;
    private final ItemListener itemListener;
    private final Object oldValue;
    private final Object newValue;

    public ComboBoxUndoableEdit(JComboBox<?> comboBox, ItemListener itemListener, Object oldValue, Object newValue) {
        this.comboBox = comboBox;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.itemListener = itemListener;
        ItemListener[] itemListeners = comboBox.getItemListeners();
        boolean itemListenerFound = false;
        for(ItemListener il : itemListeners) {
            if((itemListenerFound = itemListener.equals(il)) == true) {
                break;
            }
        }
        if(!itemListenerFound) throw new IllegalStateException("The JComboBox must contain the ItemListener passed in as one of its ItemListeners");
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        try {
            comboBox.removeItemListener(itemListener);
            comboBox.setSelectedItem(oldValue);
        } finally {
            comboBox.addItemListener(itemListener);
        }
    }

    @Override
    public void redo() throws CannotUndoException {
        super.redo();
        try {
            comboBox.removeItemListener(itemListener);
            comboBox.setSelectedItem(newValue);
        } finally {
            comboBox.addItemListener(itemListener);
        }
    }

}
