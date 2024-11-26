package org.senegas.trafficlight.view;

import org.llschall.ardwloop.ArdwloopStarter;
import org.llschall.ardwloop.IArdwConfig;
import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.TrafficLightProgram;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerialMessageEmitter implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(SerialMessageEmitter.class.getName());

    private TrafficLightModel model;

    private final TrafficLightProgram program = new TrafficLightProgram();

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
            String command = this.model.toArduinoCommand();
            program.arduinoCmd.set(command);
        }

    private void initSerialPort() {
        ArdwloopStarter.get().start(program, IArdwConfig.BAUD_19200);
        LOGGER.log(Level.INFO, "Ardwloop started.");
    }
}
