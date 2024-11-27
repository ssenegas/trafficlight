package org.senegas.trafficlight.model;

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLightTest {

    @Test
    void givenDefaultConstructorShouldInitializeAllLedsOffAndNoDelay() {
        // when
        TrafficLight trafficLight = new TrafficLight();

        // then
        assertFalse(trafficLight.getBulb(Light.RED).isOn());
        assertFalse(trafficLight.getBulb(Light.YELLOW).isOn());
        assertFalse(trafficLight.getBulb(Light.GREEN).isOn());
        assertEquals(0, trafficLight.getBulb(Light.RED).getDelay());
        assertEquals(0, trafficLight.getBulb(Light.YELLOW).getDelay());
        assertEquals(0, trafficLight.getBulb(Light.GREEN).getDelay());
    }

    @Test
    void givenInputToParseShouldCreateModelWithLedTrulyInitialized() {
        // given
        String[] values = { "On", "Off", "On" };

        // when
        TrafficLight trafficLight = TrafficLight.parse(
                "Green:On, Yellow:Off, Red:On"
        );

        // then
        assertTrue(trafficLight.getBulb(Light.GREEN).isOn());
        assertFalse(trafficLight.getBulb(Light.YELLOW).isOn());
        assertTrue(trafficLight.getBulb(Light.GREEN).isOn());
    }

    @Test
    void givenInputToParseShouldInitializeBlinkingLedsWithDelays() {
        // given
        String input = "Green:Blinking, Yellow:Off, Red:Blinking";

        // when
        TrafficLight trafficLight = TrafficLight.parse(input);

        // then
        assertEquals(1000, trafficLight.getBulb(Light.GREEN).getDelay());
        assertEquals(0, trafficLight.getBulb(Light.YELLOW).getDelay());
        assertEquals(1000, trafficLight.getBulb(Light.RED).getDelay());
    }

    @Test
    void givenIllegalInputToParseShouldThrowException() {
        // given
        String input = "foo";

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                TrafficLight.parse(input)
        );

        // then
        assertTrue(exception.getMessage().contains("Invalid input format"));
    }

    @Test
    void shouldTurnOnAndOffLedsCorrectly() {
        // given
        TrafficLight trafficLight = new TrafficLight();

        // when
        trafficLight.getBulb(Light.RED).turnOn();
        trafficLight.getBulb(Light.GREEN).turnOff();

        // then
        assertTrue(trafficLight.getBulb(Light.RED).isOn());
        assertFalse(trafficLight.getBulb(Light.GREEN).isOn());
    }

    @Test
    void shouldSetAndRetrieveDelayForLeds() {
        // given
        TrafficLight trafficLight = new TrafficLight();

        // when
        trafficLight.getBulb(Light.RED).setBlinking(1500);
        trafficLight.getBulb(Light.YELLOW).setBlinking(3000);
        trafficLight.getBulb(Light.GREEN).setBlinking(500);

        // then
        assertEquals(1500, trafficLight.getBulb(Light.RED).getDelay());
        assertEquals(3000, trafficLight.getBulb(Light.YELLOW).getDelay());
        assertEquals(500, trafficLight.getBulb(Light.GREEN).getDelay());
    }

    @Test
    void shouldGenerateCorrectArduinoCommand() {
        // given
        TrafficLight trafficLight = TrafficLight.parse("Green:On, Yellow:Off, Red:Blinking");
        trafficLight.getBulb(Light.RED).setBlinking(1000);

        // when
        String arduinoCommand = trafficLight.toArduinoCommand();

        // then
        assertEquals("r1000y0000G0000", arduinoCommand);
    }

    @Test
    void trafficLightInstancesShouldBeEqualWhenStatesMatch() {
        // given
        TrafficLight t1 = TrafficLight.parse("Green:On, Yellow:Off, Red:On");
        TrafficLight t2 = TrafficLight.parse("Green:On, Yellow:Off, Red:On");

        // then
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void trafficLightInstancesShouldNotBeEqualWhenStatesDiffer() {
        // given
        TrafficLight t1 = TrafficLight.parse("Green:On, Yellow:Off, Red:On");
        TrafficLight t2 = TrafficLight.parse("Green:Off, Yellow:Off, Red:On");

        // then
        assertNotEquals(t1, t2);
    }
}
