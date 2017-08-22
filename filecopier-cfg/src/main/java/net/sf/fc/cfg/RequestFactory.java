package net.sf.fc.cfg;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import net.sf.fc.script.CopyScriptProxy;
import net.sf.fc.script.OptionsScriptProxy;
import net.sf.fc.script.ScriptHelper;
import net.sf.fc.script.gen.copy.CopyScript;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.script.gen.settings.Settings;

/**
 * Factory class for creating objects per request.
 * @author David Armstrong
 *
 */
public final class RequestFactory {

    private final ScriptHelper<OptionsScript> optionsHelper;
    private final ScriptHelper<Settings> settingsHelper;
    private final ScriptHelper<CopyScript> copyHelper;
    private final OptionsScript cachedDefaultOptionsScript;
    private final File defaultOptionsFile;
    private final File settingsFile;

    public RequestFactory(ScriptHelper<OptionsScript> optionsHelper,
            ScriptHelper<Settings> settingsHelper,
            ScriptHelper<CopyScript> copyHelper,
            OptionsScript cachedDefaultOptionsScript,
            File defaultOptionsFile,
            File settingsFile) {
        this.optionsHelper = optionsHelper;
        this.settingsHelper = settingsHelper;
        this.copyHelper = copyHelper;
        this.cachedDefaultOptionsScript = (OptionsScript)cachedDefaultOptionsScript.copyTo(null);
        this.defaultOptionsFile = defaultOptionsFile;
        this.settingsFile = settingsFile;
    }

    /**
     * This method is for returning an OptionsScriptProxy object that represents a new OptionsScript. It will have
     * default values. It first tries to get the default values from what is saved to disk. If that fails, it
     * gets the default values that are cached.
     * @return OptionsScriptProxy
     */
    public OptionsScriptProxy getOptionsScriptProxy() {
        try {
            return new OptionsScriptProxy(optionsHelper, getDiskDefaultOptions());
        } catch (JAXBException e) {
            //TODO: Log that the attempt to get the default options from disk failed.
            return new OptionsScriptProxy(optionsHelper, cachedDefaultOptionsScript);
        }
    }

    /**
     * This method is for returning an OptionsScriptProxy object that represents an OptionsScript that was saved
     * to disk.
     * @param optionsFile
     * @return
     * @throws JAXBException
     * @throws IOException
     * @throws SAXException
     */
    public OptionsScriptProxy getOptionsScriptProxy(File optionsFile) throws JAXBException, IOException, SAXException {

        return new OptionsScriptProxy(optionsHelper, optionsFile);
    }

    public OptionsScript getDiskDefaultOptions() throws JAXBException {
        return optionsHelper.unmarshal(defaultOptionsFile);
    }

    public Settings getSettingsFromDisk() throws JAXBException {
        return settingsHelper.unmarshal(settingsFile);
    }

    /**
     * This method creates a CopyScript object with the default options that are saved to disk. If it cannot open
     * the default options that were saved to disk, it throws a JAXBException.
     * @return
     * @throws JAXBException
     */
    public CopyScriptProxy getCopyScriptProxy() throws JAXBException {
        return new CopyScriptProxy(copyHelper, getDiskDefaultOptions());
    }

    /**
     * This method uses the cached default options script that is from the classpath, not the default options that is
     * saved to disk. Use this method only if getCopyScriptProxy throws a JAXBException.
     * @return
     */
    public CopyScriptProxy getCachedCopyScriptProxy() {
        return new CopyScriptProxy(copyHelper, cachedDefaultOptionsScript);
    }

//    public CopyScriptProxy getCopyScriptProxy(File copyScriptXml) {
//    	return new CopyScriptProxy(copyHelper, defaultOptions, copyScriptXml)
//    }

}
