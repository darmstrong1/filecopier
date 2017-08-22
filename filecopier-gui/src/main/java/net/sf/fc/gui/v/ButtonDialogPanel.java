package net.sf.fc.gui.v;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import net.sf.fc.gui.v.options.OptionsDialog;

/**
 * ButtonDialogPanel is used by the SettingsDialog and OptionsDialogs. It has an OK button, Cancel button, and Use Defaults button.
 * @author david
 *
 */
@SuppressWarnings("serial")
public class ButtonDialogPanel extends JPanel {

    private final JButton okBtn;

    /**
     * Constructor
     * @param dialog
     * @param valueSetter
     */
    public ButtonDialogPanel(List<ActionListener> actionListeners) {
        if(actionListeners == null) {
            throw new IllegalArgumentException("actionListeners must not be null.");
        }
        if(actionListeners.size() != OptionsDialog.CANCEL_IDX+1) {
            throw new IllegalArgumentException("actionListeners must contain three ActionListeners; one for the 'Use Defaults' button, one for the Ok button, and one for the Cancel button.");
        }

        setLayout(new MigLayout("", "[103px][103px][103px]", "[25px]"));
        JButton btnUseDefaults = initUseDefaultsBtn(actionListeners.get(OptionsDialog.DEFAULTS_IDX));
        add(btnUseDefaults, "cell 0 0");
        okBtn = initOkBtn(actionListeners.get(OptionsDialog.OK_IDX));
        add(okBtn, "cell 1 0,growx,aligny top");
        JButton cancelBtn = initCancelBtn(actionListeners.get(OptionsDialog.CANCEL_IDX));
        add(cancelBtn, "cell 2 0,growx,aligny top");
    }

    public JButton getDefaultButton() {
        return okBtn;
    }

    private JButton initUseDefaultsBtn(ActionListener actionListener) {
        JButton btn = new JButton("Use Defaults");
        btn.addActionListener(actionListener);
        return btn;
    }

    private JButton initOkBtn(ActionListener actionListener) {
        JButton btn = new JButton("OK");
        btn.setActionCommand("OK");
        btn.addActionListener(actionListener);

        return btn;
    }

    private JButton initCancelBtn(ActionListener actionListener) {
        JButton btn = new JButton("Cancel");
        btn.setActionCommand("Cancel");
        btn.addActionListener(actionListener);

        return btn;
    }

    //TODO: Add addActionListener, removeActionListener methods for each button.

}
