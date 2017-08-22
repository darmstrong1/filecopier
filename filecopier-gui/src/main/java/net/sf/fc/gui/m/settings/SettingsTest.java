package net.sf.fc.gui.m.settings;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JDialog;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import net.sf.fc.cfg.AppFactory;
import net.sf.fc.gui.factory.MVCFactory;
import net.sf.fc.gui.v.settings.SettingsDialog;

public class SettingsTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        try {
            MVCFactory mvcAppFactory = new MVCFactory(new AppFactory());
            SettingsDialog settingsDialog = mvcAppFactory.getSettingsDialog();
            //Display the window.
            settingsDialog.setVisible(true);
            settingsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        } catch (JAXBException|SAXException|IOException e) {
            e.printStackTrace();
        }

    }

}
