package net.sf.fc.gui.factory;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.undo.UndoManager;
import javax.xml.bind.JAXBException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import net.sf.fc.cfg.DirPath;
import net.sf.fc.cfg.RequestFactory;
import net.sf.fc.gui.c.Retriever;
import net.sf.fc.gui.c.copy.CopyController;
import net.sf.fc.gui.c.options.OptionsController;
import net.sf.fc.gui.m.copy.CopyModel;
import net.sf.fc.gui.v.copy.CopyPanel;
import net.sf.fc.gui.v.copy.edit.CkBoxEditPanel;
import net.sf.fc.gui.v.copy.edit.EditPanel;
import net.sf.fc.gui.v.copy.edit.EditPnlType;
import net.sf.fc.gui.v.copy.edit.FileEditPanel;
import net.sf.fc.gui.v.copy.edit.FileTypeEditPanel;
import net.sf.fc.gui.v.copy.edit.FilterValuePanel;
import net.sf.fc.gui.v.copy.edit.FmtTxtFldEditPanel;
import net.sf.fc.gui.v.copy.edit.RenameSfxFmtEditPanel;
import net.sf.fc.gui.v.copy.edit.RenameTypeEditPanel;
import net.sf.fc.gui.v.copy.edit.ViewOnlyPanel;
import net.sf.fc.gui.v.copy.menu.CopyScriptPopupMenu;
import net.sf.fc.gui.v.copy.menu.CopyScriptPopupMenuFactory;
import net.sf.fc.gui.v.options.OptionsDialog;
import net.sf.fc.gui.v.tree.CopyScriptXMLTreeNode;
import net.sf.fc.gui.v.tree.XMLTreeModel;
import net.sf.fc.script.OptionsScriptProxy;
import net.sf.fc.script.SettingsProxy;
import net.sf.fc.script.gen.options.OptionsScript;

/**
 * MVCRequestFactory creates objects as they are requested.
 * @author David Armstrong
 *
 */
public final class MVCRequestFactory {

    private final RequestFactory requestFactory;
    private final SettingsProxy settingsProxy;
    private final OptionsScript cachedDefaultOptionsScript;

    public MVCRequestFactory(RequestFactory requestFactory,
            OptionsScript cachedDefaultOptionsScript,
            SettingsProxy settingsProxy) {
        this.requestFactory = requestFactory;
        this.cachedDefaultOptionsScript = cachedDefaultOptionsScript;
        this.settingsProxy = settingsProxy;
    }

    // Start of methods for creating Options MVC objects

    /**
     * This method returns an OptionsDialog for a new Options script.
     * @return OptionsDialog
     * @throws SAXException
     * @throws IOException
     * @throws JAXBException
     */
//    public OptionsDialog getOptionsDialog() {
//        OptionsScriptProxy optionsScriptProxy = requestFactory.getOptionsScriptProxy();
//        final OptionsController optionsController = initOptionsController(optionsScriptProxy);
//        UndoManager undoManager = new UndoManager();
//        DirPath optionsPath = new DirPath(settingsProxy.getSettings().getPaths().getOptionsPath());
//        return getOptionsDialog("Options", optionsScriptProxy.getOptions(), optionsController, undoManager, optionsPath);
//    }
    public OptionsDialog getOptionsDialog() throws JAXBException, IOException, SAXException {
        OptionsScriptProxy optionsScriptProxy = requestFactory.getOptionsScriptProxy();
        final OptionsController optionsController = initOptionsController(optionsScriptProxy);
        UndoManager undoManager = new UndoManager();
        DirPath optionsPath = new DirPath(settingsProxy.getSettings().getPaths().getOptionsPath());
        return getOptionsDialog("Options", requestFactory.getDiskDefaultOptions(), optionsController, undoManager, optionsPath);
        //return getOptionsDialog(requestFactory.getDefaultOptionsFile());
    }

    /**
     * This method returns an OptionsDialog for an Options script that is saved to disk.
     * @param optionsFile
     * @return OptionsDialog
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     */
    public OptionsDialog getOptionsDialog(File optionsFile) throws JAXBException, IOException, SAXException {
        OptionsScriptProxy optionsScriptProxy = requestFactory.getOptionsScriptProxy(optionsFile);
        OptionsController optionsController = initOptionsController(optionsScriptProxy);
        UndoManager undoManager = new UndoManager();
        DirPath optionsPath = new DirPath(settingsProxy.getSettings().getPaths().getOptionsPath());
        return getOptionsDialog(optionsFile.getPath(), optionsScriptProxy.getOptions(), optionsController, undoManager, optionsPath);
    }

//    private OptionsDialog getOptionsDialog(String title, OptionsScript optionsScript,
//            final OptionsController optionsController, UndoManager undoManager, DirPath optionsPath) {
//        return new OptionsDialog(title, optionsController,
//                MVCOptionsFactory.createOptionsPanel(optionsScript, optionsController, undoManager),
//                optionsPath, undoManager,
//                // Create a WindowAdapter to remove the Settings Model from the Options model. It is important to do this
//                // so that a bunch of Options Controllers for closed Options Dialogs don't accumulate in the Settings Model's
//                // list of PropertyChangeListeners.
//                new WindowAdapter() {
//
//                    @Override
//                    public void windowClosing(WindowEvent e) {
//                        optionsController.removeModel(settingsModel);
//                    }
//                });
//    }

