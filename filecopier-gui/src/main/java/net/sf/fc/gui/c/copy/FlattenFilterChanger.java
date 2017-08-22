package net.sf.fc.gui.c.copy;

import net.sf.fc.gui.c.options.OptionsController;

public class FlattenFilterChanger implements FilterChanger {

    private final OptionsController optionsController;

    public FlattenFilterChanger(OptionsController optionsController) {
        this.optionsController = optionsController;
    }
    @Override
    public void changeFilterValue(String value) {
        optionsController.changeFlattenFilterValue(value);
    }

    @Override
    public void changeFilterExclusive(boolean exclusive) {
        optionsController.changeFlattenFilterExclusive(exclusive);
    }

    @Override
    public void changeFilterCaseSensitive(boolean caseSensitive) {
        optionsController.changeFlattenFilterCaseSensitive(caseSensitive);
    }

    @Override
    public void changeFilterRegEx(boolean regex) {
        optionsController.changeFlattenFilterRegEx(regex);
    }

}
