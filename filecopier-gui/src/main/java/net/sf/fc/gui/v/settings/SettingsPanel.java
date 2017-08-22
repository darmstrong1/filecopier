package net.sf.fc.gui.v.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import javax.swing.undo.UndoManager;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.c.FlagChanger;
import net.sf.fc.gui.c.IntegerChanger;
import net.sf.fc.gui.c.TextChanger;
import net.sf.fc.gui.c.settings.SettingsController;
import net.sf.fc.gui.c.settings.SettingsProperty;
import net.sf.fc.gui.v.AbstractViewPanel;
import net.sf.fc.gui.v.edit.CheckBoxUndoableEdit;
import net.sf.fc.gui.v.event.ComboBoxUndoItemListener;
import net.sf.fc.script.gen.settings.LogLevel;
import net.sf.fc.script.gen.settings.Settings;

@SuppressWarnings("serial")
public class SettingsPanel extends AbstractViewPanel {
    private final JComboBox<String> lookFeelComboBox;
    private final JComboBox<LogLevel> logLevelComboBox;
    private final JFormattedTextField mruTextField;
    private final JCheckBox chckbxDrawWindowBorders;
    private final SettingsController settingsController;
    private final UndoManager undoManager;

    private final JTextField[] pathTxtFlds = new JTextField[OPTIONS_PATH_TEXT_IDX+1];

    private static final int MRU_MIN = 1;
    private static final int MRU_MAX = 12;

    public static final int COPY_PATH_TEXT_IDX = 0;
    public static final int RESTORE_PATH_TEXT_IDX = 1;
    public static final int OPTIONS_PATH_TEXT_IDX = 2;

    /**
     * Create the panel.
     */
    public SettingsPanel(Settings settings, final SettingsController settingsController, UndoManager undoManager) {
        this.settingsController = settingsController;
        this.undoManager = undoManager;

        setLayout(new MigLayout("", "[grow]", "[][grow][grow][grow]"));

        // Initialize the MRU Panel and its components
        mruTextField = initFormattedTxtFld(MRU_MIN, MRU_MAX, settings.getMostRecentlyUsed().getLimit(),
                new IntegerChanger() {

                    @Override
                    public void changeInteger(Integer integer) {
                        settingsController.changeMostRecentlyUsedLimit(integer);
                    }
                }, undoManager);

        JPanel mruPanel = initMruPanel(mruTextField);
        add(mruPanel, "cell 0 0,growx,aligny top");

        // Initialize the log level panel and its components.
        logLevelComboBox = initLogLevelComboBox(settings.getLogLevel());
        JPanel logLevelPanel = initLogLevelPanel(logLevelComboBox);
        add(logLevelPanel, "cell 0 1,growx,aligny top");

        // Initialize the look and feel panel and its components.
        lookFeelComboBox = initLookFeelComboBox(settings.getLookAndFeel().getSelected());

        chckbxDrawWindowBorders = initCheckBox("Draw window borders using Swing look and feel.",
                new FlagChanger() {

                    @Override
                    public void changeFlag(boolean flag) {
                        settingsController.changeDrawWindowBorders(flag);
                        SettingsPanel.this.undoManager.addEdit(new CheckBoxUndoableEdit(chckbxDrawWindowBorders.getModel(),
                                chckbxDrawWindowBorders.isSelected(), initDrawWindowBordersFlagChanger()));
                    }
                }, settings.getLookAndFeel().isDrawWindowBorders());

        JPanel lookFeelPanel = initLookFeelPanel(lookFeelComboBox, chckbxDrawWindowBorders);
        add(lookFeelPanel, "cell 0 2,growx,aligny top");

        // Initialize the path panel and its components.
        pathTxtFlds[COPY_PATH_TEXT_IDX] = initTextField(settings.getPaths().getCopyPath(), new TextChanger() {

            @Override
            public void changeText(String text) {
                settingsController.changeCopyPath(text);
            }
        }, undoManager);

        pathTxtFlds[RESTORE_PATH_TEXT_IDX] = initTextField(settings.getPaths().getRestorePath(), new TextChanger() {

            @Override
            public void changeText(String text) {
                settingsController.changeRestorePath(text);
            }
        }, undoManager);

        pathTxtFlds[OPTIONS_PATH_TEXT_IDX] = initTextField(settings.getPaths().getOptionsPath(), new TextChanger() {

            @Override
            public void changeText(String text) {
                settingsController.changeOptionsPath(text);
            }
        }, undoManager);
        JPanel pathPanel = initPathPanel(pathTxtFlds);
        add(pathPanel, "cell 0 3,grow");

        settingsController.addView(this);
    }

