package org.senegas.trafficlight.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrafficLight {
    public static final int DEFAULT_BLINKING_DELAY = 1_000;

    private final EnumMap<Light, TrafficLightBulb> bulbs;

    public TrafficLight() {
        this.bulbs = new EnumMap<>(Light.class);
        for (Light light : Light.values()) {
            this.bulbs.put(light, new TrafficLightBulb(light));
        }
    }

    public TrafficLightBulb getBulb(Light light) {
        return this.bulbs.get(light);
    }

    public boolean isBulbOn(Light light) {
        return getBulb(light).isOn();
    }

    public void turnOnBulb(Light light) {
        getBulb(light).turnOn();
    }

    public void turnOffBulb(Light light) {
        getBulb(light).turnOff();
    }

    public void setBulbBlinking(Light light, int delay) {
        getBulb(light).setBlinking(delay);
    }

    public String toArduinoCommand() {
        StringBuilder command = new StringBuilder();
        for (Light light : Light.values()) {
            TrafficLightBulb bulb = getBulb(light);
            String delay = String.format("%04d", bulb.getDelay());
            char lightChar =
                    bulb.isOn()
                            ? Character.toUpperCase(light.getName().charAt(0))
                            : Character.toLowerCase(light.getName().charAt(0));
            command.append(lightChar).append(delay);
        }
        return command.toString();
    }

    public static TrafficLight parse(String input) {
        final Pattern p =
                Pattern.compile(
                        "Green:(On|Off|Blinking), Yellow:(On|Off|Blinking), Red:(On|Off|Blinking)");

        final Matcher matcher = p.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid input format: " + input);
        }

        Map<Light, String> states =
                Map.of(
                        Light.GREEN, matcher.group(1),
                        Light.YELLOW, matcher.group(2),
                        Light.RED, matcher.group(3));

        TrafficLight trafficLight = new TrafficLight();
        for (Map.Entry<Light, String> entry : states.entrySet()) {
            setTrafficLightBulbState(trafficLight, entry.getKey(), entry.getValue());
        }
        return trafficLight;
    }

    private static void setTrafficLightBulbState(
            TrafficLight trafficLight, Light light, String state) {
        TrafficLightBulb bulb = trafficLight.getBulb(light);
        switch (state) {
            case "On" -> bulb.turnOn();
            case "Off" -> bulb.turnOff();
            case "Blinking" -> bulb.setBlinking(DEFAULT_BLINKING_DELAY);
            default ->
                    throw new IllegalArgumentException(
                            "Unknown state for " + light.getName() + ": " + state);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrafficLight)) return false;
        TrafficLight that = (TrafficLight) o;
        return Objects.equals(this.bulbs, that.bulbs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bulbs);
    }
}
