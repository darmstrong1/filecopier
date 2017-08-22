package net.sf.fc.gui.v.copy.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.undo.UndoManager;

import net.sf.fc.gui.c.TextChanger;
import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.gui.v.tree.XMLTreeNode;

public abstract class EditPanel extends JPanel implements TextChanger {

    protected XMLTreeNode selectedNode;
    protected final XMLTreeModel model;
    protected final UndoManager undoManager;

    public EditPanel(XMLTreeModel model) {
        this.model = model;
        undoManager = new UndoManager();
    }

    public void setTreeNode(XMLTreeNode node) {
        if(node != selectedNode) {
            this.selectedNode = node;
        }
    }

    public void discardAllEdits() {
        undoManager.discardAllEdits();
    }

    @Override
    public abstract void changeText(String text);
}
