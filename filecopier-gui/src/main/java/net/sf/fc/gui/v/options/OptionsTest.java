package net.sf.fc.gui.v.options;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JDialog;
import javax.xml.bind.JAXBException;

import net.sf.fc.cfg.AppFactory;
import net.sf.fc.gui.factory.MVCFactory;
import net.sf.fc.gui.factory.MVCRequestFactory;

import org.xml.sax.SAXException;

public class OptionsTest {

    private static boolean isDefault;

    /**
     * @param args
     */
    public static void main(String[] args) {
        isDefault = args.length >= 1 ? Boolean.valueOf(args[0]) : true;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
         try {
             AppFactory appFactory = new AppFactory();
             MVCFactory mvcAppFactory = new MVCFactory(appFactory);
             OptionsDialog optionsDialog;
             if(isDefault) {
                 optionsDialog = mvcAppFactory.getDefaultOptionsDialog();
             } else {
                 MVCRequestFactory mvcRequestFactory = mvcAppFactory.getMVCRequestFactory();
                 optionsDialog = mvcRequestFactory.getOptionsDialog();
             }

             //Display the window.
             optionsDialog.setVisible(true);
             optionsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         } catch(IOException|SAXException|JAXBException e) {
             e.printStackTrace();
         }
    }

}
