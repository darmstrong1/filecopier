package net.sf.fc.gui.c.copy;

import net.sf.fc.gui.c.options.OptionsController;

public class FileCopyFilterChanger implements FilterChanger {

    private final OptionsController optionsController;

    public FileCopyFilterChanger(OptionsController optionsController) {
        this.optionsController = optionsController;
    }
    @Override
    public void changeFilterValue(String value) {
        optionsController.changeFileCopyFilterValue(value);
    }

    @Override
    public void changeFilterExclusive(boolean exclusive) {
        optionsController.changeFileCopyFilterExclusive(exclusive);
    }

    @Override
    public void changeFilterCaseSensitive(boolean caseSensitive) {
        optionsController.changeFileCopyFilterCaseSensitive(caseSensitive);
    }

    @Override
    public void changeFilterRegEx(boolean regex) {
        optionsController.changeFileCopyFilterRegEx(regex);
    }

}
