package org.senegas.trafficlight.view;

import net.miginfocom.swing.MigLayout;

import org.apache.hc.core5.net.URIBuilder;
import org.senegas.trafficlight.TrafficLightApp;
import org.senegas.trafficlight.model.TrafficLightModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

/**
 * View-Controller class (UI Delegate) that handles UI and events Specialized with system tray icon
 * see <a href="https://docs.oracle.com/javase/tutorial/uiswing/misc/systemtray.html">systemtray</a>
 */
public class TrafficLightFrame extends JFrame implements PropertyChangeListener {
    private static final Logger LOGGER = Logger.getLogger(TrafficLightFrame.class.getName());

    private static final String INFO_URL_LIGHT_KEY = "info.url.light";
    private static final String FOUR_LETTER_CODE_KEY = "4lc";
    private static final String PERIOD_KEY = "period";
    private static final String DEFAULT_URL = "https://info.swiss-as.com/light.txt";
    private static final String DEFAULT_PERIOD = "60";

    private final TrafficLightController controller;
    private final Properties appProperties;

    private JToggleButton redButton;
    private JToggleButton yellowButton;
    private JToggleButton greenButton;
    private JSpinner redSpinner;
    private JSpinner yellowSpinner;
    private JSpinner greenSpinner;

    private TrafficLightComponent trafficLightComponent;

    private TrayIcon trayIcon;
    private SystemTray tray;

    public TrafficLightFrame(String title, TrafficLightModel model) throws HeadlessException {
        super(title);
        setLayout(new BorderLayout());
        this.appProperties = loadAppProperties();
        this.controller = new TrafficLightController(model);
        model.addPropertyChangeListener(this);

        initComponents();
        initSerialMessageEmitter();
        createTrayIcon();

        long period = Long.parseLong(this.appProperties.getProperty(PERIOD_KEY, DEFAULT_PERIOD));
        this.controller.startPolling(getLightURL(), period);
    }

    @Override
    public void dispose() {
        this.controller.stop();
        this.trafficLightComponent.stop();
        this.controller.getModel().removePropertyChangeListener(this);
        super.dispose();
    }

    private void initSerialMessageEmitter() {
        final SerialMessageEmitter serialMessageEmitter = new SerialMessageEmitter();
        serialMessageEmitter.setModel(this.controller.getModel());
    }

    private void initComponents() {
        final JPanel actionPanel = new JPanel(new MigLayout());

        this.redButton = addLabeledToggleButton(actionPanel, "Red");
        this.redButton.addItemListener(
                e -> this.controller.handleRedToggle(e.getStateChange() == ItemEvent.SELECTED));

        this.redSpinner = addSpinner(actionPanel);
        this.redSpinner.addChangeListener(
                e -> this.controller.handleRedDelayChange((Integer) this.redSpinner.getValue()));

        this.yellowButton = addLabeledToggleButton(actionPanel, "Yellow");
        this.yellowButton.addItemListener(
                e -> this.controller.handleYellowToggle(e.getStateChange() == ItemEvent.SELECTED));

        this.yellowSpinner = addSpinner(actionPanel);
        this.yellowSpinner.addChangeListener(
                e ->
                        this.controller.handleYellowDelayChange(
                                (Integer) this.yellowSpinner.getValue()));

        this.greenButton = addLabeledToggleButton(actionPanel, "Green");
        this.greenButton.addItemListener(
                e -> this.controller.handleGreenToggle(e.getStateChange() == ItemEvent.SELECTED));

        this.greenSpinner = addSpinner(actionPanel);
        this.greenSpinner.addChangeListener(
                e ->
                        this.controller.handleGreenDelayChange(
                                (Integer) this.greenSpinner.getValue()));

        this.trafficLightComponent = new TrafficLightComponent(this.controller.getModel());

        this.add(actionPanel, BorderLayout.CENTER);
        this.add(this.trafficLightComponent, BorderLayout.EAST);
    }

