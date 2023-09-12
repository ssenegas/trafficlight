package org.senegas.trafficlight.model;

import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLightModelTest {

    static final private MessageFormat form = new MessageFormat("Green:{0}, Yellow:{1}, Red:{2}");

    @Test
    void givenInputToParseShouldCreateModelWithLedTrulyInitialized() {
        // given
        String[] values = { "On", "Off", "On" };

        //when
        TrafficLightModel model = TrafficLightModel.parse(form.format(values));

        //then
        assertTrue(model.isRedOn());
        assertFalse(model.isAmberOn());
        assertTrue(model.isGreenOn());
    }

    @Test
    void givenIllegalInputToParseShouldThrowException() {
        String[] values = { "foo", "bar", "baz" };

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                TrafficLightModel.parse(form.format(values)));

        String expectedMessage = "Given argument does not match";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}