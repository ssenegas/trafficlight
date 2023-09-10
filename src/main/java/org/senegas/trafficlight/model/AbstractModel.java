package org.senegas.trafficlight.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
public abstract class AbstractModel {
    protected PropertyChangeSupport propertyChangeSupport;

    protected AbstractModel() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
