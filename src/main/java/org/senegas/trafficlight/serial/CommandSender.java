package org.senegas.trafficlight.serial;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandSender implements ConnectionListener {
    private static final Logger LOGGER = Logger.getLogger(CommandSender.class.getName());

    private final ConnectionManager connectionManager;
    private String lastSentCommand;

    public CommandSender(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        connectionManager.addConnectionListener(this);
    }

    public void send(String command) {
        if (connectionManager.isConnected()) {
            int written = connectionManager.getSerialPort().writeBytes((command + "\0").getBytes(), command.length() + 1);
            if (written == -1) {
                LOGGER.log(Level.SEVERE, "There was an error writing to the port.");
            }
            lastSentCommand = command;
        }

    }

    @Override
    public void onDisconnect() {
        LOGGER.log(Level.SEVERE, "Disconnected, will attempt to resend last command on reconnect.");
    }

    @Override
    public void onReconnect() {
        if (lastSentCommand != null) {
            LOGGER.log(Level.INFO, "Resend last command after reconnect.");
            try {
                LOGGER.log(Level.INFO, "Wait seconds in case the Arduino board is booting...");
                Thread.sleep(10_000);
                send(lastSentCommand); // Resend last command after reconnect
                LOGGER.log(Level.INFO, "Last command resent.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
