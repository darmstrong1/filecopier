package net.sf.fc.gui.v.options;

import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.c.FlagChanger;
import net.sf.fc.gui.c.copy.FilterChanger;
import net.sf.fc.gui.c.options.OptionsController;
import net.sf.fc.gui.c.options.OptionsProperty;
import net.sf.fc.gui.v.AbstractViewPanel;
import net.sf.fc.gui.v.edit.CheckBoxUndoableEdit;
import net.sf.fc.script.gen.options.Filter;

@SuppressWarnings("serial")
public class FilterPanel extends AbstractViewPanel {
    private final JTextField filterValueTxtFld;
    private final JCheckBox chckbxCaseSensitive;
    private final JCheckBox chckbxRegex;
    private final JCheckBox chckbxExclusive;

    private final FilterChanger filterChanger;
    private final String propertyNamePrefix;

    private final UndoManager undoManager;

    /**
     * Create the panel.
     */
    public FilterPanel(final OptionsController optionsController, final FilterChanger filterChanger, final Filter filterValue,
            final String propertyNamePrefix, UndoManager undoManager) {
        this.filterChanger = filterChanger;
        this.propertyNamePrefix = propertyNamePrefix;
        this.undoManager = undoManager;

        setLayout(new MigLayout("", "[][grow]", "[][][][]"));

        JLabel lblFilterValue = new JLabel("Filter Value:");
        //add(lblFilterValue, "cell 0 0,alignx trailing");
        add(lblFilterValue, "cell 0 0,alignx");

        filterValueTxtFld = initFilterValueTxtFld(filterValue.getValue());
        add(filterValueTxtFld, "cell 1 0,growx");
        filterValueTxtFld.setColumns(10);

        chckbxCaseSensitive = initCheckBox("Case Sensitive", new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                filterChanger.changeFilterCaseSensitive(flag);
                FilterPanel.this.undoManager.addEdit(new CheckBoxUndoableEdit(chckbxCaseSensitive.getModel(),
                        chckbxCaseSensitive.isSelected(), initCaseSensitiveFlagChanger()));
            }

        }, filterValue.isCaseSensitive());
        add(chckbxCaseSensitive, "cell 0 1");

        chckbxRegex = initCheckBox("Regular Expression", new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                filterChanger.changeFilterRegEx(flag);
                FilterPanel.this.undoManager.addEdit(new CheckBoxUndoableEdit(chckbxRegex.getModel(),
                        chckbxRegex.isSelected(), initRegexFlagChanger()));
            }

        }, filterValue.isRegularExpression());
        add(chckbxRegex, "cell 0 2");

        chckbxExclusive = initCheckBox("Exclusive", new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                filterChanger.changeFilterExclusive(flag);
                FilterPanel.this.undoManager.addEdit(new CheckBoxUndoableEdit(chckbxExclusive.getModel(),
                        chckbxExclusive.isSelected(), initExclusiveFlagChanger()));
            }

        }, filterValue.isExclusive());
        add(chckbxExclusive, "cell 0 3");

        optionsController.addView(this);
    }

    private FlagChanger initCaseSensitiveFlagChanger() {
        return new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                filterChanger.changeFilterCaseSensitive(flag);
            }

        };
    }

    private FlagChanger initRegexFlagChanger() {
        return new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                filterChanger.changeFilterRegEx(flag);
            }

        };
    }

    private FlagChanger initExclusiveFlagChanger() {
        return new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                filterChanger.changeFilterExclusive(flag);
            }

        };
    }

    private JTextField initFilterValueTxtFld(String initialValue) {
        JTextField valueTxtFld = new JTextField();
        Document doc = valueTxtFld.getDocument();
        doc.addUndoableEditListener(undoManager);
        doc.addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeValue(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeValue(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeValue(e);
            }

            public void changeValue(DocumentEvent e) {
                try {
                    Document doc = e.getDocument();
                    filterChanger.changeFilterValue(doc.getText(0, doc.getLength()));
                } catch (BadLocationException ignored) {
                }
            }

        });

        valueTxtFld.setText(initialValue);

        return valueTxtFld;
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if((propertyNamePrefix+OptionsProperty.FilterValue.toString()).equals(evt.getPropertyName()) &&
                !filterValueTxtFld.getText().equals(evt.getNewValue())) {
            filterValueTxtFld.setText((String)evt.getNewValue());
        } else if((propertyNamePrefix+OptionsProperty.FilterCaseSensitive.toString()).equals(evt.getPropertyName()) &&
                !Boolean.valueOf(chckbxCaseSensitive.isSelected()).equals(evt.getNewValue())) {
            chckbxCaseSensitive.setSelected((Boolean)evt.getNewValue());
        } else if((propertyNamePrefix+OptionsProperty.FilterRegex.toString()).equals(evt.getPropertyName()) &&
                !Boolean.valueOf(chckbxRegex.isSelected()).equals(evt.getNewValue())) {
            chckbxRegex.setSelected((Boolean)evt.getNewValue());
        } else if((propertyNamePrefix+OptionsProperty.FilterExclusive.toString()).equals(evt.getPropertyName()) &&
                !Boolean.valueOf(chckbxExclusive.isSelected()).equals(evt.getNewValue())) {
            chckbxExclusive.setSelected((Boolean)evt.getNewValue());
        }
    }

}
