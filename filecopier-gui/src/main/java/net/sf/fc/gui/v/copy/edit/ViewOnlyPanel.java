package net.sf.fc.gui.v.copy.edit;

import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.v.tree.XMLTreeModel;

public class ViewOnlyPanel extends TxtFldEditPanel {

    public ViewOnlyPanel(XMLTreeModel model) {
        super(model);
        txtFld = initDefaultTxtFld();

        setLayout(new MigLayout("", "[grow]", "[]"));

        add(txtFld, "cell 0 0,growx");
    }

    private JTextField initDefaultTxtFld() {
        JTextField txtFld = new JTextField();
        txtFld.setColumns(10);
        txtFld.setEnabled(false);
        return txtFld;
    }

}
