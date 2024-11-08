package org.senegas.trafficlight.serial;

public interface ConnectionListener {
    void onDisconnect();
    void onReconnect();
}
