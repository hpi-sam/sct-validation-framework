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

	int currentLevel = 1;
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
        valueField.setText("1");

        // Slider (with tooltip) to input changes
        JSlider valueSlider = new JSlider((int) (minValue * multiplier),
                (int) (maxValue * multiplier), (int) (initValue * multiplier));
        setter.setValue(toMagicSpeedValue(1));
        valueSlider.setToolTipText(toolTip);
        valueSlider.addChangeListener(e -> {
        	setter.setValue(toMagicSpeedValue(discreteValueOf(valueSlider.getValue() / multiplier, minValue, maxValue, 10)));
            valueField.setText(Integer.toString(discreteValueOf(valueSlider.getValue() / multiplier, minValue, maxValue, 10)));
        });

        // Button to reset
        JButton button = new JButton("Reset");
        button.addActionListener(e -> {
            valueSlider.setValue((int) (initValue * multiplier));
            valueField.setText("1");
            setter.setValue(toMagicSpeedValue(1));
        });

        input.add(valueField, BorderLayout.WEST);
        input.add(valueSlider, BorderLayout.CENTER);
        input.add(button, BorderLayout.EAST);

        root.add(label, BorderLayout.NORTH);
        root.add(input, BorderLayout.CENTER);

        add(root);
    }

    private float toMagicSpeedValue(int level) {
    	switch(level) {
    	case 1:
    		return 0.0005f;
    	case 2:
    		return 0.001f;
    	case 3:
    		return 0.0015f;
    	case 4:
    		return 0.0025f;
    	case 5:
    		return 0.0035f;
    	case 6:
    		return 0.0045f;
    	case 7:
    		return 0.006f;
    	case 8:
    		return 0.0075f;
    	case 9:
    		return 0.009f;
    	case 10:
    		return 0.011f;
		default:
			return (float) 0;
    	}
	}

	private int discreteValueOf(float f, float minValue, float maxValue, int maxRange) {
		int level = 0; 
		
		for(int i=1; i<=maxRange; i++) {
			if(f*i >= (maxValue-minValue)) {
				level = maxRange-i+1;
				break;
			}
		}
		currentLevel = level;
		return currentLevel;
	}

	private interface ValueSetter {
        void setValue(float value);
    }
}
