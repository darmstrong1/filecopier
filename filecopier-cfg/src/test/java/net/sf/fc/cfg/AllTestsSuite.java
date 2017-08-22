package net.sf.fc.cfg;

import javax.xml.bind.JAXBException;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.xml.sax.SAXException;

@RunWith(Suite.class)
@Suite.SuiteClasses
( { /*AppConfigTest.class,*/ DirPathTest.class })
public class AllTestsSuite {

    @BeforeClass
    public static void setUp() throws SAXException, JAXBException {
        System.out.println("createTestDirectory");
        TestUtil.createTestDirectory();
        TestUtil.setHomeDirectory();
        TestUtil.createSettingsHelper();
        TestUtil.createOptionsHelper();
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("deleteTestDirectory");
        TestUtil.deleteTestDirectory();
    }

}
