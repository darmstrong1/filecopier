package net.sf.fc.cfg;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.fc.script.gen.options.Filter;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.script.gen.settings.LogLevel;
import net.sf.fc.script.gen.settings.Settings;

import org.junit.Test;

import javax.xml.bind.JAXBException;

import static net.sf.fc.cfg.TestUtil.getOptionsHelper;
import static net.sf.fc.cfg.TestUtil.getSettingsHelper;
import static net.sf.fc.cfg.TestUtil.createDefaultOptionsObject;
import static net.sf.fc.cfg.TestUtil.createDefaultSettings;

public class AppConfigTest {

    @Test
    public void testDirectoryStructure() {
        try {
            new AppConfig(getSettingsHelper(), getOptionsHelper());
            assertTrue(new File(System.getProperty("filecopier.home")).exists());
            assertTrue(new File(System.getProperty("filecopier.scripts")).exists());
            assertTrue(new File(System.getProperty("filecopier.scripts.copy")).exists());
            assertTrue(new File(System.getProperty("filecopier.scripts.restore")).exists());
            assertTrue(new File(System.getProperty("filecopier.scripts.options")).exists());
            assertTrue(new File(System.getProperty("filecopier.cfg")).exists());
            assertTrue(new File(System.getProperty("filecopier.log")).exists());

        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testGetDefaultOptions() {
        try {
            AppConfig appCfg = new AppConfig(getSettingsHelper(), getOptionsHelper());
            OptionsScript o = appCfg.getDefaultOptions();
            assertEquals(createDefaultOptionsObject(), o);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testSetDefaultOptions() {
        try {
            AppConfig appCfg = new AppConfig(getSettingsHelper(), getOptionsHelper());
            OptionsScript o = appCfg.getDefaultOptions();
            Filter copyFilter = new Filter();
            copyFilter.setValue("filter");
            copyFilter.setCaseSensitive(true);
            o.getSrcDirOptions().setFileCopyFilter(copyFilter);
            o.setCopyAttributes(false);
            appCfg.setDefaultOptions(o);

            OptionsScript o2 = appCfg.getDefaultOptions();
            assertEquals(o, o2);

        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testGetSettings() {
        try {
            AppConfig appCfg = new AppConfig(getSettingsHelper(), getOptionsHelper());
            Settings s = appCfg.getSettings();
            assertEquals(createDefaultSettings(), s);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testSetSettings() {
        try {
            AppConfig appCfg = new AppConfig(getSettingsHelper(), getOptionsHelper());
            Settings s = appCfg.getSettings();
            s.getMostRecentlyUsed().setLimit(8);
            s.setLogLevel(LogLevel.DEBUG);
            appCfg.setSettings(s);

            Settings s2 = appCfg.getSettings();
            assertEquals(s, s2);

        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testSaveSettingsToDisk() {
        try {
            AppConfig appCfg = new AppConfig(getSettingsHelper(), getOptionsHelper());
            Settings s = appCfg.getSettings();
            s.getMostRecentlyUsed().setLimit(8);
            s.setLogLevel(LogLevel.DEBUG);
            appCfg.setSettings(s);

            appCfg.saveSettingsToDisk();

            Settings s2 = getSettingsHelper().unmarshal(new File(System.getProperty("filecopier.cfg"), "settings.xml"));
            assertEquals(s, s2);

        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testSaveDefaultOptionsToDisk() {
        try {
            AppConfig appCfg = new AppConfig(getSettingsHelper(), getOptionsHelper());
            OptionsScript o = appCfg.getDefaultOptions();
            Filter copyFilter = new Filter();
            copyFilter.setValue("filter");
            copyFilter.setCaseSensitive(true);
            o.getSrcDirOptions().setFileCopyFilter(copyFilter);
            o.setCopyAttributes(false);
            appCfg.setDefaultOptions(o);

            appCfg.saveDefaultOptionsToDisk();

            OptionsScript o2 = getOptionsHelper().unmarshal(new File(System.getProperty("filecopier.cfg"), "defaultOptions.xml"));
            assertEquals(o, o2);

        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
