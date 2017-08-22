package net.sf.fc.gui.c.copy;

import net.sf.fc.gui.c.options.OptionsController;

public class MergeFilterChanger implements FilterChanger {

    private final OptionsController optionsController;

    public MergeFilterChanger(OptionsController optionsController) {
        this.optionsController = optionsController;
    }
    @Override
    public void changeFilterValue(String value) {
        optionsController.changeMergeFilterValue(value);
    }

    @Override
    public void changeFilterExclusive(boolean exclusive) {
        optionsController.changeMergeFilterExclusive(exclusive);
    }

    @Override
    public void changeFilterCaseSensitive(boolean caseSensitive) {
        optionsController.changeMergeFilterCaseSensitive(caseSensitive);
    }

    @Override
    public void changeFilterRegEx(boolean regex) {
        optionsController.changeMergeFilterRegEx(regex);
    }

}
