package org.senegas.trafficlight.view;

import net.miginfocom.swing.MigLayout;
import org.senegas.trafficlight.model.TrafficLight;
import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficLightController extends JPanel  {
    private final TrafficLightModel model;
    private final TrafficLightView view;
    private Properties appProps = new Properties();
    private JToggleButton redButton;
    private JToggleButton yellowButton;
    private JToggleButton greenButton;
    private JSpinner redSpinner;
    private JSpinner YellowSpinner;
    private JSpinner greenSpinner;

    public TrafficLightController(TrafficLightModel model, TrafficLightView view) {
        super(new BorderLayout());
        this.model = model;
        this.view = view;
        initGui();
        loadAppProperties();
        startTimer();
    }

    private void startTimer() {
        TimerTask task = new PollingLightsURLTask();
        Timer timer = new Timer("PollingLightsURLTimer");
        timer.scheduleAtFixedRate(task, 2_000, 5_000); // delay timer task for serial port detection
    }

    private void initGui() {
        final JPanel actionPanel = new JPanel(new MigLayout());
        redButton = addLabeledToggleButton(actionPanel, "Red");
        redSpinner = addSpinner(actionPanel);
        yellowButton = addLabeledToggleButton(actionPanel, "Yellow");
        YellowSpinner = addSpinner(actionPanel);
        greenButton = addLabeledToggleButton(actionPanel, "Green");
        greenSpinner = addSpinner(actionPanel);

        redButton.addItemListener(this::handleRedItemAction);
        redSpinner.addChangeListener(this::handleRedDelayChange);
        yellowButton.addItemListener(this::handleYellowItemAction);
        YellowSpinner.addChangeListener(this::handleYellowDelayChange);
        greenButton.addItemListener(this::handleGreenItemAction);
        greenSpinner.addChangeListener(this::handleGreenDelayChange);

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

    private void pollLightsURL() {
        try {
            URL url = new URL(appProps.getProperty("lightsURL"));

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

            synchronizeGUIToModel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAppProperties() {
        try (InputStream resourceAsStream = TrafficLightController.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            appProps.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void synchronizeGUIToModel() {
        redButton.setSelected(model.isRedOn());
        yellowButton.setSelected(model.isYellowOn());
        greenButton.setSelected(model.isGreenOn());
        redSpinner.setValue((Integer) model.getRedDelay());
        YellowSpinner.setValue((Integer) model.getYellowDelay());
        greenSpinner.setValue((Integer) model.getGreenDelay());
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

    private class PollingLightsURLTask extends TimerTask {

        @Override
        public void run() {
            pollLightsURL();
        }
    }
}
