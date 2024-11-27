package org.senegas.trafficlight.model;

import java.util.Objects;

public class TrafficLightBulb implements Switchable {
    private final Light light;
    private boolean isOn;
    private int delay;

    public TrafficLightBulb(Light light) {
        this(light, false, 0);
    }

    public TrafficLightBulb(Light light, boolean isOn, int delay) {
        this.light = light;
        this.isOn = isOn;
        this.delay = delay;
    }

    public Light getLight() {
        return light;
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean isBlinking() {
        return delay > 0;
    }

    public int getDelay() {
        return delay;
    }

    // State Management
    @Override
    public void turnOn() {
        this.isOn = true;
        this.delay = 0; // Turning on disables blinking
    }

    @Override
    public void turnOff() {
        this.isOn = false;
        this.delay = 0; // Turning off disables blinking
    }

    public void setBlinking(int delay) {
        if (delay < 0) {
            throw new IllegalArgumentException("Delay must be non-negative");
        }
        this.isOn = false; // Blinking starts from "off" state
        this.delay = delay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrafficLightBulb)) return false;
        TrafficLightBulb that = (TrafficLightBulb) o;
        return isOn == that.isOn &&
                delay == that.delay &&
                light == that.light;
    }

    @Override
    public int hashCode() {
        return Objects.hash(light, isOn, delay);
    }

    @Override
    public String toString() {
        return String.format("%s: %s%s", light.getName(), isOn ? "On" : "Off", isBlinking() ? " (Blinking, delay=" + delay + ")" : "");
    }
}
