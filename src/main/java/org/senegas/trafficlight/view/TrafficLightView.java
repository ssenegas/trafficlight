package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TrafficLightView extends JPanel implements PropertyChangeListener  {

    private TrafficLightComponent trafficLight = new TrafficLightComponent();
    private TrafficLightModel model;

    public TrafficLightView(TrafficLightModel model) {
        super(new BorderLayout());
        this.model = model;
        this.add(trafficLight);
    }

    @Override
    public Dimension getPreferredSize() {
        return trafficLight.getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawFrame(g);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    private void drawFrame(Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        Dimension size = this.getSize();
        g2.setColor(Color.MAGENTA);
        g.drawRect(0, 0 , size.width, size.height);
    }
}
