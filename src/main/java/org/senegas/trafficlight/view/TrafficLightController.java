package org.senegas.trafficlight.view;

import net.miginfocom.swing.MigLayout;
import org.senegas.trafficlight.model.TrafficLight;
import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

public class TrafficLightController extends JPanel  {
    private final TrafficLightModel model;
    private final TrafficLightView view;
    private JSpinner redSpinner;
    private JSpinner YellowSpinner;
    private JSpinner greenSpinner;

    public TrafficLightController(TrafficLightModel model, TrafficLightView view) {
        super(new BorderLayout());
        this.model = model;
        this.view = view;
        initGui();
        initTimer();
    }

    private void initTimer() {
        //TimerTask task = new BlinkerLedsTask(this.model);
        //Timer timer = new Timer("Timer");
        //timer.scheduleAtFixedRate(task, 0, 1_000);
    }

    private void initGui() {
        final JPanel actionPanel = new JPanel(new MigLayout());
        final JToggleButton redButton = addLabeledToggleButton(actionPanel, "Red");
        redSpinner = addSpinner(actionPanel);
        final JToggleButton YellowButton = addLabeledToggleButton(actionPanel, "Yellow");
        YellowSpinner = addSpinner(actionPanel);
        final JToggleButton greenButton = addLabeledToggleButton(actionPanel, "Green");
        greenSpinner = addSpinner(actionPanel);
        final JButton test = new JButton("Test");
        actionPanel.add(test, "grow");

        redButton.addItemListener(this::handleRedItemAction);
        redSpinner.addChangeListener(this::handleRedDelayChange);
        YellowButton.addItemListener(this::handleYellowItemAction);
        YellowSpinner.addChangeListener(this::handleYellowDelayChange);
        greenButton.addItemListener(this::handleGreenItemAction);
        greenSpinner.addChangeListener(this::handleGreenDelayChange);
        test.addActionListener(this::handleTestAction);

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

    private void handleYellowItemAction(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        if (state == ItemEvent.SELECTED) {
            model.turnOnYellow();
        } else {
            model.turnOffYellow();
        }
    }

    private void handleYellowDelayChange(ChangeEvent changeEvent) {
        SpinnerModel spinnerModel = YellowSpinner.getModel();
        this.model.setYellowDelay((Integer) spinnerModel.getValue());
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

    private void handleTestAction(ActionEvent event) {
        final MessageFormat form = new MessageFormat("Green:{0}, Yellow:{1}, Red:{2}");
        String[] values = { "On", "Off", "On" };

        try {
            URL url = new URL("http://localhost:8080/examples/light.txt");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            System.out.println(content);

            TrafficLight trafficLight = TrafficLight.parse(content.toString());
            model.setTrafficLight(trafficLight);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                5000,
                500);
        JSpinner spinner = new JSpinner(model);
        c.add(spinner);
        c.add(new JLabel("ms"), "wrap");
        return spinner;
    }
}
