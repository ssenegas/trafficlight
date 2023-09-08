package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TrafficLightController extends JPanel  {

    private TrafficLightModel model;
    private TrafficLightView view;
    public TrafficLightController(TrafficLightModel model, TrafficLightView view) {
        super(new BorderLayout());

        this.model = model;
        this.view = view;
        initGui();
    }

    private void initGui() {
        final JButton red = new JButton("Red");
        red.addActionListener(this::handleRedAction);

        final JButton amber = new JButton("Yellow");
        amber.addActionListener(this::handleYellowAction);

        final JToggleButton green = new JToggleButton("Green");
        green.addActionListener(this::handleGreenAction);

        final JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.add(red);
        actionPanel.add(amber);
        actionPanel.add(green);
        this.add(actionPanel, BorderLayout.CENTER);
    }

    private void handleRedAction(ActionEvent event) {
        // Not yet implemented
    }

    private void handleYellowAction(ActionEvent event) {
        // Not yet implemented
    }

    private void handleGreenAction(ActionEvent event) {
        // Not yet implemented
    }
}
