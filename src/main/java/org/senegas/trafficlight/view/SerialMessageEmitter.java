package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.serial.CommPort;
import org.senegas.trafficlight.serial.CommPortException;
import org.senegas.trafficlight.serial.CommPortSelector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerialMessageEmitter implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(SerialMessageEmitter.class.getName());
    private TrafficLightModel model;
    private CommPort port;

    public SerialMessageEmitter() {
        initSerialPort();
    }

    public void setModel(TrafficLightModel model) {
        assert model != null : "model should not be null";
        this.model = model;
        this.model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.port.send(this.model.toArduinoCommand());
    }

    private void initSerialPort() {
        try {
            this.port = CommPortSelector.INSTANCE.select();
        } catch (CommPortException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
}
