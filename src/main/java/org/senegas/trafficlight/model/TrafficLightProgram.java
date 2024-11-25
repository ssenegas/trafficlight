package org.senegas.trafficlight.model;

import org.llschall.ardwloop.IArdwProgram;
import org.llschall.ardwloop.structure.data.LoopData;
import org.llschall.ardwloop.structure.data.SerialData;
import org.llschall.ardwloop.structure.data.SetupData;

public class TrafficLightProgram implements IArdwProgram {

    @Override
    public SetupData ardwSetup(SetupData setupData) {
        return new SetupData(new SerialData(0,0,0,0,0,0));
    }

    @Override
    public LoopData ardwLoop(LoopData loopData) {
        return new LoopData(new SerialData(0,0,0,0,0,0));
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
