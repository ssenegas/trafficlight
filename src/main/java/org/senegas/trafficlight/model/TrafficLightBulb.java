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
        return this.light;
    }

    public boolean isOn() {
        return this.isOn;
    }

    public boolean isBlinking() {
        return this.delay > 0;
    }

    public int getDelay() {
        return this.delay;
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
        return this.isOn == that.isOn && this.delay == that.delay && this.light == that.light;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.light, this.isOn, this.delay);
    }

    @Override
    public String toString() {
        return String.format(
                "%s: %s%s",
                this.light.getName(),
                this.isOn ? "On" : "Off",
                isBlinking() ? " (Blinking, delay=" + this.delay + ")" : "");
    }
}
