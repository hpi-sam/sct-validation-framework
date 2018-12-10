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
                0, .05f, 10000,
                SimulatorConfig.DEFAULT_ROBOT_MOVE_SPEED,
                SimulatorConfig::setRobotMoveSpeed);
    }

    /**
     * Adds a configurable value to the Panel.
     * @param name The name of the Value
     * @param toolTip The tooltip to show on mouse over
     * @param initValue The default Value
     * @param setter The setter to change the value
     */
    private void addConfigElement(String name, String toolTip, float minValue, float maxValue,
                                  float multiplier, float initValue, ValueSetter setter) {

        /*
         * Panel:
         * | ------------------------ |
         * | Name - Slider - Reset |
         * | ------------------------ |
         */

        JPanel root = new JPanel(new BorderLayout());
        JPanel input = new JPanel(new BorderLayout());

        // Label shows the name
        JLabel label = new JLabel(name);
        label.setFont(label.getFont().deriveFont(Font.PLAIN));

        // TextField to show values
        JTextField valueField = new JTextField();
        valueField.setEditable(false);
        valueField.setPreferredSize(new Dimension(50, 0));
        valueField.setText(Float.toString(initValue));

        // Slider (with tooltip) to input changes
        JSlider valueSlider = new JSlider((int) (minValue * multiplier),
                (int) (maxValue * multiplier), (int) (initValue * multiplier));
        valueSlider.setToolTipText(toolTip);
        valueSlider.addChangeListener(e -> {
            setter.setValue(valueSlider.getValue() / multiplier);
            valueField.setText(Float.toString(valueSlider.getValue() / multiplier));
        });

        // Button to reset
        JButton button = new JButton("Reset");
        button.addActionListener(e -> {
            valueSlider.setValue((int) (initValue * multiplier));
            valueField.setText(Float.toString(initValue));
            setter.setValue(initValue);
        });

        input.add(valueField, BorderLayout.WEST);
        input.add(valueSlider, BorderLayout.CENTER);
        input.add(button, BorderLayout.EAST);

        root.add(label, BorderLayout.NORTH);
        root.add(input, BorderLayout.CENTER);

        add(root);
    }

    private interface ValueSetter {
        void setValue(float value);
    }
}
