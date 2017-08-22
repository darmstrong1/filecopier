package net.sf.fc.gui.v.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import net.sf.fc.gui.v.tree.XMLTreeNode;

public class JTreeUtil {

    /**
     * Expands all children of a TreePath
     * @param tree
     * @param parent
     */
    public static void expandAll(JTree tree, TreePath parent) {
        expandAll(tree, parent, true);
    }

    /**
     * Collapses all children of a TreePath
     * @param tree
     * @param parent
     */
    public static void collapseAll(JTree tree, TreePath parent) {
        expandAll(tree, parent, false);
    }

    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        XMLTreeNode node = (XMLTreeNode)parent.getLastPathComponent();
        if(node.getChildCount() > 0) {
            for(XMLTreeNode child : node.children()) {
                TreePath path = parent.pathByAddingChild(child);
                expandAll(tree, path, expand);
            }
        }

        if(expand) tree.expandPath(parent);
        else tree.collapsePath(parent);
    }

    public static void setExpandScrollTo(JTree tree, TreePath path) {
        tree.setSelectionPath(path);
        JTreeUtil.expandAll(tree, path);
        tree.scrollPathToVisible(path);
    }

    public static List<TreePath> findTreePathsByName(XMLTreeNode root, String name) {
        List<TreePath> treePaths = new ArrayList<>();
        findTreePathsByName(root, name, treePaths);

        return treePaths;
    }

    private static void findTreePathsByName(XMLTreeNode node, String name, List<TreePath> treePaths) {

        if(node.toString().equals(name)) {
            treePaths.add(new TreePath(node.getPath()));
        }

        for(XMLTreeNode child : node.children()) {
            findTreePathsByName(child, name, treePaths);
        }
    }

}
