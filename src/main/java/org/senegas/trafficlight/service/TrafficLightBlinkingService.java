package org.senegas.trafficlight.service;

import org.senegas.trafficlight.model.TrafficLightModel;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

/** Handles blinking logic for the traffic light bulbs, without modifying the model. */
public class TrafficLightBlinkingService {
    private static final Logger LOGGER =
            Logger.getLogger(TrafficLightBlinkingService.class.getName());

    private final TrafficLightModel model;
    private final Timer swingTimer;

    // Internal blinking states for bulbs
    private boolean blinkingRed = false;
    private boolean blinkingYellow = false;
    private boolean blinkingGreen = false;

    // Last update timestamps for each bulb
    private long lastRedUpdate = 0;
    private long lastYellowUpdate = 0;
    private long lastGreenUpdate = 0;

    public TrafficLightBlinkingService(TrafficLightModel model) {
        this.model = model;

        // Timer to update the blinking logic at a fixed interval (20ms)
        this.swingTimer = new Timer(20, e -> updateBlinkingState());
    }

    /** Starts the blinking timer. */
    public void start() {
        LOGGER.log(Level.INFO, "Starting blinking service.");
        this.swingTimer.start();
    }

    /** Stops the blinking timer. */
    public void stop() {
        LOGGER.log(Level.INFO, "Stopping blinking service.");
        this.swingTimer.stop();
    }

    /**
     * Checks if the red LED should be shown as ON (blinking or static).
     *
     * @return true if the red LED is ON, false otherwise.
     */
    public boolean isRedVisible() {
        return this.model.getRedDelay() > 0 ? this.blinkingRed : this.model.isRedOn();
    }

    /**
     * Checks if the yellow LED should be shown as ON (blinking or static).
     *
     * @return true if the yellow LED is ON, false otherwise.
     */
    public boolean isYellowVisible() {
        return this.model.getYellowDelay() > 0 ? this.blinkingYellow : this.model.isYellowOn();
    }

    /**
     * Checks if the green LED should be shown as ON (blinking or static).
     *
     * @return true if the green LED is ON, false otherwise.
     */
    public boolean isGreenVisible() {
        return this.model.getGreenDelay() > 0 ? this.blinkingGreen : this.model.isGreenOn();
    }

    /** Updates the blinking state for each LED based on the delays configured in the model. */
    private void updateBlinkingState() {
        long currentTime = System.currentTimeMillis();

        // Update blinking states locally (without modifying the model)
        boolean redStateChanged = updateRedBlinking(currentTime);
        boolean yellowStateChanged = updateYellowBlinking(currentTime);
        boolean greenStateChanged = updateGreenBlinking(currentTime);

        // Trigger UI updates only if a state changed
        if (redStateChanged || yellowStateChanged || greenStateChanged) {
            this.model.notifyBlinkingStateChanged();
        }
    }

    private boolean updateRedBlinking(long currentTime) {
        int redDelay = this.model.getRedDelay();
        if (redDelay > 0) {
            if (currentTime - this.lastRedUpdate >= redDelay) {
                this.blinkingRed = !this.blinkingRed;
                this.lastRedUpdate = currentTime;
                return true;
            }
        } else {
            this.blinkingRed = this.model.isRedOn(); // Use static toggle state
        }
        return false;
    }

    private boolean updateYellowBlinking(long currentTime) {
        int yellowDelay = this.model.getYellowDelay();
        if (yellowDelay > 0) {
            if (currentTime - this.lastYellowUpdate >= yellowDelay) {
                this.blinkingYellow = !this.blinkingYellow;
                this.lastYellowUpdate = currentTime;
                return true;
            }
        } else {
            this.blinkingYellow = this.model.isYellowOn(); // Use static toggle state
        }
        return false;
    }

    private boolean updateGreenBlinking(long currentTime) {
        int greenDelay = this.model.getGreenDelay();
        if (greenDelay > 0) {
            if (currentTime - this.lastGreenUpdate >= greenDelay) {
                this.blinkingGreen = !this.blinkingGreen;
                this.lastGreenUpdate = currentTime;
                return true;
            }
        } else {
            this.blinkingGreen = this.model.isGreenOn(); // Use static toggle state
        }
        return false;
    }
}
