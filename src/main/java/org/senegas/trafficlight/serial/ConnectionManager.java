package org.senegas.trafficlight.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {

    private static final Logger LOGGER = Logger.getLogger(ConnectionManager.class.getName());

    private SerialPort serialPort;
    private final int BASE_RECONNECT_INTERVAL = 5000; // 5 seconds
    private final int MAX_RECONNECT_INTERVAL = 60000; // 1 minute
    private final int MAX_RETRIES = 10; // Max retry limit

    private final List<ConnectionListener> listeners = new ArrayList<>();

    public ConnectionManager(SerialPort jSerialPort) {
        this.serialPort = jSerialPort;

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    LOGGER.log(
                                            Level.INFO,
                                            "Closing port {0}...",
                                            jSerialPort.getSystemPortPath());
                                    boolean isPortClosed = jSerialPort.closePort();
                                    LOGGER.log(
                                            Level.INFO,
                                            "Closing port has {0}",
                                            (isPortClosed ? "been successful" : "failed"));
                                }));

        connect();
    }

    public void addConnectionListener(ConnectionListener listener) {
        this.listeners.add(listener);
    }

    private void notifyDisconnect() {
        for (ConnectionListener listener : this.listeners) {
            listener.onDisconnect();
        }
    }

    private void notifyReconnect() {
        for (ConnectionListener listener : this.listeners) {
            listener.onReconnect();
        }
    }

    public void connect() {
        if (this.serialPort.openPort()) {
            // https://fazecast.github.io/jSerialComm/javadoc/com/fazecast/jSerialComm/SerialPortDataListener.html
            // https://github.com/Fazecast/jSerialComm/wiki/Event-Based-Reading-Usage-Example
            this.serialPort.addDataListener(
                    new SerialPortDataListener() {
                        @Override
                        public int getListeningEvents() {
                            return SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
                        }

                        @Override
                        public void serialEvent(SerialPortEvent event) {
                            switch (event.getEventType()) {
                                // https://github.com/Fazecast/jSerialComm/wiki/Modes-of-Operation#for-port-disconnects
                                case SerialPort.LISTENING_EVENT_PORT_DISCONNECTED:
                                    LOGGER.log(
                                            Level.INFO,
                                            "Port disconnected. Attempting to reconnect...");

                                    handlePortDisconnection();
                                    break;
                            }
                        }
                    });
        }
    }

    public boolean isConnected() {
        return this.serialPort.isOpen();
    }

    public SerialPort getSerialPort() {
        return this.serialPort;
    }

    private void handlePortDisconnection() {
        this.serialPort.closePort();
        notifyDisconnect();
        int reconnectInterval = this.BASE_RECONNECT_INTERVAL;
        int attemptCount = 0;

        while (!this.serialPort.isOpen() && attemptCount < this.MAX_RETRIES) {
            try {
                Thread.sleep(reconnectInterval);
                LOGGER.log(Level.INFO, "Attempting to reconnect...");
                if (this.serialPort.openPort()) {
                    LOGGER.log(Level.INFO, "Reconnected successfully.");
                    reconnectInterval = this.BASE_RECONNECT_INTERVAL; // Reset interval on success
                    notifyReconnect();
                } else {
                    reconnectInterval =
                            Math.min(
                                    reconnectInterval * 2,
                                    this.MAX_RECONNECT_INTERVAL); // Exponential backoff
                    LOGGER.log(
                            Level.INFO,
                            "Reconnection failed. Next attempt in "
                                    + reconnectInterval / 1000
                                    + " seconds.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.SEVERE, "Reconnect attempt interrupted.", e);
                break;
            }
            attemptCount++;
        }

        if (!this.serialPort.isOpen()) {
            LOGGER.log(
                    Level.SEVERE, "Failed to reconnect after " + this.MAX_RETRIES + " attempts.");
        }
    }
}
