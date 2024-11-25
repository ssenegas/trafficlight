package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLight;
import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TrafficLightController handles business logic, including periodic tasks.
 */
public class TrafficLightController {
    private static final Logger LOGGER = Logger.getLogger(TrafficLightController.class.getName());

    private final TrafficLightModel model;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TrafficLightController(TrafficLightModel model) {
        this.model = model;
    }

    /**
     * Start polling the traffic light data from the specified URL periodically.
     *
     * @param url the URL to poll
     */
    public void startPolling(URL url) {
        // Schedule the PollingLightURLTask to run every 5 seconds
        scheduler.scheduleAtFixedRate(() -> {
            try {
                pollLightURL(url);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error during polling task: {0}", e);
            }
        }, 2, 15, TimeUnit.SECONDS);
    }

    /**
     * Stops the scheduler gracefully.
     */
    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Returns the model.
     */
    public TrafficLightModel getModel() {
        return model;
    }

    /**
     * Handles the toggle button event for the red light.
     *
     * @param isSelected true if the red light should turn on, false otherwise
     */
    public void handleRedToggle(boolean isSelected) {
        if (isSelected) {
            model.turnOnRed();
        } else {
            model.turnOffRed();
        }
    }

    /**
     * Handles the delay spinner change event for the red light.
     *
     * @param delay the new delay value
     */
    public void handleRedDelayChange(int delay) {
        model.setRedDelay(delay);
    }

    /**
     * Handles the toggle button event for the yellow light.
     *
     * @param isSelected true if the yellow light should turn on, false otherwise
     */
    public void handleYellowToggle(boolean isSelected) {
        if (isSelected) {
            model.turnOnYellow();
        } else {
            model.turnOffYellow();
        }
    }

    /**
     * Handles the delay spinner change event for the yellow light.
     *
     * @param delay the new delay value
     */
    public void handleYellowDelayChange(int delay) {
        model.setYellowDelay(delay);
    }

    /**
     * Handles the toggle button event for the green light.
     *
     * @param isSelected true if the green light should turn on, false otherwise
     */
    public void handleGreenToggle(boolean isSelected) {
        if (isSelected) {
            model.turnOnGreen();
        } else {
            model.turnOffGreen();
        }
    }

    /**
     * Handles the delay spinner change event for the green light.
     *
     * @param delay the new delay value
     */
    public void handleGreenDelayChange(int delay) {
        model.setGreenDelay(delay);
    }

    /**
     * Poll the traffic light data from the provided URL.
     *
     * @param url the URL to poll
     * @throws IOException if an error occurs during the HTTP request
     */
    private void pollLightURL(URL url) throws IOException {
        LOGGER.log(Level.INFO, "Polling light data from URL: {0}", url);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5_000);
        connection.setReadTimeout(5_000);

        int responseCode = connection.getResponseCode();
        LOGGER.log(Level.INFO, "HTTP response code: {0}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }

                this.model.setTrafficLight(TrafficLight.parse(content.toString()));
            }
        } else {
            LOGGER.log(Level.WARNING, "Received non-OK response code: {0}", responseCode);
        }

        connection.disconnect();
    }
}
