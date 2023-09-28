package org.senegas.trafficlight.serial;

import com.fazecast.jSerialComm.SerialPort;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommPort {
    private static final Logger LOGGER = Logger.getLogger(CommPort.class.getName());

    private final SerialPort serialPort;

    public CommPort(SerialPort jSerialPort) {
        this.serialPort = jSerialPort;

        openPort();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.log(Level.INFO, "Closing port {0}...", jSerialPort.getSystemPortPath());
            boolean isPortClosed = jSerialPort.closePort();
            LOGGER.log(Level.INFO, "Closing port has {0}", (isPortClosed ? "been successful" : "failed"));
        }));

        this.serialPort.setBaudRate(ICommPortConfig.BAUD_RATE);
    }

    private void openPort() {
        if (! this.serialPort.openPort()) {
            throw new RuntimeException("Could not open port ["+ this.serialPort.getDescriptivePortName()+"] "+ this.serialPort.getSystemPortPath());
        }
    }

    public void send(String command) {
        int written = this.serialPort.writeBytes((command + "\n").getBytes(),
        		command.length() + 1);
        if (written == -1) {
            LOGGER.log(Level.SEVERE, "Write error");
        }
    }
}
