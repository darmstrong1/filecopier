package net.sf.fc.gui.v.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DefaultXMLTreeNode implements XMLTreeNode {

    protected final Node xmlNode;
    protected final XMLTreeNode parent;
    protected final List<XMLTreeNode> children;

    public DefaultXMLTreeNode(Node xmlNode) {
        this.xmlNode = xmlNode;
        parent = null;
        children = initChildren();
    }

    public DefaultXMLTreeNode(XMLTreeNode parent, Node xmlNode) {
        this.parent = parent;
        this.xmlNode = xmlNode;
        children = initChildren();
    }

    protected List<XMLTreeNode> initChildren() {
        List<XMLTreeNode> nodes = new ArrayList<>();
        NodeList nodeList = xmlNode.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            XMLTreeNode node = new DefaultXMLTreeNode(this, nodeList.item(i));
            nodes.add(node);
        }
        return nodes;
    }

    public Node getNode() {
        return xmlNode;
    }

    public String toString() {
        return (xmlNode.getNodeType() == Node.TEXT_NODE) ? ((Text)xmlNode).getTextContent() : xmlNode.getNodeName();
    }

    public String getText() {
        return toString();
    }

    @Override
    public XMLTreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public XMLTreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(XMLTreeNode node) {
        if(node == null) {
            throw new IllegalArgumentException("argument is null");
        }
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return xmlNode.getNodeType() == Node.ELEMENT_NODE || xmlNode.getNodeType() == Node.DOCUMENT_NODE;
    }

    @Override
    public boolean isLeaf() {
        return children.size() == 0;
    }

    @Override
    public List<XMLTreeNode> children() {
        return children;
    }

    @Override
    public void remove(int idx) {
        // First, remove the node from the XML structure.
        Node childXmlNode = children.get(idx).getNode();
        xmlNode.removeChild(childXmlNode);
        // Now, remove the XMLTreeNode.
        children.remove(idx);
    }

    @Override
    public void insert(XMLTreeNode child, int idx) {
        if(!getAllowsChildren()) {
            throw new IllegalStateException("node does not allow children");
        }
        // When the child is created, it should have set this XMLTreeNode as its parent.
        // Check that it did. If it did not, throw an IllegalStateException.
        if(!child.getParent().equals(this)) {
            throw new IllegalStateException("Child must be constructed with this as its parent.");
        }
        // Explicitly check to see if idx is out of range. If it is, throw an IndexOutOfBoundsException.
        if(idx < 0 || idx > children.size()) {
            throw new IndexOutOfBoundsException("idx must be between 0 and the count of children minus one.");
        }
        // First, get the node in the XML structure of the idx. If idx is equal to children.size(), xmlChildren
        // should have the same number of children, so item(idx) will return null. Calling Node's insertBefore
        // with null as the refChild will append newChild to the end of the list.
        NodeList xmlChildren = xmlNode.getChildNodes();
        Node xmlChild = xmlChildren.item(idx);
        // Add the node in the XML structure.
        xmlNode.insertBefore(child.getNode(), xmlChild);
        // Now, insert the XMLTreeNode.
        children.add(idx, child);
    }

    @Override
    public XMLTreeNode[] getPath() {
        return getPathToRoot(this, 0);
    }

    private XMLTreeNode[] getPathToRoot(XMLTreeNode node, int depth) {
        XMLTreeNode[] nodes = null;

        if(node == null) {
            if(depth > 0) nodes = new XMLTreeNode[depth];
        } else {
            depth++;
            nodes = getPathToRoot(node.getParent(), depth);
            nodes[nodes.length - depth] = node;
        }

        return nodes;
    }

}
