
// Use version 0.2.1
#include <Ardwloop.h>

int led = LED_BUILTIN;

void setup() {
  pinMode(led, OUTPUT);

  // Baud value should be set to the same as on the Java side
  ardw_setup(BAUD_19200);

}

void loop() {
  ardw_loop();

  int v = ardw_r()->a.v;

  if (v == 1) {
    digitalWrite(LED_BUILTIN, HIGH);
  } else {
    digitalWrite(LED_BUILTIN, LOW);
  }
  delay(99);
}
