package net.sf.fc.gui.v.options;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.cfg.DirPath;
import net.sf.fc.gui.c.SaveAsChanger;
import net.sf.fc.gui.c.options.OptionsController;
import net.sf.fc.gui.c.options.OptionsProperty;
import net.sf.fc.gui.c.settings.SettingsProperty;
import net.sf.fc.gui.v.AbstractViewDialog;
import net.sf.fc.gui.v.ButtonDialogPanel;

@SuppressWarnings("serial")
public class OptionsDialog extends AbstractViewDialog {

    public static enum OptionsDlgType {
        DEFAULT, NEW, SAVED
    }

    private final JPanel contentPanel = new JPanel();
    private final OptionsController optionsController;
    private final UndoManager undoManager;
    private final boolean isDefault;
    private final Map<OptionsDlgType,List<ActionListener>> actionListenerMap;
    private List<ActionListener> actionListenerList;
    private DirPath optionsPath;

    public static final int DEFAULTS_IDX = 0;
    public static final int OK_IDX = 1;
    public static final int CANCEL_IDX = 2;

    /**
     * Create the dialog.
     */
    public OptionsDialog(String title, final OptionsController optionsController, JPanel optionsPanel,
            DirPath optionsPath, UndoManager undoManager, boolean isDefault) {
        this.optionsController = optionsController;
        this.undoManager = undoManager;
        this.optionsPath = optionsPath;
        this.isDefault = isDefault;
        actionListenerMap = initActionListenerMap();

        setTitle(title);
        //setBounds(100, 100, 450, 700);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
        contentPanel.add(optionsPanel, "cell 0 0,grow");

        optionsController.addView(this);

        // If windowAdapter is null, it means this is the default Options Dialog. The default Options Dialog is just hidden.
        // It is not destroyed, so it does not need to have a WindowAdapter passed to it that removes the Settings Model from
        // the OptionsController.
        //isDefault = windowListener == null;

        JMenuBar menuBar = initMenuBar(isDefault);
        setJMenuBar(menuBar);

        if(isDefault) {
            setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            actionListenerList = actionListenerMap.get(OptionsDlgType.DEFAULT);

            // Add a window listener that retrieves the last saved default options from disk. The Window Closing event will
            // get sent to the default options dialog only if the user closes the dialog by clicking the Cancel button or the
            // red X in the top right corner.
            addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    optionsController.retrieveRestoreSnapshotOptions();
                }
            });

        } else {
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            //addWindowListener(windowListener);
            actionListenerList = title.equals("Options") ? actionListenerMap.get(OptionsDlgType.NEW) : actionListenerMap.get(OptionsDlgType.SAVED);
        }

        ButtonDialogPanel buttonPane = new ButtonDialogPanel(actionListenerList);
        getRootPane().setDefaultButton(buttonPane.getDefaultButton());
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        setVisible(false);
        pack();
    }

    private JMenuBar initMenuBar(boolean isDefault) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(initFileMenu(isDefault));
        menuBar.add(initEditMenu());
        return menuBar;
    }

    private JMenu initFileMenu(boolean isDefault) {
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        if(!isDefault) {
            menu.add(initNewMenuItem());
            menu.add(initOpenMenuItem());
        }
        menu.add(initSaveMenuItem());
        if(!isDefault) {
            menu.add(initSaveAsMenuItem());
        }
        menu.add(initExitMenuItem());
        return menu;
    }

    private JMenu initEditMenu() {
        JMenu menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.add(initUndoMenuItem());
        menu.add(initRedoMenuItem());
        return menu;
    }

    private JMenuItem initNewMenuItem() {
        JMenuItem newItem = new JMenuItem("New");
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_MASK));
        newItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                optionsController.retrieveNewOptions();
                setTitle("Options");
            }
        });
        return newItem;
    }

    private JMenuItem initOpenMenuItem() {
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_MASK));
        openItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Send a message to retrieve the options path from the settings.xml file saved to disk.
                optionsController.retrieveOpenOptionsPath();


