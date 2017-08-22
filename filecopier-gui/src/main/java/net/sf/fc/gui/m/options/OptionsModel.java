package net.sf.fc.gui.m.options;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import net.sf.fc.cfg.DirPath;
import net.sf.fc.cfg.RequestFactory;
import net.sf.fc.gui.c.options.OptionsProperty;
import net.sf.fc.gui.m.BaseModel;
import net.sf.fc.script.OptionsScriptProxy;
import net.sf.fc.script.ScriptHelper;
import net.sf.fc.script.gen.options.Filter;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.script.gen.options.RenameOptions;
import net.sf.fc.script.gen.options.RenameType;
import net.sf.fc.script.gen.options.SrcDirOptions;
import net.sf.fc.script.gen.settings.Settings;

public final class OptionsModel extends BaseModel {

    private OptionsScriptProxy optionsProxy;
    // Object that holds a snapshot of the OptionsScript's state. It is set in the constructor and whenever the
    // OK button is clicked on the Options Dialog.
    private OptionsScript snapshotOptions;
    private final RequestFactory requestFactory;
    // This OptionsScript represents what is the standard default options.
    private final OptionsScript cachedDefaultOptions;

    public OptionsModel(OptionsScriptProxy optionsProxy,
            OptionsScript cachedDefaultOptions,
            RequestFactory requestFactory) {
        this.optionsProxy = optionsProxy;
        snapshotOptions = optionsProxy.getOptions();
        this.requestFactory = requestFactory;
        this.cachedDefaultOptions = cachedDefaultOptions;
    }

    public void setCopyAttributes(Boolean preserve) {
        Boolean oldPreserve = optionsProxy.isCopyAttributes();
        optionsProxy.setCopyAttributes(preserve);
        firePropertyChange(OptionsProperty.CopyAttributes.toString(), oldPreserve, preserve);
    }

    public void setFollowLinks(Boolean follow) {
        Boolean oldPreserve = optionsProxy.isFollowLinks();
        optionsProxy.setFollowLinks(follow);
        firePropertyChange(OptionsProperty.FollowLinks.toString(), oldPreserve, follow);
    }

    public void setBuildRestoreScript(Boolean build) {
        Boolean oldBuild = optionsProxy.isBuildRestoreScript();
        optionsProxy.setBuildRestoreScript(build);
        firePropertyChange(OptionsProperty.BuildRestoreScript.toString(), oldBuild, build);
    }

    public void setPromptBeforeOverwrite(Boolean prompt) {
        Boolean oldPrompt = optionsProxy.isPromptBeforeOverwrite();
        optionsProxy.setPromptBeforeOverwrite(prompt);
        firePropertyChange(OptionsProperty.PromptBeforeOverwrite.toString(), oldPrompt, prompt);
    }

    public void setUpdate(Boolean update) {
        Boolean oldUpdate = optionsProxy.isUpdate();
        optionsProxy.setUpdate(update);
        firePropertyChange(OptionsProperty.Update.toString(), oldUpdate, update);
    }

    public void setRenameSuffixFormat(String renameSuffixFormat) {
        String oldRenameSuffixFormat = optionsProxy.getRenameSuffixFormat();
        optionsProxy.setRenameSuffixFormat(renameSuffixFormat);
        firePropertyChange(OptionsProperty.RenameSuffixFormat.toString(), oldRenameSuffixFormat,
                renameSuffixFormat);
    }

    public void setUseGMT(Boolean useGMT) {
        Boolean oldUseGMT = optionsProxy.isUseGMT();
        optionsProxy.setUseGMT(useGMT);
        firePropertyChange(OptionsProperty.UseGMT.toString(), oldUseGMT, useGMT);
    }

    public void setRenameType(RenameType renameType) {
        RenameType oldRenameType = optionsProxy.getRenameType();
        optionsProxy.setRenameType(renameType);
        firePropertyChange(OptionsProperty.RenameType.toString(), oldRenameType, renameType);
    }

