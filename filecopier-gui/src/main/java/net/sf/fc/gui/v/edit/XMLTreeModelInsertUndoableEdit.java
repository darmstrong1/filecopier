package net.sf.fc.gui.v.edit;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.gui.v.tree.XMLTreeNode;
import net.sf.fc.gui.v.util.JTreeUtil;

@SuppressWarnings("serial")
public class XMLTreeModelInsertUndoableEdit extends BasicUndoableEdit {

    private final JTree tree;
    private final XMLTreeModel treeModel;
    private final XMLTreeNode parent;
    private final XMLTreeNode child;
    private final int idx;

    public XMLTreeModelInsertUndoableEdit(JTree tree, XMLTreeNode parent, XMLTreeNode child, int idx) {
        this.tree = tree;
        treeModel = (XMLTreeModel)tree.getModel();
        this.parent = parent;
        this.child = child;
        this.idx = idx;
    }

    @Override
    public void undo() throws CannotUndoException {
        System.out.println("In XMLTreeModelInsertUndoableEdit's undo");
        super.undo();
        treeModel.removeNodeFromParent(child);

        TreePath path = new TreePath(parent.getPath());
        tree.setSelectionPath(path);
    }


    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        treeModel.insertNodeInto(child, parent, idx);

        TreePath path = new TreePath(child.getPath());
        tree.setSelectionPath(path);
        JTreeUtil.expandAll(tree, path);
    }
}
