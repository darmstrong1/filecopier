package net.sf.fc.gui.c.copy;

public interface FilterChanger {

    public void changeFilterValue(String value);
    public void changeFilterExclusive(boolean exclusive);
    public void changeFilterCaseSensitive(boolean caseSensitive);
    public void changeFilterRegEx(boolean regex);
}
