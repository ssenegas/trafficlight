package org.senegas.trafficlight.model;

import java.awt.*;

public class Led implements Switchable {
    private final Color color;
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
    public String toString() {
        return "Led{" +
                "color=" + color +
                ", isOn=" + isOn +
                ", delay=" + delay +
                '}';
    }
}
