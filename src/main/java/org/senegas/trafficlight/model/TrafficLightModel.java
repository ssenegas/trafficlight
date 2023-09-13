package org.senegas.trafficlight.model;

import java.io.Serializable;

public class TrafficLightModel extends AbstractModel implements Serializable {
    private TrafficLight trafficLight = new TrafficLight();

    public TrafficLightModel() {
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        final TrafficLight oldTrafficLight = this.trafficLight;
        this.trafficLight = trafficLight;
        firePropertyChange("traffic-light", oldTrafficLight, this.trafficLight);
    }

    public void turnOnRed() {
        boolean oldValue = trafficLight.isRedOn();
        trafficLight.turnOnRed();
        firePropertyChange("turnOn", oldValue, trafficLight.isRedOn());
    }

    public void turnOnAmber() {
        boolean oldValue = trafficLight.isAmberOn();
        trafficLight.turnOnAmber();
        firePropertyChange("turnOn", oldValue, trafficLight.isAmberOn());
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

    public void turnOffAmber() {
        boolean oldValue = trafficLight.isAmberOn();
        trafficLight.turnOffAmber();
        firePropertyChange("turnOff", oldValue, trafficLight.isAmberOn());
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

    public void setAmberDelay(int value) {
        int oldValue = trafficLight.getAmberDelay();
        trafficLight.setAmberDelay(value);
        firePropertyChange("delay", oldValue, trafficLight.getAmberDelay());
    }

    public void setGreenDelay(int value) {
        int oldValue = trafficLight.getGreenDelay();
        trafficLight.setGreenDelay(value);
        firePropertyChange("delay", oldValue, trafficLight.getGreenDelay());
    }

    public boolean isRedOn() {
        return trafficLight.isRedOn();
    }

    public boolean isAmberOn() {
        return trafficLight.isAmberOn();
    }

    public boolean isGreenOn() {
        return trafficLight.isGreenOn();
    }

    public int getRedDelay() {
        return trafficLight.getRedDelay();
    }

    public int getAmberDelay() {
        return trafficLight.getAmberDelay();
    }

    public int getGreenDelay() {
        return trafficLight.getGreenDelay();
    }
}
