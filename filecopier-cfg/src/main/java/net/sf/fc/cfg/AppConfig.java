package net.sf.fc.cfg;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import net.sf.fc.script.ScriptHelper;
import net.sf.fc.script.gen.options.Filter;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.script.gen.options.RenameOptions;
import net.sf.fc.script.gen.options.RenameSuffixOptions;
import net.sf.fc.script.gen.options.RenameType;
import net.sf.fc.script.gen.options.SrcDirOptions;
import net.sf.fc.script.gen.settings.FileList;
import net.sf.fc.script.gen.settings.LogLevel;
import net.sf.fc.script.gen.settings.LookAndFeel;
import net.sf.fc.script.gen.settings.MostRecentlyUsed;
import net.sf.fc.script.gen.settings.Paths;
import net.sf.fc.script.gen.settings.Settings;

public final class AppConfig {

    private static final String fileSep = System.getProperty("file.separator");

    private final ScriptHelper<Settings> settingsHelper;
    private final ScriptHelper<OptionsScript> optionsHelper;
    private final Settings settings;
    private final OptionsScript defaultOptions;
    private final File settingsXml;
    private final File defaultOptionsXml;

    public AppConfig(ScriptHelper<Settings> settingsHelper,
            ScriptHelper<OptionsScript> optionsHelper) throws IOException, JAXBException {
        this.settingsHelper = settingsHelper;
        this.optionsHelper = optionsHelper;
        initDirectories();
        settingsXml = new File(System.getProperty("filecopier.cfg"), "settings.xml");
        defaultOptionsXml = new File(System.getProperty("filecopier.cfg"), "defaultOptions.xml");
        settings = createSettings();
        defaultOptions = createDefaultOptions();
    }

    private String initHomeDir() throws IOException {
        System.setProperty("filecopier.home", System.getProperty("user.home") +
                fileSep + "filecopier");
        File homeDir = new File(System.getProperty("filecopier.home"));
        if ((!homeDir.exists()) && (!homeDir.mkdir())) {
            throw new IOException(homeDir + "directory could not be created.");
        }

        return homeDir.getPath();
    }

    private String initScriptsDir(String homeDir) throws IOException {
        System.setProperty("filecopier.scripts", homeDir + fileSep + "scripts");
        File scriptsDir = new File(System.getProperty("filecopier.scripts"));
        if ((!scriptsDir.exists()) && (!scriptsDir.mkdir())) {
            throw new IOException(scriptsDir + "directory could not be created.");
        }

        return scriptsDir.getPath();
    }

    private String initDefaultCopyScriptsDir(String scriptsDir) throws IOException {
        System.setProperty("filecopier.scripts.copy", scriptsDir +
                fileSep + "copy");
        File defaultCopyScriptsDir = new File(System.getProperty("filecopier.scripts.copy"));
        if ((!defaultCopyScriptsDir.exists()) && (!defaultCopyScriptsDir.mkdir())) {
            throw new IOException(defaultCopyScriptsDir + "directory could not be created.");
        }
        return defaultCopyScriptsDir.getPath();
    }

    private String initDefaultRestoreScriptsDir(String scriptsDir) throws IOException {
        System.setProperty("filecopier.scripts.restore", scriptsDir +
                fileSep + "restore");
        File defaultRestoreScriptsDir = new File(System.getProperty("filecopier.scripts.restore"));
        if ((!defaultRestoreScriptsDir.exists()) && (!defaultRestoreScriptsDir.mkdir())) {
            throw new IOException(defaultRestoreScriptsDir + "directory could not be created.");
        }
        return defaultRestoreScriptsDir.getPath();
    }

    private String initDefaultOptionsScriptsDir(String scriptsDir) throws IOException {
        System.setProperty("filecopier.scripts.options", scriptsDir +
                fileSep + "options");
        File defaultOptionsScriptsDir = new File(System.getProperty("filecopier.scripts.options"));
        if ((!defaultOptionsScriptsDir.exists()) && (!defaultOptionsScriptsDir.mkdir())) {
            throw new IOException(defaultOptionsScriptsDir + "directory could not be created.");
        }
        return defaultOptionsScriptsDir.getPath();
    }

    private String initCfgDir(String homeDir) throws IOException {
        System.setProperty("filecopier.cfg", homeDir + fileSep + "cfg");
        File cfgDir = new File(System.getProperty("filecopier.cfg"));
        if ((!cfgDir.exists()) && (!cfgDir.mkdir())) {
            throw new IOException(cfgDir + "directory could not be created.");
        }

        return cfgDir.getPath();
    }

