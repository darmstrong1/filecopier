package net.sf.fc.gui.c.settings;

import java.io.File;

import net.sf.fc.cfg.DirPath;
import net.sf.fc.gui.c.BaseController;
import net.sf.fc.script.gen.settings.LogLevel;

public final class SettingsController extends BaseController {

    public void changeLogLevel(LogLevel level) {
        setModelProperty(SettingsProperty.LogLevel.toString(), level);
    }

    public void changeMostRecentlyUsedLimit(int limit) {
        setModelProperty(SettingsProperty.MostRecentlyUsedLimit.toString(), limit);
    }

    public void changeMostRecentlyUsedList(File copyScript) {
        setModelProperty(SettingsProperty.MostRecentlyUsedList.toString(), copyScript);
    }

    public void changeLookAndFeel(String laf) {
        setModelProperty(SettingsProperty.LookAndFeel.toString(), laf);
    }

    public void changeDrawWindowBorders(boolean draw) {
        setModelProperty(SettingsProperty.DrawWindowBorders.toString(), draw);
    }

    public void changeCopyPath(String copyPath) {
        setModelProperty(SettingsProperty.CopyPath.toString(), copyPath);
    }

    public void changeRestorePath(String restorePath) {
        setModelProperty(SettingsProperty.RestorePath.toString(), restorePath);
    }

    public void changeOptionsPath(String optionsPath) {
        setModelProperty(SettingsProperty.OptionsPath.toString(), optionsPath);
    }

    public void retrieveDefaultSettings() {
        getModelProperty(SettingsProperty.DefaultSettings.toString());
    }

    public void retrieveSnapshotSettings() {
        getModelProperty(SettingsProperty.SnapshotSettings.toString());
    }

    public void retrieveRestoreSnapshotSettings() {
        getModelProperty(SettingsProperty.RestoreSnapshotSettings.toString());
    }

    public void retrieveSaveSettings() {
        getModelProperty(SettingsProperty.SaveSettings.toString());
    }
}
