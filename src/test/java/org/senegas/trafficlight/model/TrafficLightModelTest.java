package org.senegas.trafficlight.model;

import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLightModelTest {

    static final private MessageFormat form = new MessageFormat("Green:{0}, Yellow:{1}, Red:{2}");

    @Test
    void givenInputToMakeShouldCreateModelWithLedTrulyInitialized() {
        // given
        String[] values = { "On", "Off", "On" };

        //when
        TrafficLightModel model = TrafficLightModel.parse(form.format(values));

        //then
        assertTrue(model.isRedOn());
        assertFalse(model.isAmberOn());
        assertTrue(model.isGreenOn());
    }
}