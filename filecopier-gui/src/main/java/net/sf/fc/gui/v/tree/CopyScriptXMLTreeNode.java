package net.sf.fc.gui.v.tree;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class CopyScriptXMLTreeNode extends DefaultXMLTreeNode {

    public CopyScriptXMLTreeNode(Node xmlNode) {
        super(xmlNode);
    }

    public CopyScriptXMLTreeNode(XMLTreeNode parent, Node xmlNode) {
        super(parent, xmlNode);
    }

    protected List<XMLTreeNode> initChildren() {
        List<XMLTreeNode> nodes = new ArrayList<>();
        NodeList nodeList = xmlNode.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            XMLTreeNode node = new CopyScriptXMLTreeNode(this, nodeList.item(i));
            nodes.add(node);
        }
        return nodes;
    }


    public String toString() {
        return (xmlNode.getNodeType() == Node.TEXT_NODE)
                ? ((Text)xmlNode).getTextContent()
                : xmlNode.getNodeName().startsWith("ns2:") ? xmlNode.getNodeName().substring(4) : xmlNode.getNodeName();
    }

}
