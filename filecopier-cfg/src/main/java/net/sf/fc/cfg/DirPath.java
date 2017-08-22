package net.sf.fc.cfg;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class DirPath {

    private final String path;
    private final List<String> pathList;
    private final String separator;

    private static final String fileSep = System.getProperty("file.separator");

    public DirPath(String path) {
        this(path, ";");
    }

    public DirPath(String path, String separator) {
        if(path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        if(separator == null) {
            throw new IllegalArgumentException("Separator cannot be null");
        }

        this.path = path;
        this.separator = separator;
        pathList = Arrays.asList(path.split(separator));
    }

    public DirPath(List<String> pathList) {
        this(pathList, ";");
    }

    public DirPath(List<String> pathList, String separator) {
        if(pathList == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        if(separator == null) {
            throw new IllegalArgumentException("Separator cannot be null");
        }
        this.pathList = new ArrayList<String>(pathList);
        this.separator = separator;
        this.path = initFullPath();
    }

    public String initFullPath() {
        StringBuilder pathBuilder = new StringBuilder();
        for(String path: pathList) {
            pathBuilder.append(path).append(separator);
        }
        pathBuilder.deleteCharAt(pathBuilder.length()-1);
        return pathBuilder.toString();
    }

    public List<String> getPathList() {
        return Collections.unmodifiableList(pathList);
    }

    public String getFullPath() {
        return path;
    }

    public int getPathSize() {
        return pathList.size();
    }

    public String getPathAt(int idx) {
        return pathList.get(idx);
    }

    public int getPathIndex(String path) {
        return pathList.indexOf(path);
    }

    /**
     * Reorders the path. The path at endIdx is promoted to start at startIdx. If startIdx is greater than or equal to
     * endIdx, it throws an IllegalArgumentException. If startIdx or endIdx is less than 0 or greater than the size of
     * the List, it throws an IndexOutOfBoundsException.
     * @param startIdx
     * @param endIdx
     * @return a new DirPath instance
     */
    public DirPath reorderPath(int startIdx, int endIdx) {
        if(startIdx >= endIdx) throw new IllegalArgumentException("startIdx must be less than endIdx");

        List<String> pList = new ArrayList<String>(pathList);
        pList.add(startIdx, pList.remove(endIdx));
        return new DirPath(pList, separator);
    }

    /**
     * Reorders the path. The startPath is the path at which the endPath will be put before. If startPath or endPath is null,
     * it throws an IllegalArgumentException. If startPath or endPath are not in the path list, it will throw an
     * IllegalArgumentException if neither one is in the list or if endPath is not in the list. If startPath is not in the
     * list, but endPath is, it will throw an IndexOutOfBoundsException
     * @param startPath
     * @param endPath
     * @return a new DirPath instance
     */
    public DirPath reorderPath(String startPath, String endPath) {
        if(startPath == null) throw new IllegalArgumentException("startPath must not be null");
        if(endPath == null) throw new IllegalArgumentException("endPath must not be null");
        return reorderPath(getPathIndex(startPath), getPathIndex(endPath));
    }

    /**
     * Returns the full path of the file with the name passed in if it exists in any of the paths represented by the
     * DirPath, or null if it does not exist in any of the paths.
     * @param fileName
     * @return
     */
    public File getFile(final String fileName) {
        File file = null;

        for (String dir : pathList) {
            File f = new File(dir + (dir.endsWith(fileSep) ? "" : fileSep) + fileName);
            if (f.exists()) {
                file = f;
                break;
            }
        }
        return file;
    }

    public String getFilePath(final String fileName) {
        File file = getFile(fileName);
        return (file != null ? file.getPath() : null);
    }

    public DirPath addPath(String path) {
        List<String> pList = new ArrayList<String>(pathList);
        pList.add(path);
        return new DirPath(pList, separator);
    }

    public DirPath insertPath(int idx, String path) {
        List<String> pList = new ArrayList<String>(pathList);
        pList.add(idx, path);
        return new DirPath(pList, separator);
    }

    public DirPath removePath(String path) {
        List<String> pList = new ArrayList<String>(pathList);
        pList.remove(path);
        return new DirPath(pList, separator);
    }

    public DirPath removePath(int idx) {
        List<String> pList = new ArrayList<String>(pathList);
        pList.remove(idx);
        return new DirPath(pList, separator);
    }

    @Override
    public String toString() {
        return getFullPath();
    }

    /**
     * Returns true if the pathList of two DirPaths are the same. It does not consider the full path or the separator.
     * @param that
     * @return
     */
    @Override
    public boolean equals(Object that) {
        if(!(that instanceof DirPath)) return false;
        if(that == this) return true;
        return pathList.equals(((DirPath)that).getPathList());
    }

    @Override
    public int hashCode() {
        return 17 * 37 + pathList.hashCode();
    }

}
