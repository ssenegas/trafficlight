package org.senegas.trafficlight.model;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class TrafficLightModel extends AbstractModel implements Serializable {

    Map<Color, Led> leds = new HashMap<>();
    private final Led red = new Led(Color.RED);
    private final Led amber = new Led(Color.YELLOW);
    private final Led green = new Led(Color.GREEN);

    public static TrafficLightModel parse(String input) {
        final Pattern p = Pattern.compile("Green:(On|Off|Blinking), Yellow:(On|Off|Blinking), Red:(On|Off|Blinking)");
        final Matcher m = p.matcher(input);
        if (m.find()) {
            Map<Color, Integer> map = Map.of(Color.GREEN, 1, Color.YELLOW, 2, Color.RED, 3);

            Function<Map.Entry<Color, Integer>, Led> toLed = e -> {
                boolean isOn = false;
                int delay = 0;
                if (m.group(e.getValue()).equals("Blinking")) {
                    delay = 1_000;
                } else {
                    isOn = m.group(e.getValue()).equals("On");
                }
                return new Led(e.getKey(), isOn, delay);
            };

            List<Led> leds =
            map.entrySet().stream()
                    .map(toLed)
                    .collect(Collectors.toList());

            return new TrafficLightModel(leds);
        }
        throw new IllegalArgumentException("Given argument does not match to the expected format");
    }

    public TrafficLightModel() {
        this(List.of(new Led(Color.RED), new Led(Color.YELLOW), new Led(Color.GREEN)));
    }

    // package-private to prevent explicit instantiation
    TrafficLightModel(List<Led> list) {
        for (Led l: list) {
            this.leds.put(l.getColor(), l);
        }
    }

    public void turnOnRed() {
        turnOn(this.leds.get(Color.RED));
    }

    public void turnOnAmber() {
        turnOn(this.leds.get(Color.YELLOW));
    }

    public void turnOnGreen() {
        turnOn(this.leds.get(Color.GREEN));
    }

    private void turnOn(Led l) {
        boolean oldValue = l.isOn();
        l.turnOn();
        firePropertyChange("turnOn", oldValue, l.isOn());
    }

    public void turnOffRed() {
        turnOff(this.leds.get(Color.RED));
    }

    public void turnOffAmber() {
        turnOff(this.leds.get(Color.YELLOW));
    }

    public void turnOffGreen() {
        turnOff(this.leds.get(Color.GREEN));
    }

    private void turnOff(Led l) {
        boolean oldValue = l.isOn();
        l.turnOff();
        firePropertyChange("turnOff", oldValue, l.isOn());
    }

    public void setRedDelay(int value) {
        setDelay(this.leds.get(Color.RED), value);
    }

    public void setAmberDelay(int value) {
        setDelay(this.leds.get(Color.YELLOW), value);
    }

    public void setGreenDelay(int value) {
        setDelay(this.leds.get(Color.GREEN), value);
    }

    private void setDelay(Led l, int value) {
        int oldValue = l.getDelay();
        l.setDelay(value);
        firePropertyChange("delay", oldValue, l.getDelay());
    }

    public boolean isRedOn() {
        return isOn(this.leds.get(Color.RED));
    }

    public boolean isAmberOn() {
        return isOn(this.leds.get(Color.YELLOW));
    }

    public boolean isGreenOn() {
        return isOn(this.leds.get(Color.GREEN));
    }

    public int getRedDelay() {
        return getDelay(this.leds.get(Color.RED));
    }

    public int getAmberDelay() {
        return getDelay(this.leds.get(Color.YELLOW));
    }

    public int getGreenDelay() {
        return getDelay(this.leds.get(Color.GREEN));
    }

    private boolean isOn(Led c) {
        return c.isOn();
    }

    private int getDelay(Led c) {
        return c.getDelay();
    }
}