    private OptionsDialog getOptionsDialog(String title, OptionsScript optionsScript,
            final OptionsController optionsController, UndoManager undoManager, DirPath optionsPath) {
        return new OptionsDialog(title, optionsController,
                MVCOptionsFactory.createOptionsPanel(optionsScript, optionsController, undoManager),
                optionsPath, undoManager, false);
    }

    private OptionsController initOptionsController(OptionsScriptProxy optionsScriptProxy) {
        OptionsController optionsController = MVCOptionsFactory.createOptionsController();
        optionsController.addModel(MVCOptionsFactory.createOptionsModel(optionsScriptProxy,
                cachedDefaultOptionsScript,
                requestFactory));
        // Add the Settings model, too. The options views need to know when some of the app settings change.
        //optionsController.addModel(settingsModel);
        return optionsController;
    }
    // End of methods for creating Options MVC objects

    // Start of methods for creating Copy MVC Objects
    private XMLTreeModel getTreeModel(Document doc) {
        return new XMLTreeModel(new CopyScriptXMLTreeNode(doc.getFirstChild()));
    }

    public CopyPanel getCopyPanel(Document doc) {
        XMLTreeModel model = getTreeModel(doc);
        JTree tree = new JTree(model);

        CopyController copyController = getCopyController();
        Map<EditPnlType,EditPanel> editPnlMap = getEditPanelMap(model, copyController);

        UndoManager treeUndoManager = new UndoManager();
        CopyScriptPopupMenu popupMenu = new CopyScriptPopupMenuFactory(copyController, tree, treeUndoManager);

        return new CopyPanel(copyController, tree, editPnlMap, popupMenu, treeUndoManager);
    }

