package org.senegas.trafficlight.serial;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum CommPortSelector {
	
	INSTANCE;

	private static final Logger LOGGER = Logger.getLogger(CommPortSelector.class.getName());

    private CommPortSelector() {
        // hide constructor
    }

    void dumpPorts() {
        SerialPort[] commPorts = SerialPort.getCommPorts();
        List<SerialPort> list = Arrays.asList(commPorts);
        LOGGER.log(Level.INFO, "{0} port(s) found:", list.size());

        for (SerialPort port : list) {
        	LOGGER.log(Level.INFO, "{0} {1} {2} {3}", 
        			new Object[] { port.getSystemPortName(), port.getDescriptivePortName(),
        					               port.getSystemPortPath(), port.getPortLocation() });
        }
    }

    public CommPort select() throws CommPortException {

        SerialPort[] commPorts = SerialPort.getCommPorts();

        Optional<SerialPort> opt = Arrays.stream(commPorts)
                .filter(port -> port.getDescriptivePortName().contains("CH340"))
                .findFirst();

        if (opt.isEmpty()) {
            opt = Arrays.stream(commPorts)
                    .filter(p -> p.getDescriptivePortName().contains("USB"))
                    .findFirst();
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
