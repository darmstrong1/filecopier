package net.sf.fc.gui.v.options;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.IllegalFormatException;
import java.util.SimpleTimeZone;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.c.FlagChanger;
import net.sf.fc.gui.c.options.OptionsController;
import net.sf.fc.gui.c.options.OptionsProperty;
import net.sf.fc.gui.v.AbstractViewPanel;
import net.sf.fc.gui.v.edit.CheckBoxUndoableEdit;
import net.sf.fc.gui.v.event.ComboBoxUndoItemListener;
import net.sf.fc.script.gen.options.RenameOptions;
import net.sf.fc.script.gen.options.RenameType;

@SuppressWarnings("serial")
public class FileExistOptionsPanel extends AbstractViewPanel {
    //private final JTextField renameSfxFmtTxtFld;
    //private final JLabel renamePreviewSfxFmtLabel;
    private final RenameSfxFmtPanel renameSfxFmtPanel;
    private final JComboBox<RenameType> renameTypeComboBox;
    private final JCheckBox chckbxBuildRestoreScript;
    private final JCheckBox chckbxPromptBeforeOverwrite;
    private final JCheckBox chckbxUpdate;
    private final JCheckBox chckbxUseGmt;

    private final OptionsController optionsController;
    private final UndoManager undoManager;
    /**
     * Create the panel.
     */
    public FileExistOptionsPanel(final OptionsController optionsController, final RenameOptions renameOptions, UndoManager undoManager) {
        this.optionsController = optionsController;
        this.undoManager = undoManager;

        setLayout(new MigLayout("", "[][grow]", "[][][][][][grow][][]"));

        JLabel lblIfFileExists = new JLabel("If File Exists:");
        add(lblIfFileExists, "cell 0 0,alignx leading");

        renameTypeComboBox = initRenameTypeComboBox(renameOptions.getRenameType());
        add(renameTypeComboBox, "cell 1 0,growx");

        chckbxBuildRestoreScript = initCheckBox("Build Restore Script", new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                optionsController.changeBuildRestoreScript(flag);
                FileExistOptionsPanel.this.undoManager.addEdit(new CheckBoxUndoableEdit(chckbxBuildRestoreScript.getModel(),
                        chckbxBuildRestoreScript.isSelected(), initBuildRestoreScriptFlagChanger()));
            }

        }, renameOptions.isBuildRestoreScript());
        chckbxBuildRestoreScript.setToolTipText("Build a restore script to reverse the copy operation.");
        add(chckbxBuildRestoreScript, "cell 0 1");

        chckbxPromptBeforeOverwrite = initCheckBox("Prompt Before Overwrite", new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                optionsController.changePromptBeforeOverwrite(flag);
                FileExistOptionsPanel.this.undoManager.addEdit(new CheckBoxUndoableEdit(chckbxPromptBeforeOverwrite.getModel(),
                        chckbxPromptBeforeOverwrite.isSelected(), initPromptBeforeOverwriteFlagChanger()));
            }

        }, renameOptions.isPromptBeforeOverwrite());
        chckbxPromptBeforeOverwrite.setToolTipText("If destination file exists, confirm an overwrite.");
        add(chckbxPromptBeforeOverwrite, "cell 0 2");

        chckbxUpdate = initCheckBox("Update", new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                optionsController.changeUpdate(flag);
                FileExistOptionsPanel.this.undoManager.addEdit(new CheckBoxUndoableEdit(chckbxUpdate.getModel(),
                        chckbxUpdate.isSelected(), initUpdateFlagChanger()));
            }

        }, renameOptions.isUpdate());
        chckbxUpdate.setToolTipText("Rename/replace existing file only if destination file is older than source file.");
        add(chckbxUpdate, "cell 0 3");

        JLabel lblRenameSfxFmtr = new JLabel("Rename Suffix Formatter");
        add(lblRenameSfxFmtr, "cell 0 4,alignx leading");

