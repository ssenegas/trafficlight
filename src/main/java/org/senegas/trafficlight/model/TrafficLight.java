package org.senegas.trafficlight.model;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TrafficLight {
    private final Map<Color, Led> leds = new HashMap<>();

    public static TrafficLight parse(String input) {
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

            return new TrafficLight(leds);
        }
        throw new IllegalArgumentException("Given argument does not match to the expected format");
    }

    public TrafficLight() {
        this(java.util.List.of(new Led(Color.RED), new Led(Color.YELLOW), new Led(Color.GREEN)));
    }

    // package-private to prevent explicit instantiation
    TrafficLight(List<Led> list) {
        for (Led l: list) {
            this.leds.put(l.getColor(), l);
        }
    }

    public String toArduinoCommand() {
        return (isRedOn() ? "R" : "r") +
                String.format("%04d", getRedDelay()) +
                (isYellowOn() ? "Y" : "y") +
                String.format("%04d", getYellowDelay()) +
                (isGreenOn() ? "G" : "g") +
                String.format("%04d", getGreenDelay());
    }

    public void turnOnRed() {
        turnOn(this.leds.get(Color.RED));
    }

    public void turnOnYellow() {
        turnOn(this.leds.get(Color.YELLOW));
    }

    public void turnOnGreen() {
        turnOn(this.leds.get(Color.GREEN));
    }

    public void turnOffRed() {
        turnOff(this.leds.get(Color.RED));
    }

    public void turnOffYellow() {
        turnOff(this.leds.get(Color.YELLOW));
    }

    public void turnOffGreen() {
        turnOff(this.leds.get(Color.GREEN));
    }

    public boolean isRedOn() {
        return isOn(this.leds.get(Color.RED));
    }

    public boolean isYellowOn() {
        return isOn(this.leds.get(Color.YELLOW));
    }

    public boolean isGreenOn() {
        return isOn(this.leds.get(Color.GREEN));
    }

    public int getRedDelay() {
        return getDelay(this.leds.get(Color.RED));
    }

    public int getYellowDelay() {
        return getDelay(this.leds.get(Color.YELLOW));
    }

    public int getGreenDelay() {
        return getDelay(this.leds.get(Color.GREEN));
    }

    public void setRedDelay(int value) {
        setDelay(this.leds.get(Color.RED), value);
    }

    public void setYellowDelay(int value) {
        setDelay(this.leds.get(Color.YELLOW), value);
    }

    public void setGreenDelay(int value) {
        setDelay(this.leds.get(Color.GREEN), value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrafficLight that = (TrafficLight) o;
        return Objects.equals(this.leds.get(Color.RED), that.leds.get(Color.RED)) &&
               Objects.equals(this.leds.get(Color.YELLOW), that.leds.get(Color.YELLOW)) &&
               Objects.equals(this.leds.get(Color.GREEN), that.leds.get(Color.GREEN));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.leds.get(Color.RED), this.leds.get(Color.YELLOW), this.leds.get(Color.GREEN));
    }

    private void turnOn(Led l) {
        l.turnOn();
    }

    private void turnOff(Led l) {
        l.turnOff();
    }

    private boolean isOn(Led c) {
        return c.isOn();
    }

    private int getDelay(Led c) {
        return c.getDelay();
    }

    private void setDelay(Led l, int value) {
        l.setDelay(value);
    }
}
