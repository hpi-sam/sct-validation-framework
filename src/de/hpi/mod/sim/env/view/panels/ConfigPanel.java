package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;

import javax.swing.*;
import java.awt.*;

/**
 * rotationSpeed
 * unloadingTime
 *
 * moveSpeed
 * blockSize
 * offsetX
 * offsetY
 * sensorRefreshInterval
 */
public class ConfigPanel extends JPanel {

//    private JTextField rotationSpeedT, unloadingTimeT, moveSpeedT, blockSizeT,
//            offsetXT, offsetYT, sensorRefreshIntervalT;

    public ConfigPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addConfigElement("Move Speed", "In cells per milliseconds",
                Float.toString(SimulatorConfig.DEFAULT_ROBOT_MOVE_SPEED),
                (v) -> SimulatorConfig.setRobotMoveSpeed(Float.parseFloat(v)));
    }

    private void addConfigElement(String name, String toolTip, String initValue, ValueSetter setter) {
        JPanel root = new JPanel(new BorderLayout());
        JPanel input = new JPanel(new BorderLayout());

        JLabel label = new JLabel(name);
        JTextField textField = new JTextField(initValue);
        JButton button = new JButton("Reset");

        label.setFont(label.getFont().deriveFont(Font.PLAIN));
        textField.setToolTipText(toolTip);

        textField.addActionListener(e -> setter.setValue(textField.getText()));
        button.addActionListener(e -> {
            textField.setText(initValue);
            setter.setValue(initValue);
        });

        input.add(textField, BorderLayout.CENTER);
        input.add(button, BorderLayout.EAST);
        root.add(label, BorderLayout.NORTH);
        root.add(input, BorderLayout.CENTER);

        add(root);
    }

    private interface ValueSetter {
        void setValue(String value);
    }
}
