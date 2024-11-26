package org.senegas.trafficlight.view;

import com.fazecast.jSerialComm.SerialPort;
import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.serial.CommandSender;
import org.senegas.trafficlight.serial.ConnectionManager;
import org.senegas.trafficlight.serial.CommPortException;
import org.senegas.trafficlight.serial.CommPortSelector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerialMessageEmitter implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(SerialMessageEmitter.class.getName());

    private TrafficLightModel model;
    private CommandSender commandSender;

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
        if (this.commandSender != null && ! "blinkingState".equals(evt.getPropertyName())) {
            String command = this.model.toArduinoCommand();
            this.commandSender.send(command);
        }
    }

    private void initSerialPort() {
        try {
            SerialPort serialPort = CommPortSelector.INSTANCE.select();
            serialPort.setBaudRate(9600);  // Set as needed

            this.commandSender = new CommandSender(new ConnectionManager(serialPort));
        } catch (CommPortException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
}
