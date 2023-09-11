package org.senegas.trafficlight.model;

import java.awt.*;
import java.io.Serializable;

public class TrafficLightModel extends AbstractModel implements Serializable {
    private Led red = new Led(Color.RED);
    private Led amber = new Led(Color.YELLOW);
    private Led green = new Led(Color.GREEN);

    public TrafficLightModel() {
    }

    public void turnOnRed() {
        Led oldLed = this.red;
        this.red = new Led(oldLed.getColor(), true, oldLed.getDelay());
        firePropertyChange("turnOn", oldLed, this.red);
    }

    public void turnOnAmber() {
        Led oldLed = this.amber;
        this.amber = new Led(oldLed.getColor(), true, oldLed.getDelay());
        firePropertyChange("turnOn", oldLed, this.amber);
    }

    public void turnOnGreen() {
        Led oldLed = this.green;
        this.green = new Led(oldLed.getColor(), true, oldLed.getDelay());
        firePropertyChange("turnOn", oldLed, this.green);
    }

    public void turnOffRed() {
        Led oldLed = this.red;
        this.red = new Led(oldLed.getColor(), false, oldLed.getDelay());
        firePropertyChange("turnOff", oldLed, this.red);
    }

    public void turnOffAmber() {
        Led oldLed = this.amber;
        this.amber = new Led(oldLed.getColor(), false, oldLed.getDelay());
        firePropertyChange("turnOff", oldLed, this.amber);
    }

    public void turnOffGreen() {
        Led oldLed = this.green;
        this.green = new Led(oldLed.getColor(), false, oldLed.getDelay());
        firePropertyChange("turnOff", oldLed, this.green);
    }

    public void setRedDelay(long value) {
        Led oldLed = this.red;
        this.red = new Led(oldLed.getColor(), oldLed.isOn(), value);
        firePropertyChange("delay", oldLed, this.red);
    }

    public void setAmberDelay(long value) {
        Led oldLed = this.amber;
        this.amber = new Led(oldLed.getColor(), oldLed.isOn(), value);
        firePropertyChange("delay", oldLed, this.amber);
    }

    public void setGreenDelay(long value) {
        Led oldLed = this.green;
        this.green = new Led(oldLed.getColor(), oldLed.isOn(), value);
        firePropertyChange("delay", oldLed, this.green);
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

    public long getRedDelay() {
        return getDelay(red);
    }

    public long getAmberDelay() {
        return getDelay(amber);
    }

    public long getGreenDelay() {
        return getDelay(green);
    }

    private boolean isOn(Led c) {
        return c.isOn();
    }

    private long getDelay(Led c) {
        return c.getDelay();
    }
}
