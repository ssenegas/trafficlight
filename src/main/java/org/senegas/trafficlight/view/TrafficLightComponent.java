package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficLightComponent extends JComponent {
    private static final int TRAFFIC_LIGHT_WIDTH = 50;
    private static final int GAP = 5;
    private boolean blinkyRedLed = false;
    private boolean blinkyYellowLed = false;
    private boolean blinkyGreenLed = false;
    private TrafficLightModel model;
    private Timer timer;

    public TrafficLightComponent() {
    }

    public void setModel(TrafficLightModel model) {
        this.model = model;
        startTimer();
    }

    public void restartTimer() {
        this.timer.cancel();
        startTimer();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        drawTrafficLight(g2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(TRAFFIC_LIGHT_WIDTH, TRAFFIC_LIGHT_WIDTH * 3 + 15);
    }

    private void startTimer() {
        TimerTask task = new BlinkyLedTimerTask(this.model);
        this.timer = new Timer("Blinky timer");
        this.timer.scheduleAtFixedRate(task, 0, 20);
    }

    private void drawTrafficLight(Graphics2D g2) {
        drawTrafficLightBlackBox(g2, 0, 0);
        drawTrafficLightBulbs(g2, 0, 0);
    }

    private void drawTrafficLightBulbs(Graphics2D g2, int xLeft, int yTop) {
        drawRedBulb(g2, xLeft, yTop);
        drawYellowBulb(g2, xLeft, yTop);
        drawGreenBulb(g2, xLeft, yTop);
    }

    private void drawTrafficLightBlackBox(Graphics2D g2, int xLeft, int yTop) {
        Rectangle box = new Rectangle(xLeft, yTop, TRAFFIC_LIGHT_WIDTH, 3 * TRAFFIC_LIGHT_WIDTH - 2 * GAP);
        g2.setColor(Color.BLACK);
        g2.fill(box);
    }

    private void drawRedBulb(Graphics2D g2, int xLeft, int yTop) {
        Ellipse2D.Double redBulb = new Ellipse2D.Double(xLeft + GAP, yTop + GAP, TRAFFIC_LIGHT_WIDTH - 2 * GAP, TRAFFIC_LIGHT_WIDTH - 2 * GAP);
        boolean isRedOn = this.model.isRedOn();
        if (this.model.getRedDelay() > 0) {
            isRedOn = this.blinkyRedLed;
        }
        g2.setColor(isRedOn ? Color.RED.brighter() : Color.RED.darker());
        g2.fill(redBulb);
    }

    private void drawYellowBulb(Graphics2D g2, int xLeft, int yTop) {
        Ellipse2D.Double YellowBulb = new Ellipse2D.Double(xLeft + GAP, yTop + TRAFFIC_LIGHT_WIDTH, TRAFFIC_LIGHT_WIDTH - 2 * GAP, TRAFFIC_LIGHT_WIDTH - 2 * GAP);
        boolean isYellowOn = this.model.isYellowOn();
        if (this.model.getYellowDelay() > 0) {
            isYellowOn = this.blinkyYellowLed;
        }
        g2.setColor(isYellowOn ? Color.YELLOW.brighter() : Color.YELLOW.darker());
        g2.fill(YellowBulb);
    }

    private void drawGreenBulb(Graphics2D g2, int xLeft, int yTop) {
        Ellipse2D.Double greenBulb = new Ellipse2D.Double(xLeft + GAP, yTop + 2 * TRAFFIC_LIGHT_WIDTH - GAP, TRAFFIC_LIGHT_WIDTH - 2 * GAP, TRAFFIC_LIGHT_WIDTH - 2 * GAP);
        boolean isGreenOn = this.model.isGreenOn();
        if (this.model.getGreenDelay() > 0) {
            isGreenOn = this.blinkyGreenLed;
        }
        g2.setColor(isGreenOn ? Color.GREEN.brighter() : Color.GREEN.darker());
        g2.fill(greenBulb);
    }

    private class BlinkyLedTimerTask extends TimerTask {
        final private TrafficLightModel model;
        private long previousRedMillis = 0;
        private long previousYellowMillis = 0;
        private long previousGreenMillis = 0;

        public BlinkyLedTimerTask(TrafficLightModel model) {
            this.model = model;
        }

        @Override
        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            updateRed(currentTimeMillis);
            updateYellow(currentTimeMillis);
            updateGreen(currentTimeMillis);
            repaint();
        }

        private void updateRed(long currentTimeMillis) {
            if (currentTimeMillis - this.previousRedMillis >= this.model.getRedDelay()) {
                TrafficLightComponent.this.blinkyRedLed = !TrafficLightComponent.this.blinkyRedLed;
                this.previousRedMillis = currentTimeMillis;
            }
        }

        private void updateYellow(long currentTimeMillis) {
            if (currentTimeMillis - this.previousYellowMillis >= this.model.getYellowDelay()) {
                TrafficLightComponent.this.blinkyYellowLed = !TrafficLightComponent.this.blinkyYellowLed;
                this.previousYellowMillis = currentTimeMillis;
            }
        }

        private void updateGreen(long currentTimeMillis) {
            if (currentTimeMillis - this.previousGreenMillis >= this.model.getGreenDelay()) {
                TrafficLightComponent.this.blinkyGreenLed = !TrafficLightComponent.this.blinkyGreenLed;
                this.previousGreenMillis = currentTimeMillis;
            }
        }
    }
}
