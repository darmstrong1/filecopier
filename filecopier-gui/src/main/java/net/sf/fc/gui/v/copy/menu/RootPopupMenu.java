package net.sf.fc.gui.v.copy.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.undo.UndoManager;

import org.w3c.dom.Node;

import net.sf.fc.gui.c.copy.CopyController;
import net.sf.fc.gui.c.copy.CopyProperty;
import net.sf.fc.gui.v.View;
import net.sf.fc.gui.v.edit.XMLTreeModelInsertUndoableEdit;
import net.sf.fc.gui.v.tree.CopyScriptXMLTreeNode;
import net.sf.fc.gui.v.tree.XMLTreeNode;
import net.sf.fc.gui.v.util.JTreeUtil;
import net.sf.fc.util.CopyScriptXmlUtil;

@SuppressWarnings("serial")
public class RootPopupMenu extends CopyScriptPopupMenu implements View {

    private final CopyController copyController;
    private final JMenuItem addMapItm;
    private String lastSrcDirPath;
    private boolean getLastPaths = false;

    public RootPopupMenu(CopyController copyController, JTree tree, UndoManager undoManager) {
        super(tree, undoManager);
        this.copyController = copyController;
        addMapItm = createAddMapItm();
        add(addMapItm);
        copyController.addView(this);
    }

    private JMenuItem createAddMapItm() {
        JMenuItem menuItem = createMenuItem(MenuItemData.ADD_MAP);
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getLastPaths = true;
                copyController.retrieveLastSrcDirPath();
                copyController.retrieveLastDstDirPath();
            }
        });
        return menuItem;
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if(getLastPaths) {
            try {
                CopyProperty cpProperty = CopyProperty.valueOf(evt.getPropertyName());
                switch(cpProperty) {
                case LastSrcDirPath:
                    lastSrcDirPath = (String)evt.getNewValue();
                    break;

                case LastDstDirPath:
                    getLastPaths = false;
                    Node node = treeNode.getNode();
                    List<String> lastPaths = new ArrayList<>();
                    lastPaths.add(lastSrcDirPath);
                    lastPaths.add((String)evt.getNewValue());

                    Node mapNode = CopyScriptXmlUtil.createMapElement(node.getOwnerDocument(), lastPaths);
                    XMLTreeNode mapTreeNode = new CopyScriptXMLTreeNode(treeNode, mapNode);

                    int idx = treeNode.getChildCount();
                    treeModel.insertNodeInto(mapTreeNode, treeNode, idx);

                    TreePath path = new TreePath(mapTreeNode.getPath());
                    JTreeUtil.setExpandScrollTo(tree, path);

                    undoManager.addEdit(new XMLTreeModelInsertUndoableEdit(tree, treeNode, mapTreeNode, idx));
                    break;
                }

            } catch(IllegalArgumentException ignored){}
        }
    }

}
