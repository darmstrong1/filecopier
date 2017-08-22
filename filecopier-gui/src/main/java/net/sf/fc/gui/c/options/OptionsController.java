package net.sf.fc.gui.c.options;

import java.io.File;

import net.sf.fc.gui.c.BaseController;
import net.sf.fc.script.gen.options.RenameType;

public class OptionsController extends BaseController {

    public void changeCopyAttributes(boolean preserve) {
        setModelProperty(OptionsProperty.CopyAttributes.toString(), preserve);
    }

    public void changeFollowLinks(boolean follow) {
        setModelProperty(OptionsProperty.FollowLinks.toString(), follow);
    }

    public void changeBuildRestoreScript(boolean build) {
        setModelProperty(OptionsProperty.BuildRestoreScript.toString(), build);
    }

    public void changePromptBeforeOverwrite(boolean prompt) {
        setModelProperty(OptionsProperty.PromptBeforeOverwrite.toString(), prompt);
    }

    public void changeUpdate(boolean update) {
        setModelProperty(OptionsProperty.Update.toString(), update);
    }

    public void changeRenameSuffixFormat(String renameSuffixFormat) {
        setModelProperty(OptionsProperty.RenameSuffixFormat.toString(), renameSuffixFormat);
    }

    public void changeUseGMT(boolean useGMT) {
        setModelProperty(OptionsProperty.UseGMT.toString(), useGMT);
    }

    public void changeRenameType(RenameType renameType) {
        setModelProperty(OptionsProperty.RenameType.toString(), renameType);
    }

    public void changeFileCopyFilterValue(String value) {
        setModelProperty(OptionsProperty.FileCopyFilterValue.toString(), value);
    }

    public void changeFileCopyFilterExclusive(boolean exclusive) {
        setModelProperty(OptionsProperty.FileCopyFilterExclusive.toString(), exclusive);
    }

    public void changeFileCopyFilterCaseSensitive(boolean caseSensitive) {
        setModelProperty(OptionsProperty.FileCopyFilterCaseSensitive.toString(), caseSensitive);
    }

    public void changeFileCopyFilterRegEx(boolean regex) {
        setModelProperty(OptionsProperty.FileCopyFilterRegEx.toString(), regex);
    }

    public void changeDirCopyFilterValue(String value) {
        setModelProperty(OptionsProperty.DirCopyFilterValue.toString(), value);
    }

    public void changeDirCopyFilterExclusive(boolean exclusive) {
        setModelProperty(OptionsProperty.DirCopyFilterExclusive.toString(), exclusive);
    }

    public void changeDirCopyFilterCaseSensitive(boolean caseSensitive) {
        setModelProperty(OptionsProperty.DirCopyFilterCaseSensitive.toString(), caseSensitive);
    }

    public void changeDirCopyFilterRegEx(boolean regex) {
        setModelProperty(OptionsProperty.DirCopyFilterRegEx.toString(), regex);
    }

    public void changeFlattenFilterValue(String value) {
        setModelProperty(OptionsProperty.FlattenFilterValue.toString(), value);
    }

    public void changeFlattenFilterExclusive(boolean exclusive) {
        setModelProperty(OptionsProperty.FlattenFilterExclusive.toString(), exclusive);
    }

    public void changeFlattenFilterCaseSensitive(boolean caseSensitive) {
        setModelProperty(OptionsProperty.FlattenFilterCaseSensitive.toString(), caseSensitive);
    }

    public void changeFlattenFilterRegEx(boolean regex) {
        setModelProperty(OptionsProperty.FlattenFilterRegEx.toString(), regex);
    }

    public void changeMergeFilterValue(String value) {
        setModelProperty(OptionsProperty.MergeFilterValue.toString(), value);
    }

    public void changeMergeFilterExclusive(boolean exclusive) {
        setModelProperty(OptionsProperty.MergeFilterExclusive.toString(), exclusive);
    }

    public void changeMergeFilterCaseSensitive(boolean caseSensitive) {
        setModelProperty(OptionsProperty.MergeFilterCaseSensitive.toString(), caseSensitive);
    }

    public void changeMergeFilterRegEx(boolean regex) {
        setModelProperty(OptionsProperty.MergeFilterRegEx.toString(), regex);
    }

    public void changeMaxCopyLevel(int maxCopyLevel) {
        setModelProperty(OptionsProperty.MaxCopyLevel.toString(), maxCopyLevel);
    }

    public void changeMaxFlattenLevel(int maxFlattenLevel) {
        setModelProperty(OptionsProperty.MaxFlattenLevel.toString(), maxFlattenLevel);
    }

    public void changeMaxMergeLevel(int maxMergeLevel) {
        setModelProperty(OptionsProperty.MaxMergeLevel.toString(), maxMergeLevel);
    }

    public void retrieveCachedDefaultOptions() {
        getModelProperty(OptionsProperty.CachedDefaultOptions.toString());
    }

    public void retrieveDiskDefaultOptions() {
        getModelProperty(OptionsProperty.DiskDefaultOptions.toString());
    }

    public void retrieveSnapshotOptions() {
        getModelProperty(OptionsProperty.SnapshotOptions.toString());
    }

    public void retrieveRestoreSnapshotOptions() {
        getModelProperty(OptionsProperty.RestoreSnapshotOptions.toString());
    }

    public void changeSaveOptionsAs(File optionsFile) {
        setModelProperty(OptionsProperty.SaveOptionsAs.toString(), optionsFile);
    }

    public void retrieveSaveOptionsFromOk() {
        getModelProperty(OptionsProperty.SaveOptionsFromOk.toString());
    }

    public void changeSaveOptionsAsFromOk(File optionsFile) {
        setModelProperty(OptionsProperty.SaveOptionsAsFromOk.toString(), optionsFile);
    }

    public void retrieveNewOptions() {
        getModelProperty(OptionsProperty.NewOptions.toString());
    }

    public void changeOpenOptions(File optionsFile) {
        setModelProperty(OptionsProperty.OpenOptions.toString(), optionsFile);
    }

    public void retrieveSaveAsOptionsPath() {
        getModelProperty(OptionsProperty.SaveAsOptionsPath.toString());
    }

    public void retrieveSaveOptions() {
        getModelProperty(OptionsProperty.SaveOptions.toString());
    }

    public void retrieveOpenOptionsPath() {
        getModelProperty(OptionsProperty.OpenOptionsPath.toString());
    }
}
