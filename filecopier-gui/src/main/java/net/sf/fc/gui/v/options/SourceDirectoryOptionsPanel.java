package net.sf.fc.gui.v.options;

import static net.sf.fc.gui.factory.MVCFactory.DIR_COPY_FILTER_IDX;
import static net.sf.fc.gui.factory.MVCFactory.FILE_COPY_FILTER_IDX;
import static net.sf.fc.gui.factory.MVCFactory.FLATTEN_FILTER_IDX;
import static net.sf.fc.gui.factory.MVCFactory.MERGE_FILTER_IDX;

import java.beans.PropertyChangeEvent;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.undo.UndoManager;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.c.IntegerChanger;
import net.sf.fc.gui.c.options.OptionsController;
import net.sf.fc.gui.c.options.OptionsProperty;
import net.sf.fc.gui.v.AbstractViewPanel;
import net.sf.fc.io.Options;
import net.sf.fc.script.gen.options.SrcDirOptions;

@SuppressWarnings("serial")
public class SourceDirectoryOptionsPanel extends AbstractViewPanel {

    private final JFormattedTextField copyMaxDirLvlFld;
    private final JFormattedTextField flattenMaxDirLvlFld;
    private final JFormattedTextField mergeMaxDirLvlFld;

    /**
     * Create the panel.
     */
    public SourceDirectoryOptionsPanel(final OptionsController optionsController, SrcDirOptions srcDirOptions, FilterPanel[] filterPanels,
            UndoManager undoManager) {

        setLayout(new MigLayout("", "[][38.00][][grow]", "[][][][][grow]"));

        JLabel lblCopy = new JLabel("Copy");
        add(lblCopy, "cell 0 0,alignx trailing");

        copyMaxDirLvlFld = initFormattedTxtFld(Options.COPY_ALL_DIRS, srcDirOptions.getMaxCopyLevel(),
                new IntegerChanger() {

                    @Override
                    public void changeInteger(Integer value) {
                        try {
                            optionsController.changeMaxCopyLevel(value);
                        } catch(NumberFormatException ignored) {}
                    }

        }, undoManager);
        add(copyMaxDirLvlFld, "cell 1 0,growx");

        JLabel lblLevelsOfDirectories = new JLabel("levels of directories.");
        add(lblLevelsOfDirectories, "cell 2 0");

        JLabel lblFlatten = new JLabel("Flatten");
        add(lblFlatten, "cell 0 1,alignx trailing");

        flattenMaxDirLvlFld = initFormattedTxtFld(Options.NO_FLATTEN, srcDirOptions.getMaxFlattenLevel(),
                new IntegerChanger() {

                    @Override
                    public void changeInteger(Integer value) {
                        try {
                            optionsController.changeMaxFlattenLevel(value);
                        } catch(NumberFormatException ignored) {}
                    }

        }, undoManager);
        add(flattenMaxDirLvlFld, "cell 1 1,growx");

        JLabel label = new JLabel("levels of directories.");
        add(label, "cell 2 1");

        JLabel lblMerge = new JLabel("Merge");
        add(lblMerge, "cell 0 2,alignx trailing");

        mergeMaxDirLvlFld = initFormattedTxtFld(Options.NO_MERGE, srcDirOptions.getMaxMergeLevel(),
                new IntegerChanger() {

                    @Override
                    public void changeInteger(Integer value) {
                        try {
                            optionsController.changeMaxMergeLevel(value);
                        } catch(NumberFormatException ignored) {}
                    }

        }, undoManager);
        add(mergeMaxDirLvlFld, "cell 1 2,growx");

        JLabel label_1 = new JLabel("levels of directories.");
        add(label_1, "cell 2 2");

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        add(tabbedPane, "cell 0 4 4 1,grow");

        tabbedPane.addTab("File Copy", null, filterPanels[FILE_COPY_FILTER_IDX], null);
        tabbedPane.addTab("Directory Copy", null, filterPanels[DIR_COPY_FILTER_IDX], null);
        tabbedPane.addTab("Directory Flatten", null, filterPanels[FLATTEN_FILTER_IDX], null);
        tabbedPane.addTab("Directory Merge", null, filterPanels[MERGE_FILTER_IDX], null);

        optionsController.addView(this);
    }

//    private JFormattedTextField initFormattedTxtFld(int minimum, int initialValue, final UpdateHandler updateHandler) {
//        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
//        formatter.setValueClass(Integer.class);
//        formatter.setMinimum(minimum);
//        final JFormattedTextField fmtTxtFld = new JFormattedTextField(formatter);
//        fmtTxtFld.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
//
//        fmtTxtFld.setValue(Integer.valueOf(initialValue));
//        fmtTxtFld.getDocument().addDocumentListener(new DocumentListener() {
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                updateHandler.handleUpdate((Integer)fmtTxtFld.getValue());
//            }
//
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                updateHandler.handleUpdate((Integer)fmtTxtFld.getValue());
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                updateHandler.handleUpdate((Integer)fmtTxtFld.getValue());
//            }
//        });
//        fmtTxtFld.getDocument().addUndoableEditListener(undoManager);
//        return fmtTxtFld;
//    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        try {
            switch(OptionsProperty.valueOf(evt.getPropertyName())) {
            case MaxCopyLevel:
                if(!((Integer)copyMaxDirLvlFld.getValue()).equals((Integer)evt.getNewValue())) {
                    copyMaxDirLvlFld.setValue(evt.getNewValue());
                }
                break;

            case MaxFlattenLevel:
                if(!((Integer)flattenMaxDirLvlFld.getValue()).equals((Integer)evt.getNewValue())) {
                    flattenMaxDirLvlFld.setValue(evt.getNewValue());
                }
                break;

            case MaxMergeLevel:
                if(!((Integer)mergeMaxDirLvlFld.getValue()).equals((Integer)evt.getNewValue())) {
                    mergeMaxDirLvlFld.setValue(evt.getNewValue());
                }
                break;

            default:
                break;
            }
        } catch(IllegalArgumentException ignored) {}
    }

}
