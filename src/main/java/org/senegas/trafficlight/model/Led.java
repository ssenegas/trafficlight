package org.senegas.trafficlight.model;

import java.awt.*;
import java.util.Objects;

public class Led implements Switchable {
    private Color color;
    private boolean isOn;
    private long delay;

    Led(Color c) {
        this(c, false, 0);
    }

    Led(Color c, boolean isOn, long delay) {
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
        return color;
    }
    public boolean isOn() {
        return isOn;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Led led = (Led) o;
        return isOn == led.isOn && delay == led.delay && Objects.equals(color, led.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, isOn, delay);
    }
}
