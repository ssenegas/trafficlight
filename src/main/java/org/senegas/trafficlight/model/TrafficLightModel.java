package org.senegas.trafficlight.model;

import java.awt.*;
import java.io.Serializable;

public class TrafficLightModel extends AbstractModel implements Serializable {
    private final Led red = new Led(Color.RED);
    private final Led amber = new Led(Color.YELLOW);
    private final Led green = new Led(Color.GREEN);

    public TrafficLightModel() {
    }

    public void turnOnRed() {
        turnOn(this.red);
    }

    public void turnOnAmber() {
        turnOn(this.amber);
    }

    public void turnOnGreen() {
        turnOn(this.green);
    }

    private void turnOn(Led l) {
        boolean oldValue = l.isOn();
        l.turnOn();
        firePropertyChange("turnOn", oldValue, l.isOn());
    }

    public void turnOffRed() {
        turnOff(this.red);
    }

    public void turnOffAmber() {
        turnOff(this.amber);
    }

    public void turnOffGreen() {
        turnOff(this.green);
    }

    private void turnOff(Led l) {
        boolean oldValue = l.isOn();
        l.turnOff();
        firePropertyChange("turnOff", oldValue, l.isOn());
    }

    public void setRedDelay(int value) {
        setDelay(this.red, value);
    }

    public void setAmberDelay(int value) {
        setDelay(this.amber, value);
    }

    public void setGreenDelay(int value) {
        setDelay(this.green, value);
    }

    private void setDelay(Led l, int value) {
        int oldValue = l.getDelay();
        l.setDelay(value);
        firePropertyChange("delay", oldValue, l.getDelay());
    }

    public boolean isRedOn() {
        return isOn(red);
    }

    public boolean isAmberOn() {
        return isOn(amber);
    }

    public boolean isGreenOn() {
        return isOn(green);
    }

    public int getRedDelay() {
        return getDelay(red);
    }

    public int getAmberDelay() {
        return getDelay(amber);
    }

    public int getGreenDelay() {
        return getDelay(green);
    }

    private boolean isOn(Led c) {
        return c.isOn();
    }

    private int getDelay(Led c) {
        return c.getDelay();
    }
}
