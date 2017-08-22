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
import net.sf.fc.gui.c.Retriever;
import net.sf.fc.gui.c.TextChanger;
import net.sf.fc.gui.c.copy.CopyController;
import net.sf.fc.gui.c.copy.CopyProperty;
import net.sf.fc.gui.v.event.ComboBoxUndoItemListener;
import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.script.gen.options.RenameType;

public class RenameTypeEditPanel extends OptionsEditPanel {

    private final JComboBox<RenameType> cmbBoxRenameType;

    public RenameTypeEditPanel(XMLTreeModel model, Retriever retriever) {
        super(model, retriever);

        setLayout(new MigLayout("", "[grow]", "[][]"));
        cmbBoxRenameType = initCmbBoxRenameType();
        add(cmbBoxRenameType, "cell 0 0, growx");
        add(btnUseDefault, "cell 0 1, growx");
    }

    @Override
    public void changeText(String text) {
        for(RenameType rt : RenameType.values()) {
            if(rt.value().equals(text)) {
                cmbBoxRenameType.setSelectedItem(rt);
                break;
            }
        }
    }

    private JComboBox<RenameType> initCmbBoxRenameType() {
        final JComboBox<RenameType> cmbBox = new JComboBox<>();
        cmbBox.setModel(new DefaultComboBoxModel<RenameType>(RenameType.values()));

        cmbBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RenameType renameType = (RenameType)cmbBox.getSelectedItem();
                selectedNode.getNode().setTextContent(renameType.value());
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
}
