package net.sf.fc.gui.v.copy.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.v.event.ComboBoxUndoItemListener;
import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.script.gen.copy.Type;

public class FileTypeEditPanel extends EditPanel {

    private final JComboBox<Type> cmbBox;

    public FileTypeEditPanel(XMLTreeModel model) {
        super(model);
        this.cmbBox = initCmbBox();
        setLayout(new MigLayout("", "[grow]", "[]"));

        add(cmbBox, "cell 0 0, growx");
    }

    private JComboBox<Type> initCmbBox() {
        final JComboBox<Type> cmbBox = new JComboBox<Type>();
        cmbBox.setModel(new DefaultComboBoxModel<Type>(Type.values()));

        cmbBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Type fileType = (Type)cmbBox.getSelectedItem();
                selectedNode.getNode().setTextContent(fileType.value());
                model.nodeChanged(selectedNode);
            }
        });

        cmbBox.addItemListener(new ComboBoxUndoItemListener(cmbBox, undoManager));

        cmbBox.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getModifiers()) {
                case InputEvent.CTRL_MASK:
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
                    break;

                default:
                    break;
                }
            }
        });

        return cmbBox;
    }

    @Override
    public void changeText(String text) {
        for(Type t : Type.values()) {
            if(t.value().equals(text)) {
                cmbBox.setSelectedItem(t);
                break;
            }
        }
    }

}
