package org.senegas.trafficlight.service;

import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles blinking logic for the traffic light LEDs, without modifying the model.
 */
public class TrafficLightBlinkingService {
    private static final Logger LOGGER = Logger.getLogger(TrafficLightBlinkingService.class.getName());

    private final TrafficLightModel model;
    private final Timer swingTimer;

    // Internal blinking states for LEDs
    private boolean blinkingRed = false;
    private boolean blinkingYellow = false;
    private boolean blinkingGreen = false;

    // Last update timestamps for each LED
    private long lastRedUpdate = 0;
    private long lastYellowUpdate = 0;
    private long lastGreenUpdate = 0;

    public TrafficLightBlinkingService(TrafficLightModel model) {
        this.model = model;

        // Timer to update the blinking logic at a fixed interval (20ms)
        this.swingTimer = new Timer(20, e -> updateBlinkingState());
    }

    /**
     * Starts the blinking timer.
     */
    public void start() {
        LOGGER.log(Level.INFO, "Starting blinking service.");
        swingTimer.start();
    }

    /**
     * Stops the blinking timer.
     */
    public void stop() {
        LOGGER.log(Level.INFO, "Stopping blinking service.");
        swingTimer.stop();
    }

    /**
     * Checks if the red LED should be shown as ON (blinking or static).
     *
     * @return true if the red LED is ON, false otherwise.
     */
    public boolean isRedVisible() {
        return model.getRedDelay() > 0 ? blinkingRed : model.isRedOn();
    }

    /**
     * Checks if the yellow LED should be shown as ON (blinking or static).
     *
     * @return true if the yellow LED is ON, false otherwise.
     */
    public boolean isYellowVisible() {
        return model.getYellowDelay() > 0 ? blinkingYellow : model.isYellowOn();
    }

    /**
     * Checks if the green LED should be shown as ON (blinking or static).
     *
     * @return true if the green LED is ON, false otherwise.
     */
    public boolean isGreenVisible() {
        return model.getGreenDelay() > 0 ? blinkingGreen : model.isGreenOn();
    }

    /**
     * Updates the blinking state for each LED based on the delays configured in the model.
     */
    private void updateBlinkingState() {
        long currentTime = System.currentTimeMillis();

        // Update blinking states locally (without modifying the model)
        boolean redStateChanged = updateRedBlinking(currentTime);
        boolean yellowStateChanged = updateYellowBlinking(currentTime);
        boolean greenStateChanged = updateGreenBlinking(currentTime);

        // Trigger UI updates only if a state changed
        if (redStateChanged || yellowStateChanged || greenStateChanged) {
            model.notifyBlinkingStateChanged();
        }
    }

    private boolean updateRedBlinking(long currentTime) {
        int redDelay = model.getRedDelay();
        if (redDelay > 0) {
            if (currentTime - lastRedUpdate >= redDelay) {
                blinkingRed = !blinkingRed;
                lastRedUpdate = currentTime;
                return true;
            }
        } else {
            blinkingRed = model.isRedOn(); // Use static toggle state
        }
        return false;
    }

    private boolean updateYellowBlinking(long currentTime) {
        int yellowDelay = model.getYellowDelay();
        if (yellowDelay > 0) {
            if (currentTime - lastYellowUpdate >= yellowDelay) {
                blinkingYellow = !blinkingYellow;
                lastYellowUpdate = currentTime;
                return true;
            }
        } else {
            blinkingYellow = model.isYellowOn(); // Use static toggle state
        }
        return false;
    }

    private boolean updateGreenBlinking(long currentTime) {
        int greenDelay = model.getGreenDelay();
        if (greenDelay > 0) {
            if (currentTime - lastGreenUpdate >= greenDelay) {
                blinkingGreen = !blinkingGreen;
                lastGreenUpdate = currentTime;
                return true;
            }
        } else {
            blinkingGreen = model.isGreenOn(); // Use static toggle state
        }
        return false;
    }
}
