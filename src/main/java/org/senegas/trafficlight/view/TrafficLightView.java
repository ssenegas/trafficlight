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
        this.add(this.trafficLightComponent);
    }

    public void setModel(TrafficLightModel model) {
        assert model != null : "model should not be null";
        this.model = model;
        this.model.addPropertyChangeListener(this);
        this.trafficLightComponent.setModel(this.model);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.trafficLightComponent.restartTimer();
        repaint();
    }
}
