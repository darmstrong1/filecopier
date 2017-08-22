package net.sf.fc.cfg;

import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;

import net.sf.fc.script.SchemaData;
import net.sf.fc.script.ScriptHelper;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.script.gen.options.RenameOptions;
import net.sf.fc.script.gen.options.RenameSuffixOptions;
import net.sf.fc.script.gen.options.SrcDirOptions;
import net.sf.fc.script.gen.settings.FileList;
import net.sf.fc.script.gen.settings.LookAndFeel;
import net.sf.fc.script.gen.settings.MostRecentlyUsed;
import net.sf.fc.script.gen.settings.Paths;
import net.sf.fc.script.gen.settings.Settings;

import org.junit.Ignore;
import org.xml.sax.SAXException;

public class TestUtil {

    private static File testDir;
    private static ScriptHelper<Settings> settingsHelper;
    private static ScriptHelper<OptionsScript> optionsHelper;

    @Ignore
    public static void createTestDirectory() {
      testDir = (new File("unittest-files/")).getAbsoluteFile();
      testDir.mkdirs();
    }

    @Ignore
    public static void deleteTestDirectory() {
        deleteTestFiles(testDir);
        testDir.delete();
    }

    @Ignore
    public static void deleteTestFiles(File dir) {
        assertTrue(ensureDirIsTest(dir));
        File[] files = dir.listFiles();
        for(File f: files) {
            if(f.isDirectory()) {
                deleteTestFiles(f);
            }
            f.delete();
        }
    }

    @Ignore
    public static boolean ensureDirIsTest(File dir) {

        if(dir.isDirectory()) {
            File parent = dir;
            while(parent != null) {
                if(parent.equals(testDir)) {
                    return true;
                }
                parent = parent.getParentFile();
            }
        }
        return false;

    }

    @Ignore
    public static File getTestDirectory() {
        return testDir;
    }

    // AppConfig uses user.home, so set user.home to the test directory.
    @Ignore
    public static void setHomeDirectory() {
        if(!new File("unittest-files/").getAbsoluteFile().exists()) {
            createTestDirectory();
        }
        System.setProperty("user.home", testDir.getPath());
    }

    @Ignore
    public static void createSettingsHelper() throws JAXBException, SAXException {
        settingsHelper = ScriptHelper.createScriptHelper(Settings.class, SchemaData.SETTINGS);
    }

    @Ignore
    public static void createOptionsHelper() throws JAXBException, SAXException {
        optionsHelper = ScriptHelper.createScriptHelper(OptionsScript.class, SchemaData.OPTIONS);
    }

    @Ignore
    public static ScriptHelper<Settings> getSettingsHelper() {
        return settingsHelper;
    }

    @Ignore
    public static ScriptHelper<OptionsScript> getOptionsHelper() {
        return optionsHelper;
    }

    @Ignore
    public static OptionsScript createDefaultOptionsObject() {
        RenameOptions ro = new RenameOptions();
        ro.setRenameSuffixOptions(new RenameSuffixOptions());

        SrcDirOptions sdo = new SrcDirOptions();

        OptionsScript options = new OptionsScript();
        options.setRenameOptions(ro);
        options.setSrcDirOptions(sdo);

        return options;
    }

    @Ignore
    public static Settings createDefaultSettings() {
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

}
