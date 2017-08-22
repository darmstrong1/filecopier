package net.sf.fc.cfg;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import net.sf.fc.script.OptionsScriptProxy;
import net.sf.fc.script.SchemaData;
import net.sf.fc.script.ScriptHelper;
import net.sf.fc.script.SettingsProxy;
import net.sf.fc.script.gen.copy.CopyScript;
import net.sf.fc.script.gen.options.Filter;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.script.gen.options.RenameOptions;
import net.sf.fc.script.gen.options.RenameSuffixOptions;
import net.sf.fc.script.gen.options.SrcDirOptions;
import net.sf.fc.script.gen.settings.Settings;

/**
 * AppFactory creates singleton objects. Any object that depends on one of the objects created in AppFactory should
 * take that object as an argument in its constructor. Any object that depends on more than one object created in
 * AppFactory should take AppFactory as an argument in its constructor.
 *
 * @author David Armstrong
 *
 */
public final class AppFactory {

    private static final String fileSep = System.getProperty("file.separator");

    private final File defaultOptionsXml;
    private final File settingsXml;
    private final ScriptHelper<Settings> settingsHelper;
    private final ScriptHelper<OptionsScript> optionsHelper;
    private final ScriptHelper<CopyScript> copyScriptHelper;
    private final SettingsProxy settingsProxy;
    private final RequestFactory requestFactory;

    /**
     * OptionsScriptProxy object that represents the contents of what is saved in the default options
     * xml file. The file is saved in defaultOptionsXml.
     */
    private final OptionsScriptProxy diskDefaultOptionsScriptProxy;

    /**
     * OptionsScriptProxy object that represents the cached OptionsScript object in the classpath.
     */
    private final OptionsScriptProxy cachedDefaultOptionsScriptProxy;

