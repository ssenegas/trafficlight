package org.senegas.trafficlight.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.llschall.ardwloop.ArdwloopStarter;

public class BuildTest {

    @Test
    public void test() {
        Assertions.assertEquals("0.2.1",ArdwloopStarter.VERSION);
    }
}
