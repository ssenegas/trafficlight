package org.senegas.trafficlight.serial;

import com.fazecast.jSerialComm.SerialPort;

public class CommPort {

    final SerialPort port;

    public CommPort(SerialPort jSerialPort) {
        port = jSerialPort;

        boolean opened = port.openPort();
        if (!opened) {
            throw new RuntimeException("Could not open port ["+port.getDescriptivePortName()+"] "+port.getSystemPortPath());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing port " + jSerialPort.getDescriptivePortName());
            jSerialPort.closePort();
        }));

        port.setBaudRate(9600);
    }

    public void send(String msg) {

        char[] chars = msg.toCharArray();
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }

        port.writeBytes(bytes, bytes.length);
    }

}
