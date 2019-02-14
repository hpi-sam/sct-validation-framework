package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

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
public class ConfigPanel extends JPanel implements ITimeListener{

	private int currentLevel = SimulatorConfig.getRobotDefaultSpeedLevel();
	private SimulationWorld world;
	private JButton playButton;
	private ImageIcon playIcon;
	private ImageIcon pauseIcon;
    /**
     * Initializes the Panel and adds Config Elements
     */
    public ConfigPanel(SimulationWorld world) {
    	this.world = world;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addConfigElement("Move Speed", "Recent robot speed level",
                SimulatorConfig.ROBOT_MIN_SPEED_LEVEL, SimulatorConfig.ROBOT_MAX_SPEED_LEVEL,
                SimulatorConfig.ROBOT_DEFAULT_SPEED_LEVEL,
                SimulatorConfig::setRobotMoveSpeed);
    }

    /**
     * Adds a configurable value to the Panel.
     * @param name The name of the Value
     * @param toolTip The tooltip to show on mouse over
     * @param initValue The default Value
     * @param setter The setter to change the value
     * @param world 
     */
    private void addConfigElement(String name, String toolTip, int minValue, int maxValue, int initValue, ValueSetter setter) {

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
        valueField.setText(Integer.toString(SimulatorConfig.getRobotDefaultSpeedLevel()));

        // Slider (with tooltip) to input changes
        JSlider valueSlider = new JSlider(minValue, maxValue, initValue);
        setter.setValue(toMagicSpeedValue(SimulatorConfig.getRobotDefaultSpeedLevel()));
        valueSlider.setToolTipText(toolTip);
        valueSlider.setEnabled(SimulationWorld.isRunning());
        valueSlider.addChangeListener(e -> {
        	valueSlider.setEnabled(SimulationWorld.isRunning());
        	setter.setValue(toMagicSpeedValue(valueSlider.getValue()));
            valueField.setText(Integer.toString(valueSlider.getValue()));
        });

        // Button to reset
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
        	valueSlider.setEnabled(SimulationWorld.isRunning());
            valueSlider.setValue(initValue);
            valueField.setText(Integer.toString(SimulatorConfig.getRobotDefaultSpeedLevel()));
            setter.setValue(toMagicSpeedValue(SimulatorConfig.getRobotDefaultSpeedLevel()));
        });
        
        //Play/Pause button
        loadIcons();

        playButton = new JButton();
        refresh();  // Refresh to set icon
        playButton.addActionListener(e -> world.toggleRunning());
        add(playButton);

        input.add(valueField, BorderLayout.WEST);
        input.add(valueSlider, BorderLayout.CENTER);
        //input.add(resetButton, BorderLayout.EAST);
        
        JPanel buttons = new JPanel(new GridLayout(0,1));
        buttons.add(resetButton);
        buttons.add(playButton);
        input.add(buttons, BorderLayout.EAST);

        root.add(label, BorderLayout.NORTH);
        root.add(input, BorderLayout.CENTER);

        add(root);
    }
   
    
    private void loadIcons() {
    	playIcon = new ImageIcon(SimulatorConfig.getStringPathToPlayIcon());
        pauseIcon = new ImageIcon(SimulatorConfig.getStringPathToPauseIcon());
	}

	public int getCurrentLevel() {
    	return currentLevel;
    }

    private float toMagicSpeedValue(int level) {
    	switch(level) {
    	case 1:
    		return SimulatorConfig.ROBOT_LEVEL_1_SPEED;
    	case 2:
    		return SimulatorConfig.ROBOT_LEVEL_2_SPEED;
    	case 3:
    		return SimulatorConfig.ROBOT_LEVEL_3_SPEED;
    	case 4:
    		return SimulatorConfig.ROBOT_LEVEL_4_SPEED;
    	case 5:
    		return SimulatorConfig.ROBOT_LEVEL_5_SPEED;
    	case 6:
    		return SimulatorConfig.ROBOT_LEVEL_6_SPEED;
    	case 7:
    		return SimulatorConfig.ROBOT_LEVEL_7_SPEED;
    	case 8:
    		return SimulatorConfig.ROBOT_LEVEL_8_SPEED;
    	case 9:
    		return SimulatorConfig.ROBOT_LEVEL_9_SPEED;
    	case 10:
    		return SimulatorConfig.ROBOT_LEVEL_10_SPEED;
		default:
			return (float) 0;
    	}
	}

	private int discreteValueOf(float sliderValue, float minValue, float maxValue, int maxRange) {
		int level = 0; 
		
		for(int i=1; i<=maxRange; i++) {
			if(sliderValue*i >= (maxValue-minValue)) {
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

	@Override
    public void refresh() {
        if (world.isRunning()) {
        	playButton.setIcon(pauseIcon);
        } else {
        	playButton.setIcon(playIcon);
        }
    }
}
