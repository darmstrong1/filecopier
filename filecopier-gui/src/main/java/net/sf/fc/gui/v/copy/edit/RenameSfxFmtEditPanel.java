package net.sf.fc.gui.v.copy.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.c.Retriever;
import net.sf.fc.gui.v.options.RenameSfxFmtPanel;
import net.sf.fc.gui.v.tree.XMLTreeModel;

public class RenameSfxFmtEditPanel extends OptionsEditPanel {

    private final RenameSfxFmtPanel pnlRenameSfxFmt;

    public RenameSfxFmtEditPanel(XMLTreeModel model, Retriever retriever) {
        super(model, retriever);

        pnlRenameSfxFmt = initRenameSfxFmtPanel();

        setLayout(new MigLayout("", "[grow]", "[][]"));
        add(pnlRenameSfxFmt, "cell 0 0, growx");
        add(btnUseDefault, "cell 0 1,growx");
    }

    private RenameSfxFmtPanel initRenameSfxFmtPanel() {

        RenameSfxFmtPanel pnl = new RenameSfxFmtPanel(undoManager);
        pnl.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if("renameFmtString".equals(evt.getPropertyName())) {
                    selectedNode.getNode().setTextContent((String)evt.getNewValue());
                    model.nodeChanged(selectedNode);
                }
            }
        });

        return pnl;
    }

    @Override
    public void changeText(String text) {
        pnlRenameSfxFmt.getTextField().setText(text);
    }

}
