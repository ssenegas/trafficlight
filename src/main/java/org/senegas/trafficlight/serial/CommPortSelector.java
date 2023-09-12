package org.senegas.trafficlight.serial;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    void dumpPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        List<SerialPort> list = Arrays.asList(ports);

        System.err.println(list.size()+" ports found:");

        for (SerialPort port : list) {
            System.err.println("["+port.getSystemPortName()
                    + "][" + port.getDescriptivePortName()
                    + "][" + port.getSystemPortPath()
                    + "][" + port.getPortLocation() + "]"
            );
        }
    }

    public CommPort select() throws CommPortException {

        SerialPort[] ports = SerialPort.getCommPorts();

        Optional<SerialPort> opt = Arrays.stream(ports)
                .filter(port -> port.getDescriptivePortName().contains("CH340")).findFirst();

        if (opt.isEmpty()) {
            opt = Arrays.stream(ports)
                    .filter(p -> p.getDescriptivePortName().contains("USB")).findFirst();
        }

        if (opt.isEmpty()) {
            dumpPorts();
            throw new CommPortException("Could not found a connected Arduino");
        }

        SerialPort port = opt.get();

        System.out.println("Selected port [" + port.getSystemPortName()+ "]");
        return new CommPort(port);
    }

}
