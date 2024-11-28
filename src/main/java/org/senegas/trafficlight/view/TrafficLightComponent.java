package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.service.TrafficLightBlinkingService;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TrafficLightComponent extends JComponent implements PropertyChangeListener {
    private static final double WIDTH_TO_HEIGHT_RATIO = 1.0 / 3.0; // Black box width is 1/3 of its height
    private static final double BULB_TO_BOX_RATIO = 0.66; // Bulb diameter is 66% of black box width

    private final TrafficLightModel model;
    private final TrafficLightBlinkingService blinkingService;

    public TrafficLightComponent(TrafficLightModel model) {
        this.model = model;
        this.blinkingService = new TrafficLightBlinkingService(model);

        // Start the blinking service
        this.blinkingService.start();

        // Listen to model changes
        this.model.addPropertyChangeListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        // Default size: 300px height with a 1:3 width-to-height ratio
        int height = getParent() != null ? getParent().getHeight() : 300;
        int width = (int) (height * WIDTH_TO_HEIGHT_RATIO);
        return new Dimension(width, height);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    public void stop() {
        this.blinkingService.stop();
        this.model.removePropertyChangeListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // Calculate black box dimensions
        int blackBoxHeight = height;
        int blackBoxWidth = (int) (blackBoxHeight * WIDTH_TO_HEIGHT_RATIO);
        int blackBoxX = (width - blackBoxWidth) / 2; // Center the black box horizontally
        int blackBoxY = 0; // Black box starts at the top of the component

        // Calculate bulb dimensions
        int bulbDiameter = (int) (blackBoxWidth * BULB_TO_BOX_RATIO);
        int gap = (blackBoxHeight - 3 * bulbDiameter) / 4; // Divide remaining space into 4 gaps (top, middle, bottom)

        // Draw the traffic light
        drawTrafficLightBlackBox(g2, blackBoxX, blackBoxY, blackBoxWidth, blackBoxHeight);
        drawTrafficLightBulbs(g2, blackBoxX, blackBoxY, blackBoxWidth, bulbDiameter, gap);
    }

    private void drawTrafficLightBlackBox(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(Color.BLACK);
        g2.fillRect(x, y, width, height);
    }

    private void drawTrafficLightBulbs(Graphics2D g2, int x, int y, int width, int diameter, int gap) {
        int bulbX = x + (width - diameter) / 2; // Center bulbs horizontally within the black box

        // Draw the bulbs
        drawBulb(g2, bulbX, y + gap, diameter, blinkingService.isRedVisible() ? Color.RED.brighter().brighter() : Color.RED.darker().darker());
        drawBulb(g2, bulbX, y + diameter + 2 * gap, diameter, blinkingService.isYellowVisible() ? Color.YELLOW.brighter().brighter() : Color.YELLOW.darker().darker());
        drawBulb(g2, bulbX, y + 2 * (diameter + gap) + gap, diameter, blinkingService.isGreenVisible() ? Color.GREEN.brighter().brighter() : Color.GREEN.darker().darker());
    }

    private void drawBulb(Graphics2D g2, int x, int y, int diameter, Color color) {
        Ellipse2D.Double bulb = new Ellipse2D.Double(x, y, diameter, diameter);
        g2.setColor(color);
        g2.fill(bulb);
    }
}
