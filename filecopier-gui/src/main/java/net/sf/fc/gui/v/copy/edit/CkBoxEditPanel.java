package net.sf.fc.gui.v.copy.edit;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.c.FlagChanger;
import net.sf.fc.gui.c.Retriever;
import net.sf.fc.gui.v.edit.CheckBoxUndoableEdit;
import net.sf.fc.gui.v.tree.XMLTreeModel;

public class CkBoxEditPanel extends OptionsEditPanel {

    private final JCheckBox ckBox;

    public CkBoxEditPanel(XMLTreeModel model, Retriever retriever, String text) {
        super(model, retriever);
        ckBox = initCkBox(text);

        setLayout(new MigLayout("", "[grow]", "[][]"));
        add(ckBox, "cell 0 0, growx");
        add(btnUseDefault, "cell 0 1, growx");
    }

    private JCheckBox initCkBox(String text) {
        final JCheckBox ckBox = new JCheckBox();
        ckBox.setText(text);

        ckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectedNode.getNode().setTextContent(ckBox.isSelected() ? "true" : "false");
                model.nodeChanged(selectedNode);
                undoManager.addEdit(new CheckBoxUndoableEdit(ckBox.getModel(), ckBox.isSelected(), initUndoFlagChanger()));
            }
        });

        // Activate CTRL-Z (undo) and CTRL-Y (redo)
        ckBox.addKeyListener(new KeyAdapter() {
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

        return ckBox;
    }

    private FlagChanger initUndoFlagChanger() {
        return new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                selectedNode.getNode().setTextContent(flag ? "true" : "false");
                model.nodeChanged(selectedNode);
            }
        };
    }

    @Override
    public void changeText(String text) {
        // We must call doClick because setSelected does not fire an ActionEvent. Call doClick only if the text value,
        // which will either be "true" or "false", does not match with the check box's selected state.
        if(("true".equals(text) && !ckBox.isSelected()) || ("false".equals(text) && ckBox.isSelected())) ckBox.doClick();
    }

}
