package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimerTask;

class BlinkerLedsTask extends TimerTask {
    private final TrafficLightModel model;

    public BlinkerLedsTask(TrafficLightModel model) {
        this.model = model;
    }

    @Override
    public void run() {
        System.out.println("Task performed on: " +
                LocalDateTime.ofInstant(Instant.ofEpochMilli(scheduledExecutionTime()), ZoneId.systemDefault()));
        toggleLeds();
    }

    private void toggleLeds() {
        toggleRed();
        toggleAmber();
        toggleGreen();
    }

    private void toggleRed() {
        if (model.getRedDelay() > 0) {
            if (model.isRedOn()) {
                model.turnOffRed();
            } else {
                model.turnOnRed();
            }
        }
    }

    private void toggleAmber() {
        if (model.getAmberDelay() > 0) {
            if (model.isAmberOn()) {
                model.turnOffAmber();
            } else {
                model.turnOnAmber();
            }
        }
    }

    private void toggleGreen() {
        if (model.getGreenDelay() > 0) {
            if (model.isGreenOn()) {
                model.turnOffGreen();
            } else {
                model.turnOnGreen();
            }
        }
    }
}