    public void setFileCopyFilterValue(String value) {
        String oldValue = optionsProxy.getFileCopyFilterValue();
        optionsProxy.setFileCopyFilterValue(value);
        firePropertyChange(OptionsProperty.FileCopyFilterValue.toString(), oldValue, value);
    }

    public void setFileCopyFilterExclusive(Boolean exclusive) {
        Boolean oldExclusive = optionsProxy.isFileCopyFilterExclusive();
        optionsProxy.setFileCopyFilterExclusive(exclusive);
        firePropertyChange(OptionsProperty.FileCopyFilterExclusive.toString(), oldExclusive, exclusive);
    }

    public void setFileCopyFilterCaseSensitive(Boolean caseSensitive) {
        Boolean oldCaseSensitive = optionsProxy.isFileCopyFilterCaseSensitive();
        optionsProxy.setFileCopyFilterCaseSensitive(caseSensitive);
        firePropertyChange(OptionsProperty.FileCopyFilterCaseSensitive.toString(), oldCaseSensitive, caseSensitive);
    }

    public void setFileCopyFilterRegEx(Boolean regex) {
        Boolean oldRegex = optionsProxy.isFileCopyFilterRegEx();
        optionsProxy.setFileCopyFilterRegEx(regex);
        firePropertyChange(OptionsProperty.FileCopyFilterRegEx.toString(), oldRegex, regex);
    }

    public void setDirCopyFilterValue(String value) {
        String oldValue = optionsProxy.getDirCopyFilterValue();
        optionsProxy.setDirCopyFilterValue(value);
        firePropertyChange(OptionsProperty.DirCopyFilterValue.toString(), oldValue, value);
    }

    public void setDirCopyFilterExclusive(Boolean exclusive) {
        Boolean oldExclusive = optionsProxy.isDirCopyFilterExclusive();
        optionsProxy.setDirCopyFilterExclusive(exclusive);
        firePropertyChange(OptionsProperty.DirCopyFilterExclusive.toString(), oldExclusive, exclusive);
    }

    public void setDirCopyFilterCaseSensitive(Boolean caseSensitive) {
        Boolean oldCaseSensitive = optionsProxy.isDirCopyFilterCaseSensitive();
        optionsProxy.setDirCopyFilterCaseSensitive(caseSensitive);
        firePropertyChange(OptionsProperty.DirCopyFilterCaseSensitive.toString(), oldCaseSensitive, caseSensitive);
    }

    public void setDirCopyFilterRegEx(Boolean regex) {
        Boolean oldRegex = optionsProxy.isDirCopyFilterRegEx();
        optionsProxy.setDirCopyFilterRegEx(regex);
        firePropertyChange(OptionsProperty.DirCopyFilterRegEx.toString(), oldRegex, regex);
    }

    public void setFlattenFilterValue(String value) {
        String oldValue = optionsProxy.getFlattenFilterValue();
        optionsProxy.setFlattenFilterValue(value);
        firePropertyChange(OptionsProperty.FlattenFilterValue.toString(), oldValue, value);
    }

    public void setFlattenFilterExclusive(Boolean exclusive) {
        Boolean oldExclusive = optionsProxy.isFlattenFilterExclusive();
        optionsProxy.setFlattenFilterExclusive(exclusive);
        firePropertyChange(OptionsProperty.FlattenFilterExclusive.toString(), oldExclusive, exclusive);
    }

    public void setFlattenFilterCaseSensitive(Boolean caseSensitive) {
        Boolean oldCaseSensitive = optionsProxy.isFlattenFilterCaseSensitive();
        optionsProxy.setFlattenFilterCaseSensitive(caseSensitive);
        firePropertyChange(OptionsProperty.FlattenFilterCaseSensitive.toString(), oldCaseSensitive, caseSensitive);
    }

