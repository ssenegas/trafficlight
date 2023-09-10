package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.serial.CommPort;
import org.senegas.trafficlight.serial.CommPortSelector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;

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

        final JButton amber = new JButton("Amber");
        amber.addActionListener(this::handleAmberAction);

        final JToggleButton green = new JToggleButton("Green");
        green.addActionListener(this::handleGreenAction);

        final JToggleButton send = new JToggleButton("Send");
        send.addActionListener(this::handleSendAction);

        final JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.add(red);
        actionPanel.add(amber);
        actionPanel.add(green);
        actionPanel.add(send);
        this.add(actionPanel, BorderLayout.CENTER);
    }

    private void handleRedAction(ActionEvent event) {
        // Not yet implemented
    }

    private void handleAmberAction(ActionEvent event) {
        // Not yet implemented
    }

    private void handleGreenAction(ActionEvent event) {
        // Not yet implemented
    }

    private void handleSendAction(ActionEvent event) {
        List<String> ports = CommPortSelector.get().listPorts();
        Optional<String> first = ports.stream()
                .filter(name -> name.contains("USB")).findFirst();
        first.ifPresent(name -> {
            System.out.println("Found port " + name);
            CommPort port = CommPortSelector.get().select(name);
            port.send("hello 2023");
        });
    }
}
