package net.sf.fc.gui.v.copy.edit;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.w3c.dom.DOMException;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.c.Retriever;
import net.sf.fc.gui.c.TextChanger;
import net.sf.fc.gui.c.copy.CopyController;
import net.sf.fc.gui.c.copy.CopyProperty;
import net.sf.fc.gui.v.View;
import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.gui.v.widget.FileAutoCompleteTextField;

@SuppressWarnings("serial")
public class FileEditPanel extends TxtFldEditPanel implements View {

    private final JButton btnBrowse;
    private final TextChanger textChanger;
    private final Retriever retriever;
    private final CopyProperty lastPathProperty;
    private boolean getPath = false;

    public FileEditPanel(XMLTreeModel model, final CopyController copyController, boolean isSrc) {
        super(model);
        setLayout(new MigLayout("", "[grow]", "[]"));

        if(isSrc) {
            lastPathProperty = CopyProperty.LastSrcDirPath;

            textChanger = new TextChanger() {

                @Override
                public void changeText(String text) {
                    copyController.changeLastSrcDirPath(text);
                }
            };

            retriever = new Retriever() {

                @Override
                public void retrieve() {
                    copyController.retrieveLastSrcDirPath();
                }
            };
        } else {
            lastPathProperty = CopyProperty.LastDstDirPath;

            textChanger = new TextChanger() {

                @Override
                public void changeText(String text) {
                    copyController.changeLastDstDirPath(text);
                }
            };

            retriever = new Retriever() {

                @Override
                public void retrieve() {
                    copyController.retrieveLastDstDirPath();
                }
            };

        }

        txtFld = initTxtFld();
        add(txtFld, "cell 0 0,growx");

        btnBrowse = initBrowseBtn();
        add(btnBrowse, "cell 0 1,growx");

        copyController.addView(this);
    }

    private JTextField initTxtFld() {
        JTextField txtFld = new FileAutoCompleteTextField();
        txtFld.setColumns(10);
        Document doc = txtFld.getDocument();
        doc.addDocumentListener(new TxtFldDocListener(this));
        doc.addUndoableEditListener(undoManager);

        // Add undo/redo key actions.
        txtFld.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.undo();
                } catch(CannotUndoException ignored) {}
            }
        });

        txtFld.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.redo();
                } catch(CannotRedoException ignored) {}
            }
        });

        return txtFld;
    }

    private JButton initBrowseBtn() {
        JButton btn = new JButton("Browse");
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File dir;
                String text = txtFld.getText();
                if(!"".equals(text)) {
                    String dirStr = getDirVal(new File(text));
                    if(dirStr != null) {
                        dir = new File(dirStr);
                        showFileChooser(dir);
                    } else {
                        getPath = true;
                        retriever.retrieve();
                    }
                } else {
                    getPath = true;
                    retriever.retrieve();
                }
            }
        });
        return btn;
    }

    private void showFileChooser(File dir) {
        JFileChooser fileChooser = new JFileChooser(dir);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtFld.setText(fileChooser.getSelectedFile().getPath());
        }
    }

    private static String getDirVal(File file) {
        if(file != null) {
            if(file.isDirectory()) {
                return file.getPath();
            }
            return getDirVal(file.getParentFile());
        }
        return null;
    }

    /**
     * TxtFldDocListener updates the tree with the value the user enters in the text field. It also updates the last src/dst dir path
     * in the SettingsProxy object if the directory the user chose is different than the one that was previously used.
     * @author david
     *
     */
    private static class TxtFldDocListener implements DocumentListener {

        private final FileEditPanel editPnl;
        private String currentDir;

        public TxtFldDocListener(FileEditPanel editPnl) {
            this.editPnl = editPnl;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            setNodeTextAndUpdateLastPath(e.getDocument());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setNodeTextAndUpdateLastPath(e.getDocument());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setNodeTextAndUpdateLastPath(e.getDocument());
        }

        private void setNodeTextAndUpdateLastPath(javax.swing.text.Document doc) {
            if(doc != null && editPnl.selectedNode != null) {
                try {
                    String value = doc.getText(0, doc.getLength());
                    editPnl.selectedNode.getNode().setTextContent(value);
                    editPnl.model.nodeChanged(editPnl.selectedNode);
                    System.out.println(editPnl.selectedNode.getNode().getTextContent());
                    updateLastPath(value);
                } catch (DOMException | BadLocationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        private void updateLastPath(String value) {
            String dirVal = getDirVal(new File(value));
            if(dirVal != null && !dirVal.equals(currentDir)) {
                editPnl.textChanger.changeText(dirVal);
                currentDir = dirVal;
            }
        }

    } // end of TxtFldDocListener class

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        try {
            CopyProperty cpProperty = CopyProperty.valueOf(evt.getPropertyName());
            if(cpProperty.equals(lastPathProperty) && getPath) {
                getPath = false;
                showFileChooser(new File(getDirVal(new File((String)evt.getNewValue()))));
            }

        } catch(IllegalArgumentException ignored){}
    }

}
