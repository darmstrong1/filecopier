package net.sf.fc.gui.m;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BaseModel implements Model {

    /**
     * Convenience class that allow others to observe changes to the model properties
     */
    protected PropertyChangeSupport propertyChangeSupport;

    public BaseModel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    /**
     * Fires an event to all registered listeners informing them that a property in
     * this model has changed.
     * @param propertyName The name of the property
     * @param oldValue The previous value of the property before the change
     * @param newValue The new property value after the change
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