    private String initLogDir(String homeDir) throws IOException {
        System.setProperty("filecopier.log", homeDir + fileSep + "log");
        File logDir = new File(System.getProperty("filecopier.log"));
        if ((!logDir.exists()) && (!logDir.mkdir())) {
            throw new IOException(logDir + "directory could not be created.");
        }

        return logDir.getPath();
    }

    private void initDirectories() throws IOException {
        String homeDir = initHomeDir();
        String scriptsDir = initScriptsDir(homeDir);
        initDefaultCopyScriptsDir(scriptsDir);
        initDefaultRestoreScriptsDir(scriptsDir);
        initDefaultOptionsScriptsDir(scriptsDir);
        initCfgDir(homeDir);
        initLogDir(homeDir);
    }

    // Create default OptionsScript object. If not found on disk, create OptionsScript with default values.
    private OptionsScript createDefaultOptions() throws JAXBException {
        return(defaultOptionsXml.exists() ? optionsHelper.unmarshal(defaultOptionsXml) : createDefaultOptionsObject());
    }

    private OptionsScript createDefaultOptionsObject() {
        RenameOptions ro = new RenameOptions();
        ro.setRenameSuffixOptions(new RenameSuffixOptions());

        SrcDirOptions sdo = new SrcDirOptions();

        OptionsScript options = new OptionsScript();
        options.setRenameOptions(ro);
        options.setSrcDirOptions(sdo);

        return options;
    }

    // Create Settings object. If not found on disk, create Settings with default values.
    private Settings createSettings() throws JAXBException {
        return(settingsXml.exists() ? settingsHelper.unmarshal(settingsXml) : createDefaultSettings());
    }

    private Settings createDefaultSettings() {
        Settings settings = new Settings();
        MostRecentlyUsed mru = new MostRecentlyUsed();
        mru.setFileList(new FileList());
        settings.setMostRecentlyUsed(mru);
        settings.setLookAndFeel(new LookAndFeel());
        Paths paths = new Paths();
        paths.setCopyPath(System.getProperty("filecopier.scripts.copy"));
        paths.setOptionsPath(System.getProperty("filecopier.scripts.options"));
        paths.setRestorePath(System.getProperty("filecopier.scripts.restore"));
        settings.setPaths(paths);
        return settings;
    }

    private void ensureFileCopyFilterNotNull() {
        if(defaultOptions.getSrcDirOptions().getFileCopyFilter() == null) {
            defaultOptions.getSrcDirOptions().setFileCopyFilter(new Filter());
        }
    }

    private void ensureDirCopyFilterNotNull() {
        if(defaultOptions.getSrcDirOptions().getDirCopyFilter() == null) {
            defaultOptions.getSrcDirOptions().setDirCopyFilter(new Filter());
        }
    }

    private void ensureFlattenFilterNotNull() {
        if(defaultOptions.getSrcDirOptions().getFlattenFilter() == null) {
            defaultOptions.getSrcDirOptions().setFlattenFilter(new Filter());
        }
    }

    private void ensureMergeFilterNotNull() {
        if(defaultOptions.getSrcDirOptions().getMergeFilter() == null) {
            defaultOptions.getSrcDirOptions().setMergeFilter(new Filter());
        }
    }

    // OptionsScript getter method. Return a copy of the OptionsScript object. To change the default settings, clients must
    // use the setter methods defined in this class.
    public OptionsScript getDefaultOptions() {
        return (OptionsScript)defaultOptions.copyTo(null);
    }

    // OptionsScript setter methods
    public void setCopyAttributes(boolean preserve) {
        defaultOptions.setCopyAttributes(preserve);
    }

    public void setFollowLinks(boolean follow) {
        defaultOptions.setFollowLinks(follow);
    }

    public void setBuildRestoreScript(boolean build) {
        defaultOptions.getRenameOptions().setBuildRestoreScript(build);
    }

    public void setPromptBeforeOverwrite(boolean prompt) {
        defaultOptions.getRenameOptions().setPromptBeforeOverwrite(prompt);
    }

    public void setRenameSuffixFormat(String renameSuffixFormat) {
        if(renameSuffixFormat == null) throw new NullPointerException("Rename suffix format string must not be null");
        defaultOptions.getRenameOptions().getRenameSuffixOptions().setFormat(renameSuffixFormat);
    }

    public void setUseGMT(boolean useGMT) {
        defaultOptions.getRenameOptions().getRenameSuffixOptions().setUseGMT(useGMT);
    }

