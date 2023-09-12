package org.senegas.trafficlight.view;

import net.miginfocom.swing.MigLayout;
import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.serial.CommPort;
import org.senegas.trafficlight.serial.CommPortSelector;
import org.senegas.trafficlight.serial.ICommPortConfig;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficLightController extends JPanel  {
    private final TrafficLightModel model;
    private final TrafficLightView view;
    private CommPort port;
    private JSpinner redSpinner;
    private JSpinner amberSpinner;
    private JSpinner greenSpinner;

    public TrafficLightController(TrafficLightModel model, TrafficLightView view) {
        super(new BorderLayout());
        this.model = model;
        this.view = view;
        initGui();
        initSerialPort();
        initTimer();
    }

    private void initTimer() {
        TimerTask task = new BlinkerLedsTask(this.model);
        Timer timer = new Timer("Timer");
        timer.scheduleAtFixedRate(task, 0, 1_000);
    }

    private void initSerialPort() {
        List<String> ports = CommPortSelector.get().listPorts();
        Optional<String> first = ports.stream()
                .filter(ICommPortConfig.PORT_NAME::equals).findFirst();
        first.ifPresent(name -> {
            System.out.println("Found port " + name);
            port = CommPortSelector.get().select(name);
        });
        if (port == null) {
            System.err.println("Could not find port [" + ICommPortConfig.PORT_NAME + "]");
        }
    }

    private void initGui() {
        final JPanel actionPanel = new JPanel(new MigLayout());
        final JToggleButton redButton = addLabeledToggleButton(actionPanel, "Red");
        redSpinner = addSpinner(actionPanel);
        final JToggleButton amberButton = addLabeledToggleButton(actionPanel, "Amber");
        amberSpinner = addSpinner(actionPanel);
        final JToggleButton greenButton = addLabeledToggleButton(actionPanel, "Green");
        greenSpinner = addSpinner(actionPanel);
        final JButton send = new JButton("Send");
        actionPanel.add(send, "grow");

        redButton.addItemListener(this::handleRedItemAction);
        redSpinner.addChangeListener(this::handleRedDelayChange);
        amberButton.addItemListener(this::handleAmberItemAction);
        amberSpinner.addChangeListener(this::handleAmberDelayChange);
        greenButton.addItemListener(this::handleGreenItemAction);
        greenSpinner.addChangeListener(this::handleGreenDelayChange);
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

    private void handleRedDelayChange(ChangeEvent changeEvent) {
        SpinnerModel spinnerModel = redSpinner.getModel();
        this.model.setRedDelay((Integer) spinnerModel.getValue());
    }

    private void handleAmberItemAction(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        if (state == ItemEvent.SELECTED) {
            model.turnOnAmber();
        } else {
            model.turnOffAmber();
        }
    }

    private void handleAmberDelayChange(ChangeEvent changeEvent) {
        SpinnerModel spinnerModel = amberSpinner.getModel();
        this.model.setAmberDelay((Integer) spinnerModel.getValue());
    }

    private void handleGreenItemAction(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        if (state == ItemEvent.SELECTED) {
            model.turnOnGreen();
        } else {
            model.turnOffGreen();
        }
    }

    private void handleGreenDelayChange(ChangeEvent changeEvent) {
        SpinnerModel spinnerModel = greenSpinner.getModel();
        this.model.setGreenDelay((Integer) spinnerModel.getValue());
    }

    private void handleSendAction(ActionEvent event) {
        String msg = (model.isRedOn() ? "R" : "r") +
                (model.isAmberOn() ? "A" : "a") +
                (model.isGreenOn() ? "G" : "g");
        port.send(msg);
    }

    private JToggleButton addLabeledToggleButton(Container c, String label) {
        JLabel l = new JLabel(label);
        c.add(l);
        final JToggleButton toggleButton = new JToggleButton(label);
        l.setLabelFor(toggleButton);
        c.add(toggleButton, "grow");
        return toggleButton;
    }

    private JSpinner addSpinner(Container c) {
        SpinnerModel model = new SpinnerNumberModel(0,
                0,
                1000,
                1000);
        JSpinner spinner = new JSpinner(model);
        c.add(spinner);
        c.add(new JLabel("ms"), "wrap");
        return spinner;
    }
}
