package net.sf.fc.gui.v.options;

import java.beans.PropertyChangeEvent;

import javax.swing.JPanel;

import net.sf.fc.gui.c.FlagChanger;
import net.sf.fc.gui.c.options.OptionsController;
import net.sf.fc.gui.c.options.OptionsProperty;
import net.sf.fc.gui.v.AbstractViewPanel;
import net.sf.fc.gui.v.edit.CheckBoxUndoableEdit;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;
import javax.swing.undo.UndoManager;

@SuppressWarnings("serial")
public class CopyOptionsPanel extends AbstractViewPanel {

    private final JCheckBox chckbxCopyAttributes;
    private final JCheckBox chckbxFollowLinks;

    /**
     * Create the panel.
     */
    public CopyOptionsPanel(final OptionsController optionsController, boolean initialCopyAttributes,
            boolean initialFollowLinks, final UndoManager undoManager) {

        setLayout(new MigLayout("", "[]", "[][]"));

        chckbxCopyAttributes = initCheckBox("Copy Attributes", new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                optionsController.changeCopyAttributes(flag);
                undoManager.addEdit(new CheckBoxUndoableEdit(chckbxCopyAttributes.getModel(),
                        chckbxCopyAttributes.isSelected(), new FlagChanger() {

                            @Override
                            public void changeFlag(boolean flag) {
                                optionsController.changeCopyAttributes(flag);
                            }
                        }));
            }
        }, initialCopyAttributes);
        add(chckbxCopyAttributes, "cell 0 0");

        chckbxFollowLinks = initCheckBox("Follow Links", new FlagChanger() {

            @Override
            public void changeFlag(boolean flag) {
                optionsController.changeFollowLinks(flag);
                undoManager.addEdit(new CheckBoxUndoableEdit(chckbxFollowLinks.getModel(),
                        chckbxFollowLinks.isSelected(), new FlagChanger() {

                            @Override
                            public void changeFlag(boolean flag) {
                                optionsController.changeFollowLinks(flag);
                            }
                        }));
            }
        }, initialFollowLinks);

        add(chckbxFollowLinks, "cell 0 1");

        optionsController.addView(this);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        try {
            switch(OptionsProperty.valueOf(evt.getPropertyName())) {
            case CopyAttributes:
                if(!Boolean.valueOf(chckbxCopyAttributes.isSelected()).equals(evt.getNewValue())) {
                    chckbxCopyAttributes.setSelected((Boolean)evt.getNewValue());
                }
                break;
            case FollowLinks:
                if(!Boolean.valueOf(chckbxFollowLinks.isSelected()).equals(evt.getNewValue())) {
                    chckbxFollowLinks.setSelected((Boolean)evt.getNewValue());
                }
                break;

            default:
                break;
            }
        } catch(IllegalArgumentException ignored) {
        }
    }

}
