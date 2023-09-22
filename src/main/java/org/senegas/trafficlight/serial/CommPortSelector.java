package org.senegas.trafficlight.serial;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fazecast.jSerialComm.SerialPort;

public enum CommPortSelector {
	
	INSTANCE;

	private static final Logger LOGGER = Logger.getLogger(CommPortSelector.class.getName());

    private CommPortSelector() {
        // hide constructor
    }

    public static CommPortSelector get() {
        return INSTANCE;
    }

    void dumpPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        List<SerialPort> list = Arrays.asList(ports);
        
        LOGGER.log(Level.INFO, "{0} port(s) found:",
        		list.size());

        for (SerialPort port : list) {
        	LOGGER.log(Level.INFO, "{0} {1} {2} {3}", 
        			new Object[] { port.getSystemPortName(), port.getDescriptivePortName(),
        					               port.getSystemPortPath(), port.getPortLocation() });
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
            throw new CommPortException("Could not find a connected Arduino");
        }

        SerialPort port = opt.get();

        LOGGER.log(Level.INFO, "Arduino detected on port {0}", port.getSystemPortName());
        return new CommPort(port);
    }

}
