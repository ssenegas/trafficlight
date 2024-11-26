
// Use version 0.2.1
#include <Ardwloop.h>

void setup() {
  pinMode(2, OUTPUT);
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);

  // Baud value should be set to the same value as on the Java side
  ardw_setup(BAUD_19200);
}

void loop() {
  ardw_loop();

  int ax = ardw_r()->a.x;
  int ay = ardw_r()->a.y;
  int az = ardw_r()->a.z;

  digitalWrite(2, ax == 1 ? HIGH : LOW);
  digitalWrite(3, ay == 1 ? HIGH : LOW);
  digitalWrite(4, az == 1 ? HIGH : LOW);

  delay(99);
}
