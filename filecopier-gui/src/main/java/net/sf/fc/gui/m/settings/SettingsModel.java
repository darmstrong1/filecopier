package net.sf.fc.gui.m.settings;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBException;

import net.sf.fc.gui.c.settings.SettingsProperty;
import net.sf.fc.gui.m.BaseModel;
import net.sf.fc.script.SettingsProxy;
import net.sf.fc.script.gen.settings.LogLevel;
import net.sf.fc.script.gen.settings.LookAndFeel;
import net.sf.fc.script.gen.settings.MostRecentlyUsed;
import net.sf.fc.script.gen.settings.Paths;
import net.sf.fc.script.gen.settings.Settings;

public final class SettingsModel extends BaseModel {

    private final SettingsProxy settingsProxy;
    private Settings snapshotSettings;

    public SettingsModel(SettingsProxy settingsProxy) {
        this.settingsProxy = settingsProxy;
        snapshotSettings = settingsProxy.getSettings();
    }

    public void setLogLevel(LogLevel logLevel) {
        LogLevel oldLogLevel = settingsProxy.getLogLevel();
        settingsProxy.setLogLevel(logLevel);
        firePropertyChange(SettingsProperty.LogLevel.toString(), oldLogLevel, logLevel);
    }

    public void setMostRecentlyUsedLimit(Integer limit) {
        Integer oldLimit = settingsProxy.getMostRecentlyUsedLimit();
        settingsProxy.setMostRecentlyUsedLimit(limit);
        firePropertyChange(SettingsProperty.MostRecentlyUsedLimit.toString(), oldLimit, limit);
    }

    public void setMostRecentlyUsedList(File file) {
        List<File> oldFiles = settingsProxy.getMostRecentlyUsedList().getFiles();
        settingsProxy.addMostRecentlyUsedFile(file);
        firePropertyChange(SettingsProperty.MostRecentlyUsedList.toString(), oldFiles, settingsProxy.getMostRecentlyUsedList().getFiles());
    }

    public void setLookAndFeel(String laf) {
        String oldLaf = settingsProxy.getLookAndFeelName();
        settingsProxy.setLookAndFeelName(laf);
        firePropertyChange(SettingsProperty.LookAndFeel.toString(), oldLaf, laf);
    }

    public void setDrawWindowBorders(Boolean draw) {
        Boolean oldDraw = settingsProxy.isDrawWindowBorders();
        settingsProxy.setDrawWindowBorders(draw);
        firePropertyChange(SettingsProperty.DrawWindowBorders.toString(), oldDraw, draw);
    }

    public void setCopyPath(String copyPath) {
        String oldCopyPath = settingsProxy.getCopyPath();
        settingsProxy.setCopyPath(copyPath);
        firePropertyChange(SettingsProperty.CopyPath.toString(), oldCopyPath, copyPath);
    }

    public void setRestorePath(String restorePath) {
        String oldRestorePath = settingsProxy.getRestorePath();
        settingsProxy.setRestorePath(restorePath);
        firePropertyChange(SettingsProperty.RestorePath.toString(), oldRestorePath, restorePath);
    }

    public void setOptionsPath(String optionsPath) {
        String oldOptionsPath = settingsProxy.getOptionsPath();
        settingsProxy.setOptionsPath(optionsPath);
        firePropertyChange(SettingsProperty.OptionsPath.toString(), oldOptionsPath, optionsPath);
    }

    public void getDefaultSettings() {
        setSettings(settingsProxy.getDefaultSettings());
    }

    public void setSettings(Settings settings) {
        if(!settingsProxy.getSettings().equals(settings)) {
            setLogLevel(settings.getLogLevel());
            LookAndFeel laf = settings.getLookAndFeel();
            if(!laf.equals(settingsProxy.getSettings().getLookAndFeel())) {
                setLookAndFeel(laf.getSelected());
                setDrawWindowBorders(laf.isDrawWindowBorders());
            }
            MostRecentlyUsed mru = settings.getMostRecentlyUsed();
            if(!mru.equals(settingsProxy.getMostRecentlyUsed())) {
                setMostRecentlyUsedLimit(mru.getLimit());
            }
            Paths paths = settings.getPaths();
            if(!paths.equals(settingsProxy.getPaths())) {
                setCopyPath(paths.getCopyPath());
                setRestorePath(paths.getRestorePath());
                setOptionsPath(paths.getOptionsPath());
            }
        }
    }

    public void getSnapshotSettings() {
        snapshotSettings = settingsProxy.getSettings();
    }

    public void getRestoreSnapshotSettings() {
        setSettings(snapshotSettings);
    }

    public void getSaveSettings() {
        try {
            settingsProxy.saveSettingsToDisk();
            snapshotSettings = settingsProxy.getSettings();
            firePropertyChange(SettingsProperty.SaveSettings.toString(), Boolean.FALSE, Boolean.TRUE);
        } catch (JAXBException e) {
            // Send a save settings failure message with the message from the JAXBException as the object.
            firePropertyChange(SettingsProperty.SaveSettingsFailure.toString(), null, e.getMessage());
            // TODO Log the Exception.
            e.printStackTrace();
        }
    }

}
