package net.sf.fc.gui.m;

import java.beans.PropertyChangeListener;

public interface Model {

    /**
     * Adds a property change listener to the observer list.
     * @param l The property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener l);

    /**
     * Removes a property change listener from the observer list.
     * @param l The property change listener
     */
    public void removePropertyChangeListener(PropertyChangeListener l);
}
