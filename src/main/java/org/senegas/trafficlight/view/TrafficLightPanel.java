package org.senegas.trafficlight.view;

import org.senegas.trafficlight.model.TrafficLightModel;

import javax.swing.*;
import java.awt.*;

public class TrafficLightPanel extends JPanel {

    public TrafficLightPanel() {
        super(new BorderLayout());

        final TrafficLightModel model = new TrafficLightModel();
        final TrafficLightView view = new TrafficLightView();
        final ArduinoMessageEmitter anotherView = new ArduinoMessageEmitter();
        final TrafficLightController controller = new TrafficLightController(model, view);

        view.setModel(model);
        anotherView.setModel(model);

        this.add(view, BorderLayout.EAST);
        this.add(controller, BorderLayout.CENTER);
    }

}
