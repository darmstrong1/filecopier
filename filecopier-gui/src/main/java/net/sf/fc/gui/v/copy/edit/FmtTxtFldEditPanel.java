package net.sf.fc.gui.v.copy.edit;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.NumberFormatter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.w3c.dom.DOMException;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.c.Retriever;
import net.sf.fc.gui.v.tree.XMLTreeModel;

public class FmtTxtFldEditPanel extends OptionsEditPanel {

    private final JFormattedTextField fmtTxtFld;

    public FmtTxtFldEditPanel(XMLTreeModel model,
            Retriever retriever) {
        super(model, retriever);

        fmtTxtFld = initFmtTxtFld();

        setLayout(new MigLayout("", "[grow]", "[][]"));
        add(fmtTxtFld, "cell 0 0, growx");
        add(btnUseDefault, "cell 0 1, growx");
    }

    private JFormattedTextField initFmtTxtFld() {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setValueClass(Integer.class);
        final JFormattedTextField fmtTxtFld = new JFormattedTextField(formatter);
        Document doc = fmtTxtFld.getDocument();

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
                        //tree.revalidate();
                        //tree.repaint();
                        System.out.println(selectedNode.getNode().getTextContent());
                    } catch (DOMException | BadLocationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        });

        doc.addUndoableEditListener(undoManager);

        // Add undo/redo key actions.
        fmtTxtFld.addKeyListener(new KeyAdapter() {
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

        return fmtTxtFld;
    }

    @Override
    public void changeText(String text) {
        fmtTxtFld.setValue(Integer.valueOf(text));
    }

}
