package org.senegas.trafficlight.model;

import java.awt.*;
import java.util.Objects;

public class Led implements Switchable {
    private final Color color;
    private boolean isOn;
    private int delay;

    Led(Color c) {
        this(c, false, 0);
    }

    Led(Color c, boolean isOn, int delay) {
        this.color = c;
        this.isOn = isOn;
        this.delay = delay;
    }

    @Override
    public void turnOn() {
        this.isOn = true;
    }

    @Override
    public void turnOff() {
        this.isOn = false;
    }
    public Color getColor() {
        return this.color;
    }
    public boolean isOn() {
        return this.isOn;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "Led{" +
                "color=" + this.color +
                ", isOn=" + this.isOn +
                ", delay=" + this.delay +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Led led = (Led) o;
        return this.isOn == led.isOn && this.delay == led.delay && Objects.equals(this.color, led.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.color, this.isOn, this.delay);
    }
}
