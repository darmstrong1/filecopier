package net.sf.fc.gui.v.tree;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.fc.cfg.AppFactory;
import net.sf.fc.script.CopyScriptProxy;
import net.sf.fc.script.SchemaData;
import net.sf.fc.script.ScriptHelper;
import net.sf.fc.script.gen.copy.CopyScript;
import net.sf.fc.script.gen.options.OptionsScript;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLTreeModelTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        final File xmlFile = args.length > 0 ? new File(args[0]) : null;

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI(xmlFile);
            }
        });
    }

    private static void createAndShowGUI(File xmlFile) {
        Document document = null;
//        try {
//            String fileSep = System.getProperty("file.separator");
//            document = parseFile(new File(System.getProperty("user.home") +
//                    fileSep + "filecopier" + fileSep + "cfg" + fileSep + "copyScript.xml"));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            System.exit(1);
//        }

        try {
            AppFactory appFactory = new AppFactory();
            ScriptHelper<CopyScript> copyScriptHelper = appFactory.getCopyScriptHelper();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            document = builder.newDocument();

            if(xmlFile != null) {
                CopyScript cs = copyScriptHelper.unmarshal(xmlFile);
                copyScriptHelper.marshal(document, cs);
            } else {
                CopyScriptProxy copyScriptProxy = new CopyScriptProxy(copyScriptHelper,
                        appFactory.getRequestFactory().getDiskDefaultOptions());
                copyScriptHelper.marshal(document, copyScriptProxy.getDefaultCopyScript());
            }
        } catch (SAXException | JAXBException | IOException | ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }

        XMLTreePanelTest panel = new XMLTreePanelTest(document);

        JFrame frame = new JFrame("XMLTreeModelTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }


    private static Document parseFile(File xmlFile) throws IOException, SAXException, ParserConfigurationException {

        FileReader fr = null;
        BufferedReader br = null;
        InputStream is = null;
        Document document;
        try {
            fr = new FileReader(xmlFile);
            br = new BufferedReader(fr);
            String line;
            StringBuilder sb = new StringBuilder();

            while((line = br.readLine()) != null) {
                sb.append(line.trim());
            }

            is = new ByteArrayInputStream(sb.toString().getBytes());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            document = builder.parse(is);
            document.normalize();
        } finally {
            try { fr.close(); } catch(Exception ignored) {}
            try { br.close(); } catch(Exception ignored) {}
            try { is.close(); } catch(Exception ignored) {}
        }
        return document;
    }

}
