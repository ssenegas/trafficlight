# trafficlight
This small app is used to pilot a traffic-light signal mounted on an Arduino board.

It periodically polls a URL where an HTML page is refreshed according to integration test results. This web page provides simple text. (e.g. `Green:Off, Yellow:On, Red:On`). The status values can be On, Off, or Blinking.

This text is then parsed to create a new traffic-light model that will replace the current one. The different views reflect the model state. One of these views is responsible for sending a command to the Arduino board using serial port communication. GUI is a helper while developing to interact manually with the model and reflect its state. Most of the time, the app is running minimized in the tray icon.

Then, the Arduino firmware will decode the command to turn on/off/blink the LEDs. This project is in direct relation with RedLight(https://github.com/ssenegas/RedLight) project.

![image](https://github.com/ssenegas/trafficlight/assets/9662172/ac1c9416-7c4d-4277-b4eb-d46e34ebe32e)
