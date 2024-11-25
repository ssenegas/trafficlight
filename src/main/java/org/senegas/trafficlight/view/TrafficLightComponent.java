package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.service.TrafficLightBlinkingService;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TrafficLightComponent extends JComponent implements PropertyChangeListener {
    private static final int TRAFFIC_LIGHT_WIDTH = 50;
    private static final int GAP = 5;

    private static final Color RED_ON = Color.RED.brighter().brighter();
    private static final Color YELLOW_ON = Color.YELLOW.brighter().brighter();
    private static final Color GREEN_ON = Color.GREEN.brighter().brighter();
    private static final Color RED_OFF = Color.RED.darker().darker();
    private static final Color YELLOW_OFF = Color.YELLOW.darker().darker();
    private static final Color GREEN_OFF = Color.GREEN.darker().darker();

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
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    public void stop() {
        this.blinkingService.stop();
        this.model.removePropertyChangeListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        drawTrafficLight(g2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(TRAFFIC_LIGHT_WIDTH, TRAFFIC_LIGHT_WIDTH * 3 + 15);
    }

    private void drawTrafficLight(Graphics2D g2) {
        drawTrafficLightBlackBox(g2, 0, 0);
        drawTrafficLightBulbs(g2, 0, 0);
    }

    private void drawTrafficLightBlackBox(Graphics2D g2, int xLeft, int yTop) {
        Rectangle box = new Rectangle(xLeft, yTop, TRAFFIC_LIGHT_WIDTH, 3 * TRAFFIC_LIGHT_WIDTH - 2 * GAP);
        g2.setColor(Color.BLACK);
        g2.fill(box);
    }

    private void drawTrafficLightBulbs(Graphics2D g2, int xLeft, int yTop) {
        drawRedBulb(g2, xLeft, yTop);
        drawYellowBulb(g2, xLeft, yTop);
        drawGreenBulb(g2, xLeft, yTop);
    }

    private void drawRedBulb(Graphics2D g2, int xLeft, int yTop) {
        Ellipse2D.Double redBulb = new Ellipse2D.Double(xLeft + GAP, yTop + GAP, TRAFFIC_LIGHT_WIDTH - 2 * GAP, TRAFFIC_LIGHT_WIDTH - 2 * GAP);
        g2.setColor(blinkingService.isRedVisible() ? RED_ON : RED_OFF);
        g2.fill(redBulb);
    }

    private void drawYellowBulb(Graphics2D g2, int xLeft, int yTop) {
        Ellipse2D.Double YellowBulb = new Ellipse2D.Double(xLeft + GAP, yTop + TRAFFIC_LIGHT_WIDTH, TRAFFIC_LIGHT_WIDTH - 2 * GAP, TRAFFIC_LIGHT_WIDTH - 2 * GAP);
        g2.setColor(blinkingService.isYellowVisible() ? YELLOW_ON : YELLOW_OFF);
        g2.fill(YellowBulb);
    }

    private void drawGreenBulb(Graphics2D g2, int xLeft, int yTop) {
        Ellipse2D.Double greenBulb = new Ellipse2D.Double(xLeft + GAP, yTop + 2 * TRAFFIC_LIGHT_WIDTH - GAP, TRAFFIC_LIGHT_WIDTH - 2 * GAP, TRAFFIC_LIGHT_WIDTH - 2 * GAP);
        g2.setColor(blinkingService.isGreenVisible() ? GREEN_ON : GREEN_OFF);
        g2.fill(greenBulb);
    }
}