    public void setFlattenFilterRegEx(Boolean regex) {
        Boolean oldRegex = optionsProxy.isFlattenFilterRegEx();
        optionsProxy.setFlattenFilterRegEx(regex);
        firePropertyChange(OptionsProperty.FlattenFilterRegEx.toString(), oldRegex, regex);
    }

    public void setMergeFilterValue(String value) {
        String oldValue = optionsProxy.getMergeFilterValue();
        optionsProxy.setMergeFilterValue(value);
        firePropertyChange(OptionsProperty.MergeFilterValue.toString(), oldValue, value);
    }

    public void setMergeFilterExclusive(Boolean exclusive) {
        Boolean oldExclusive = optionsProxy.isMergeFilterExclusive();
        optionsProxy.setMergeFilterExclusive(exclusive);
        firePropertyChange(OptionsProperty.MergeFilterExclusive.toString(), oldExclusive, exclusive);
    }

    public void setMergeFilterCaseSensitive(Boolean caseSensitive) {
        Boolean oldCaseSensitive = optionsProxy.isMergeFilterCaseSensitive();
        optionsProxy.setMergeFilterCaseSensitive(caseSensitive);
        firePropertyChange(OptionsProperty.MergeFilterCaseSensitive.toString(), oldCaseSensitive, caseSensitive);
    }

    public void setMergeFilterRegEx(Boolean regex) {
        Boolean oldRegex = optionsProxy.isMergeFilterRegEx();
        optionsProxy.setMergeFilterRegEx(regex);
        firePropertyChange(OptionsProperty.MergeFilterRegEx.toString(), oldRegex, regex);
    }

    public void setMaxCopyLevel(Integer maxCopyLevel) {
        Integer oldMaxCopyLevel = optionsProxy.getMaxCopyLevel();
        optionsProxy.setMaxCopyLevel(maxCopyLevel);
        firePropertyChange(OptionsProperty.MaxCopyLevel.toString(), oldMaxCopyLevel, maxCopyLevel);
    }

    public void setMaxFlattenLevel(Integer maxFlattenLevel) {
        Integer oldMaxFlattenLevel = optionsProxy.getMaxFlattenLevel();
        optionsProxy.setMaxFlattenLevel(maxFlattenLevel);
        firePropertyChange(OptionsProperty.MaxFlattenLevel.toString(), oldMaxFlattenLevel, maxFlattenLevel);
    }

    public void setMaxMergeLevel(Integer maxMergeLevel) {
        Integer oldMaxMergeLevel = optionsProxy.getMaxMergeLevel();
        optionsProxy.setMaxMergeLevel(maxMergeLevel);
        firePropertyChange(OptionsProperty.MaxMergeLevel.toString(), oldMaxMergeLevel, maxMergeLevel);
    }

