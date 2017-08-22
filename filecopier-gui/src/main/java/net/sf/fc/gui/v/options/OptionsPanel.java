package net.sf.fc.gui.v.options;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.undo.UndoManager;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class OptionsPanel extends JPanel {

    /**
     * Create the panel.
     */
    public OptionsPanel(JPanel copyOptionsPanel, JPanel fileExistOptionsPanel, JPanel srcDirOptionsPanel, UndoManager undoManager) {

        setLayout(new MigLayout("", "[grow]", "[grow][grow][grow]"));
        copyOptionsPanel.setBorder(new TitledBorder(null, "Copy Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(copyOptionsPanel, "cell 0 0,growx,aligny top");
        fileExistOptionsPanel.setBorder(new TitledBorder(null, "Existing Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(fileExistOptionsPanel, "cell 0 1,grow");
        srcDirOptionsPanel.setBorder(new TitledBorder(null, "Source Directories", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(srcDirOptionsPanel, "cell 0 2,grow");
    }

}
