package org.senegas.trafficlight.model;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficLightModel extends AbstractModel implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(TrafficLightModel.class.getName());
	
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
    	LOGGER.log(Level.INFO, "Arduino command: {0} length {1}",
    			new Object[] {arduinoCommand, arduinoCommand.length()});
        return arduinoCommand;
    }

    public void turnOnRed() {
        boolean oldValue = trafficLight.isRedOn();
        trafficLight.turnOnRed();
        firePropertyChange("turnOn", oldValue, trafficLight.isRedOn());
    }

    public void turnOnYellow() {
        boolean oldValue = trafficLight.isYellowOn();
        trafficLight.turnOnYellow();
        firePropertyChange("turnOn", oldValue, trafficLight.isYellowOn());
    }

    public void turnOnGreen() {
        boolean oldValue = trafficLight.isGreenOn();
        trafficLight.turnOnGreen();
        firePropertyChange("turnOn", oldValue, trafficLight.isGreenOn());
    }

    public void turnOffRed() {
        boolean oldValue = trafficLight.isRedOn();
        trafficLight.turnOffRed();
        firePropertyChange("turnOff", oldValue, trafficLight.isRedOn());
    }

    public void turnOffYellow() {
        boolean oldValue = trafficLight.isYellowOn();
        trafficLight.turnOffYellow();
        firePropertyChange("turnOff", oldValue, trafficLight.isYellowOn());
    }

    public void turnOffGreen() {
        boolean oldValue = trafficLight.isGreenOn();
        trafficLight.turnOffGreen();
        firePropertyChange("turnOff", oldValue, trafficLight.isGreenOn());
    }

    public void setRedDelay(int value) {
        int oldValue = trafficLight.getRedDelay();
        trafficLight.setRedDelay(value);
        firePropertyChange("delay", oldValue, trafficLight.getRedDelay());
    }

    public void setYellowDelay(int value) {
        int oldValue = trafficLight.getYellowDelay();
        trafficLight.setYellowDelay(value);
        firePropertyChange("delay", oldValue, trafficLight.getYellowDelay());
    }

    public void setGreenDelay(int value) {
        int oldValue = trafficLight.getGreenDelay();
        trafficLight.setGreenDelay(value);
        firePropertyChange("delay", oldValue, trafficLight.getGreenDelay());
    }

    public boolean isRedOn() {
        return trafficLight.isRedOn();
    }

    public boolean isYellowOn() {
        return trafficLight.isYellowOn();
    }

    public boolean isGreenOn() {
        return trafficLight.isGreenOn();
    }

    public int getRedDelay() {
        return trafficLight.getRedDelay();
    }

    public int getYellowDelay() {
        return trafficLight.getYellowDelay();
    }

    public int getGreenDelay() {
        return trafficLight.getGreenDelay();
    }
}
