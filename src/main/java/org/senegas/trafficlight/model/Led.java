package org.senegas.trafficlight.model;

public class Led implements Switchable {

    private boolean isOn;

    private long delay;

    public Led() {
        this(false, 0);
    }

    public Led(boolean isOn) {
        this(isOn, 0);
    }

    public Led(boolean isOn, long delay) {
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

    public boolean isOn() {
        return isOn;
    }

    public long getDelay() {
        return delay;
    }
}
