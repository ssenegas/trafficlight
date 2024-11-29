package org.senegas.trafficlight.model;

import java.awt.Color;

public enum Light {
    RED("Red", Color.RED),
    YELLOW("Yellow", Color.YELLOW),
    GREEN("Green", Color.GREEN);

    private final String name;
    private final Color color;

    Light(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }
}
