package net.sf.fc.gui.v.tree;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

public class XMLTreePanelTest extends JPanel {

    private JTree tree;
    private XMLTreeModel model;
    private final Document document;

    private XMLTreeNode selectedNode;

    public XMLTreePanelTest(Document doc) {
        setLayout(new BorderLayout());

        document = doc;
        model = initModel(document);
        tree = initTree(model);
        //tree.setModel(model);
        tree.setShowsRootHandles(true);
        tree.setEditable(false);

        JScrollPane pane = new JScrollPane(tree);
        pane.setPreferredSize(new Dimension(300,400));

        add(pane, "Center");

        final JTextField text = new JTextField();
        text.setEditable(true);
        add(text, "South");

        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Object lpc = e.getPath().getLastPathComponent();
                if (isText(lpc)) {
                    selectedNode = (XMLTreeNode)lpc;
                    text.setText( selectedNode.toString() );
                } else {
                    selectedNode = null;
                    text.setText("");
                }
            }
        });

        text.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                setNodeText(e.getDocument());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setNodeText(e.getDocument());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setNodeText(e.getDocument());
            }

            private void setNodeText(javax.swing.text.Document doc) {
                if(doc != null && selectedNode != null) {
                    try {
                        selectedNode.getNode().setTextContent(doc.getText(0, doc.getLength()));
                        model.nodeChanged(selectedNode);
                        //tree.revalidate();
                        //tree.repaint();
                        System.out.println(selectedNode.getNode().getTextContent());
                    } catch (DOMException | BadLocationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static XMLTreeModel initModel(Document doc) {
        XMLTreeModel model = new XMLTreeModel(new CopyScriptXMLTreeNode(doc.getFirstChild()));
        return model;
    }

    private static JTree initTree(XMLTreeModel model) {
        JTree tree = new JTree(model);
        return tree;
    }

    private static boolean isText(Object selected) {
        return (selected instanceof DefaultXMLTreeNode && ((DefaultXMLTreeNode)selected).getNode() instanceof Text);
    }

}
