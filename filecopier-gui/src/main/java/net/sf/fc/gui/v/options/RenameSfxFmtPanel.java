package net.sf.fc.gui.v.options;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.IllegalFormatException;
import java.util.SimpleTimeZone;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import net.miginfocom.swing.MigLayout;

public class RenameSfxFmtPanel extends JPanel {

    private final JTextField renameSfxFmtTxtFld;
    private final JLabel renamePreviewSfxFmtLabel;
    private Calendar cal;
    private RenameFmtString renameFmt;

    public RenameSfxFmtPanel(UndoManager undoManager) {
        this(null, false, true, undoManager);
    }

    public RenameSfxFmtPanel(boolean txtFldFirst, UndoManager undoManager) {
        this(null, false, txtFldFirst, undoManager);
    }

    public RenameSfxFmtPanel(String renameFmtString, boolean useGMT, UndoManager undoManager) {

        this(renameFmtString, useGMT, true, undoManager);
    }

    public RenameSfxFmtPanel(String renameFmtString, boolean useGMT, boolean txtFldFirst, UndoManager undoManager) {

        renameSfxFmtTxtFld = initRenameSfxFmtTxtFld(renameFmtString, undoManager);

        cal = useGMT ? Calendar.getInstance(new SimpleTimeZone(0, "GMT")) : Calendar.getInstance();
        renameFmt = new RenameFmtString(renameFmtString != null ? renameFmtString : "", cal);

        renamePreviewSfxFmtLabel = initRenamePreviewSfxFmtLabel(renameFmt.getPreviewRenameFmtString());

        layoutComponents(txtFldFirst);
    }

    public JTextField getTextField() {
        return renameSfxFmtTxtFld;
    }

    public void useGMT(boolean useGMT) {
        cal = useGMT ? Calendar.getInstance(new SimpleTimeZone(0, "GMT")) : Calendar.getInstance();
        handleRenameSfxFmtChange();
    }

    private void layoutComponents(boolean txtFldFirst) {
        this.setLayout(new MigLayout("", "[grow]", "[][]"));

        if(txtFldFirst) {
            add(renameSfxFmtTxtFld, "cell 0 0, growx");
            add(renamePreviewSfxFmtLabel, "cell 0 1, growx");
        } else {
            add(renamePreviewSfxFmtLabel, "cell 0 0, growx");
            add(renameSfxFmtTxtFld, "cell 0 1, growx");
        }
    }

    private JLabel initRenamePreviewSfxFmtLabel(String initialValue) {
        JLabel label = new JLabel(initialValue);
        return label;
    }

    private JTextField initRenameSfxFmtTxtFld(String initialValue, final UndoManager undoManager) {

        JTextField txtFld = new JTextField(initialValue);

        Document doc = txtFld.getDocument();
        doc.addUndoableEditListener(undoManager);

        doc.addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleRenameSfxFmtChange();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                handleRenameSfxFmtChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleRenameSfxFmtChange();
            }
        });

        // Add undo/redo key actions.
        txtFld.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getModifiers() == Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) {
                    switch(e.getKeyCode()) {
                    case KeyEvent.VK_Z:
                        try { undoManager.undo(); } catch(CannotUndoException ignored) {}
                        break;

                    case KeyEvent.VK_Y:
                        try { undoManager.redo(); } catch(CannotRedoException ignored) {}
                        break;

                    default:
                        break;
                    }
                }
            }
        });

        return txtFld;

    }

    private void handleRenameSfxFmtChange() {
        String renameSfxFmtTxtVal = renameSfxFmtTxtFld.getText();
        RenameFmtString renameFmtString;
        try {
            renameFmtString = new RenameFmtString(renameSfxFmtTxtVal, cal);
            renamePreviewSfxFmtLabel.setText(renameFmtString.getPreviewRenameFmtString());
            renameSfxFmtTxtFld.setForeground(Color.BLACK);
            // Fire the property change only if an IllegalFormatException did not get thrown.
            firePropertyChange("renameFmtString", renameFmt.getRenameFmtString(), renameFmtString.getRenameFmtString());
            renameFmt = renameFmtString;

        } catch(IllegalFormatException e) {
            renamePreviewSfxFmtLabel.setText("");
            renameSfxFmtTxtFld.setForeground(Color.RED);
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        renameSfxFmtTxtFld.setEnabled(enabled);
        renamePreviewSfxFmtLabel.setEnabled(enabled);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("RenameSfxFmtPanel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new RenameSfxFmtPanel("_old", false, new UndoManager()));
        frame.setSize(450, 260);
        frame.setVisible(true);
    }

}
