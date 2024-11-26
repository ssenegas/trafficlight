package org.senegas.trafficlight.view;

import org.llschall.ardwloop.ArdwloopStarter;
import org.llschall.ardwloop.IArdwConfig;
import org.llschall.ardwloop.IArdwProgram;
import org.llschall.ardwloop.structure.StructureTimer;
import org.llschall.ardwloop.structure.data.LoopData;
import org.llschall.ardwloop.structure.data.SerialData;
import org.llschall.ardwloop.structure.data.SetupData;
import org.senegas.trafficlight.model.TrafficLightModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerialMessageEmitter implements IArdwProgram, PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(SerialMessageEmitter.class.getName());

    private TrafficLightModel model;

    boolean isRedOn = false;
    boolean isYellowOn = false;
    boolean isGreenOn = false;

    public SerialMessageEmitter() {
        initSerialPort();
    }

    public void setModel(TrafficLightModel model) {
        assert model != null : "model should not be null";
        this.model = model;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
            isRedOn = model.isRedOn();
            isYellowOn = model.isYellowOn();
            isGreenOn = model.isGreenOn();
        }

    private void initSerialPort() {
        ArdwloopStarter.get().start(this, IArdwConfig.BAUD_19200);
        LOGGER.log(Level.INFO, "Ardwloop started.");
    }

    @Override
    public SetupData ardwSetup(SetupData setupData) {
        return new SetupData(new SerialData(0,0,0,0,0,0));
    }

    @Override
    public LoopData ardwLoop(LoopData loopData) {

        StructureTimer.get().delayMs(99);

        int ax = model.isRedOn() ? 1 : 0;
        int ay = model.isYellowOn() ? 1 : 0;
        int az = model.isGreenOn() ? 1 : 0;

        return new LoopData(new SerialData(0,0,0,ax,ay,az));
    }

    @Override
    public int getRc() {
        return 1;
    }

    @Override
    public int getSc() {
        return 1;
    }
}
