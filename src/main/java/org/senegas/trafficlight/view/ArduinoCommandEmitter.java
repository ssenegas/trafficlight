package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.serial.CommPort;
import org.senegas.trafficlight.serial.CommPortException;
import org.senegas.trafficlight.serial.CommPortSelector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ArduinoCommandEmitter  implements PropertyChangeListener {

    private TrafficLightModel model;
    private CommPort port;

    public ArduinoCommandEmitter(TrafficLightModel model) {
        this.model = model;
        initSerialPort();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        port.send(model.toArduinoCommand());
    }

    private void initSerialPort() {
        try {
            port = CommPortSelector.get().select();
        } catch (CommPortException e) {
            System.err.println(e);
        }
    }
}
