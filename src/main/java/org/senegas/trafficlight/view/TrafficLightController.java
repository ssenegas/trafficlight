package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.serial.CommPort;
import org.senegas.trafficlight.serial.CommPortSelector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Optional;

public class TrafficLightController extends JPanel  {

    private TrafficLightModel model;
    private TrafficLightView view;
    private CommPort port;

    public TrafficLightController(TrafficLightModel model, TrafficLightView view) {
        super(new BorderLayout());

        this.model = model;
        this.view = view;
        initGui();
        initComm();
    }

    private void initComm() {
        List<String> ports = CommPortSelector.get().listPorts();
        Optional<String> first = ports.stream()
                .filter(name -> name.contains("USB")).findFirst();
        first.ifPresent(name -> {
            System.out.println("Found port " + name);
            this.port = CommPortSelector.get().select(name);
        });
    }

    private void initGui() {
        final JToggleButton red = new JToggleButton("Red");
        red.addItemListener(this::handleRedItemAction);

        final JToggleButton amber = new JToggleButton("Amber");
        amber.addItemListener(this::handleAmberItemAction);

        final JToggleButton green = new JToggleButton("Green");
        green.addItemListener(this::handleGreenItemAction);

        final JButton send = new JButton("Send");
        send.addActionListener(this::handleSendAction);

        final JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.add(red);
        actionPanel.add(amber);
        actionPanel.add(green);
        actionPanel.add(send);
        this.add(actionPanel, BorderLayout.CENTER);
    }

    private void handleRedItemAction(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        if (state == ItemEvent.SELECTED) {
            model.turnOnRed();
        } else {
            model.turnOffRed();
        }
    }

    private void handleAmberItemAction(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        if (state == ItemEvent.SELECTED) {
            model.turnOnAmber();
        } else {
            model.turnOffAmber();
        }
    }

    private void handleGreenItemAction(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        if (state == ItemEvent.SELECTED) {
            model.turnOnGreen();
        } else {
            model.turnOffGreen();
        }
    }

    private void handleSendAction(ActionEvent event) {
        port.send("hello 2023");
    }
}
