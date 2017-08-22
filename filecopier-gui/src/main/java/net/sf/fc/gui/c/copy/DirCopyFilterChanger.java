package net.sf.fc.gui.c.copy;

import net.sf.fc.gui.c.options.OptionsController;

public class DirCopyFilterChanger implements FilterChanger {

    private final OptionsController optionsController;

    public DirCopyFilterChanger(OptionsController optionsController) {
        this.optionsController = optionsController;
    }
    @Override
    public void changeFilterValue(String value) {
        optionsController.changeDirCopyFilterValue(value);
    }

    @Override
    public void changeFilterExclusive(boolean exclusive) {
        optionsController.changeDirCopyFilterExclusive(exclusive);
    }

    @Override
    public void changeFilterCaseSensitive(boolean caseSensitive) {
        optionsController.changeDirCopyFilterCaseSensitive(caseSensitive);
    }

    @Override
    public void changeFilterRegEx(boolean regex) {
        optionsController.changeDirCopyFilterRegEx(regex);
    }

}
