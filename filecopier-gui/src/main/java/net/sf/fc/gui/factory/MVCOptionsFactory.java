package net.sf.fc.gui.factory;

import javax.swing.JPanel;
import javax.swing.undo.UndoManager;

import net.sf.fc.cfg.RequestFactory;
import net.sf.fc.gui.c.copy.DirCopyFilterChanger;
import net.sf.fc.gui.c.copy.FileCopyFilterChanger;
import net.sf.fc.gui.c.copy.FlattenFilterChanger;
import net.sf.fc.gui.c.copy.MergeFilterChanger;
import net.sf.fc.gui.c.options.OptionsController;
import net.sf.fc.gui.m.options.OptionsModel;
import net.sf.fc.gui.v.options.CopyOptionsPanel;
import net.sf.fc.gui.v.options.FileExistOptionsPanel;
import net.sf.fc.gui.v.options.FilterPanel;
import net.sf.fc.gui.v.options.OptionsPanel;
import net.sf.fc.gui.v.options.SourceDirectoryOptionsPanel;
import net.sf.fc.script.OptionsScriptProxy;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.script.gen.options.SrcDirOptions;

/**
 * Factory class that has static methods that create Options MVC objects.
 * @author david
 *
 */
public class MVCOptionsFactory {

    public static final int FILE_COPY_FILTER_IDX = 0;
    public static final int DIR_COPY_FILTER_IDX = 1;
    public static final int FLATTEN_FILTER_IDX = 2;
    public static final int MERGE_FILTER_IDX = 3;

    public static SourceDirectoryOptionsPanel createSrcDirOptionsPanel(OptionsController optionsController, SrcDirOptions srcDirOptions,
            UndoManager undoManager) {
        FilterPanel[] filterPanels = new FilterPanel[MERGE_FILTER_IDX+1];
        filterPanels[FILE_COPY_FILTER_IDX] = new FilterPanel(optionsController, new FileCopyFilterChanger(optionsController),
                srcDirOptions.getFileCopyFilter(), "FileCopy", undoManager);
        filterPanels[DIR_COPY_FILTER_IDX] = new FilterPanel(optionsController, new DirCopyFilterChanger(optionsController),
                srcDirOptions.getDirCopyFilter(), "DirCopy", undoManager);
        filterPanels[FLATTEN_FILTER_IDX] = new FilterPanel(optionsController, new FlattenFilterChanger(optionsController),
                srcDirOptions.getFlattenFilter(), "Flatten", undoManager);
        filterPanels[MERGE_FILTER_IDX] = new FilterPanel(optionsController, new MergeFilterChanger(optionsController),
                srcDirOptions.getMergeFilter(), "Merge", undoManager);

        return new SourceDirectoryOptionsPanel(optionsController, srcDirOptions, filterPanels, undoManager);
    }

    public static OptionsPanel createOptionsPanel(OptionsScript optionsScript, OptionsController optionsController,
            UndoManager undoManager) {
        JPanel copyOptionsPanel = new CopyOptionsPanel(optionsController, optionsScript.isCopyAttributes(),
                optionsScript.isFollowLinks(), undoManager);
        JPanel fileExistOptionsPanel = new FileExistOptionsPanel(optionsController,
                optionsScript.getRenameOptions(), undoManager);
        JPanel srcDirOptionsPanel = createSrcDirOptionsPanel(optionsController,
                optionsScript.getSrcDirOptions(), undoManager);
        return new OptionsPanel(copyOptionsPanel, fileExistOptionsPanel, srcDirOptionsPanel, undoManager);
    }

    public static OptionsController createOptionsController() {
        return new OptionsController();
    }

    public static OptionsModel createOptionsModel(OptionsScriptProxy optionsScriptProxy,
            OptionsScript cachedDefaultOptions,
            RequestFactory requestFactory) {
        return new OptionsModel(optionsScriptProxy, cachedDefaultOptions, requestFactory);
    }

}