    public void setRenameSuffixOptions(RenameSuffixOptions rso) {
        if(rso == null) throw new NullPointerException("RenameSuffixOptions must not be null");
        setRenameSuffixFormat(rso.getFormat());
        setUseGMT(rso.isUseGMT());
    }

    public void setRenameType(RenameType renameType) {
        if(renameType == null) throw new NullPointerException("Rename type must not be null");
        defaultOptions.getRenameOptions().setRenameType(renameType);
    }

    public void setRenameOptions(RenameOptions renameOptions) {
        if(renameOptions == null) throw new NullPointerException("RenameOptions must not be null");
        setBuildRestoreScript(renameOptions.isBuildRestoreScript());
        setPromptBeforeOverwrite(renameOptions.isPromptBeforeOverwrite());
        setRenameSuffixOptions(renameOptions.getRenameSuffixOptions());
    }

    public void setFileCopyFilterValue(String value) {
        if(value == null) throw new NullPointerException("Copy filter value must not be null");
        ensureFileCopyFilterNotNull();
        defaultOptions.getSrcDirOptions().getFileCopyFilter().setValue(value);
    }

    public void setFileCopyFilterExclusive(boolean exclusive) {
        ensureFileCopyFilterNotNull();
        defaultOptions.getSrcDirOptions().getFileCopyFilter().setExclusive(exclusive);
    }

    public void setFileCopyFilterCaseSensitive(boolean caseSensitive) {
        ensureFileCopyFilterNotNull();
        defaultOptions.getSrcDirOptions().getFileCopyFilter().setCaseSensitive(caseSensitive);
    }

    public void setFileCopyFilterRegEx(boolean regex) {
        ensureFileCopyFilterNotNull();
        defaultOptions.getSrcDirOptions().getFileCopyFilter().setRegularExpression(regex);
    }

    public void setFileCopyFilter(Filter copyFilter) {
        // Filters are optional and can be null.
        if(copyFilter != null) {
            setFileCopyFilterValue(copyFilter.getValue());
            setFileCopyFilterExclusive(copyFilter.isExclusive());
            setFileCopyFilterCaseSensitive(copyFilter.isCaseSensitive());
            setFileCopyFilterRegEx(copyFilter.isRegularExpression());
        }
    }

    public void setDirCopyFilterValue(String value) {
        if(value == null) throw new NullPointerException("Copy filter value must not be null");
        ensureFileCopyFilterNotNull();
        defaultOptions.getSrcDirOptions().getDirCopyFilter().setValue(value);
    }

    public void setDirCopyFilterExclusive(boolean exclusive) {
        ensureFileCopyFilterNotNull();
        defaultOptions.getSrcDirOptions().getDirCopyFilter().setExclusive(exclusive);
    }

    public void setDirCopyFilterCaseSensitive(boolean caseSensitive) {
        ensureFileCopyFilterNotNull();
        defaultOptions.getSrcDirOptions().getDirCopyFilter().setCaseSensitive(caseSensitive);
    }

    public void setDirCopyFilterRegEx(boolean regex) {
        ensureFileCopyFilterNotNull();
        defaultOptions.getSrcDirOptions().getDirCopyFilter().setRegularExpression(regex);
    }

    public void setDirCopyFilter(Filter copyFilter) {
        // Filters are optional and can be null.
        if(copyFilter != null) {
            setDirCopyFilterValue(copyFilter.getValue());
            setDirCopyFilterExclusive(copyFilter.isExclusive());
            setDirCopyFilterCaseSensitive(copyFilter.isCaseSensitive());
            setDirCopyFilterRegEx(copyFilter.isRegularExpression());
        }
    }

    public void setFlattenFilterValue(String value) {
        if(value == null) throw new NullPointerException("Flatten filter value must not be null");
        ensureFlattenFilterNotNull();
        defaultOptions.getSrcDirOptions().getFlattenFilter().setValue(value);
    }

    public void setFlattenFilterExclusive(boolean exclusive) {
        ensureFlattenFilterNotNull();
        defaultOptions.getSrcDirOptions().getFlattenFilter().setExclusive(exclusive);
    }

    public void setFlattenFilterCaseSensitive(boolean caseSensitive) {
        ensureFlattenFilterNotNull();
        defaultOptions.getSrcDirOptions().getFlattenFilter().setCaseSensitive(caseSensitive);
    }

