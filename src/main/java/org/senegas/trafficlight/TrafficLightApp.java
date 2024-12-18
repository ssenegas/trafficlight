/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.senegas.trafficlight;

import com.formdev.flatlaf.FlatIntelliJLaf;

import org.senegas.trafficlight.model.TrafficLightModel;
import org.senegas.trafficlight.view.TrafficLightFrame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.*;

public class TrafficLightApp {

    private static final Logger LOGGER = Logger.getLogger(TrafficLightApp.class.getName());

    public static final String TITLE = "Traffic Light App";
    // TODO
    // // see
    // https://stackoverflow.com/questions/33020069/how-to-get-version-attribute-from-a-gradle-build-to-be-included-in-runtime-swing
    public static final String VERSION = "2.1.0";

    static {
        try {
            // Ensure the logs directory exists
            Files.createDirectories(Paths.get("logs"));

            // Load the custom logging configuration from resources
            InputStream loggingConfig =
                    TrafficLightApp.class
                            .getClassLoader()
                            .getResourceAsStream("logging.properties");
            if (loggingConfig == null) {
                LOGGER.severe("Logging configuration file not found.");
                throw new RuntimeException("Logging configuration is required.");
            }
            LogManager.getLogManager().readConfiguration(loggingConfig);
        } catch (IOException e) {
            LOGGER.severe("Failed to load logging configuration: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        final String asciiArtTitle =
                "\n _____           __  __ _      _     _       _     _   \n"
                        + "|_   _| __ __ _ / _|/ _(_) ___| |   (_) __ _| |__ | |_ \n"
                        + "  | || '__/ _` | |_| |_| |/ __| |   | |/ _` | '_ \\| __|\n"
                        + "  | || | | (_| |  _|  _| | (__| |___| | (_| | | | | |_ \n"
                        + "  |_||_|  \\__,_|_| |_| |_|\\___|_____|_|\\__, |_| |_|\\__|\n"
                        + "                                       |___/           ";
        LOGGER.log(Level.INFO, asciiArtTitle);
        LOGGER.log(Level.INFO, TITLE + " has started.");

        EventQueue.invokeLater(
                () -> {
                    new TrafficLightApp().create();
                });
    }

    private void create() {
        FlatIntelliJLaf.setup();

        final TrafficLightModel model = new TrafficLightModel();

        final String title = MessageFormat.format("{0} v{1}", TITLE, VERSION);
        final JFrame f = new TrafficLightFrame(title, model);

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        ((TrafficLightFrame) f).dispose();
                    }
                });
        f.setPreferredSize(new Dimension(350, 180));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
