package net.sf.fc.gui.v;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;
import javax.swing.undo.UndoManager;

import net.sf.fc.gui.c.FlagChanger;
import net.sf.fc.gui.c.IntegerChanger;
import net.sf.fc.gui.c.TextChanger;

@SuppressWarnings("serial")
public abstract class AbstractViewPanel extends JPanel implements View {

    @Override
    public abstract void modelPropertyChange(PropertyChangeEvent evt);


    protected JCheckBox initCheckBox(final String label, final FlagChanger flagChanger, boolean initialValue) {
        final JCheckBox checkBox = new JCheckBox(label);
        checkBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                flagChanger.changeFlag(checkBox.isSelected());
            }

        });

        checkBox.setSelected(initialValue);

        return checkBox;
    }

    // doClick will not work if the checkbox is not enabled so if it is disabled, enable it, call doClick and disable it.
    // Otherwise, just call doClick.
    protected void clickCheckBox(JCheckBox checkBox) {
        if(!checkBox.isEnabled()) {
            checkBox.setEnabled(true);
            checkBox.doClick();
            checkBox.setEnabled(false);
        } else {
            checkBox.doClick();
        }
    }

    protected JFormattedTextField initFormattedTxtFld(int minimum, int initialValue, final IntegerChanger integerChanger,
            UndoManager undoManager) {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(minimum);
        final JFormattedTextField fmtTxtFld = new JFormattedTextField(formatter);
        fmtTxtFld.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);

        fmtTxtFld.setValue(Integer.valueOf(initialValue));
        fmtTxtFld.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                integerChanger.changeInteger((Integer)fmtTxtFld.getValue());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                integerChanger.changeInteger((Integer)fmtTxtFld.getValue());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                integerChanger.changeInteger((Integer)fmtTxtFld.getValue());
            }
        });
        fmtTxtFld.getDocument().addUndoableEditListener(undoManager);
        return fmtTxtFld;
    }

    protected JFormattedTextField initFormattedTxtFld(int minimum, int maximum, int initialValue, final IntegerChanger integerChanger,
            UndoManager undoManager) {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(minimum);
        formatter.setMaximum(maximum);
        final JFormattedTextField fmtTxtFld = new JFormattedTextField(formatter);
        fmtTxtFld.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);

        fmtTxtFld.setValue(Integer.valueOf(initialValue));
        fmtTxtFld.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                integerChanger.changeInteger((Integer)fmtTxtFld.getValue());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                integerChanger.changeInteger((Integer)fmtTxtFld.getValue());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                integerChanger.changeInteger((Integer)fmtTxtFld.getValue());
            }
        });
        fmtTxtFld.getDocument().addUndoableEditListener(undoManager);
        return fmtTxtFld;
    }

    protected JTextField initTextField(String initialValue, final TextChanger textChanger, UndoManager undoManager) {
        JTextField textField = new JTextField(initialValue);

        Document doc = textField.getDocument();
        doc.addUndoableEditListener(undoManager);
        doc.addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePath(e.getDocument());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePath(e.getDocument());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePath(e.getDocument());
            }

            public void updatePath(Document d) {
                try {
                    textChanger.changeText(d.getText(0, d.getLength()));
                } catch (BadLocationException ignored) {}
            }
        });

        return textField;
    }

    protected JPopupMenu createEditPopupMenu(JTextComponent txtCmp) {
        Map<Object,Action> actions = createTextComponentActionTable(txtCmp);

        JPopupMenu menu = new JPopupMenu();
        menu.add(getActionByName(actions, DefaultEditorKit.cutAction));
        menu.add(getActionByName(actions, DefaultEditorKit.copyAction));
        menu.add(getActionByName(actions, DefaultEditorKit.pasteAction));
        menu.add(getActionByName(actions, DefaultEditorKit.selectAllAction));

        return menu;

    }

    protected Action getActionByName(Map<Object,Action> actions, Object name) {
        return actions.get(name);
    }

    protected Map<Object,Action> createTextComponentActionTable(JTextComponent txtCmp) {
        Map<Object,Action> actions = new HashMap<Object,Action>();
        Action[] actionArray = txtCmp.getActions();
        for(Action action: actionArray) {
            actions.put(action.getValue(Action.NAME), action);
        }
        return actions;
    }

}
