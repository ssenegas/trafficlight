package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.serial.CommPort;
import org.senegas.trafficlight.serial.CommPortException;
import org.senegas.trafficlight.serial.CommPortSelector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ArduinoMessageEmitter implements PropertyChangeListener {
    private TrafficLightModel model;
    private CommPort port;

    public ArduinoMessageEmitter() {
        initSerialPort();
    }

    public void setModel(TrafficLightModel model) {
        this.model = model;
        if (this.model != null) {
            this.model.addPropertyChangeListener(this);
        } else {
            throw new NullPointerException("Given model is null");
        }
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
