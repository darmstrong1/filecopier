package net.sf.fc.cfg;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import static net.sf.fc.cfg.TestUtil.getTestDirectory;
import static org.junit.Assert.*;

public class DirPathTest {

    @Test
    public void testReorderPath() {
        StringBuilder pathBuilder = new StringBuilder();
        File testDir = getTestDirectory();
        for(int i = 1; i <= 10; i++) {
            File pathFile = new File(testDir, "path"+i);
            pathBuilder.append(pathFile.toString()).append(";");
        }
        pathBuilder.deleteCharAt(pathBuilder.length()-1);

        DirPath origDirPath = new DirPath(pathBuilder.toString());

        DirPath reorderedDirPath = origDirPath.reorderPath(4, 8);
        String fullPath = reorderedDirPath.getFullPath();

        pathBuilder = new StringBuilder();
        for(int i = 1; i <= 4; i++) {
            File pathFile = new File(testDir, "path"+i);
            pathBuilder.append(pathFile.toString()).append(";");
        }
        pathBuilder.append(new File(testDir, "path9").toString()).append(";");
        for(int i = 5; i < 9; i++) {
            File pathFile = new File(testDir, "path"+i);
            pathBuilder.append(pathFile.toString()).append(";");
        }
        pathBuilder.append(new File(testDir, "path10").toString()).append(";");
        pathBuilder.deleteCharAt(pathBuilder.length()-1);

        assertEquals(pathBuilder.toString(), fullPath);
    }

    @Test
    public void testReorderPathString() {
        StringBuilder pathBuilder = new StringBuilder();
        File testDir = getTestDirectory();
        for(int i = 1; i <= 10; i++) {
            File pathFile = new File(testDir, "path"+i);
            pathBuilder.append(pathFile.toString()).append(";");
        }
        pathBuilder.deleteCharAt(pathBuilder.length()-1);

        DirPath origDirPath = new DirPath(pathBuilder.toString());

        DirPath reorderedDirPath = origDirPath.reorderPath(new File(testDir, "path5").toString(), new File(testDir, "path9").toString());
        String fullPath = reorderedDirPath.getFullPath();

        pathBuilder = new StringBuilder();
        for(int i = 1; i <= 4; i++) {
            File pathFile = new File(testDir, "path"+i);
            pathBuilder.append(pathFile.toString()).append(";");
        }
        pathBuilder.append(new File(testDir, "path9").toString()).append(";");
        for(int i = 5; i < 9; i++) {
            File pathFile = new File(testDir, "path"+i);
            pathBuilder.append(pathFile.toString()).append(";");
        }
        pathBuilder.append(new File(testDir, "path10").toString()).append(";");
        pathBuilder.deleteCharAt(pathBuilder.length()-1);

        assertEquals(pathBuilder.toString(), fullPath);
    }

    @Test
    public void testGetFile() {
        StringBuilder pathBuilder = new StringBuilder();
        File testDir = getTestDirectory();
        for(int i = 1; i <= 10; i++) {
            File pathFile = new File(testDir, "path"+i);
            pathFile.mkdirs();
            pathBuilder.append(pathFile.toString()).append(";");
        }
        pathBuilder.deleteCharAt(pathBuilder.length()-1);

        DirPath dirPath = new DirPath(pathBuilder.toString());

        File testFile = new File(dirPath.getPathAt(9), "testFile");

        try {
            testFile.createNewFile();
            File tstFile = dirPath.getFile("testFile");
            assertTrue(tstFile != null);
            assertEquals(dirPath.getPathAt(9), tstFile.getParent());
        } catch(IOException e) {
            assertTrue(false);
        }

    }

    @Test
    public void testEquals() {
        StringBuilder pathBuilder = new StringBuilder();
        File testDir = getTestDirectory();
        for(int i = 1; i <= 10; i++) {
            File pathFile = new File(testDir, "path"+i);
            pathFile.mkdirs();
            pathBuilder.append(pathFile.toString()).append(";");
        }
        pathBuilder.deleteCharAt(pathBuilder.length()-1);

        DirPath dirPath1 = new DirPath(pathBuilder.toString());

        pathBuilder = new StringBuilder();
        for(int i = 1; i <= 10; i++) {
            File pathFile = new File(testDir, "path"+i);
            pathFile.mkdirs();
            pathBuilder.append(pathFile.toString()).append("~");
        }

        DirPath dirPath2 = new DirPath(pathBuilder.toString(), "~");

        assertEquals(dirPath1, dirPath2);
        assertEquals(dirPath1.hashCode(), dirPath2.hashCode());
    }
}
