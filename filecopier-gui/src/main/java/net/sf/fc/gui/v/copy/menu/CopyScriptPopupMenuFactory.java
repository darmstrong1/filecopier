package net.sf.fc.gui.v.copy.menu;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.undo.UndoManager;

import org.w3c.dom.Element;

import net.sf.fc.cfg.RequestFactory;
import net.sf.fc.gui.c.copy.CopyController;
import net.sf.fc.gui.v.tree.XMLTreeNode;

@SuppressWarnings("serial")
public class CopyScriptPopupMenuFactory extends CopyScriptPopupMenu {

    private static enum Type {
        NONE,
        DST,
        DST_DIR_PATH,
        DST_FILE_PATH,
        DST_VAL_DIR_PATH,
        DST_VAL_FILE_PATH,
        SRC,
        SRC_PATH,
        SRC_VAL_PATH,
        MAP,
        COPY_SCRIPT,
        SRC_OPTIONS,
        DST_OPTIONS
    }

    private final Map<Type, CopyScriptPopupMenu> popupMenuMap = new HashMap<>();
    private final CopyController copyController;
    private CopyScriptPopupMenu activePopupMenu;
    private final CopyScriptPopupMenu defaultPopupMenu;

    public CopyScriptPopupMenuFactory(CopyController copyController, JTree tree, UndoManager undoManager) {
        super(tree, undoManager);
        defaultPopupMenu = new NoPopupMenu(tree, undoManager);
        activePopupMenu = defaultPopupMenu;
        this.copyController = copyController;
    }

    @Override
    public void setTreeNode(XMLTreeNode treeNode) {

        activePopupMenu = getPopupMenu(getPopupMenuType(treeNode));
        activePopupMenu.setTreeNode(treeNode);
    }

    @Override
    public void show(Component invoker, int x, int y) {
        activePopupMenu.show(invoker, x, y);
    }

    private Type getPopupMenuType(XMLTreeNode treeNode) {
        Type type = Type.NONE;

        if(treeNode.getNode() instanceof Element) {
            switch(treeNode.toString()) {
            case "path":
                XMLTreeNode treeParent = treeNode.getParent();
                switch(treeParent.toString()) {
                case "paths":
                    treeParent = treeParent.getParent();
                    switch(treeParent.toString()) {
                    case "dst":
                        List<XMLTreeNode> children = treeParent.children();
                        for(XMLTreeNode child : children) {
                            if(child.toString().equals("type")) {
                                if(!child.isLeaf()) {
                                    XMLTreeNode t = child.getChildAt(0);
                                    type = t.toString().equals("Directory") ? Type.DST_VAL_DIR_PATH : Type.DST_VAL_FILE_PATH;
                                }
                            }
                        }
                        break;

                    case "src":
                        type = Type.SRC_VAL_PATH;
                        break;
                    }
                    break;
                }
                break;

            case "paths":
                treeParent = treeNode.getParent();
                switch(treeParent.toString()) {
                case "dst":
                    List<XMLTreeNode> children = treeParent.children();
                    for(XMLTreeNode child : children) {
                        if(child.toString().equals("type")) {
                            if(!child.isLeaf()) {
                                XMLTreeNode t = child.getChildAt(0);
                                type = t.toString().equals("Directory") ? Type.DST_DIR_PATH : Type.DST_FILE_PATH;
                            }
                        }
                    }
                    break;

                case "src":
                    type = Type.SRC_PATH;
                    break;

                default:
                    break;
                }
                break;

            case "dst":
                type = Type.DST;
                break;

            case "src":
                type = Type.SRC;
                break;

            case "map":
                type = Type.MAP;
                break;

            case "copyScript":
                type = Type.COPY_SCRIPT;
                break;

            case "options":
                treeParent = treeNode.getParent().getParent();
                switch(treeParent.toString()) {
                case "src":
                    type = Type.SRC_OPTIONS;
                    break;

                case "dst":
                    type = Type.DST_OPTIONS;
                    break;
                }
                break;
            }
        }

        return type;
    }

    private CopyScriptPopupMenu getPopupMenu(Type type) {
        CopyScriptPopupMenu popupMenu = popupMenuMap.get(type);
        if(popupMenu == null) {
            switch(type) {
            case DST_DIR_PATH:
                popupMenu = new PathsPopupMenu(copyController, net.sf.fc.script.gen.copy.Type.DIRECTORY, tree, undoManager);
                break;

            case DST_FILE_PATH:
                popupMenu = new PathsPopupMenu(copyController, net.sf.fc.script.gen.copy.Type.FILE, tree, undoManager);
                break;

            case SRC_PATH:
                popupMenu = new PathsPopupMenu(copyController, tree, undoManager);
                break;

            case DST_VAL_DIR_PATH:
                popupMenu = new PathPopupMenu(net.sf.fc.script.gen.copy.Type.DIRECTORY, tree, undoManager);
                break;

            case DST_VAL_FILE_PATH:
                popupMenu = new PathPopupMenu(net.sf.fc.script.gen.copy.Type.FILE, tree, undoManager);
                break;

            case SRC_VAL_PATH:
                popupMenu = new PathPopupMenu(tree, undoManager);
                break;

            case DST:
                popupMenu = new DstOrSrcPopupMenu(copyController, tree, undoManager, false);
                break;

            case SRC:
                popupMenu = new DstOrSrcPopupMenu(copyController, tree, undoManager, true);
                break;

            case MAP:
                popupMenu = new MapPopupMenu(copyController, tree, undoManager);
                break;

            case COPY_SCRIPT:
                popupMenu = new RootPopupMenu(copyController, tree, undoManager);
                break;

            case SRC_OPTIONS:
                popupMenu = new OptionsPopupMenu(tree, undoManager, true);
                break;

            case DST_OPTIONS:
                popupMenu = new OptionsPopupMenu(tree, undoManager, false);
                break;

            default: // NONE
                popupMenu = defaultPopupMenu;

            }
            popupMenuMap.put(type, popupMenu);
            popupMenu = getPopupMenu(type);
        }

        return popupMenu;
    }

}
