package org.senegas.trafficlight.view;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class TrafficLightDrawer {
    private final int xLeft;
    private final int yTop;

    public TrafficLightDrawer(int x, int y) {
        this.xLeft = x;
        this.yTop = y;
    }

    public void draw(Graphics2D g2) {
        int width = 50;
        int gap = 5;
        Rectangle box = new Rectangle(xLeft, yTop, width, 3 * width - 2 * gap);
        g2.setColor(Color.BLACK);
        g2.fill(box);

        Ellipse2D.Double redBulb = new Ellipse2D.Double(xLeft + gap, yTop + gap,
                width - 2 * gap, width - 2 * gap);
        g2.setColor(Color.RED);
        g2.fill(redBulb);

        Ellipse2D.Double amberBulb = new Ellipse2D.Double(xLeft + gap, yTop + width,
                width - 2 * gap, width - 2 * gap);
        g2.setColor(Color.YELLOW);
        g2.fill(amberBulb);

        Ellipse2D.Double greenBulb = new Ellipse2D.Double(xLeft + gap, yTop + 2 * width - gap,
                width - 2 * gap, width - 2 * gap);
        g2.setColor(Color.GREEN);
        g2.fill(greenBulb);
    }
}