    public void setOptions(OptionsScript options) {
        if(!options.equals(optionsProxy.getOptions())) {
            setCopyAttributes(options.isCopyAttributes());
            setFollowLinks(options.isFollowLinks());
            RenameOptions ro = options.getRenameOptions();
            if(!ro.equals(optionsProxy.getOptions().getRenameOptions())) {
                setBuildRestoreScript(ro.isBuildRestoreScript());
                setPromptBeforeOverwrite(ro.isPromptBeforeOverwrite());
                setUpdate(ro.isUpdate());
                setRenameSuffixFormat(ro.getRenameSuffixOptions().getFormat());
                setUseGMT(ro.getRenameSuffixOptions().isUseGMT());
                setRenameType(ro.getRenameType());
            }
            SrcDirOptions sdo = options.getSrcDirOptions();
            if(!sdo.equals(optionsProxy.getOptions().getSrcDirOptions())) {
                Filter filter = sdo.getFileCopyFilter();
                if(!filter.equals(optionsProxy.getOptions().getSrcDirOptions().getFileCopyFilter())) {
                    setFileCopyFilterValue(filter.getValue());
                    setFileCopyFilterExclusive(filter.isExclusive());
                    setFileCopyFilterCaseSensitive(filter.isCaseSensitive());
                    setFileCopyFilterRegEx(filter.isRegularExpression());
                }
                filter = sdo.getDirCopyFilter();
                if(!filter.equals(optionsProxy.getOptions().getSrcDirOptions().getDirCopyFilter())) {
                    setDirCopyFilterValue(filter.getValue());
                    setDirCopyFilterExclusive(filter.isExclusive());
                    setDirCopyFilterCaseSensitive(filter.isCaseSensitive());
                    setDirCopyFilterRegEx(filter.isRegularExpression());
                }
                filter = sdo.getFlattenFilter();
                if(!filter.equals(optionsProxy.getOptions().getSrcDirOptions().getFlattenFilter())) {
                    setFlattenFilterValue(filter.getValue());
                    setFlattenFilterExclusive(filter.isExclusive());
                    setFlattenFilterCaseSensitive(filter.isCaseSensitive());
                    setFlattenFilterRegEx(filter.isRegularExpression());
                }
                filter = sdo.getMergeFilter();
                if(!filter.equals(optionsProxy.getOptions().getSrcDirOptions().getMergeFilter())) {
                    setMergeFilterValue(filter.getValue());
                    setMergeFilterExclusive(filter.isExclusive());
                    setMergeFilterCaseSensitive(filter.isCaseSensitive());
                    setMergeFilterRegEx(filter.isRegularExpression());
                }
                setMaxCopyLevel(sdo.getMaxCopyLevel());
                setMaxFlattenLevel(sdo.getMaxFlattenLevel());
                setMaxMergeLevel(sdo.getMaxMergeLevel());
            }
        }
    }

    /**
     * This method returns the default options that are the standard. They are on the classpath.
     */
    public void getCachedDefaultOptions() {
        setOptions(cachedDefaultOptions);
    }

    public void getDiskDefaultOptions() {
        try {
            setOptions(requestFactory.getDiskDefaultOptions());
//            snapshotOptions = optionsProxy.getOptions();
        } catch (JAXBException e) {
            firePropertyChange(OptionsProperty.DiskDefaultOptionsFailure.toString(), null, e.getMessage());
        }
    }

    public void getSnapshotOptions() {
        snapshotOptions = optionsProxy.getOptions();
    }

    public void getRestoreSnapshotOptions() {
        setOptions(snapshotOptions);
    }

    public void getSaveOptions() {
        getSaveOptions(OptionsProperty.SaveOptions.toString(), OptionsProperty.SaveOptionsFailure.toString());
    }

    public void getSaveOptionsFromOk() {
        getSaveOptions(OptionsProperty.SaveOptionsFromOk.toString(), OptionsProperty.SaveOptionsFromOkFailure.toString());
    }

    private void getSaveOptions(String propertyNm, String failurePropNm) {
        if(optionsProxy.isFileConfigured()) {
            try {
                optionsProxy.saveOptionsToDisk();
                // Update snapshotOptions to match what we just saved to disk.
                snapshotOptions = optionsProxy.getOptions();
                firePropertyChange(propertyNm, Boolean.FALSE, Boolean.TRUE);
            } catch (JAXBException e) {
                // Send a save options failure message with the message from the JAXBException as the object.
                firePropertyChange(failurePropNm, null, e.getMessage());
                // TODO Log the Exception.
                e.printStackTrace();
            }
        } else {
            // If optionsProxy.isFileConfigured returns false, it means the Options object has not been saved to disk yet.
            // Return the value of FALSE so the view will know that the attempt to save failed. The view should then give
            // the user the option to "save as".
            firePropertyChange(propertyNm, Boolean.TRUE, Boolean.FALSE);
        }
    }

    public void setSaveOptionsAs(File optionsFile) {
        setSaveOptionsAs(OptionsProperty.SaveOptionsAs.toString(), OptionsProperty.SaveOptionsFailure.toString(), optionsFile);
    }

