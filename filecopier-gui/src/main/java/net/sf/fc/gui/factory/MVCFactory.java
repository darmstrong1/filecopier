package net.sf.fc.gui.factory;

import javax.swing.undo.UndoManager;
import javax.xml.bind.JAXBException;

import net.sf.fc.cfg.AppFactory;
import net.sf.fc.cfg.DirPath;
import net.sf.fc.gui.c.options.OptionsController;
import net.sf.fc.gui.c.settings.SettingsController;
import net.sf.fc.gui.m.options.OptionsModel;
import net.sf.fc.gui.m.settings.SettingsModel;
import net.sf.fc.gui.v.options.OptionsDialog;
import net.sf.fc.gui.v.settings.SettingsDialog;
import net.sf.fc.gui.v.settings.SettingsPanel;
import net.sf.fc.script.OptionsScriptProxy;
import net.sf.fc.script.SettingsProxy;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.script.gen.settings.Settings;

/**
 * MVCFactory creates singleton objects. Any object that depends on one of the objects created in MVCFactory should
 * take that object as an argument in its constructor. Any object that depends on more than one object created in
 * AppFactory should take AppFactory as an argument in its constructor. It contains an AppFactory that has the same
 * principles.
 *
 * @author David Armstrong
 *
 */

public final class MVCFactory {

    // Settings objects
    private final SettingsController settingsController;
    private final SettingsModel settingsModel;
    private final SettingsDialog settingsDialog;

    // Default Options Controller
    private final OptionsController defaultOptionsController;
    // Default Options model
    private final OptionsModel defaultOptionsModel;
    // Default Options Dialog
    private final OptionsDialog defaultOptionsDialog;

    private final MVCRequestFactory mvcRequestFactory;

    public static final int FILE_COPY_FILTER_IDX = 0;
    public static final int DIR_COPY_FILTER_IDX = 1;
    public static final int FLATTEN_FILTER_IDX = 2;
    public static final int MERGE_FILTER_IDX = 3;

    public MVCFactory(AppFactory appFactory) throws JAXBException {

        SettingsProxy settingsProxy = appFactory.getSettingsProxy();
        settingsController = new SettingsController();
        settingsModel = new SettingsModel(settingsProxy);
        settingsDialog = createSettingsDialog(settingsProxy.getSettings(), settingsController);
        initSettings();

        defaultOptionsController = new OptionsController();

        DirPath optionsPath = new DirPath(settingsProxy.getSettings().getPaths().getOptionsPath());
        OptionsScriptProxy optionsScriptProxy = appFactory.getDiskDefaultOptionsScriptProxy();
        defaultOptionsModel = MVCOptionsFactory.createOptionsModel(optionsScriptProxy,
                appFactory.getCachedDefaultOptionsScriptProxy().getOptions(),
                appFactory.getRequestFactory());
        defaultOptionsDialog = createDefaultOptionsDialog(optionsScriptProxy.getOptions(),
                defaultOptionsController, optionsPath);
        initOptions();

        mvcRequestFactory = new MVCRequestFactory(appFactory.getRequestFactory(),
                appFactory.getCachedDefaultOptionsScriptProxy().getOptions(),
                settingsProxy);
    }

    private void initSettings() {
        settingsController.addModel(settingsModel);
    }

    private void initOptions() {
        defaultOptionsController.addModel(defaultOptionsModel);
    }

    private OptionsDialog createDefaultOptionsDialog(OptionsScript optionsScript, OptionsController optionsController, DirPath optionsPath) {
        UndoManager undoManager = new UndoManager();
        return new OptionsDialog("Default Options", optionsController,
                MVCOptionsFactory.createOptionsPanel(optionsScript, optionsController, undoManager),
                optionsPath, undoManager, true);
    }

    private SettingsDialog createSettingsDialog(Settings settings, SettingsController settingsController) {
        UndoManager undoManager = new UndoManager();
        return new SettingsDialog(new SettingsPanel(settings, settingsController, undoManager), settingsController, undoManager);
    }

    public SettingsController getSettingsController() {
        return settingsController;
    }

    public SettingsModel getSettingsModel() {
        return settingsModel;
    }

    public OptionsController getDefaultOptionsController() {
        return defaultOptionsController;
    }

    public OptionsModel getDefaultOptionsModel() {
        return defaultOptionsModel;
    }

    public OptionsDialog getDefaultOptionsDialog() {
        return defaultOptionsDialog;
    }

    public SettingsDialog getSettingsDialog() {
        return settingsDialog;
    }

    public MVCRequestFactory getMVCRequestFactory() {
        return mvcRequestFactory;
    }

}
