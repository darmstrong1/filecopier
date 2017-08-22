package net.sf.fc.gui.v.copy.edit;

import javax.swing.JTextField;
import net.sf.fc.gui.v.tree.XMLTreeModel;

public class TxtFldEditPanel extends EditPanel {

    protected JTextField txtFld;

    public TxtFldEditPanel(XMLTreeModel model) {
        super(model);
    }

    @Override
    public void changeText(String text) {
        txtFld.setText(text);
    }
}