    public void setSaveOptionsAsFromOk(File optionsFile) {
        setSaveOptionsAs(OptionsProperty.SaveOptionsAsFromOk.toString(), OptionsProperty.SaveOptionsAsFromOkFailure.toString(), optionsFile);
    }

    public void setSaveOptionsAs(String propertyNm, String failurePropNm, File optionsFile) {
        try {
            File oldOptionsFile = optionsProxy.getFile();
            optionsProxy = optionsProxy.saveOptionsToDiskAs(optionsFile);
            firePropertyChange(propertyNm, oldOptionsFile, optionsFile);
        } catch (JAXBException|SAXException|IOException e) {
            // Send a save options failure message with the message from the JAXBException as the object.
            firePropertyChange(failurePropNm, null, e.getMessage());
            // TODO Log the Exception.
            e.printStackTrace();
        }
    }

    public void getNewOptions() {
        // Set return value of requestFactory.getOptionsScriptProxy to a new OptionsScriptProxy instance.
        // This is so that we can call setOptions and it will update the OptionsScript object of the
        // OptionModel's OptionsScriptProxy object. Otherwise, setOptions would not send messages to the
        // views because the two options objects would be equal.
        OptionsScriptProxy newOptionsProxy = requestFactory.getOptionsScriptProxy();
        setOptions(newOptionsProxy.getOptions());
        // It's important to set optionsProxy to the newOptionsScriptProxy object. The optionsProxy object
        // may have been for an OptionsScript that had been saved to disk. This one is for a new Options
        // Script that has not been saved to disk.
        optionsProxy = newOptionsProxy;
        firePropertyChange(OptionsProperty.NewOptions.toString(), Boolean.FALSE, Boolean.TRUE);
    }

    public void setOpenOptions(File optionsFile) {
        try {
            OptionsScriptProxy newOptionsProxy = requestFactory.getOptionsScriptProxy(optionsFile);
            setOptions(newOptionsProxy.getOptions());
            firePropertyChange(OptionsProperty.OpenOptions.toString(), optionsProxy.getFile(), optionsFile);
            optionsProxy = newOptionsProxy;
        } catch (JAXBException|SAXException|IOException e) {
            // Send an open options failure message with the message from the JAXBException as the object.
            firePropertyChange(OptionsProperty.OpenOptionsFailure.toString(), null, e.getMessage());
            // TODO Log the Exception.
            e.printStackTrace();
        }

    }

    /**
     * This method retrieves the Options paths that are saved in the settings.xml file on disk and returns it
     * to the Options dialog so that it will know what the default directory in which to save an Options file
     * should be.
     * @param set
     */
    public void getSaveAsOptionsPath() {
        try {
            Settings settings = requestFactory.getSettingsFromDisk();
            DirPath dirPath = new DirPath(settings.getPaths().getOptionsPath());
            firePropertyChange(OptionsProperty.SaveAsOptionsPath.toString(), null, dirPath);
        } catch (JAXBException e) {
            // Send an open options failure message with the message from the JAXBException as the object.
            firePropertyChange(OptionsProperty.SaveAsOptionsPathFailure.toString(), null, e.getMessage());
            // TODO Log the Exception.
            e.printStackTrace();
        }
    }

    /**
     * This method retrieves the Options paths that are saved in the settings.xml file on disk and returns it
     * to the Options dialog so that it will know what the default directory in which to open an Options file
     * should be.
     * @param set
     */
    public void getOpenOptionsPath() {
        try {
            Settings settings = requestFactory.getSettingsFromDisk();
            DirPath dirPath = new DirPath(settings.getPaths().getOptionsPath());
            firePropertyChange(OptionsProperty.OpenOptionsPath.toString(), null, dirPath);
        } catch (JAXBException e) {
            // Send an open options failure message with the message from the JAXBException as the object.
            firePropertyChange(OptionsProperty.OpenOptionsPathFailure.toString(), null, e.getMessage());
            // TODO Log the Exception.
            e.printStackTrace();
        }
    }
}
