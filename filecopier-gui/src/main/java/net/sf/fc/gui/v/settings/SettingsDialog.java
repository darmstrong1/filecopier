package net.sf.fc.gui.v.settings;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.undo.UndoManager;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.c.settings.SettingsController;
import net.sf.fc.gui.v.ButtonDialogPanel;

@SuppressWarnings("serial")
public class SettingsDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private final SettingsController settingsController;
    private final UndoManager undoManager;

    /**
     * Create the dialog.
     */
    public SettingsDialog(SettingsPanel settingsPanel, final SettingsController settingsController,
            UndoManager undoManager) {
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.settingsController = settingsController;
        this.undoManager = undoManager;

        setTitle("Settings");
        setBounds(100, 100, 575, 520);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
        contentPanel.add(settingsPanel, "cell 0 0,grow");

        ButtonDialogPanel buttonPane = new ButtonDialogPanel(createActionListenerList());
        getRootPane().setDefaultButton(buttonPane.getDefaultButton());
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JMenuBar menuBar = initMenuBar();
        setJMenuBar(menuBar);
        setVisible(false);
        pack();

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // If we get this event, the user clicked the red X at the top right corner to close the window
                // or the Cancel button was clicked. Revert the settings back to the snapshot settings (the
                // settings the last time they were saved to disk).
                settingsController.retrieveRestoreSnapshotSettings();
            }
        });
    }

    private List<ActionListener> createActionListenerList() {
        List<ActionListener> actionListenerList = new ArrayList<ActionListener>();
        // Use Defaults listener
        actionListenerList.add(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                settingsController.retrieveDefaultSettings();
            }
        });

        // OK Listener
        actionListenerList.add(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                settingsController.retrieveSaveSettings();
                // Set visibility to false. Don't dispatch a WindowClosing event because the WindowAdapter
                // set up will revert the settings back to what was saved on disk. It will still work
                // because we just saved them to disk, but there's no need to make an unnecessary call.
                setVisible(false);
            }
        });

        // Cancel Listener
        actionListenerList.add(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispatch a Window Closing event. The Window Adapter set up will revert the Settings in the
                // Settings model to match what was last saved to disk.
                dispatchEvent(new WindowEvent(SettingsDialog.this, WindowEvent.WINDOW_CLOSING));
            }
        });

        return actionListenerList;
    }

    private JMenuBar initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
//        menuBar.add(initFileMenu());
        menuBar.add(initEditMenu());
        return menuBar;
    }

    private JMenu initFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        //JMenuItem mntmSave = initSaveMenuItem();
        //menu.add(mntmSave);
        JMenuItem mntmExit = initExitMenuItem();
        menu.add(mntmExit);
        return menu;
    }

    private JMenu initEditMenu() {
        JMenu menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        JMenuItem mntmUndo = initUndoMenuItem();
        menu.add(mntmUndo);
        JMenuItem mntmRedo = initRedoMenuItem();
        menu.add(mntmRedo);
        return menu;
    }

    private JMenuItem initSaveMenuItem() {
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));

        saveItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                settingsController.retrieveSaveSettings();
            }
        });
        return saveItem;
    }

    private JMenuItem initExitMenuItem() {
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK));

        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsDialog.this.dispatchEvent(new WindowEvent(SettingsDialog.this, WindowEvent.WINDOW_CLOSING));
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
                if(undoManager.canUndo()) {
                    undoManager.undo();
                }
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
                if(undoManager.canRedo()) {
                    undoManager.redo();
                }
            }

        });
        return redoItem;
    }

}
