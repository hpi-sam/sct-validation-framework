package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Panel that lets the user set und reset configurations.<br>
 * Currently supports:
 * <ul>
 *     <li>Move Speed</li>
 * </ul>
 *
 * No other class must change the values, since this class does not listen to changes
 */
public class ConfigPanel extends JPanel {


    /**
     * Initializes the Panel and adds Config Elements
     */
    public ConfigPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addConfigElement("Move Speed", "In cells per milliseconds",
                Float.toString(SimulatorConfig.DEFAULT_ROBOT_MOVE_SPEED),
                (v) -> SimulatorConfig.setRobotMoveSpeed(Float.parseFloat(v)));
    }

    /**
     * Adds a configurable value to the Panel.
     * @param name The name of the Value
     * @param toolTip The tooltip to show on mouse over
     * @param initValue The default Value
     * @param setter The setter to change the value
     */
    private void addConfigElement(String name, String toolTip, String initValue, ValueSetter setter) {

        /*
         * Panel:
         * | ------------------------ |
         * | Name - TextField - Reset |
         * | ------------------------ |
         */

        JPanel root = new JPanel(new BorderLayout());
        JPanel input = new JPanel(new BorderLayout());

        // Label shows the name
        JLabel label = new JLabel(name);
        label.setFont(label.getFont().deriveFont(Font.PLAIN));

        // Textfield (with tooltip) to input changes
        JTextField textField = new JTextField(initValue);
        textField.setToolTipText(toolTip);
        textField.addActionListener(e -> setter.setValue(textField.getText()));

        // Button to reset
        JButton button = new JButton("Reset");
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