//        Calendar cal = renameOptions.getRenameSuffixOptions().isUseGMT() ? Calendar.getInstance(new SimpleTimeZone(0, "GMT")) : Calendar.getInstance();
//
//        RenameFmtString renameFmtString = new RenameFmtString(renameOptions.getRenameSuffixOptions().getFormat(), cal);
//        renameSfxFmtTxtFld = initRenameSfxFmtTxtFld(renameFmtString.getRenameFmtString());
//        add(renameSfxFmtTxtFld, "cell 0 5 2 1,growx");
//        renameSfxFmtTxtFld.setColumns(10);
//
//        renamePreviewSfxFmtLabel = initRenamePreviewSfxFmtLabel(renameFmtString.getPreviewRenameFmtString());
//        add(renamePreviewSfxFmtLabel, "cell 0 6 2 1,growx");

        renameSfxFmtPanel = initRenameSfxFmtPanel(renameOptions.getRenameSuffixOptions().getFormat(),
                renameOptions.getRenameSuffixOptions().isUseGMT(), undoManager);
        add(renameSfxFmtPanel, "cell 0 5 2 2,grow");

        chckbxUseGmt = initCheckBox("Use GMT", new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                optionsController.changeUseGMT(flag);
//                handleRenameSfxFmtChange();
                renameSfxFmtPanel.useGMT(flag);
                FileExistOptionsPanel.this.undoManager.addEdit(new CheckBoxUndoableEdit(chckbxUseGmt.getModel(),
                        chckbxUseGmt.isSelected(), initUseGmtFlagChanger()));
            }

        }, renameOptions.getRenameSuffixOptions().isUseGMT());
        add(chckbxUseGmt, "cell 0 7");

        updateComponentState(renameOptions.getRenameType());

        optionsController.addView(this);
    }

    // Flag Changers used for undo functionality of JCheckBoxes.
    private FlagChanger initUseGmtFlagChanger() {
        return new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                optionsController.changeUseGMT(flag);
//                handleRenameSfxFmtChange();
                renameSfxFmtPanel.useGMT(flag);
            }

        };
    }

    private FlagChanger initUpdateFlagChanger() {
        return new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                optionsController.changeUpdate(flag);
            }

        };
    }

    private FlagChanger initBuildRestoreScriptFlagChanger() {
        return new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                optionsController.changeBuildRestoreScript(flag);
            }

        };
    }

    private FlagChanger initPromptBeforeOverwriteFlagChanger() {
        return new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                optionsController.changePromptBeforeOverwrite(flag);
            }

        };
    }

    private JComboBox<RenameType> initRenameTypeComboBox(RenameType renameType) {
        final JComboBox<RenameType> comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<RenameType>(RenameType.values()));
        comboBox.setMaximumRowCount(5);
        comboBox.setSelectedItem(renameType);

        // The Action listener takes care of notifying the model and enabling/disabling the check boxes based on the rename type selected.
        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RenameType renameType = (RenameType)comboBox.getSelectedItem();
                optionsController.changeRenameType(renameType);
                updateComponentState(renameType);
            }

        });

        // The Item listener takes care of creating ComboBoxUndoableEdit objects so that undo/redo functionality will work.
        comboBox.addItemListener(new ComboBoxUndoItemListener(comboBox, undoManager));

        return comboBox;
    }

    private void updateComponentState(RenameType renameType) {
        switch(renameType) {
        case ASK:
            chckbxBuildRestoreScript.setEnabled(true);
            chckbxPromptBeforeOverwrite.setEnabled(false);
            chckbxUpdate.setEnabled(false);
//            renameSfxFmtTxtFld.setEnabled(true);
//            renamePreviewSfxFmtLabel.setEnabled(true);
            renameSfxFmtPanel.setEnabled(true);
            chckbxUseGmt.setEnabled(true);
            break;

        case SKIP:
            chckbxBuildRestoreScript.setEnabled(false);
            chckbxPromptBeforeOverwrite.setEnabled(false);
            chckbxUpdate.setEnabled(false);
//            renameSfxFmtTxtFld.setEnabled(false);
//            renamePreviewSfxFmtLabel.setEnabled(false);
            renameSfxFmtPanel.setEnabled(false);
            chckbxUseGmt.setEnabled(false);
            break;

        case RENAME_AUTO:
        case RENAME_MANUAL:
            chckbxBuildRestoreScript.setEnabled(true);
            chckbxPromptBeforeOverwrite.setEnabled(false);
            chckbxUpdate.setEnabled(true);
//            renameSfxFmtTxtFld.setEnabled(true);
//            renamePreviewSfxFmtLabel.setEnabled(true);
            renameSfxFmtPanel.setEnabled(true);
            chckbxUseGmt.setEnabled(true);
            break;

        default:/* OVERWRITE */
            chckbxBuildRestoreScript.setEnabled(false);
            chckbxPromptBeforeOverwrite.setEnabled(true);
            chckbxUpdate.setEnabled(true);
//            renameSfxFmtTxtFld.setEnabled(false);
//            renamePreviewSfxFmtLabel.setEnabled(false);
            renameSfxFmtPanel.setEnabled(false);
            chckbxUseGmt.setEnabled(false);
            break;
        }
    }

    private RenameSfxFmtPanel initRenameSfxFmtPanel(String initialValue, boolean useGMT, UndoManager undoManager) {
        RenameSfxFmtPanel pnl = new RenameSfxFmtPanel(initialValue, useGMT, undoManager);
        final JTextField txtFld = pnl.getTextField();
        txtFld.setToolTipText("<html>FileCopier appends a format of the last modified date to renamed files. Type in<br>" +
                "your desired format. For help on how to build valid formats, click the Help button<br>" +
                "and go to the \"Rename Options\" section</html>");
        txtFld.setComponentPopupMenu(createEditPopupMenu(txtFld));

        pnl.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if("renameFmtString".equals(evt.getPropertyName())) {
                    optionsController.changeRenameSuffixFormat((String)evt.getNewValue());
                }
            }
        });

        return pnl;
    }

