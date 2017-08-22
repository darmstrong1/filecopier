package net.sf.fc.gui.v.copy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.print.Doc;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.fc.cfg.AppFactory;
import net.sf.fc.gui.factory.MVCFactory;
import net.sf.fc.gui.factory.MVCRequestFactory;
import net.sf.fc.gui.v.tree.CopyScriptXMLTreeNode;
import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.script.CopyScriptProxy;
import net.sf.fc.script.ScriptHelper;
import net.sf.fc.script.gen.copy.CopyScript;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CopyPanelTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        final File[] xmlFiles = new File[2];
        xmlFiles[0] = args.length > 0 ? new File(args[0]) : null;
        xmlFiles[1] = args.length > 1 ? new File(args[1]) : null;

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI(xmlFiles);
            }
        });
    }

    private static void createAndShowGUI(final File[] xmlFiles) {

        try {
            AppFactory appFactory = new AppFactory();
            final ScriptHelper<CopyScript> copyScriptHelper = appFactory.getCopyScriptHelper();
            final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document document = builder.newDocument();
            String title;

            if(xmlFiles[0] != null) {
                CopyScript cs = copyScriptHelper.unmarshal(xmlFiles[0]);
                copyScriptHelper.marshal(document, cs);
                title = xmlFiles[0].getPath();
            } else {
                CopyScriptProxy copyScriptProxy = new CopyScriptProxy(copyScriptHelper,
                        appFactory.getRequestFactory().getDiskDefaultOptions());
                copyScriptHelper.marshal(document, copyScriptProxy.getDefaultCopyScript());
                title = "XMLTreeModelTest";
            }

            MVCFactory mvcFactory = new MVCFactory(appFactory);
            MVCRequestFactory mvcReqFactory = mvcFactory.getMVCRequestFactory();

            final JPanel panel = mvcReqFactory.getCopyPanel(document);

            final JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(300,400));

            frame.setLayout(new BorderLayout());

            frame.add(panel, BorderLayout.CENTER);
            if(xmlFiles[1] != null) {
                JButton btnChange = new JButton("Change");
                btnChange.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            changeFile(frame, xmlFiles, copyScriptHelper, builder, (CopyPanel)panel);
                        } catch (JAXBException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                });
                frame.add(btnChange, BorderLayout.SOUTH);
            }

            frame.pack();
            frame.setVisible(true);

        } catch (SAXException | JAXBException | IOException | ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void changeFile(JFrame frame, File[] xmlFiles, ScriptHelper<CopyScript> copyScriptHelper,
            DocumentBuilder builder, CopyPanel copyPanel) throws JAXBException {
        int idx = frame.getTitle() == xmlFiles[0].getPath() ? 1 : 0;
        CopyScript cs = copyScriptHelper.unmarshal(xmlFiles[idx]);
        Document document = builder.newDocument();
        copyScriptHelper.marshal(document, cs);

        XMLTreeModel treeModel = new XMLTreeModel(new CopyScriptXMLTreeNode(document.getFirstChild()));
        copyPanel.setModel(treeModel);
        frame.setTitle(xmlFiles[idx].getPath());

    }

}
