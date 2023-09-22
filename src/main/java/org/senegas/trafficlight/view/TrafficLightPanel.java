package org.senegas.trafficlight.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.senegas.trafficlight.model.TrafficLightModel;

public class TrafficLightPanel extends JPanel {

    public TrafficLightPanel() {
        super(new BorderLayout());

        final TrafficLightModel model = new TrafficLightModel();
        final TrafficLightView view = new TrafficLightView();
        final SerialMessageEmitter anotherView = new SerialMessageEmitter();
        final TrafficLightController controller = new TrafficLightController(model, view);

        view.setModel(model);
        anotherView.setModel(model);

        this.add(view, BorderLayout.EAST);
        this.add(controller, BorderLayout.CENTER);
    }

}
