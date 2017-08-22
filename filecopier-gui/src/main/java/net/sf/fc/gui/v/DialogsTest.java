package net.sf.fc.gui.v;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import net.sf.fc.cfg.AppFactory;
import net.sf.fc.cfg.DirPath;
import net.sf.fc.gui.factory.MVCFactory;
import net.sf.fc.gui.factory.MVCRequestFactory;
import net.sf.fc.script.SettingsProxy;

import org.xml.sax.SAXException;

public class DialogsTest {

    private JMenuBar createMenuBar(final MVCFactory mvcFactory,
            final MVCRequestFactory mvcReqFactory, final SettingsProxy settingsProxy) {

        final JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu settingsMenu = new JMenu("Settings");
        settingsMenu.setMnemonic(KeyEvent.VK_S);
        JMenuItem settingsItem = new JMenuItem("App Settings");
        settingsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        settingsItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mvcFactory.getSettingsDialog().setVisible(true);
            }
        });
        settingsMenu.add(settingsItem);

        JMenu optionsMenu = new JMenu("Copy Options");
        optionsMenu.setMnemonic(KeyEvent.VK_C);
        JMenuItem defaultOptItem = new JMenuItem("Defaults");
        defaultOptItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
        defaultOptItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mvcFactory.getDefaultOptionsDialog().setVisible(true);
            }
        });
        optionsMenu.add(defaultOptItem);

        JMenuItem newOptItem = new JMenuItem("New");
        newOptItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        newOptItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mvcReqFactory.getOptionsDialog().setVisible(true);
                } catch (JAXBException | IOException | SAXException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        optionsMenu.add(newOptItem);


        JMenuItem openOptItem = new JMenuItem("Open");
        openOptItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        openOptItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser chooser = new JFileChooser();
                    DirPath optionsPath = new DirPath(settingsProxy.getPaths().getOptionsPath());
                    chooser.setCurrentDirectory(new File(optionsPath.getPathAt(0)));
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(menuBar.getParent());
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        mvcReqFactory.getOptionsDialog(chooser.getSelectedFile()).setVisible(true);
                    }
                } catch (JAXBException|SAXException|IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        optionsMenu.add(openOptItem);


        settingsMenu.add(optionsMenu);
        menuBar.add(settingsMenu);

        return menuBar;
    }

    public Container createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        return contentPane;
    }

    private static void createAndShowGUI() {
        try {
            AppFactory appFactory = new AppFactory();
            MVCFactory mvcFactory = new MVCFactory(appFactory);
            MVCRequestFactory mvcReqFactory = mvcFactory.getMVCRequestFactory();
            DialogsTest dlgsTest = new DialogsTest();

            JFrame frame = new JFrame("Dialogs Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setJMenuBar(dlgsTest.createMenuBar(mvcFactory, mvcReqFactory, appFactory.getSettingsProxy()));
            frame.setContentPane(dlgsTest.createContentPane());

            frame.setSize(450, 260);
            frame.setVisible(true);
        } catch (JAXBException|SAXException|IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
