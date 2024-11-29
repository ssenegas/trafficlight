package org.senegas.trafficlight.model;

import java.awt.*;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficLightModel extends AbstractModel implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(TrafficLightModel.class.getName());

    public static final String TURN_ON = "turnOn";
    public static final String TURN_OFF = "turnOff";
    public static final String DELAY = "delay";
    public static final String BLINKING_STATE = "blinkingState";

    private TrafficLight trafficLight;

    public TrafficLightModel() {
        this(new TrafficLight());
    }

    public TrafficLightModel(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        final TrafficLight oldTrafficLight = this.trafficLight;
        this.trafficLight = trafficLight;
        firePropertyChange("traffic-light", oldTrafficLight, this.trafficLight);
    }

    public String toArduinoCommand() {
        String arduinoCommand = this.trafficLight.toArduinoCommand();
        LOGGER.log(
                Level.INFO,
                "Arduino command: {0} length {1}",
                new Object[] {arduinoCommand, arduinoCommand.length()});
        return arduinoCommand;
    }

    public void turnOnRed() {
        boolean oldValue = this.trafficLight.getBulb(Light.RED).isOn();
        this.trafficLight.getBulb(Light.RED).turnOn();
        firePropertyChange(TURN_ON, oldValue, this.trafficLight.getBulb(Light.RED).isOn());
    }

    public void turnOnYellow() {
        boolean oldValue = this.trafficLight.getBulb(Light.YELLOW).isOn();
        this.trafficLight.getBulb(Light.YELLOW).turnOn();
        firePropertyChange(TURN_ON, oldValue, this.trafficLight.getBulb(Light.YELLOW).isOn());
    }

    public void turnOnGreen() {
        boolean oldValue = this.trafficLight.getBulb(Light.GREEN).isOn();
        this.trafficLight.getBulb(Light.GREEN).turnOn();
        firePropertyChange(TURN_ON, oldValue, this.trafficLight.getBulb(Light.GREEN).isOn());
    }

    public void turnOffRed() {
        boolean oldValue = this.trafficLight.getBulb(Light.RED).isOn();
        this.trafficLight.getBulb(Light.RED).turnOff();
        firePropertyChange(TURN_OFF, oldValue, this.trafficLight.getBulb(Light.RED).isOn());
    }

    public void turnOffYellow() {
        boolean oldValue = this.trafficLight.getBulb(Light.YELLOW).isOn();
        this.trafficLight.getBulb(Light.YELLOW).turnOff();
        firePropertyChange(TURN_OFF, oldValue, this.trafficLight.getBulb(Light.YELLOW).isOn());
    }

    public void turnOffGreen() {
        boolean oldValue = this.trafficLight.getBulb(Light.GREEN).isOn();
        this.trafficLight.getBulb(Light.GREEN).turnOff();
        firePropertyChange(TURN_OFF, oldValue, this.trafficLight.getBulb(Light.GREEN).isOn());
    }

    public void setRedDelay(int value) {
        int oldValue = this.trafficLight.getBulb(Light.RED).getDelay();
        this.trafficLight.getBulb(Light.RED).setBlinking(value);
        firePropertyChange(DELAY, oldValue, this.trafficLight.getBulb(Light.RED).getDelay());
    }

    public void setYellowDelay(int value) {
        int oldValue = this.trafficLight.getBulb(Light.YELLOW).getDelay();
        this.trafficLight.getBulb(Light.YELLOW).setBlinking(value);
        firePropertyChange(DELAY, oldValue, this.trafficLight.getBulb(Light.YELLOW).getDelay());
    }

    public void setGreenDelay(int value) {
        int oldValue = this.trafficLight.getBulb(Light.GREEN).getDelay();
        this.trafficLight.getBulb(Light.GREEN).setBlinking(value);
        firePropertyChange(DELAY, oldValue, this.trafficLight.getBulb(Light.GREEN).getDelay());
    }

    public boolean isRedOn() {
        return this.trafficLight.getBulb(Light.RED).isOn();
    }

    public boolean isYellowOn() {
        return this.trafficLight.getBulb(Light.YELLOW).isOn();
    }

    public boolean isGreenOn() {
        return this.trafficLight.getBulb(Light.GREEN).isOn();
    }

    public int getRedDelay() {
        return this.trafficLight.getBulb(Light.RED).getDelay();
    }

    public int getYellowDelay() {
        return this.trafficLight.getBulb(Light.YELLOW).getDelay();
    }

    public int getGreenDelay() {
        return this.trafficLight.getBulb(Light.GREEN).getDelay();
    }

    /** Notify listeners about a property change for the blinking state. */
    public void notifyBlinkingStateChanged() {
        firePropertyChange(BLINKING_STATE, null, null);
    }
}