//    private JLabel initRenamePreviewSfxFmtLabel(String initialValue) {
//        JLabel label = new JLabel(initialValue);
//        return label;
//    }
//
//    private JTextField initRenameSfxFmtTxtFld(String initialValue) {
//        final JTextField txtFld = new JTextField(initialValue);
//        txtFld.setToolTipText("<html>FileCopier appends a format of the last modified date to renamed files. Type in<br>" +
//                "your desired format. For help on how to build valid formats, click the Help button<br>" +
//                "and go to the \"Rename Options\" section</html>");
//        txtFld.setComponentPopupMenu(createEditPopupMenu(txtFld));
//
//        Document doc = txtFld.getDocument();
//        doc.addUndoableEditListener(undoManager);
//        doc.addDocumentListener(new DocumentListener() {
//
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                if(handleRenameSfxFmtChange()) {
//                    optionsController.changeRenameSuffixFormat(txtFld.getText());
//                }
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                if(handleRenameSfxFmtChange()) {
//                    optionsController.changeRenameSuffixFormat(txtFld.getText());
//                }
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                if(handleRenameSfxFmtChange()) {
//                    optionsController.changeRenameSuffixFormat(txtFld.getText());
//                }
//            }
//
//        });
//
//        return txtFld;
//    }
//
//    private String setRenamePreviewFmtString(String renameFmtStr, boolean useGMT) {
//        RenameFmtString renameFmtString;
//        String newPreview;
//        try {
//            renameFmtString = new RenameFmtString(renameFmtStr,
//                    useGMT ? Calendar.getInstance(new SimpleTimeZone(0, "GMT")) : Calendar.getInstance());
//            newPreview = renameFmtString.getPreviewRenameFmtString();
//        } catch(IllegalFormatException e) {
//            newPreview = null;
//        }
//
//        return newPreview;
//    }

//    private boolean handleRenameSfxFmtChange() {
//        String renameSfxFmtTxtVal = renameSfxFmtTxtFld.getText();
//        boolean useGMT = chckbxUseGmt.isSelected();
//        String newPreview = setRenamePreviewFmtString(renameSfxFmtTxtVal, useGMT);
//        boolean validFmt = newPreview != null;
//        if(validFmt) {
//            renamePreviewSfxFmtLabel.setText(newPreview);
//            renameSfxFmtTxtFld.setForeground(Color.BLACK);
//        } else {
//            renamePreviewSfxFmtLabel.setText("");
//            renameSfxFmtTxtFld.setForeground(Color.RED);
//        }
//        return validFmt;
//    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        try {
            switch(OptionsProperty.valueOf(evt.getPropertyName())) {
            case RenameType:
                if(!((RenameType)renameTypeComboBox.getSelectedItem()).equals((RenameType)evt.getNewValue())) {
                    renameTypeComboBox.setSelectedItem(evt.getNewValue());
                }
                break;

            case BuildRestoreScript:
                if(!Boolean.valueOf(chckbxBuildRestoreScript.isSelected()).equals(evt.getNewValue())) {
                    chckbxBuildRestoreScript.setSelected((Boolean)evt.getNewValue());
                }
                break;

            case PromptBeforeOverwrite:
                if(!Boolean.valueOf(chckbxPromptBeforeOverwrite.isSelected()).equals(evt.getNewValue())) {
                    clickCheckBox(chckbxPromptBeforeOverwrite);
                }
                break;

            case Update:
                if(!Boolean.valueOf(chckbxUpdate.isSelected()).equals(evt.getNewValue())) {
                    clickCheckBox(chckbxUpdate);
                }
                break;

            case UseGMT:
                if(!Boolean.valueOf(chckbxUseGmt.isSelected()).equals(evt.getNewValue())) {
                    clickCheckBox(chckbxUseGmt);
                }
                break;

            case RenameSuffixFormat:
//                if(!renameSfxFmtTxtFld.getText().equals(evt.getNewValue())) {
//                    renameSfxFmtTxtFld.setText((String)evt.getNewValue());
//                }
                JTextField txtFld = renameSfxFmtPanel.getTextField();
                if(!txtFld.getText().equals(evt.getNewValue())) {
                    txtFld.setText((String)evt.getNewValue());
                }
                break;

            default:
                break;
            }
        // Ignore the IllegalArgumentException. It will get thrown if the property name does not match one of the enums
        // defined in OptionsProperty.
        } catch(IllegalArgumentException ignored) {}
    }

}
