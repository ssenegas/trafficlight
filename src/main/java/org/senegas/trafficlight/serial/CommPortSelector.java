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
        SerialPort[] ports = SerialPort.getCommPorts();
        List<SerialPort> list = Arrays.asList(ports);

        System.out.println(list.size()+" ports found:");

        for (SerialPort port : list) {
            System.out.println("["+port.getSystemPortName()
                    + "][" + port.getDescriptivePortName()
                    + "][" + port.getSystemPortPath()
                    + "][" + port.getPortLocation() + "]"
            );
        }

        return list.stream()
                .map(SerialPort::getSystemPortName)
                .collect(Collectors.toList());
    }

    public CommPort select(String portName) {

        SerialPort port = SerialPort.getCommPort(portName);
        if (port==null) {
            throw new RuntimeException("Invalid port name: ["+portName+"]");
        }
        System.out.println("Selected port: " + portName);
        return new CommPort(port);
    }

}