    public AppFactory() throws JAXBException, SAXException, IOException {
        initDirectories();
        // XML Files
        defaultOptionsXml = new File(System.getProperty("filecopier.cfg"), "defaultOptions.xml");
        settingsXml = new File(System.getProperty("filecopier.cfg"), "settings.xml");

        // Helpers
        settingsHelper = createSettingsHelper();
        optionsHelper = createOptionsHelper();
        copyScriptHelper = createCopyScriptHelper();

        // Proxies
        settingsProxy = createSettingsProxy(settingsHelper, settingsXml);
        cachedDefaultOptionsScriptProxy = createCachedDefaultOptionsScriptProxy(optionsHelper);
        diskDefaultOptionsScriptProxy = createDiskDefaultOptionsScriptProxy(optionsHelper,
                cachedDefaultOptionsScriptProxy.getOptions(),
                defaultOptionsXml);

        // Request Factory
        requestFactory = createRequesFactory(optionsHelper,
                settingsHelper,
                copyScriptHelper,
                cachedDefaultOptionsScriptProxy.getOptions(),
                defaultOptionsXml,
                settingsXml);
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

    // Start of methods that get the cached default options from the classpath
    private static OptionsScriptProxy createCachedDefaultOptionsScriptProxy(ScriptHelper<OptionsScript> optionsHelper) throws JAXBException {
        return new OptionsScriptProxy(optionsHelper, createDefaultOptionsObjectFromClasspath(optionsHelper));
    }

    private static OptionsScript createDefaultOptionsObjectFromClasspath(ScriptHelper<OptionsScript> optionsHelper) throws JAXBException {
        URL url = OptionsScriptProxy.class.getResource("/cfg/defaultOptions.xml");
        OptionsScript optionsScript = url != null ? optionsHelper.unmarshal(url) : createDefaultOptionsObject();
        ensureFiltersNotNull(optionsScript);
        return optionsScript;
    }

    private static OptionsScript createDefaultOptionsObject() {
        RenameOptions ro = new RenameOptions();
        ro.setRenameSuffixOptions(new RenameSuffixOptions());

        SrcDirOptions sdo = new SrcDirOptions();

        OptionsScript options = new OptionsScript();
        options.setRenameOptions(ro);
        options.setSrcDirOptions(sdo);
        options.setCopyAttributes(true);
        options.setFollowLinks(true);

        return options;
    }

    private static void ensureFileCopyFilterNotNull(OptionsScript options) {
        if(options.getSrcDirOptions().getFileCopyFilter() == null) {
            options.getSrcDirOptions().setFileCopyFilter(new Filter());
            Filter filter = options.getSrcDirOptions().getFileCopyFilter();
            if(filter.getValue() == null) {
                filter.setValue("");
            }
        }
    }

    private static void ensureDirCopyFilterNotNull(OptionsScript options) {
        if(options.getSrcDirOptions().getDirCopyFilter() == null) {
            options.getSrcDirOptions().setDirCopyFilter(new Filter());
            Filter filter = options.getSrcDirOptions().getDirCopyFilter();
            if(filter.getValue() == null) {
                filter.setValue("");
            }
        }
    }

    private static void ensureFlattenFilterNotNull(OptionsScript options) {
        if(options.getSrcDirOptions().getFlattenFilter() == null) {
            options.getSrcDirOptions().setFlattenFilter(new Filter());
            Filter filter = options.getSrcDirOptions().getFlattenFilter();
            if(filter.getValue() == null) {
                filter.setValue("");
            }
        }
    }

    private static void ensureMergeFilterNotNull(OptionsScript options) {
        if(options.getSrcDirOptions().getMergeFilter() == null) {
            options.getSrcDirOptions().setMergeFilter(new Filter());
            Filter filter = options.getSrcDirOptions().getMergeFilter();
            if(filter.getValue() == null) {
                filter.setValue("");
            }
        }
    }

    private static void ensureFiltersNotNull(OptionsScript options) {
        ensureFileCopyFilterNotNull(options);
        ensureDirCopyFilterNotNull(options);
        ensureFlattenFilterNotNull(options);
        ensureMergeFilterNotNull(options);
    }
    // End of methods that get the cached default options from the classpath

    private static ScriptHelper<Settings> createSettingsHelper() throws JAXBException, SAXException {
        return ScriptHelper.createScriptHelper(Settings.class, SchemaData.SETTINGS);
    }

    private static ScriptHelper<OptionsScript> createOptionsHelper() throws JAXBException, SAXException {
        return ScriptHelper.createScriptHelper(OptionsScript.class, SchemaData.OPTIONS);
    }

    private static ScriptHelper<CopyScript> createCopyScriptHelper() throws JAXBException, SAXException {
        return ScriptHelper.createScriptHelper(CopyScript.class, SchemaData.COPY);
    }

    private static SettingsProxy createSettingsProxy(ScriptHelper<Settings> settingsHelper, File settingsXml)
            throws JAXBException, IOException, SAXException {
        return new SettingsProxy(settingsHelper, settingsXml);
    }

    // Start of methods that create the OptionsScriptProxy object that represent what is saved to disk
    // in defaultOptionsXml.
    private static OptionsScriptProxy createDiskDefaultOptionsScriptProxy(ScriptHelper<OptionsScript> optionsHelper,
            OptionsScript cachedDefaultOptionsScript, File defaultOptionsXml) throws JAXBException, IOException, SAXException {
        // If the defaultOptionsXml file does not exist, or does exist but is not valid, create it.
        ensureDefaultOptionsXmlIsValid(optionsHelper, cachedDefaultOptionsScript, defaultOptionsXml);

        // Now that we know the defaultOptionsXml file exists and is valid, create the OptionsScriptProxy.
        return new OptionsScriptProxy(optionsHelper, defaultOptionsXml);
    }

    private static void ensureDefaultOptionsXmlIsValid(ScriptHelper<OptionsScript> optionsHelper,
            OptionsScript cachedDefaultOptionsScript, File defaultOptionsXml) throws JAXBException, IOException, SAXException {
        if(!defaultOptionsXml.exists()) {
            // If the defaultOptionsXml file does not exist, try to create one with the cached default options script values.
            optionsHelper.marshal(defaultOptionsXml, cachedDefaultOptionsScript);
        }

        try {
            // Make sure the defaultOptionsXml is a valid Options Script.
            optionsHelper.validate(defaultOptionsXml);
        } catch (IOException | SAXException e) {
            // The defaultOptionsXml was not valid, so try to create it with the cached default options.
            optionsHelper.marshal(defaultOptionsXml, cachedDefaultOptionsScript);
            optionsHelper.validate(defaultOptionsXml);
        }
    }
    // End of methods that create the OptionsScriptProxy object that represent what is saved to disk
    // in defaultOptionsXml.

    private static RequestFactory createRequesFactory(ScriptHelper<OptionsScript> optionsHelper,
            ScriptHelper<Settings> settingsHelper,
            ScriptHelper<CopyScript> copyScriptHelper,
            OptionsScript defaultOptionsScript,
            File defaultOptionsXml,
            File settingsXml) {

        return new RequestFactory(optionsHelper,
                settingsHelper,
                copyScriptHelper,
                defaultOptionsScript,
                defaultOptionsXml,
                settingsXml);
    }

    public ScriptHelper<Settings> getSettingsHelper() {
        return settingsHelper;
    }

    public ScriptHelper<OptionsScript> getOptionsHelper() {
        return optionsHelper;
    }

    public ScriptHelper<CopyScript> getCopyScriptHelper() {
        return copyScriptHelper;
    }

    public SettingsProxy getSettingsProxy() {
        return settingsProxy;
    }

    public OptionsScriptProxy getDiskDefaultOptionsScriptProxy() {
        return diskDefaultOptionsScriptProxy;
    }

    /**
     * Method that returns the cached default options script proxy. The cached default options
     * script proxy represents the Options Script that is on the classpath and is the standard
     * default options.
     * @return
     */
    public OptionsScriptProxy getCachedDefaultOptionsScriptProxy() {
        return cachedDefaultOptionsScriptProxy;
    }

    public File getDefaultOptionsFile() {
        return defaultOptionsXml;
    }

    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

}
