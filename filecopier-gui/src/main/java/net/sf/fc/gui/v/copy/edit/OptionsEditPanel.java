package net.sf.fc.gui.v.copy.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import net.sf.fc.gui.c.Retriever;
import net.sf.fc.gui.v.tree.XMLTreeModel;

public abstract class OptionsEditPanel extends EditPanel {

    protected final JButton btnUseDefault;
    protected final Retriever retriever;

    public OptionsEditPanel(XMLTreeModel model, Retriever retriever) {
        super(model);
        this.retriever = retriever;
        btnUseDefault = initBtn();
    }

    private JButton initBtn() {
        JButton btn = new JButton("Use Default");
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                retriever.retrieve();
            }
        });

        return btn;
    }

}
