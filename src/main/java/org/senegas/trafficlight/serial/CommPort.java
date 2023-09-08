package org.senegas.trafficlight.serial;

import com.fazecast.jSerialComm.SerialPort;

public class CommPort {

    final SerialPort port;

    public CommPort(SerialPort jSerialPort) {
        this.port = jSerialPort;
        boolean opened = jSerialPort.openPort();
        if (!opened) {
            throw new RuntimeException("Could not open port ["+jSerialPort.getDescriptivePortName()+"]");
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
