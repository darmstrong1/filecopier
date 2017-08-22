package net.sf.fc.gui.v.tree;

import java.util.List;

import org.w3c.dom.Node;

public interface XMLTreeNode {

    public Node getNode();

    public XMLTreeNode getChildAt(int idx);

    public int getChildCount();

    public XMLTreeNode getParent();

    public int getIndex(XMLTreeNode child);

    public boolean getAllowsChildren();

    public boolean isLeaf();

    public List<XMLTreeNode> children();

    public void remove(int idx);

    public void insert(XMLTreeNode child, int idx);

    public XMLTreeNode[] getPath();
}
