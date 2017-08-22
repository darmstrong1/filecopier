package net.sf.fc.gui.c;

import java.beans.PropertyChangeListener;

import net.sf.fc.gui.m.Model;
import net.sf.fc.gui.v.View;

public interface Controller extends PropertyChangeListener {

    /**
     * Binds a model to this controller. Once added, the controller will listen for all
     * model property changes and propogate them on to registered views. In addition,
     * it is also responsible for resetting the model properties when a view changes
     * state.
     * @param model The model to be added
     */
    public void addModel(Model model);

    /**
     * Unbinds a model from this controller.
     * @param model The model to be removed
     */
    public void removeModel(Model model);

    /**
     * Binds a view to this controller. The controller will propogate all model property
     * changes to each view for consideration.
     * @param view The view to be added
     */
    public void addView(View view);

    /**
     * Unbinds a view from this controller.
     * @param view The view to be removed
     */
    public void removeView(View view);
}
