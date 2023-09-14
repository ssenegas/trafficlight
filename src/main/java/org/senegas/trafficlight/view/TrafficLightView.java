package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TrafficLightView extends JPanel implements PropertyChangeListener  {

    private TrafficLightModel model;
    private final TrafficLightComponent trafficLightComponent = new TrafficLightComponent();

    public TrafficLightView() {
        super(new BorderLayout());
        this.add(trafficLightComponent);
    }

    public void setModel(TrafficLightModel model) {
        this.model = model;
        if (this.model != null) {
            this.model.addPropertyChangeListener(this);
            trafficLightComponent.setModel(this.model);
        } else {
            throw new NullPointerException("Given model is null");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawDebugFrame(g);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    private void drawDebugFrame(Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        Dimension size = this.getSize();
        g2.setColor(Color.MAGENTA);
        g.drawRect(0, 0 , size.width, size.height);
    }
}
