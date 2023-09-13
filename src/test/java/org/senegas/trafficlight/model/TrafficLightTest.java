package org.senegas.trafficlight.model;

import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLightTest {

    static final private MessageFormat form = new MessageFormat("Green:{0}, Yellow:{1}, Red:{2}");

    @Test
    void givenInputToParseShouldCreateModelWithLedTrulyInitialized() {
        // given
        String[] values = { "On", "Off", "On" };

        //when
        TrafficLight trafficLight = TrafficLight.parse(form.format(values));

        //then
        assertTrue(trafficLight.isRedOn());
        assertFalse(trafficLight.isAmberOn());
        assertTrue(trafficLight.isGreenOn());
    }

    @Test
    void givenIllegalInputToParseShouldThrowException() {
        String[] values = { "foo", "bar", "baz" };

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                TrafficLight.parse(form.format(values)));

        String expectedMessage = "Given argument does not match";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}