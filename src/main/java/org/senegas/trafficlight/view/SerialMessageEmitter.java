package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.serial.CommPort;
import org.senegas.trafficlight.serial.CommPortException;
import org.senegas.trafficlight.serial.CommPortSelector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SerialMessageEmitter implements PropertyChangeListener {
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
            this.port = CommPortSelector.get().select();
        } catch (CommPortException e) {
            System.err.println(e);
        }
    }
}
