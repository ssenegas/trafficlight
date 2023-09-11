package org.senegas.trafficlight.view;

import net.miginfocom.swing.MigLayout;
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
    private final TrafficLightModel model;
    private final TrafficLightView view;
    private CommPort port;

    public TrafficLightController(TrafficLightModel model, TrafficLightView view) {
        super(new BorderLayout());
        this.model = model;
        this.view = view;
        initGui();
        initSerialPort();
    }

    private void initSerialPort() {
        List<String> ports = CommPortSelector.get().listPorts();
        Optional<String> first = ports.stream()
                .filter(name -> name.contains("COM3")).findFirst();
        first.ifPresent(name -> {
            System.out.println("Found port " + name);
            this.port = CommPortSelector.get().select(name);
        });
    }

    private void initGui() {
        SpinnerModel delayModel = new SpinnerNumberModel(0,
                0,
                5000,
                100);

        final JPanel actionPanel = new JPanel(new MigLayout());
        final JToggleButton redButton = addLabeledToggleButton(actionPanel, "Red");
        JSpinner redSpinner = addSpinner(actionPanel, delayModel);
        final JToggleButton amberButton = addLabeledToggleButton(actionPanel, "Amber");
        JSpinner amberSpinner = addSpinner(actionPanel, delayModel);
        final JToggleButton greenButton = addLabeledToggleButton(actionPanel, "Green");
        JSpinner greenSpinner = addSpinner(actionPanel, delayModel);
        final JButton send = new JButton("Send");
        actionPanel.add(send, "grow");

        redButton.addItemListener(this::handleRedItemAction);
        amberButton.addItemListener(this::handleAmberItemAction);
        greenButton.addItemListener(this::handleGreenItemAction);
        send.addActionListener(this::handleSendAction);

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
        StringBuilder sb = new StringBuilder();
        sb.append((model.isRedOn() ? "R" : "r"));
        sb.append((model.isAmberOn() ? "A" : "a"));
        sb.append((model.isGreenOn() ? "G" : "g"));
        port.send(sb.toString());
    }

    private JToggleButton addLabeledToggleButton(Container c, String label) {
        JLabel l = new JLabel(label);
        c.add(l);

        final JToggleButton toggleButton = new JToggleButton(label);
        l.setLabelFor(toggleButton);
        c.add(toggleButton, "grow");

        return toggleButton;
    }

    private JSpinner addSpinner(Container c, SpinnerModel model) {
        JSpinner spinner = new JSpinner(model);
        c.add(spinner);
        c.add(new JLabel("ms"), "wrap");
        return spinner;
    }
}
