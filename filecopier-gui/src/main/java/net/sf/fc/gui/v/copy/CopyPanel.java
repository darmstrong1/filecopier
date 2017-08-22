package net.sf.fc.gui.v.copy;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.sf.fc.gui.c.copy.CopyController;
import net.sf.fc.gui.c.copy.CopyProperty;
import net.sf.fc.gui.v.AbstractViewPanel;
import net.sf.fc.gui.v.copy.edit.EditPnlType;
import net.sf.fc.gui.v.copy.edit.EditPanel;
import net.sf.fc.gui.v.copy.edit.RenameSfxFmtEditPanel;
import net.sf.fc.gui.v.copy.event.CopyScriptPopupListener;
import net.sf.fc.gui.v.copy.menu.CopyScriptPopupMenu;
import net.sf.fc.gui.v.tree.DefaultXMLTreeNode;
import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.gui.v.tree.XMLTreeNode;
import net.sf.fc.gui.v.util.JTreeUtil;
import net.sf.fc.script.gen.options.RenameType;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class CopyPanel extends AbstractViewPanel {
    private JTree tree;
    private XMLTreeNode selectedNode;

    private XMLTreeModel model;
    private final CopyController copyController;
    private final Map<EditPnlType,EditPanel> pnlEditMap;
    private final CopyScriptPopupMenu popupMenu;
    private final UndoManager undoManager;
    private EditPanel pnlActiveEdit;

    /**
     * Create the panel.
     */
    public CopyPanel(CopyController copyController,
            JTree tree,
            Map<EditPnlType,EditPanel> pnlEditMap,
            CopyScriptPopupMenu popupMenu,
            UndoManager undoManager) {
        this.copyController = copyController;
        this.tree = tree;
        this.model = (XMLTreeModel)tree.getModel();
        this.pnlEditMap = pnlEditMap;
        this.popupMenu = popupMenu;
        this.undoManager = undoManager;
        initComponents();

        copyController.addView(this);

        // Expand all map TreePaths.
        List<TreePath> mapTreePaths = JTreeUtil.findTreePathsByName((XMLTreeNode)model.getRoot(), "map");
        for(TreePath path : mapTreePaths) {
            JTreeUtil.expandAll(tree, path);
        }

        System.out.println("Rename Suffix preferred size: " + pnlEditMap.get(EditPnlType.RENAME_SFX).getPreferredSize().height);

//        System.out.println("pnlFileEdit preferred size: " + pnlFileEdit.getPreferredSize());
//        System.out.println("pnlRenameSfxFmt preferred size: " + pnlRenameSfxFmt.getPreferredSize());
//        System.out.println("pnlDefault preferred size: " + pnlDefaultEdit.getPreferredSize());

    }

    private void initComponents() {
        setLayout(new MigLayout("hidemode 3", "[grow]", "[grow][][]"));

        addTreeListeners();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tree);
        add(scrollPane, "cell 0 0,grow");

        add(initExpandBtn(), "cell 0 1,grow");
        add(initCollapseBtn(), "cell 0 1,grow");

        // Set the visibility to false for all but the view only panel and
        // set the active edit panel to the view only panel. The active edit
        // panel is the only one that will ever be visible.
        pnlEditMap.get(EditPnlType.BUILD_RESTORE_SCRIPT).setVisible(false);
        pnlEditMap.get(EditPnlType.COPY_ATTRIBUTES).setVisible(false);
        pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_CASE_SENSITIVE).setVisible(false);
        pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_EXCLUSIVE).setVisible(false);
        pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_REGEX).setVisible(false);
        pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_VALUE).setVisible(false);
        pnlEditMap.get(EditPnlType.SRC_FILE).setVisible(false);
        pnlEditMap.get(EditPnlType.DST_FILE).setVisible(false);
        pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_CASE_SENSITIVE).setVisible(false);
        pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_EXCLUSIVE).setVisible(false);
        pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_REGEX).setVisible(false);
        pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_VALUE).setVisible(false);
        pnlEditMap.get(EditPnlType.FILE_TYPE).setVisible(false);
        pnlEditMap.get(EditPnlType.FLATTEN_FILTER_CASE_SENSITIVE).setVisible(false);
        pnlEditMap.get(EditPnlType.FLATTEN_FILTER_EXCLUSIVE).setVisible(false);
        pnlEditMap.get(EditPnlType.FLATTEN_FILTER_REGEX).setVisible(false);
        pnlEditMap.get(EditPnlType.FLATTEN_FILTER_VALUE).setVisible(false);
        pnlEditMap.get(EditPnlType.FOLLOW_LINKS).setVisible(false);
        pnlEditMap.get(EditPnlType.MAX_CPY_LVL).setVisible(false);
        pnlEditMap.get(EditPnlType.MAX_FLATTEN_LVL).setVisible(false);
        pnlEditMap.get(EditPnlType.MAX_MERGE_LVL).setVisible(false);
        pnlEditMap.get(EditPnlType.MERGE_FILTER_CASE_SENSITIVE).setVisible(false);
        pnlEditMap.get(EditPnlType.MERGE_FILTER_EXCLUSIVE).setVisible(false);
        pnlEditMap.get(EditPnlType.MERGE_FILTER_REGEX).setVisible(false);
        pnlEditMap.get(EditPnlType.MERGE_FILTER_VALUE).setVisible(false);
        pnlEditMap.get(EditPnlType.OVERWRITE_PROMPT).setVisible(false);
        pnlEditMap.get(EditPnlType.RENAME_TYPE).setVisible(false);
        pnlEditMap.get(EditPnlType.RENAME_SFX).setVisible(false);
        pnlEditMap.get(EditPnlType.UPDATE).setVisible(false);
        pnlEditMap.get(EditPnlType.USE_GMT).setVisible(false);
        pnlActiveEdit = pnlEditMap.get(EditPnlType.VIEW_ONLY);

        // Get the max preferred height from all the edit panels and make that the height for all.
        int maxPrefHt = getMaxPreferredHeight();
        String cellCoordinates = "cell 0 2,growx, h " + (maxPrefHt+10) + "!";

        add(pnlEditMap.get(EditPnlType.BUILD_RESTORE_SCRIPT), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.COPY_ATTRIBUTES), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_CASE_SENSITIVE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_EXCLUSIVE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_REGEX), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_VALUE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.SRC_FILE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.DST_FILE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_CASE_SENSITIVE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_EXCLUSIVE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_REGEX), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_VALUE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.FILE_TYPE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.FLATTEN_FILTER_CASE_SENSITIVE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.FLATTEN_FILTER_EXCLUSIVE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.FLATTEN_FILTER_REGEX), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.FLATTEN_FILTER_VALUE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.FOLLOW_LINKS), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.MAX_CPY_LVL), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.MAX_FLATTEN_LVL), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.MAX_MERGE_LVL), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.MERGE_FILTER_CASE_SENSITIVE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.MERGE_FILTER_EXCLUSIVE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.MERGE_FILTER_REGEX), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.MERGE_FILTER_VALUE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.OVERWRITE_PROMPT), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.RENAME_SFX), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.RENAME_TYPE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.UPDATE), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.USE_GMT), cellCoordinates);
        add(pnlEditMap.get(EditPnlType.VIEW_ONLY), cellCoordinates);
    }

    private JButton initExpandBtn() {
        JButton btnExpand = new JButton("Expand");
        btnExpand.setMnemonic(KeyEvent.VK_E);
        btnExpand.setToolTipText("Expand the selected node and all its children.");
        btnExpand.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedNode != null) JTreeUtil.expandAll(tree, new TreePath(selectedNode.getPath()));
            }
        });

        return btnExpand;
    }

    private JButton initCollapseBtn() {
        JButton btnExpand = new JButton("Collapse");
        btnExpand.setMnemonic(KeyEvent.VK_C);
        btnExpand.setToolTipText("Collapse the selected node and all its children.");
        btnExpand.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedNode != null) JTreeUtil.collapseAll(tree, new TreePath(selectedNode.getPath()));
            }
        });

        return btnExpand;
    }

    private void setActiveEditPanel(EditPanel pnlEdit) {
        if(pnlActiveEdit != pnlEdit) {
            pnlActiveEdit.setVisible(false);
            pnlActiveEdit = pnlEdit;
            pnlActiveEdit.setVisible(true);
        }
        //pnlActiveEdit.requestFocusInWindow();
    }

    /**
     * Get the max preferred height of all the edit panels. This will be set as the size for all the edit panels.
     * @return
     */
    private int getMaxPreferredHeight() {
        return max(pnlEditMap.get(EditPnlType.BUILD_RESTORE_SCRIPT).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.COPY_ATTRIBUTES).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_CASE_SENSITIVE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_EXCLUSIVE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_REGEX).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_VALUE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.SRC_FILE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.DST_FILE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_CASE_SENSITIVE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_EXCLUSIVE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_REGEX).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_VALUE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.FILE_TYPE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.FLATTEN_FILTER_CASE_SENSITIVE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.FLATTEN_FILTER_EXCLUSIVE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.FLATTEN_FILTER_REGEX).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.FLATTEN_FILTER_VALUE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.FOLLOW_LINKS).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.MAX_CPY_LVL).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.MAX_FLATTEN_LVL).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.MAX_MERGE_LVL).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.MERGE_FILTER_CASE_SENSITIVE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.MERGE_FILTER_EXCLUSIVE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.MERGE_FILTER_REGEX).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.MERGE_FILTER_VALUE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.OVERWRITE_PROMPT).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.RENAME_SFX).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.RENAME_TYPE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.UPDATE).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.USE_GMT).getPreferredSize().height,
                pnlEditMap.get(EditPnlType.VIEW_ONLY).getPreferredSize().height);
    }

    private int max(int a, int... beez) {
        int max = a;
        for(int b : beez) {
            max = Math.max(max, b);
        }
        return max;
    }

    private void addTreeListeners() {

        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {

                Object lpc = e.getPath().getLastPathComponent();
                String nodeTextType = getNodeTextType(lpc);

                switch(nodeTextType) {
                case "src-path":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.SRC_FILE));
                    break;

                case "dst-path":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.DST_FILE));
                    break;

                case "type":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.FILE_TYPE));
                    break;

                case "format":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.RENAME_SFX));
                    break;

                case "renameType":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.RENAME_TYPE));
                    break;

                case "useGMT":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.USE_GMT));
                    break;

                case "update":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.UPDATE));
                    break;

                case "promptBeforeOverwrite":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.OVERWRITE_PROMPT));
                    break;

                case "buildRestoreScript":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.BUILD_RESTORE_SCRIPT));
                    break;

                case "copyAttributes":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.COPY_ATTRIBUTES));
                    break;

                case "followLinks":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.FOLLOW_LINKS));
                    break;

                case "maxCopyLevel":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.MAX_CPY_LVL));
                    break;

                case "maxMergeLevel":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.MAX_MERGE_LVL));
                    break;

                case "maxFlattenLevel":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.MAX_FLATTEN_LVL));
                    break;

                case "fileCopyFilter-caseSensitive":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_CASE_SENSITIVE));
                    break;

                case "fileCopyFilter-exclusive":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_EXCLUSIVE));
                    break;

                case "fileCopyFilter-regularExpression":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_REGEX));
                    break;

                case "fileCopyFilter-value":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.FILE_COPY_FILTER_VALUE));
                    break;

                case "dirCopyFilter-caseSensitive":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_CASE_SENSITIVE));
                    break;

                case "dirCopyFilter-exclusive":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_EXCLUSIVE));
                    break;

                case "dirCopyFilter-regularExpression":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_REGEX));
                    break;

                case "dirCopyFilter-value":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.DIR_COPY_FILTER_VALUE));
                    break;

                case "mergeFilter-caseSensitive":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.MERGE_FILTER_CASE_SENSITIVE));
                    break;

                case "mergeFilter-exclusive":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.MERGE_FILTER_EXCLUSIVE));
                    break;

                case "mergeFilter-regularExpression":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.MERGE_FILTER_REGEX));
                    break;

                case "mergeFilter-value":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.MERGE_FILTER_VALUE));
                    break;

                case "flattenFilter-caseSensitive":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.FLATTEN_FILTER_CASE_SENSITIVE));
                    break;

                case "flattenFilter-exclusive":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.FLATTEN_FILTER_EXCLUSIVE));
                    break;

                case "flattenFilter-regularExpression":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.FLATTEN_FILTER_REGEX));
                    break;

                case "flattenFilter-value":
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.FLATTEN_FILTER_VALUE));
                    break;

                default: //non-text: This is the value returned if the node is not a Text type.
                    setActiveEditPanel(pnlEditMap.get(EditPnlType.VIEW_ONLY));
                }

                selectedNode = (XMLTreeNode)lpc;
                pnlActiveEdit.setTreeNode(selectedNode);
                pnlActiveEdit.changeText(selectedNode.toString());
                // This is a new node that was selected, so clear out the edit panel's undo listener.
                pnlActiveEdit.discardAllEdits();
                revalidate();
            }
        });

        tree.addMouseListener(new CopyScriptPopupListener(popupMenu));

        tree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getModifiers() == Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) {
                    switch(e.getKeyCode()) {
                    case KeyEvent.VK_Z:
                        try { undoManager.undo(); } catch(CannotUndoException ignored) {}
                        break;

                    case KeyEvent.VK_Y:
                        try { undoManager.redo(); } catch(CannotRedoException ignored) {}
                        break;

                    default:
                        break;
                    }
                }
            }
        });
    }

    private String getNodeTextType(Object selected) {
        XMLTreeNode node;
        if(isText(selected)) {
            node = (XMLTreeNode)selected;
            XMLTreeNode parent = node.getParent();
            if(parent != null && parent.getNode() instanceof Element) {
                String elementName = parent.toString();
                switch(elementName) {
                // If it's one of the following values, it's one of the filter values.
                // Precede it with the name of the filter type, which is the parent's name.
                case "value":
                case "caseSensitive":
                case "regularExpression":
                case "exclusive":
                    return String.valueOf(parent.getParent()) + "-" + elementName;

                // If it's path, we need to get the parent's parent to see if it is
                // a src or dst.
                case "path":
                    return String.valueOf(parent.getParent().getParent()) + "-" + elementName;

                default:
                    return elementName;
                }
            }
        }
        return "non-text";
    }

    private boolean isText(Object selected) {
        return (selected instanceof DefaultXMLTreeNode && ((DefaultXMLTreeNode)selected).getNode() instanceof Text);
    }

    public void setModel(XMLTreeModel model) {
        this.model = model;
        tree.setModel(model);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        try {
            CopyProperty cpProperty = CopyProperty.valueOf(evt.getPropertyName());
            switch(cpProperty) {
            case DefaultRenameType:
                pnlActiveEdit.changeText((String)((RenameType)evt.getNewValue()).value());
                break;

            case DefaultRenameSfxFmt:
            case DefaultFileCopyFilterValue:
            case DefaultDirCopyFilterValue:
            case DefaultMergeFilterValue:
            case DefaultFlattenFilterValue:
                pnlActiveEdit.changeText((String)evt.getNewValue());
                break;

            case DefaultRenameUseGMT:
            case DefaultUpdate:
            case DefaultPromptBeforeOverwrite:
            case DefaultBuildRestoreScript:
            case DefaultCopyAttributes:
            case DefaultFollowLinks:
            case DefaultDirCopyFilterCaseSensitive:
            case DefaultDirCopyFilterExclusive:
            case DefaultDirCopyFilterRegex:
            case DefaultFileCopyFilterCaseSensitive:
            case DefaultFileCopyFilterExclusive:
            case DefaultFileCopyFilterRegex:
            case DefaultFlattenFilterCaseSensitive:
            case DefaultFlattenFilterExclusive:
            case DefaultFlattenFilterRegex:
            case DefaultMergeFilterCaseSensitive:
            case DefaultMergeFilterExclusive:
            case DefaultMergeFilterRegex:
                String text = (Boolean)evt.getNewValue().equals(Boolean.TRUE) ? "true" : "false";
                pnlActiveEdit.changeText(text);
                break;

            case DefaultMaxCopyLevel:
            case DefaultMaxMergeLevel:
            case DefaultMaxFlattenLevel:
                text = ((Integer)evt.getNewValue()).toString();
                pnlActiveEdit.changeText(text);
                break;

            case DefaultRenameTypeFailure:
            case DefaultRenameSfxFmtFailure:
            case DefaultRenameUseGMTFailure:
            case DefaultUpdateFailure:
            case DefaultPromptBeforeOverwriteFailure:
            case DefaultBuildRestoreScriptFailure:
            case DefaultMaxCopyLevelFailure:
            case DefaultMaxMergeLevelFailure:
            case DefaultMaxFlattenLevelFailure:
            case DefaultCopyAttributesFailure:
            case DefaultFollowLinksFailure:
            case DefaultFileCopyFilterValueFailure:
            case DefaultDirCopyFilterValueFailure:
            case DefaultMergeFilterValueFailure:
            case DefaultFlattenFilterValueFailure:
            case DefaultDirCopyFilterCaseSensitiveFailure:
            case DefaultDirCopyFilterExclusiveFailure:
            case DefaultDirCopyFilterRegexFailure:
            case DefaultFileCopyFilterCaseSensitiveFailure:
            case DefaultFileCopyFilterExclusiveFailure:
            case DefaultFileCopyFilterRegexFailure:
            case DefaultFlattenFilterCaseSensitiveFailure:
            case DefaultFlattenFilterExclusiveFailure:
            case DefaultFlattenFilterRegexFailure:
            case DefaultMergeFilterCaseSensitiveFailure:
            case DefaultMergeFilterExclusiveFailure:
            case DefaultMergeFilterRegexFailure:
                // TODO: This event will send the JAXBException as its value. Log that the attempt to get the saved
                // rename type failed and the exception and that it will retrieve the cached rename type.

                // The value will be sent back to the view using the same string value as it did when it sent back
                // the saved default value.

                // Construct the name of what the method should be in the controller.
                StringBuilder retrieveMethodName = new StringBuilder();
                String propString = cpProperty.toString();
                // The name of the method will be "retrieveCached" followed by the name of the property name without the Failure (7 letters) at the end.
                retrieveMethodName.append("retrieveCached").append(propString.substring(0, propString.length() - 7));
                try {
                    Method method = copyController.getClass().getMethod(retrieveMethodName.toString(), ( Class<?>[])null);
                    method.invoke(copyController, (Object[])null);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
                    // TODO: This should never happen, but log it if it does.
                }
                break;
            }
        } catch(IllegalArgumentException e) {

        }
    }

}
