package org.senegas.trafficlight.view;

import net.miginfocom.swing.MigLayout;
import org.senegas.trafficlight.model.TrafficLight;
import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficLightController extends JPanel  {
	private static final Logger LOGGER = Logger.getLogger(TrafficLightController.class.getName());
	
    private final TrafficLightModel model;
    private final TrafficLightView view;
    private final Properties appProps = new Properties();
    private JToggleButton redButton;
    private JToggleButton yellowButton;
    private JToggleButton greenButton;
    private JSpinner redSpinner;
    private JSpinner yellowSpinner;
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
        TimerTask task = new PollingLightURLTask();
        Timer timer = new Timer("PollingLightURLTimer");
        
        // delays the start of the timer task for serial port detection and initialization
        timer.scheduleAtFixedRate(task, 2_000, 30_000);
    }

    private void initGui() {
        final JPanel actionPanel = new JPanel(new MigLayout());
        this.redButton = addLabeledToggleButton(actionPanel, "Red");
        this.redSpinner = addSpinner(actionPanel);
        this.yellowButton = addLabeledToggleButton(actionPanel, "Yellow");
        this.yellowSpinner = addSpinner(actionPanel);
        this.greenButton = addLabeledToggleButton(actionPanel, "Green");
        this.greenSpinner = addSpinner(actionPanel);

        this.redButton.addItemListener(this::handleRedItemAction);
        this.redSpinner.addChangeListener(this::handleRedDelayChange);
        this.yellowButton.addItemListener(this::handleYellowItemAction);
        this.yellowSpinner.addChangeListener(this::handleYellowDelayChange);
        this.greenButton.addItemListener(this::handleGreenItemAction);
        this.greenSpinner.addChangeListener(this::handleGreenDelayChange);

        this.add(actionPanel, BorderLayout.CENTER);
    }

    private void handleRedItemAction(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        if (state == ItemEvent.SELECTED) {
            this.model.turnOnRed();
        } else {
            this.model.turnOffRed();
        }
    }

    private void handleRedDelayChange(ChangeEvent changeEvent) {
        SpinnerModel spinnerModel = this.redSpinner.getModel();
        this.model.setRedDelay((Integer) spinnerModel.getValue());
    }

    private void handleYellowItemAction(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        if (state == ItemEvent.SELECTED) {
            this.model.turnOnYellow();
        } else {
            this.model.turnOffYellow();
        }
    }

    private void handleYellowDelayChange(ChangeEvent changeEvent) {
        SpinnerModel spinnerModel = this.yellowSpinner.getModel();
        this.model.setYellowDelay((Integer) spinnerModel.getValue());
    }

    private void handleGreenItemAction(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        if (state == ItemEvent.SELECTED) {
            this.model.turnOnGreen();
        } else {
            this.model.turnOffGreen();
        }
    }

    private void handleGreenDelayChange(ChangeEvent changeEvent) {
        SpinnerModel spinnerModel = this.greenSpinner.getModel();
        this.model.setGreenDelay((Integer) spinnerModel.getValue());
    }

    private void loadAppProperties() {
        try (InputStream resourceAsStream = TrafficLightController.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            this.appProps.load(resourceAsStream);
        } catch (IOException e) {
        	LOGGER.log(Level.SEVERE, "Something went wrong when reading application properties: {0}",
        			e.getMessage());
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
        SpinnerModel spinnerNumberModel = new SpinnerNumberModel(0,
                0,
                5000,
                500);
        JSpinner spinner = new JSpinner(spinnerNumberModel);
        c.add(spinner);
        c.add(new JLabel("ms"), "wrap");
        return spinner;
    }

    private class PollingLightURLTask extends TimerTask {

        private static final String INFO_URL_LIGHT_KEY = "info.url.light";
        private static final String FOUR_LETTER_CODE_KEY = "4lc";
        private static final String DEFAULT_URL = "https://info.swiss-as.com/light.txt";

        @Override
        public void run() {
            pollURL();
        }
        
        private void pollURL() {
            try {
                URL url = new URL(getLightURL());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                LOGGER.log(Level.INFO, "connection response code: {0}", con.getResponseCode());

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                LOGGER.log(Level.INFO, "{0}", content);

                TrafficLight trafficLight = TrafficLight.parse(content.toString());
                TrafficLightController.this.model.setTrafficLight(trafficLight);

                synchronizeGUIToModel();
            } catch (IOException e) {
            	LOGGER.log(Level.SEVERE, "Something went wrong when polling light URL: {0}",
            			e.getMessage());
            }
        }
        
        private void synchronizeGUIToModel() {
            TrafficLightController.this.redButton.setSelected(TrafficLightController.this.model.isRedOn());
            TrafficLightController.this.yellowButton.setSelected(TrafficLightController.this.model.isYellowOn());
            TrafficLightController.this.greenButton.setSelected(TrafficLightController.this.model.isGreenOn());
            TrafficLightController.this.redSpinner.setValue(TrafficLightController.this.model.getRedDelay());
            TrafficLightController.this.yellowSpinner.setValue(TrafficLightController.this.model.getYellowDelay());
            TrafficLightController.this.greenSpinner.setValue(TrafficLightController.this.model.getGreenDelay());
        }
        
        private String getLightURL() {
            String lightURL = TrafficLightController.this.appProps.getProperty(INFO_URL_LIGHT_KEY, DEFAULT_URL);
            String letterCode = TrafficLightController.this.appProps.getProperty(FOUR_LETTER_CODE_KEY);
            if (letterCode != null) {
                lightURL += "?user="+letterCode;
            }
            return lightURL;
        }
    }
}
