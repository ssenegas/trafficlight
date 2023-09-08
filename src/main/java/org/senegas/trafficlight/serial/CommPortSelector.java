package org.senegas.trafficlight.serial;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommPortSelector {

    // Singleton pattern
    private final static CommPortSelector INSTANCE = new CommPortSelector();

    private CommPortSelector() {
        // hide constructor
    }

    public static CommPortSelector get() {
        return INSTANCE;
    }

    public List<String> listPorts() {
        return Arrays.stream(SerialPort.getCommPorts())
                .map(SerialPort::getDescriptivePortName)
                .collect(Collectors.toList());
    }

    public CommPort select(String descriptivePortName) {

        SerialPort port = SerialPort.getCommPort(descriptivePortName);
        if (port==null) {
            throw new RuntimeException("Invalid port name: ["+descriptivePortName+"]");
        }
        return new CommPort(port);
    }

}