//                JFileChooser chooser = new JFileChooser();
//                chooser.setCurrentDirectory(new File(optionsPath.getPathAt(0)));
//                FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
//                chooser.setFileFilter(filter);
//                int returnVal = chooser.showOpenDialog(OptionsDialog.this);
//                if(returnVal == JFileChooser.APPROVE_OPTION) {
//                    optionsController.changeOpenOptions(chooser.getSelectedFile());
//                }
            }
        });
        return openItem;
    }

    private JMenuItem initSaveMenuItem() {
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
        saveItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                optionsController.retrieveSaveOptions();
            }
        });
        return saveItem;
    }

    private JMenuItem initSaveAsMenuItem() {
        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_MASK));
        saveAsItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Send a message to retrieve the options path from the settings.xml file saved to disk.
                optionsController.retrieveSaveAsOptionsPath();

//                doSaveAs(new SaveAsChanger() {
//
//                    @Override
//                    public void changeSaveAs(File file) {
//                        optionsController.changeSaveOptionsAs(file);
//                    }
//                });
            }
        });
        return saveAsItem;
    }

    private JMenuItem initExitMenuItem() {
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK));
        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                OptionsDialog.this.dispatchEvent(new WindowEvent(OptionsDialog.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        return exitItem;
    }

    private JMenuItem initUndoMenuItem() {
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        undoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                if(undoManager.canUndo()) {
//                    undoManager.undo();
//                }
                try {
                    undoManager.undo();
                } catch(CannotUndoException ignored) {}
            }

        });
        return undoItem;
    }

    private JMenuItem initRedoMenuItem() {
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        redoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                if(undoManager.canRedo()) {
//                    undoManager.redo();
//                }
                try {
                    undoManager.redo();
                } catch(CannotRedoException ignored) {}
            }

        });
        return redoItem;
    }

    private void doSaveAs(SaveAsChanger saveAsChanger) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(optionsPath.getPathAt(0)));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        chooser.setFileFilter(filter);
        if(!getTitle().equals("Options")) {
            chooser.setSelectedFile(new File(getTitle()));
        }
        int returnVal = chooser.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if(!selectedFile.getName().endsWith(".xml")) {
                selectedFile = new File(selectedFile.getPath()+".xml");
            }
            //optionsController.changeSaveOptionsAs(selectedFile);
            saveAsChanger.changeSaveAs(selectedFile);
            setTitle(selectedFile.getPath());
        }
    }

    private Map<OptionsDlgType,List<ActionListener>> initActionListenerMap() {
        Map<OptionsDlgType,List<ActionListener>> actionListenerMap = new EnumMap<>(OptionsDlgType.class);

        List<ActionListener> actionListenerList = new ArrayList<>();
        actionListenerList.add(createDefaultOptionsDefaultsActionListener());
        actionListenerList.add(createDefaultOptionsOkActionListener());
        actionListenerList.add(createDefaultOptionsCancelActionListener());
        actionListenerMap.put(OptionsDlgType.DEFAULT, actionListenerList);

        actionListenerList = new ArrayList<>();
        actionListenerList.add(createOptionsDefaultsActionListener());
        actionListenerList.add(createNewOptionsOkActionListener());
        actionListenerList.add(createNewOptionsCancelActionListener());
        actionListenerMap.put(OptionsDlgType.NEW, actionListenerList);

        actionListenerList = new ArrayList<>();
        actionListenerList.add(createOptionsDefaultsActionListener());
        actionListenerList.add(createSavedOptionsOkActionListener());
        actionListenerList.add(createSavedOptionsCancelActionListener());
        actionListenerMap.put(OptionsDlgType.SAVED, actionListenerList);

        return actionListenerMap;
    }

    private ActionListener createDefaultOptionsDefaultsActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                optionsController.retrieveCachedDefaultOptions();
            }
        };
    }

    private ActionListener createDefaultOptionsOkActionListener() {
        return createSavedOptionsOkActionListener();
    }

    private ActionListener createDefaultOptionsCancelActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Just send a Window Closing event. The window listener will revert the default Options
                // object saved in the Options model to what was last saved to disk.
                dispatchEvent(new WindowEvent(OptionsDialog.this, WindowEvent.WINDOW_CLOSING));
            }
        };
    }

    /**
     * Action Listener for the Use Defaults button for new or saved Options dialogs. It gets the options
     * that were saved to disk and sets the values displayed in the options dialog to that.
     * @return ActionListener
     */
    private ActionListener createOptionsDefaultsActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                optionsController.retrieveDiskDefaultOptions();
            }
        };
    }

    private ActionListener createNewOptionsOkActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(undoManager.canUndo()) {
                    doSaveAs(new SaveAsChanger() {

                        @Override
                        public void changeSaveAs(File file) {
                            optionsController.changeSaveOptionsAsFromOk(file);
                        }
                    });
                } else {
                    dispatchEvent(new WindowEvent(OptionsDialog.this, WindowEvent.WINDOW_CLOSING));
                }
            }
        };
    }

    private ActionListener createNewOptionsCancelActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(OptionsDialog.this, WindowEvent.WINDOW_CLOSING));
            }
        };
    }

    private ActionListener createSavedOptionsOkActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                optionsController.retrieveSaveOptionsFromOk();
            }
        };
    }

    private ActionListener createSavedOptionsCancelActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(OptionsDialog.this, WindowEvent.WINDOW_CLOSING));
            }
        };
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        try {
            switch(OptionsProperty.valueOf(propertyName)) {
            case SaveOptions:
                if(!isDefault && Boolean.FALSE.equals(evt.getNewValue())) {
                    // The SaveOptions event will return false if the save button was clicked, but the Options object this
                    // OptionsDialog is displaying has not been saved to disk yet. Show a Save as dialog.
                    doSaveAs(new SaveAsChanger() {

                        @Override
                        public void changeSaveAs(File file) {
                            optionsController.changeSaveOptionsAs(file);
                        }
                    });
                }
                break;

            case SaveOptionsFailure:
            case SaveOptionsFromOkFailure:
            case SaveOptionsAsFromOkFailure:
                //TODO: Display a dialog that says the save failed.
                break;

            case SaveOptionsFromOk:
                if(Boolean.TRUE.equals(evt.getNewValue())) {
                    // The SaveOptionsFromOk event gets returned when the Options Dialog is showing a
                    // saved options script or the default options script and the user clicks OK.
                    // It then tries to save the changes to the Options Script and if successful,
                    // it returns this event. This is when we should close the dialog if we are viewing
                    // a saved options file. If it is the default, we should just hide the dialog.
                    if(isDefault) {
                        setVisible(false);
                    } else {
                        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                    }
                }
                break;

            case SaveOptionsAsFromOk:
                // The SaveOptionsAsFromOk event gets returned when the Options Dialog is showing a
                // new options script and the user clicks OK. It then tries to save the changes
                // to the Options Script and if successful, it returns this event. This is when
                // we should close the dialog.
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;

            case NewOptions:
                undoManager.discardAllEdits();
                break;

            case OpenOptions:
                setTitle(((File)evt.getNewValue()).getPath());
                undoManager.discardAllEdits();
                break;

            case OpenOptionsFailure:
                //TODO: Display a dialog that says the open dialog failed.
                break;

            case DiskDefaultOptionsFailure:
                // We will get this if this is a New Options or an Open Options dialog box and the user clicked 'Use Defaults',
                // but it could not read the default options file for some reason. In that case, just get the standard default
                // options.
                optionsController.retrieveCachedDefaultOptions();
                break;

            case SaveAsOptionsPath:
                optionsPath = (DirPath)evt.getNewValue();
                doSaveAs(new SaveAsChanger() {

                    @Override
                    public void changeSaveAs(File file) {
                        optionsController.changeSaveOptionsAs(file);
                    }
                });
                break;

            case SaveAsOptionsPathFailure:
                //TODO: Log that the attempt to get the options directories saved in settings.xml failed and use a default
                // options directory.
                break;

            case OpenOptionsPath:
                optionsPath = (DirPath)evt.getNewValue();
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File(optionsPath.getPathAt(0)));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(OptionsDialog.this);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    optionsController.changeOpenOptions(chooser.getSelectedFile());
                }
                break;

            case OpenOptionsPathFailure:
                //TODO: Log that the attempt to get the options directories saved in settings.xml failed and use a default
                // options directory.
                break;

            default:
                break;
            }
            // Ignore the IllegalArgumentException. It will get thrown if the property name does not match one of the enums
            // defined in OptionsProperty.
        } catch(IllegalArgumentException ignored){}

//        try {
//            switch(SettingsProperty.valueOf(propertyName)) {
//            case OptionsPath:
//                if(!(new DirPath((String)evt.getNewValue()).equals(optionsPath))) {
//                    optionsPath = new DirPath((String)evt.getNewValue());
//                }
//                break;
//
//            default:
//                break;
//            }
//        } catch(IllegalArgumentException ignored){}
    }

}