    private JPanel initMruPanel(JFormattedTextField mruTxtFld) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "[][grow]", "[]"));
        JLabel lblMruPrefix = new JLabel("Retain");
        panel.add(lblMruPrefix, "cell 0 0,alignx trailing");

        mruTxtFld.setColumns(2);
        panel.add(mruTxtFld, "flowx,cell 1 0,alignx left");

        JLabel lblMruSuffix = new JLabel("items in most recently used lists.");
        panel.add(lblMruSuffix, "cell 1 0");
        return panel;
    }

    private JPanel initLogLevelPanel(JComboBox<LogLevel> comboBox) {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Log Level", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setLayout(new MigLayout("", "[]", "[]"));

        panel.add(comboBox, "cell 0 0,growx");
        return panel;
    }

    private JPanel initLookFeelPanel(JComboBox<String> comboBox, JCheckBox checkBox) {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Look & Feel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setLayout(new MigLayout("", "[]", "[][][]"));
        JLabel lblRestartMsg = new JLabel("Restart filecopier for look and feel changes to take effect");
        panel.add(lblRestartMsg, "cell 0 0");

        panel.add(lookFeelComboBox, "cell 0 1,growx");

        panel.add(chckbxDrawWindowBorders, "cell 0 2");

        return panel;
    }

    private JPanel initPathPanel(JTextField[] pathTxtFlds) {
        JPanel panel = new JPanel();

        panel.setBorder(new TitledBorder(null, "Paths", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setLayout(new MigLayout("", "[grow]", "[][][][][][]"));

        JLabel lblCopy = new JLabel("Copy");
        panel.add(lblCopy, "cell 0 0");

        panel.add(pathTxtFlds[COPY_PATH_TEXT_IDX], "cell 0 1,growx");
        pathTxtFlds[COPY_PATH_TEXT_IDX].setColumns(10);

        JLabel lblRestore = new JLabel("Restore");
        panel.add(lblRestore, "cell 0 2");

        panel.add(pathTxtFlds[RESTORE_PATH_TEXT_IDX], "cell 0 3,growx");
        pathTxtFlds[RESTORE_PATH_TEXT_IDX].setColumns(10);

        JLabel lblOptions = new JLabel("Options");
        panel.add(lblOptions, "cell 0 4");

        panel.add(pathTxtFlds[OPTIONS_PATH_TEXT_IDX], "cell 0 5,growx");
        pathTxtFlds[OPTIONS_PATH_TEXT_IDX].setColumns(10);

        return panel;
    }

    private FlagChanger initDrawWindowBordersFlagChanger() {
        return new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                settingsController.changeDrawWindowBorders(flag);
            }
        };
    }

    private String[] getInstalledLookAndFeelNames() {
        LookAndFeelInfo[] lafis = UIManager.getInstalledLookAndFeels();
        String[] names = new String[lafis.length];
        int i = 0;
        for(LookAndFeelInfo lafi : lafis) {
            names[i++] = lafi.getName();
        }
        return names;
    }

    private JComboBox<LogLevel> initLogLevelComboBox(LogLevel initialLogLevel) {

        final JComboBox<LogLevel> comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<LogLevel>(LogLevel.values()));
        comboBox.setSelectedItem(initialLogLevel);

        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LogLevel logLevel = (LogLevel)comboBox.getSelectedItem();
                settingsController.changeLogLevel(logLevel);
            }
        });
        // The Item listener takes care of creating ComboBoxUndoableEdit objects so that undo/redo functionality will work.
        comboBox.addItemListener(new ComboBoxUndoItemListener(comboBox, undoManager));

        return comboBox;
    }

    private JComboBox<String> initLookFeelComboBox(String initialLaf) {
        final JComboBox<String> comboBox = new JComboBox<>(getInstalledLookAndFeelNames());
        comboBox.setSelectedItem(initialLaf);

        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String laf = (String)comboBox.getSelectedItem();
                settingsController.changeLookAndFeel(laf);
            }
        });

        // The Item listener takes care of creating ComboBoxUndoableEdit objects so that undo/redo functionality will work.
        comboBox.addItemListener(new ComboBoxUndoItemListener(comboBox, undoManager));

        return comboBox;
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        try {
            switch(SettingsProperty.valueOf(evt.getPropertyName())) {
            case LogLevel:
                if(!((LogLevel)logLevelComboBox.getSelectedItem()).equals((LogLevel)evt.getNewValue())) {
                    logLevelComboBox.setSelectedItem(evt.getNewValue());
                }
                break;

            case LookAndFeel:
                if(!((String)lookFeelComboBox.getSelectedItem()).equals((String)evt.getNewValue())) {
                    lookFeelComboBox.setSelectedItem(evt.getNewValue());
                }
                break;

            case DrawWindowBorders:
                if(!Boolean.valueOf(chckbxDrawWindowBorders.isSelected()).equals(evt.getNewValue())) {
                    // Call doClick so the ActionListener will get notified.
                    clickCheckBox(chckbxDrawWindowBorders);
                }
                break;

            case MostRecentlyUsedLimit:
                if(!((Integer)mruTextField.getValue()).equals(evt.getNewValue())) {
                    mruTextField.setValue(evt.getNewValue());
                }
                break;

            case CopyPath:
                if(!((String)pathTxtFlds[COPY_PATH_TEXT_IDX].getText()).equals(evt.getNewValue())) {
                    pathTxtFlds[COPY_PATH_TEXT_IDX].setText((String)evt.getNewValue());
                }
                break;

            case RestorePath:
                if(!((String)pathTxtFlds[RESTORE_PATH_TEXT_IDX].getText()).equals(evt.getNewValue())) {
                    pathTxtFlds[RESTORE_PATH_TEXT_IDX].setText((String)evt.getNewValue());
                }
                break;

            case OptionsPath:
                if(!((String)pathTxtFlds[OPTIONS_PATH_TEXT_IDX].getText()).equals(evt.getNewValue())) {
                    pathTxtFlds[OPTIONS_PATH_TEXT_IDX].setText((String)evt.getNewValue());
                }
                break;

            default:
                break;
            }
        } catch(IllegalArgumentException ignored) {}
    }

}
