package net.sf.fc.gui.v.tree;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XMLTreeModel implements TreeModel {

    private final Document document;
    private final XMLTreeNode root;
    private final List<TreeModelListener> listeners;

    public XMLTreeModel(XMLTreeNode root) {
        document = root.getNode().getOwnerDocument();
        this.root = root;
        listeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((XMLTreeNode)parent).getChildAt(index);
    }
//    public Object getChild(Object parent, int index) {
//        if (parent instanceof DefaultXMLTreeNode) {
//            List<Node> nodes = getChildNodes( ((DefaultXMLTreeNode)parent).getNode() );
//            return new DefaultXMLTreeNode( nodes.get(index) );
//        }
//        else {
//            return null;
//        }
//    }

    @Override
    public int getChildCount(Object parent) {
        return ((XMLTreeNode)parent).getChildCount();
    }
//    public int getChildCount(Object parent) {
//        if (parent instanceof DefaultXMLTreeNode) {
//            List<Node> nodes = getChildNodes( ((DefaultXMLTreeNode)parent).getNode() );
//            return nodes.size();
//        }
//        return 0;
//    }

    @Override
    public boolean isLeaf(Object node) {
//        if (node instanceof DefaultXMLTreeNode) {
//            Node element = ((DefaultXMLTreeNode)node).getNode();
//            List<Node> elements = getChildNodes(element);
//            return elements.size()==0;
//        }
//        else {
//            return true;
//        }
        return !((XMLTreeNode)node).getAllowsChildren();
    }

    /**
     * This implementation will only allow users to change text nodes.
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
//        throw new UnsupportedOperationException();
        DefaultXMLTreeNode node = (DefaultXMLTreeNode)path.getLastPathComponent();
        Node nodeVal = node.getNode();
        if(nodeVal instanceof Text) {
            nodeVal.setTextContent(String.valueOf(newValue));
        }
        // TODO: Implement the nodeChanged method. See DefaultTreeModel.
    }

    /**
     * This implementation will only allow users to change text nodes.
     */
    public void nodeChanged(XMLTreeNode node) {
        if(node != null && node.getNode().getNodeType() == Node.TEXT_NODE) {
            XMLTreeNode parent = node.getParent();
            if(parent != null) { // Parent should never be null, but check anyway.
                int idx = parent.getIndex(node);
                if(idx > -1) {
                    int[] idxs = new int[1];
                    idxs[0] = idx;
                    nodesChanged((DefaultXMLTreeNode)parent, idxs);
                }
            }

//            Node treeNode = node.getNode();
//            Node parent = treeNode.getParentNode();
//            if(parent != null) { // Parent should never be null, but check anyway.
//                NodeList nodeList = parent.getChildNodes();
//                int idx = -1;
//                for(int i = 0; i < nodeList.getLength(); i++) {
//                    if(treeNode == nodeList.item(i)) {
//                        idx = i;
//                        break;
//                    }
//                }
//                if(idx > -1) {
//                    int[] idxs = new int[1];
//                    idxs[0] = idx;
//                    nodesChanged(new DefaultXMLTreeNode(parent), idxs);
//                }
//            }
        }
    }

    public void nodesChanged(XMLTreeNode node, int[] idxs) {
        if(node != null && idxs != null) {
            int ct = idxs.length;

            if(ct > 0) {

                Object[] children = new Object[ct];
                int i = 0;
                for(int idx : idxs) {
                    children[i++] = node.getChildAt(idx);
                }
                fireTreeNodesChanged(this, getPathToRoot(node), idxs, children);

//                NodeList nodeList = node.getNode().getChildNodes();
//                Object[] children = new Object[ct];
//                int i = 0;
//                for(int idx : idxs) {
//                    children[i++] = new DefaultXMLTreeNode(nodeList.item(idx));
//                }
//                fireTreeNodesChanged(this, getPathToRoot(node), idxs, children);
            }
        }
    }

    protected void fireTreeNodesChanged(Object source, Object[] path,
            int[] childIndices,
            Object[] children) {

        for(TreeModelListener l : listeners) {
            l.treeNodesChanged(new TreeModelEvent(source, path, childIndices, children));
        }

    }

//    public DefaultXMLTreeNode[] getPathToRoot(DefaultXMLTreeNode node) {
//        List<DefaultXMLTreeNode> nodes = new ArrayList<>();
//        nodes.add(node);
//        Node treeNode = node.getNode();
//        Node parent;
//        while((parent = treeNode.getParentNode()) != null) {
//            nodes.add(new DefaultXMLTreeNode(parent));
//            /*if(treeNode.getOwnerDocument() == parent) break;
//            else*/ treeNode = parent;
//        }
//
//        return nodes.toArray(new DefaultXMLTreeNode[0]);
//    }

    public XMLTreeNode[] getPathToRoot(XMLTreeNode node) {
        Deque<XMLTreeNode> nodes = new LinkedList<>();
        nodes.addFirst(node);
        XMLTreeNode parent;
        while((parent = node.getParent()) != null) {
            nodes.addFirst(parent);
            node = parent;
        }
        return nodes.toArray(new XMLTreeNode[0]);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return (parent == null || child == null) ? null : ((XMLTreeNode)parent).getIndex((XMLTreeNode)child);
    }
//    public int getIndexOfChild(Object parent, Object child) {
//        if (parent instanceof DefaultXMLTreeNode && child instanceof DefaultXMLTreeNode) {
//            Node pNode = ((DefaultXMLTreeNode)parent).getNode();
//            Node cNode = ((DefaultXMLTreeNode)child).getNode();
//            if (cNode.getParentNode() != pNode) {
//                return -1;
//            }
//            List<Node> nodes = getChildNodes(pNode);
//            return nodes.indexOf(cNode);
//        }
//        return -1;
//    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    private List<Node> getChildNodes(Node node) {
        List<Node> nodes = new ArrayList<>();
        NodeList list = node.getChildNodes();
        for (int i=0 ; i<list.getLength() ; i++) {
            switch(list.item(i).getNodeType()) {
            case Node.ELEMENT_NODE:
            case Node.TEXT_NODE:
                nodes.add(list.item(i));
                break;

            default:
                break;
            }
        }
        return nodes;
    }

    public void insertNodeInto(XMLTreeNode newChild, XMLTreeNode parent, int idx) {
        parent.insert(newChild, idx);

        int[] newIndexs = new int[1];
        newIndexs[0] = idx;
        nodesWereInserted(parent, newIndexs);
    }

    public void removeNodeFromParent(XMLTreeNode treeNode) {
        XMLTreeNode treeParent = treeNode.getParent();
        if(treeParent == null) {
            throw new IllegalArgumentException("node does not have a parent.");
        }

        int[] childIndex = new int[1];
        Object[] removedArray = new Object[1];

        childIndex[0] = treeParent.getIndex(treeNode);
        treeParent.remove(childIndex[0]);
        removedArray[0] = treeNode;
        nodesWereRemoved(treeParent, childIndex, removedArray);
    }

    public void nodesWereInserted(XMLTreeNode node, int[] childIndices) {
        if(node != null && childIndices != null && childIndices.length > 0) {
            Object[] children = new Object[childIndices.length];
            for(int ct = 0; ct < childIndices.length; ct++) {
                children[ct] = node.getChildAt(childIndices[ct]);
            }
            fireTreeNodesInserted(this, getPathToRoot(node), childIndices, children);

//            for(int idx : childIndices) {
//                children[idx] = node.getChildAt(idx);
//                fireTreeNodesInserted(this, getPathToRoot(node), childIndices, children);
//            }
        }
    }

    public void nodesWereRemoved(XMLTreeNode node, int[] childIndices,
            Object[] removedChildren) {
        if(node != null && childIndices != null) {
            fireTreeNodesRemoved(this, getPathToRoot(node), childIndices,
                    removedChildren);
        }
    }

    protected void fireTreeNodesInserted(Object source, Object[] path,
            int[] childIndices, Object[] children) {
        TreeModelEvent e = null;
        for(TreeModelListener listener : listeners) {
            if(e == null) {
                // Lazily create the event.
                e = new TreeModelEvent(source, path, childIndices, children);
            }
            listener.treeNodesInserted(e);
        }
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path,
            int[] childIndices, Object[] children) {
        TreeModelEvent e = null;
        for(TreeModelListener listener : listeners) {
            if(e == null) {
                // Lazily create the event.
                e = new TreeModelEvent(source, path, childIndices, children);
            }
            listener.treeNodesRemoved(e);
        }
    }

}
