package net.sf.fc.gui.m.copy;

import javax.xml.bind.JAXBException;

import net.sf.fc.cfg.RequestFactory;
import net.sf.fc.gui.c.copy.CopyProperty;
import net.sf.fc.gui.m.BaseModel;
import net.sf.fc.script.SettingsProxy;
import net.sf.fc.script.gen.options.OptionsScript;

public class CopyModel extends BaseModel {

    private final RequestFactory requestFactory;
    private final SettingsProxy settingsProxy;
    private final OptionsScript cdos;

    /**
     * Constructs a CopyModel.
     * @param requestFactory - RequestFactory object. Used to get the default OptionsScript object that is saved to disk.
     * @param settingsProxy - SettingsProxy object, used to get the last selected source/destination directory.
     * @param cachedDefaultOptionsScript - "default" OptionsScript object that is cached at startup. It is used to retrieve
     * a default value if the default OptionsScript object that is saved to disk cannot be retrieved for some reason.
     */
    public CopyModel(RequestFactory requestFactory, SettingsProxy settingsProxy, OptionsScript cachedDefaultOptionsScript) {
        this.requestFactory = requestFactory;
        this.settingsProxy = settingsProxy;
        cdos = cachedDefaultOptionsScript;
    }

    public void getDefaultRenameType() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultRenameType.toString(), null, optionsScript.getRenameOptions().getRenameType());
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultRenameTypeFailure.toString(), null, e);
        }
    }

    /**
     * This method retrieves the RenameType from the cached default options script object. It sends the RenameType to the view
     * using the DefaultRenameType string value since the view will handle this event the same as if the model got it from the
     * saved default options script object.
     */
    public void getCachedDefaultRenameType() {
        firePropertyChange(CopyProperty.DefaultRenameType.toString(), null, cdos.getRenameOptions().getRenameType());
    }

    public void getDefaultRenameSfxFmt() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultRenameSfxFmt.toString(), null, optionsScript.getRenameOptions().getRenameSuffixOptions().getFormat());
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultRenameSfxFmtFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultRenameSfxFmt() {
        firePropertyChange(CopyProperty.DefaultRenameSfxFmt.toString(), null, cdos.getRenameOptions().getRenameSuffixOptions().getFormat());
    }

    public void getDefaultRenameUseGMT() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultRenameUseGMT.toString(), null, Boolean.valueOf(optionsScript.getRenameOptions().getRenameSuffixOptions().isUseGMT()));
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultRenameUseGMTFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultRenameUseGMT() {
        firePropertyChange(CopyProperty.DefaultRenameUseGMT.toString(), null, Boolean.valueOf(cdos.getRenameOptions().getRenameSuffixOptions().isUseGMT()));
    }

    public void getDefaultUpdate() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultUpdate.toString(), null, Boolean.valueOf(optionsScript.getRenameOptions().isUpdate()));
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultUpdateFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultUpdate() {
        firePropertyChange(CopyProperty.DefaultUpdate.toString(), null, Boolean.valueOf(cdos.getRenameOptions().isUpdate()));
    }

    public void getDefaultPromptBeforeOverwrite() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultPromptBeforeOverwrite.toString(), null, Boolean.valueOf(optionsScript.getRenameOptions().isPromptBeforeOverwrite()));
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultPromptBeforeOverwriteFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultPromptBeforeOverwrite() {
        firePropertyChange(CopyProperty.DefaultPromptBeforeOverwrite.toString(), null, Boolean.valueOf(cdos.getRenameOptions().isPromptBeforeOverwrite()));
    }

    public void getDefaultBuildRestoreScript() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultBuildRestoreScript.toString(), null, Boolean.valueOf(optionsScript.getRenameOptions().isBuildRestoreScript()));
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultBuildRestoreScriptFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultBuildRestoreScript() {
        firePropertyChange(CopyProperty.DefaultBuildRestoreScript.toString(), null, Boolean.valueOf(cdos.getRenameOptions().isBuildRestoreScript()));
    }

    public void getDefaultMaxCopyLevel() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultMaxCopyLevel.toString(), null, Integer.valueOf(optionsScript.getSrcDirOptions().getMaxCopyLevel()));
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultMaxCopyLevelFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultMaxCopyLevel() {
        firePropertyChange(CopyProperty.DefaultMaxCopyLevel.toString(), null, Integer.valueOf(cdos.getSrcDirOptions().getMaxCopyLevel()));
    }

    public void getDefaultMaxMergeLevel() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultMaxMergeLevel.toString(), null, Integer.valueOf(optionsScript.getSrcDirOptions().getMaxMergeLevel()));
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultMaxMergeLevelFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultMaxMergeLevel() {
        firePropertyChange(CopyProperty.DefaultMaxMergeLevel.toString(), null, Integer.valueOf(cdos.getSrcDirOptions().getMaxMergeLevel()));
    }

    public void getDefaultMaxFlattenLevel() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultMaxFlattenLevel.toString(), null, Integer.valueOf(optionsScript.getSrcDirOptions().getMaxFlattenLevel()));
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultMaxFlattenLevelFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultMaxFlattenLevel() {
        firePropertyChange(CopyProperty.DefaultMaxFlattenLevel.toString(), null, Integer.valueOf(cdos.getSrcDirOptions().getMaxFlattenLevel()));
    }

    public void getDefaultCopyAttributes() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultCopyAttributes.toString(), null, Boolean.valueOf(optionsScript.isCopyAttributes()));
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultCopyAttributesFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultCopyAttributes() {
        firePropertyChange(CopyProperty.DefaultCopyAttributes.toString(), null, Boolean.valueOf(cdos.isCopyAttributes()));
    }

    public void getDefaultFollowLinks() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultFollowLinks.toString(), null, Boolean.valueOf(optionsScript.isFollowLinks()));
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultFollowLinksFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultFollowLinks() {
        firePropertyChange(CopyProperty.DefaultFollowLinks.toString(), null, Boolean.valueOf(cdos.isFollowLinks()));
    }

    public void getDefaultFileCopyFilterValue() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultFileCopyFilterValue.toString(), null, optionsScript.getSrcDirOptions().getFileCopyFilter().getValue());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultFileCopyFilterValueFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultFileCopyFilterValue() {
        firePropertyChange(CopyProperty.DefaultFileCopyFilterValue.toString(), null, cdos.getSrcDirOptions().getFileCopyFilter().getValue());
    }

    public void getDefaultFileCopyFilterCaseSensitive() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultFileCopyFilterCaseSensitive.toString(), null, optionsScript.getSrcDirOptions().getFileCopyFilter().isCaseSensitive());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultFileCopyFilterCaseSensitiveFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultFileCopyFilterCaseSensitive() {
        firePropertyChange(CopyProperty.DefaultFileCopyFilterCaseSensitive.toString(), null, cdos.getSrcDirOptions().getFileCopyFilter().isCaseSensitive());
    }

    public void getDefaultFileCopyFilterExclusive() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultFileCopyFilterExclusive.toString(), null, optionsScript.getSrcDirOptions().getFileCopyFilter().isExclusive());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultFileCopyFilterExclusiveFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultFileCopyFilterExclusive() {
        firePropertyChange(CopyProperty.DefaultFileCopyFilterExclusive.toString(), null, cdos.getSrcDirOptions().getFileCopyFilter().isExclusive());
    }

    public void getDefaultFileCopyFilterRegex() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultFileCopyFilterRegex.toString(), null, optionsScript.getSrcDirOptions().getFileCopyFilter().isRegularExpression());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultFileCopyFilterRegexFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultFileCopyFilterRegex() {
        firePropertyChange(CopyProperty.DefaultFileCopyFilterRegex.toString(), null, cdos.getSrcDirOptions().getFileCopyFilter().isRegularExpression());
    }

    public void getDefaultDirCopyFilterValue() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultDirCopyFilterValue.toString(), null, optionsScript.getSrcDirOptions().getDirCopyFilter().getValue());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultDirCopyFilterValueFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultDirCopyFilterValue() {
        firePropertyChange(CopyProperty.DefaultDirCopyFilterValue.toString(), null, cdos.getSrcDirOptions().getDirCopyFilter().getValue());
    }

    public void getDefaultDirCopyFilterCaseSensitive() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultDirCopyFilterCaseSensitive.toString(), null, optionsScript.getSrcDirOptions().getDirCopyFilter().isCaseSensitive());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultDirCopyFilterCaseSensitiveFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultDirCopyFilterCaseSensitive() {
        firePropertyChange(CopyProperty.DefaultDirCopyFilterCaseSensitive.toString(), null, cdos.getSrcDirOptions().getDirCopyFilter().isCaseSensitive());
    }

    public void getDefaultDirCopyFilterExclusive() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultDirCopyFilterExclusive.toString(), null, optionsScript.getSrcDirOptions().getDirCopyFilter().isExclusive());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultDirCopyFilterExclusiveFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultDirCopyFilterExclusive() {
        firePropertyChange(CopyProperty.DefaultDirCopyFilterExclusive.toString(), null, cdos.getSrcDirOptions().getDirCopyFilter().isExclusive());
    }

    public void getDefaultDirCopyFilterRegex() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultDirCopyFilterRegex.toString(), null, optionsScript.getSrcDirOptions().getDirCopyFilter().isRegularExpression());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultDirCopyFilterRegexFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultDirCopyFilterRegex() {
        firePropertyChange(CopyProperty.DefaultDirCopyFilterRegex.toString(), null, cdos.getSrcDirOptions().getDirCopyFilter().isRegularExpression());
    }

    public void getDefaultMergeFilterValue() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultMergeFilterValue.toString(), null, optionsScript.getSrcDirOptions().getMergeFilter().getValue());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultMergeFilterValueFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultMergeFilterValue() {
        firePropertyChange(CopyProperty.DefaultMergeFilterValue.toString(), null, cdos.getSrcDirOptions().getMergeFilter().getValue());
    }

    public void getDefaultMergeFilterCaseSensitive() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultMergeFilterCaseSensitive.toString(), null, optionsScript.getSrcDirOptions().getMergeFilter().isCaseSensitive());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultMergeFilterCaseSensitiveFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultMergeFilterCaseSensitive() {
        firePropertyChange(CopyProperty.DefaultMergeFilterCaseSensitive.toString(), null, cdos.getSrcDirOptions().getMergeFilter().isCaseSensitive());
    }

    public void getDefaultMergeFilterExclusive() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultMergeFilterExclusive.toString(), null, optionsScript.getSrcDirOptions().getMergeFilter().isExclusive());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultMergeFilterExclusiveFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultMergeFilterExclusive() {
        firePropertyChange(CopyProperty.DefaultMergeFilterExclusive.toString(), null, cdos.getSrcDirOptions().getMergeFilter().isExclusive());
    }

    public void getDefaultMergeFilterRegex() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultMergeFilterRegex.toString(), null, optionsScript.getSrcDirOptions().getMergeFilter().isRegularExpression());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultMergeFilterRegexFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultMergeFilterRegex() {
        firePropertyChange(CopyProperty.DefaultMergeFilterRegex.toString(), null, cdos.getSrcDirOptions().getMergeFilter().isRegularExpression());
    }

    public void getDefaultFlattenFilterValue() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultFlattenFilterValue.toString(), null, optionsScript.getSrcDirOptions().getFlattenFilter().getValue());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultFlattenFilterValueFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultFlattenFilterValue() {
        firePropertyChange(CopyProperty.DefaultFlattenFilterValue.toString(), null, cdos.getSrcDirOptions().getFlattenFilter().getValue());
    }

    public void getDefaultFlattenFilterCaseSensitive() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultFlattenFilterCaseSensitive.toString(), null, optionsScript.getSrcDirOptions().getFlattenFilter().isCaseSensitive());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultFlattenFilterCaseSensitiveFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultFlattenFilterCaseSensitive() {
        firePropertyChange(CopyProperty.DefaultFlattenFilterCaseSensitive.toString(), null, cdos.getSrcDirOptions().getFlattenFilter().isCaseSensitive());
    }

    public void getDefaultFlattenFilterExclusive() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultFlattenFilterExclusive.toString(), null, optionsScript.getSrcDirOptions().getFlattenFilter().isExclusive());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultFlattenFilterExclusiveFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultFlattenFilterExclusive() {
        firePropertyChange(CopyProperty.DefaultFlattenFilterExclusive.toString(), null, cdos.getSrcDirOptions().getFlattenFilter().isExclusive());
    }

    public void getDefaultFlattenFilterRegex() {
        try {
            OptionsScript optionsScript = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultFlattenFilterRegex.toString(), null, optionsScript.getSrcDirOptions().getFlattenFilter().isRegularExpression());
        } catch(JAXBException e) {
            firePropertyChange(CopyProperty.DefaultFlattenFilterRegexFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultFlattenFilterRegex() {
        firePropertyChange(CopyProperty.DefaultFlattenFilterRegex.toString(), null, cdos.getSrcDirOptions().getFlattenFilter().isRegularExpression());
    }

    public void getLastSrcDirPath() {
        firePropertyChange(CopyProperty.LastSrcDirPath.toString(), null, settingsProxy.getLastSrcDirPath());
    }

    public void getLastDstDirPath() {
        firePropertyChange(CopyProperty.LastDstDirPath.toString(), null, settingsProxy.getLastDstDirPath());
    }

    public void setLastSrcDirPath(String lastSrcDirPath) {
        String oldLastSrcDirPath = settingsProxy.getLastSrcDirPath();
        settingsProxy.setLastSrcDirPath(lastSrcDirPath);
        firePropertyChange(CopyProperty.LastSrcDirPath.toString(), oldLastSrcDirPath, lastSrcDirPath);
    }

    public void setLastDstDirPath(String lastDstDirPath) {
        String oldLastDstDirPath = settingsProxy.getLastDstDirPath();
        settingsProxy.setLastDstDirPath(lastDstDirPath);
        firePropertyChange(CopyProperty.LastDstDirPath.toString(), oldLastDstDirPath, lastDstDirPath);
    }

    public void getDefaultOptions() {
        try {
            OptionsScript defOptions = requestFactory.getDiskDefaultOptions();
            firePropertyChange(CopyProperty.DefaultOptions.toString(), null, defOptions);
        } catch (JAXBException e) {
            firePropertyChange(CopyProperty.DefaultOptionsFailure.toString(), null, e);
        }
    }

    public void getCachedDefaultOptions() {
        firePropertyChange(CopyProperty.CachedDefaultOptions.toString(), null, cdos);
    }
}
