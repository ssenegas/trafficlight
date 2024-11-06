# trafficlight

This application is designed to control a traffic light signal mounted on an Arduino board.

## Overview

The application periodically sends a GET request to a specified URL that hosts an HTML page reflecting the results of integration tests. This page provides simple text status updates for the traffic lights, such as `Green:Off, Yellow:On, Red:On`, where each color's status can be `On`, `Off`, or `Blinking`.

The response data is parsed into a `TrafficLight` object, which updates the application's model with the current status. This model is reflected across various views, with one view dedicated to sending commands to the Arduino board through serial communication. The GUI primarily serves as a development tool, allowing manual interaction with the model and displaying its state, though the app typically runs minimized in the system tray.

The Arduino firmware interprets commands from the application to control the LEDs' states (on/off/blink). 

## Related Projects

This project directly relates to [RedLight](https://github.com/ssenegas/RedLight), which contains additional code and resources necessary for full functionality.

![image](https://github.com/ssenegas/trafficlight/assets/9662172/ac1c9416-7c4d-4277-b4eb-d46e34ebe32e)
