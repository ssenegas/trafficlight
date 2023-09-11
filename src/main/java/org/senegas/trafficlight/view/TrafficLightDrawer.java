package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class TrafficLightDrawer {
    TrafficLightModel model;

    public TrafficLightDrawer(TrafficLightModel model) {
        this.model = model;
    }

    public void draw(Graphics2D g2) {
        int xLeft = 0;
        int yTop = 0;
        int width = 50;
        int gap = 5;
        Rectangle box = new Rectangle(xLeft, yTop, width, 3 * width - 2 * gap);
        g2.setColor(Color.BLACK);
        g2.fill(box);

        double x = xLeft + gap;
        double w = width - 2 * gap;
        double h = w;

        Ellipse2D.Double redBulb = new Ellipse2D.Double(x, yTop + gap, w, h);
        g2.setColor(model.isRedOn() ? Color.RED : Color.GRAY);
        g2.fill(redBulb);

        Ellipse2D.Double amberBulb = new Ellipse2D.Double(x, yTop + width, w, h);
        g2.setColor(model.isAmberOn() ? Color.YELLOW : Color.GRAY);
        g2.fill(amberBulb);

        Ellipse2D.Double greenBulb = new Ellipse2D.Double(x, yTop + 2 * width - gap, w, h);
        g2.setColor(model.isGreenOn() ? Color.GREEN : Color.GRAY);
        g2.fill(greenBulb);
    }
}