    private Map<EditPnlType,EditPanel> getEditPanelMap(XMLTreeModel model, final CopyController copyController) {
        Map<EditPnlType,EditPanel> editPnlMap = new EnumMap<>(EditPnlType.class);

        editPnlMap.put(EditPnlType.BUILD_RESTORE_SCRIPT, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultBuildRestoreScript();
                    }
                }, "buildRestoreScript"));

        editPnlMap.put(EditPnlType.COPY_ATTRIBUTES, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultCopyAttributes();
                    }
                }, "copyAttributes"));

        editPnlMap.put(EditPnlType.DIR_COPY_FILTER_CASE_SENSITIVE, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultDirCopyFilterCaseSensitive();
                    }
                }, "caseSensitive"));

        editPnlMap.put(EditPnlType.DIR_COPY_FILTER_EXCLUSIVE, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultDirCopyFilterExclusive();
                    }
                }, "exclusive"));

        editPnlMap.put(EditPnlType.DIR_COPY_FILTER_REGEX, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultDirCopyFilterRegex();
                    }
                }, "regularExpression"));

        editPnlMap.put(EditPnlType.DIR_COPY_FILTER_VALUE, getFilterValuePanel(model, new Retriever() {

            @Override
            public void retrieve() {
                copyController.retrieveDefaultDirCopyFilterValue();
            }
        }));

        editPnlMap.put(EditPnlType.SRC_FILE, getFileEditPanel(model, copyController, true));

        editPnlMap.put(EditPnlType.DST_FILE, getFileEditPanel(model, copyController, false));

        editPnlMap.put(EditPnlType.FILE_COPY_FILTER_CASE_SENSITIVE, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultFileCopyFilterCaseSensitive();
                    }
                }, "caseSensitive"));

        editPnlMap.put(EditPnlType.FILE_COPY_FILTER_EXCLUSIVE, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultFileCopyFilterExclusive();
                    }
                }, "exclusive"));

        editPnlMap.put(EditPnlType.FILE_COPY_FILTER_REGEX, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultFileCopyFilterRegex();
                    }
                }, "regularExpression"));

        editPnlMap.put(EditPnlType.FILE_COPY_FILTER_VALUE, getFilterValuePanel(model, new Retriever() {

            @Override
            public void retrieve() {
                copyController.retrieveDefaultFileCopyFilterValue();
            }
        }));

        editPnlMap.put(EditPnlType.FILE_TYPE, getFileTypeEditPanel(model));

        editPnlMap.put(EditPnlType.FLATTEN_FILTER_CASE_SENSITIVE, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultFlattenFilterCaseSensitive();
                    }
                }, "caseSensitive"));

        editPnlMap.put(EditPnlType.FLATTEN_FILTER_EXCLUSIVE, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultFlattenFilterExclusive();
                    }
                }, "exclusive"));

        editPnlMap.put(EditPnlType.FLATTEN_FILTER_REGEX, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultFlattenFilterRegex();
                    }
                }, "regularExpression"));

        editPnlMap.put(EditPnlType.FLATTEN_FILTER_VALUE, getFilterValuePanel(model, new Retriever() {

            @Override
            public void retrieve() {
                copyController.retrieveDefaultFlattenFilterValue();
            }
        }));

        editPnlMap.put(EditPnlType.FOLLOW_LINKS, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultFollowLinks();
                    }
                }, "followLinks"));

        editPnlMap.put(EditPnlType.MAX_CPY_LVL, getFmtTxtFldEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultMaxCopyLevel();
                    }
                }));

        editPnlMap.put(EditPnlType.MAX_FLATTEN_LVL, getFmtTxtFldEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultMaxFlattenLevel();
                    }
                }));

        editPnlMap.put(EditPnlType.MAX_MERGE_LVL, getFmtTxtFldEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultMaxMergeLevel();
                    }
                }));

        editPnlMap.put(EditPnlType.MERGE_FILTER_CASE_SENSITIVE, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultMergeFilterCaseSensitive();
                    }
                }, "caseSensitive"));

        editPnlMap.put(EditPnlType.MERGE_FILTER_EXCLUSIVE, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultMergeFilterExclusive();
                    }
                }, "exclusive"));

        editPnlMap.put(EditPnlType.MERGE_FILTER_REGEX, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultMergeFilterRegex();
                    }
                }, "regularExpression"));

        editPnlMap.put(EditPnlType.MERGE_FILTER_VALUE, getFilterValuePanel(model, new Retriever() {

            @Override
            public void retrieve() {
                copyController.retrieveDefaultMergeFilterValue();
            }
        }));

        editPnlMap.put(EditPnlType.OVERWRITE_PROMPT, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultPromptBeforeOverwrite();
                    }
                }, "promptBeforeOverwrite"));

        editPnlMap.put(EditPnlType.RENAME_SFX, getRenameSfxFmtEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultRenameSfxFmt();
                    }
                }));

        editPnlMap.put(EditPnlType.RENAME_TYPE, getRenameTypePanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultRenameType();
                    }
                }));

        editPnlMap.put(EditPnlType.UPDATE, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultUpdate();
                    }
                }, "update"));

        editPnlMap.put(EditPnlType.USE_GMT, getCkBoxEditPanel(model,
                new Retriever() {

                    @Override
                    public void retrieve() {
                        copyController.retrieveDefaultRenameUseGMT();
                    }
                }, "useGMT"));

        editPnlMap.put(EditPnlType.VIEW_ONLY, getViewOnlyPanel(model));

        return editPnlMap;
    }

    private EditPanel getRenameTypePanel(XMLTreeModel model, Retriever retriever) {
        return new RenameTypeEditPanel(model, retriever);
    }

    private EditPanel getRenameSfxFmtEditPanel(XMLTreeModel model, Retriever retriever) {
        return new RenameSfxFmtEditPanel(model, retriever);
    }

    private EditPanel getFmtTxtFldEditPanel(XMLTreeModel model, Retriever retriever) {
        return new FmtTxtFldEditPanel(model, retriever);
    }

    private EditPanel getFileTypeEditPanel(XMLTreeModel model) {
        return new FileTypeEditPanel(model);
    }

    private EditPanel getFileEditPanel(XMLTreeModel model, CopyController copyController, boolean isSrc) {
        return new FileEditPanel(model, copyController, isSrc);
    }

    private EditPanel getViewOnlyPanel(XMLTreeModel model) {
        return new ViewOnlyPanel(model);
    }

    private EditPanel getCkBoxEditPanel(XMLTreeModel model, Retriever retriever, String text) {
        return new CkBoxEditPanel(model, retriever, text);
    }

    private EditPanel getFilterValuePanel(XMLTreeModel model, Retriever retriever) {
        return new FilterValuePanel(model, retriever);
    }

    private CopyController getCopyController() {
        CopyController copyController = new CopyController();
        copyController.addModel(new CopyModel(requestFactory, settingsProxy, cachedDefaultOptionsScript));

        return copyController;
    }
}
