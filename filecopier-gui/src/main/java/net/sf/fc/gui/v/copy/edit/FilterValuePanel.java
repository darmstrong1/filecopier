package net.sf.fc.gui.v.copy.edit;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
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
import net.sf.fc.gui.v.tree.XMLTreeModel;

public class FilterValuePanel extends OptionsEditPanel {

    private final JTextField txtFld;

    public FilterValuePanel(XMLTreeModel model, Retriever retriever) {
        super(model, retriever);
        txtFld = initTextField();

        setLayout(new MigLayout("", "[grow]", "[][]"));
        add(txtFld, "cell 0 0, growx");
        add(btnUseDefault, "cell 0 1, growx");
    }

    private JTextField initTextField() {
        JTextField txtFld = new JTextField();
        txtFld.setColumns(10);
        Document doc = txtFld.getDocument();
        doc.addUndoableEditListener(undoManager);
        doc.addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                setNodeText(e.getDocument());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setNodeText(e.getDocument());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setNodeText(e.getDocument());
            }

            private void setNodeText(javax.swing.text.Document doc) {
                if(doc != null && selectedNode != null) {
                    try {
                        selectedNode.getNode().setTextContent(doc.getText(0, doc.getLength()));
                        model.nodeChanged(selectedNode);
                    } catch (DOMException | BadLocationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
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

    @Override
    public void changeText(String text) {
        txtFld.setText(text);
    }
}