    private Properties loadAppProperties() {
        final Properties appProps = new Properties();
        try (InputStream resourceAsStream =
                TrafficLightController.class
                        .getClassLoader()
                        .getResourceAsStream("app.properties")) {
            appProps.load(resourceAsStream);
        } catch (IOException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Something went wrong when reading application properties: {0}",
                    e.getMessage());
        }
        return appProps;
    }

    private URL getLightURL() {
        try {
            URIBuilder uriBuilder =
                    new URIBuilder(this.appProperties.getProperty(INFO_URL_LIGHT_KEY, DEFAULT_URL));
            String letterCode = this.appProperties.getProperty(FOUR_LETTER_CODE_KEY);
            if (letterCode != null) {
                uriBuilder.addParameter("user", letterCode);
            }
            // Call build() to create the final URI
            return uriBuilder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "Critical error: Unable to construct the URL.", e);
            throw new IllegalStateException("Application cannot proceed without a valid URL.", e);
        }
    }

    private JToggleButton addLabeledToggleButton(Container c, String textLabel) {
        final JLabel label = new JLabel(textLabel);
        c.add(label);
        final JToggleButton toggleButton = new JToggleButton(textLabel);
        label.setLabelFor(toggleButton);
        c.add(toggleButton, "grow");
        return toggleButton;
    }

    private JSpinner addSpinner(Container c) {
        final SpinnerModel spinnerNumberModel = new SpinnerNumberModel(0, 0, 5000, 500);
        final JSpinner spinner = new JSpinner(spinnerNumberModel);
        c.add(spinner);
        c.add(new JLabel("ms"), "wrap");
        return spinner;
    }

    private void createTrayIcon() {
        if (!SystemTray.isSupported()) {
            LOGGER.log(Level.CONFIG, "SystemTray is not supported");
            return;
        }

        this.tray = SystemTray.getSystemTray();
        this.trayIcon =
                new TrayIcon(
                        createImage("/images/Traffic_lights_icon.gif", "tray icon"),
                        "TrafficLight",
                        createTrayIconPopupMenu());
        this.trayIcon.setImageAutoSize(true);

        addWindowStateListener(this::handleWindowState);
        setIconImage(createImage("/images/Traffic_lights_icon256.png", "icon"));
    }

    private void handleWindowState(WindowEvent windowsEvent) {
        if (windowsEvent.getNewState() == ICONIFIED) {
            try {
                this.tray.add(this.trayIcon);
                setVisible(false);
                LOGGER.log(Level.CONFIG, "TrafficLight added to SystemTray");
            } catch (AWTException e) {
                LOGGER.log(Level.SEVERE, "TrayIcon could not be added.");
            }
        }
        if (windowsEvent.getNewState() == 7) {
            try {
                this.tray.add(this.trayIcon);
                setVisible(false);
                LOGGER.log(Level.CONFIG, "TrafficLight added to SystemTray");
            } catch (AWTException e) {
                LOGGER.log(Level.SEVERE, "TrayIcon could not be added.");
            }
        }
        if (windowsEvent.getNewState() == MAXIMIZED_BOTH) {
            this.tray.remove(this.trayIcon);
            setVisible(true);
            LOGGER.log(Level.CONFIG, "TrayIcon could not be removed.");
        }
        if (windowsEvent.getNewState() == NORMAL) {
            this.tray.remove(this.trayIcon);
            setVisible(true);
            LOGGER.log(Level.CONFIG, "TrayIcon could not be removed.");
        }
    }

    private PopupMenu createTrayIconPopupMenu() {
        final PopupMenu popup = new PopupMenu();

        MenuItem aboutItem = new MenuItem("About");
        MenuItem showItem = new MenuItem("Show");
        MenuItem exitItem = new MenuItem("Exit");

        popup.add(aboutItem);
        popup.add(showItem);
        popup.addSeparator();
        popup.add(exitItem);

        aboutItem.addActionListener(this::handleAboutActionListener);
        showItem.addActionListener(this::handleShowActionListener);
        exitItem.addActionListener(this::handleExitAction);

        return popup;
    }

    private void handleShowActionListener(ActionEvent actionEvent) {
        setVisible(true);
        setExtendedState(JFrame.NORMAL);
    }

    private void handleAboutActionListener(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(null, getTitle(), "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleExitAction(ActionEvent actionEvent) {
        this.tray.remove(this.trayIcon);
        System.exit(0);
    }

    protected static Image createImage(String path, String description) {
        URL imageURL = TrafficLightApp.class.getResource(path);
        if (imageURL == null) {
            LOGGER.log(Level.SEVERE, "Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("traffic-light".equals(evt.getPropertyName())) {
            SwingUtilities.invokeLater(
                    () -> {
                        updateButtonsFromModel();
                        updateSpinnersFromModel();
                    });
        }
    }

    private void updateButtonsFromModel() {
        this.redButton.setSelected(this.controller.getModel().isRedOn());
        this.yellowButton.setSelected(this.controller.getModel().isYellowOn());
        this.greenButton.setSelected(this.controller.getModel().isGreenOn());
    }

    private void updateSpinnersFromModel() {
        this.redSpinner.setValue(this.controller.getModel().getRedDelay());
        this.yellowSpinner.setValue(this.controller.getModel().getYellowDelay());
        this.greenSpinner.setValue(this.controller.getModel().getGreenDelay());
    }
}
