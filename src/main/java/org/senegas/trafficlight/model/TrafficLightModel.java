package org.senegas.trafficlight.model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TrafficLightModel extends AbstractModel implements Serializable {
    private final Map<Color, Led> leds = new HashMap<>(3);

    public TrafficLightModel() {
        this.leds.put(Color.RED, new Led(Color.RED));
        this.leds.put(Color.YELLOW, new Led(Color.YELLOW));
        this.leds.put(Color.GREEN, new Led(Color.GREEN));
    }

    public void turnOnRed() {
        turnOn(Color.RED);
    }

    public void turnOnAmber() {
        turnOn(Color.YELLOW);
    }

    public void turnOnGreen() {
        turnOn(Color.GREEN);
    }

    public void turnOffRed() {
        turnOff(Color.RED);
    }

    public void turnOffAmber() {
        turnOff(Color.YELLOW);
    }

    public void turnOffGreen() {
        turnOff(Color.GREEN);
    }

    public boolean isRedOn() {
        return isOn(Color.RED);
    }

    public boolean isAmberOn() {
        return isOn(Color.YELLOW);
    }

    public boolean isGreenOn() {
        return isOn(Color.GREEN);
    }

    public boolean isOn(Color c) {
        return this.leds.get(c).isOn();
    }

    private void turnOn(Color c) {
        final Led oldLed = this.leds.get(c);
        Led newLed = new Led(oldLed.getColor(), true, 0);
        this.leds.put(c, newLed);

        firePropertyChange("turnOn", oldLed, newLed);
    }

    private void turnOff(Color c) {
        final Led oldLed = this.leds.get(c);
        Led newLed = new Led(oldLed.getColor(), false, 0);
        this.leds.put(c, newLed);

        firePropertyChange("turnOff", oldLed, newLed);
    }
}
