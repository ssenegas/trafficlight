package org.senegas.trafficlight.view;

import javax.swing.*;
import java.awt.*;

public class TrafficLightComponent extends JComponent {

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        TrafficLightDrawer tl = new TrafficLightDrawer(0, 0);
        tl.draw(g2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50 * 3 + 15);
    }
}