    public void setFlattenFilterRegEx(boolean regex) {
        ensureFlattenFilterNotNull();
        defaultOptions.getSrcDirOptions().getFlattenFilter().setRegularExpression(regex);
    }

    public void setFlattenFilter(Filter flattenFilter) {
        // Filters are optional and can be null.
        if(flattenFilter != null) {
            setFlattenFilterValue(flattenFilter.getValue());
            setFlattenFilterExclusive(flattenFilter.isExclusive());
            setFlattenFilterCaseSensitive(flattenFilter.isCaseSensitive());
            setFlattenFilterRegEx(flattenFilter.isRegularExpression());
        }
    }

    public void setMergeFilterValue(String value) {
        if(value == null) throw new NullPointerException("Merge filter value must not be null");
        ensureMergeFilterNotNull();
        defaultOptions.getSrcDirOptions().getMergeFilter().setValue(value);
    }

    public void setMergeFilterExclusive(boolean exclusive) {
        ensureMergeFilterNotNull();
        defaultOptions.getSrcDirOptions().getMergeFilter().setExclusive(exclusive);
    }

    public void setMergeFilterCaseSensitive(boolean caseSensitive) {
        ensureMergeFilterNotNull();
        defaultOptions.getSrcDirOptions().getMergeFilter().setCaseSensitive(caseSensitive);
    }

    public void setMergeFilterRegEx(boolean regex) {
        ensureMergeFilterNotNull();
        defaultOptions.getSrcDirOptions().getMergeFilter().setRegularExpression(regex);
    }

    public void setMergeFilter(Filter mergeFilter) {
        // Filters are optional and can be null.
        if(mergeFilter != null) {
            setMergeFilterValue(mergeFilter.getValue());
            setMergeFilterExclusive(mergeFilter.isExclusive());
            setMergeFilterCaseSensitive(mergeFilter.isCaseSensitive());
            setMergeFilterRegEx(mergeFilter.isRegularExpression());
        }
    }

    public void setMaxCopyLevel(int maxCopyLevel) {
        if(maxCopyLevel < 0) throw new IllegalArgumentException("Maximum copy level must be greater than or equal to 0");
        defaultOptions.getSrcDirOptions().setMaxCopyLevel(maxCopyLevel);
    }

    public void setMaxFlattenLevel(int maxFlattenLevel) {
        if(maxFlattenLevel < -1) throw new IllegalArgumentException("Maximum flatten level must be greater than or equal to -1");
        defaultOptions.getSrcDirOptions().setMaxFlattenLevel(maxFlattenLevel);
    }

    public void setMaxMergeLevel(int maxMergeLevel) {
        if(maxMergeLevel < -1) throw new IllegalArgumentException("Maximum merge level must be greater than or equal to -1");
        defaultOptions.getSrcDirOptions().setMaxMergeLevel(maxMergeLevel);
    }

    public void setSrcDirOptions(SrcDirOptions sdo) {
        if(sdo == null) throw new NullPointerException("SrcDirOptions must not be null");
        setFileCopyFilter(sdo.getFileCopyFilter());
        setDirCopyFilter(sdo.getDirCopyFilter());
        setFlattenFilter(sdo.getFlattenFilter());
        setMergeFilter(sdo.getMergeFilter());
        setMaxCopyLevel(sdo.getMaxCopyLevel());
        setMaxFlattenLevel(sdo.getMaxFlattenLevel());
        setMaxMergeLevel(sdo.getMaxMergeLevel());
    }

    public void setDefaultOptions(OptionsScript options) {
        if(options == null) throw new NullPointerException("OptionsScript must not be null");
        setCopyAttributes(options.isCopyAttributes());
        setFollowLinks(options.isFollowLinks());
        setRenameOptions(options.getRenameOptions());
        setSrcDirOptions(options.getSrcDirOptions());
    }

    // Settings getter methods
    public Settings getSettings() {
        return (Settings)settings.copyTo(null);
    }

    public LogLevel getLogLevel() {
        return settings.getLogLevel();
    }

    public int getMostRecentlyUsedLimit() {
        return settings.getMostRecentlyUsed().getLimit();
    }

    public FileList getMostRecentlyUsedList() {
        return (FileList)settings.getMostRecentlyUsed().getFileList().copyTo(null);
    }

    public MostRecentlyUsed getMostRecentlyUsed() {
        return (MostRecentlyUsed)settings.getMostRecentlyUsed().copyTo(null);
    }

    public String getLookAndFeelName() {
        return settings.getLookAndFeel().getSelected();
    }

