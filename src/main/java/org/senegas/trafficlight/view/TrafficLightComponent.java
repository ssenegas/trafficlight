package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import java.awt.*;

public class TrafficLightComponent extends JComponent {

    private TrafficLightModel model;

    public TrafficLightComponent() {
    }

    public void setModel(TrafficLightModel model) {
        this.model = model;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        TrafficLightDrawer tl = new TrafficLightDrawer(this.model);
        tl.draw(g2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50 * 3 + 15);
    }
}
