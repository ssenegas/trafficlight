package org.senegas.trafficlight.serial;

import com.fazecast.jSerialComm.SerialPort;

public class CommPort {

    final SerialPort jSerialPort;

    public CommPort(SerialPort jSerialPort) {
        this.jSerialPort = jSerialPort;
    }
}