    public boolean isDrawWindowBorders() {
        return settings.getLookAndFeel().isDrawWindowBorders();
    }

    public LookAndFeel getLookAndFeel() {
        return (LookAndFeel)settings.getLookAndFeel().copyTo(null);
    }

    public String getCopyPath() {
        return settings.getPaths().getCopyPath();
    }

    public String getRestorePath() {
        return settings.getPaths().getRestorePath();
    }

    public String getOptionsPath() {
        return settings.getPaths().getOptionsPath();
    }

    public Paths getPaths() {
        return (Paths)settings.getPaths().copyTo(null);
    }

    // Settings setter methods
    public void setLogLevel(LogLevel level) {
        if(level == null) throw new NullPointerException("LogLevel must not be null");
        settings.setLogLevel(level);
    }

    public void setMostRecentlyUsedLimit(int limit) {
        if(limit <= 0) throw new IllegalArgumentException("limit must be greater than 0");
        settings.getMostRecentlyUsed().setLimit(limit);
    }

    public void setMostRecentlyUsedList(FileList fileList) {
        if(fileList == null) throw new NullPointerException("FileList must not be null");
        settings.getMostRecentlyUsed().setFileList(fileList);
    }

    public void setMostRecentlyUsed(MostRecentlyUsed mru) {
        if(mru == null) throw new NullPointerException("MostRecentlyUsed must not be null");
        setMostRecentlyUsedLimit(mru.getLimit());
        setMostRecentlyUsedList(mru.getFileList());
    }

    public void setLookAndFeelName(String laf) {
        if(laf == null) throw new NullPointerException("Look and Feel name must not be null");
        settings.getLookAndFeel().setSelected(laf);
    }

    public void setDrawWindowBorders(boolean draw) {
        settings.getLookAndFeel().setDrawWindowBorders(draw);
    }

    public void setLookAndFeel(LookAndFeel laf) {
        if(laf == null) throw new NullPointerException("LookAndFeel must not be null");
        setLookAndFeelName(laf.getSelected());
        setDrawWindowBorders(laf.isDrawWindowBorders());
    }

    public void setCopyPath(String copyPath) {
        if(copyPath == null) throw new NullPointerException("Copy path string must not be null");
        settings.getPaths().setCopyPath(copyPath);
    }

    public void setRestorePath(String restorePath) {
        if(restorePath == null) throw new NullPointerException("Restore path string must not be null");
        settings.getPaths().setRestorePath(restorePath);
    }

    public void setOptionsPath(String optionsPath) {
        if(optionsPath == null) throw new NullPointerException("OptionsScript path string must not be null");
        settings.getPaths().setOptionsPath(optionsPath);
    }

    public void setPaths(Paths paths) {
        if(paths == null) throw new NullPointerException("Paths must not be null");
        setCopyPath(paths.getCopyPath());
        setRestorePath(paths.getRestorePath());
        setOptionsPath(paths.getOptionsPath());
    }

    public void setSettings(Settings st) {
        if(st == null) throw new NullPointerException("Settings must not be null");
        setLogLevel(st.getLogLevel());
        setLookAndFeel(st.getLookAndFeel());
        setMostRecentlyUsed(st.getMostRecentlyUsed());
        setPaths(st.getPaths());
    }

    /**
     * Put a new file at the beginning of the most recently used list.
     * @param file
     */
    public void addMostRecentlyUsedFile(File file) {
        if(file == null) throw new NullPointerException("File must not be null");
        MostRecentlyUsed mru = settings.getMostRecentlyUsed();
        List<File> files = mru.getFileList().getFiles();
        files.add(0, file);
        // See if the number of files in the list is greater than the limit. If it is greater, remove
        // the oldest one.
        int limit = mru.getLimit();
        if(files.size() > limit) {
            files.remove(limit-1);
        }
    }

    public void saveSettingsToDisk() throws JAXBException {
        Settings settingsFromDisk = settingsXml.exists() ? settingsHelper.unmarshal(settingsXml) : null;
        if(!settings.equals(settingsFromDisk)) {
            settingsHelper.marshal(settingsXml, settings);
        }
    }

    public void saveDefaultOptionsToDisk() throws JAXBException {
        OptionsScript optionsFromDisk = defaultOptionsXml.exists() ? optionsHelper.unmarshal(defaultOptionsXml) : null;
        if(!defaultOptions.equals(optionsFromDisk)) {
            optionsHelper.marshal(defaultOptionsXml, defaultOptions);
        }
    }
}
