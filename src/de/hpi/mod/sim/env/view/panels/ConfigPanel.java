package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel implements ITimeListener{

	private int currentLevel = SimulatorConfig.getRobotDefaultSpeedLevel();
	private SimulationWorld world;
	private JTextField valueField;
	private JButton playButton;
	private JSlider valueSlider;
	private ValueSetter setter;
	private ImageIcon playIcon;
	private ImageIcon pauseIcon;
	
    public ConfigPanel(SimulationWorld world) {
    	this.world = world;
    	
    	// --------------------------------------
    	// | Move Speed							|
    	// |  -------					 ------	|
    	// |  |Value|   -------O-------- | |> |	|
    	// |  -------					 ------	|
    	// |  -------							|
    	// |  |Reset|							|
    	// |  -------							|
    	// --------------------------------------
    	
    	//We use a GridBagLayout for flexibility
        setLayout(new GridBagLayout());
        
        addLabel();
        addValueField();
        addSlider();
        addResetButton();
        addPlayButton();
    }

    private void addLabel() {
    	JLabel label = new JLabel("Move Speed");
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.gridwidth = 3;
        labelConstraints.anchor = GridBagConstraints.LINE_START;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(new MenuWrapper(390, 30, DriveSimFrame.MENU_ORANGE, label), labelConstraints);
	}

	private void addValueField() {
		valueField = new JTextField();
        valueField.setEditable(false);
        valueField.setText(Integer.toString(SimulatorConfig.getRobotDefaultSpeedLevel()));
        valueField.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints valueFieldConstraints = new GridBagConstraints();
        valueFieldConstraints.gridx = 0;
        valueFieldConstraints.gridy = 1;
        valueFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(new MenuWrapper(60, 60, DriveSimFrame.MENU_ORANGE, valueField), valueFieldConstraints);
	}

	private void addSlider() {
		valueSlider = new JSlider(SimulatorConfig.ROBOT_MIN_SPEED_LEVEL, SimulatorConfig.ROBOT_MAX_SPEED_LEVEL, SimulatorConfig.ROBOT_DEFAULT_SPEED_LEVEL);
        setter = SimulatorConfig::setRobotMoveSpeed;
        setter.setValue(toMagicSpeedValue(SimulatorConfig.getRobotDefaultSpeedLevel()));
        valueSlider.setToolTipText("Adjust Robot Speed");
        valueSlider.setEnabled(true); //in the begin the speed slider should be changeable, even if not afterwards while paused simulation
        valueSlider.addChangeListener(e -> {
        	setter.setValue(toMagicSpeedValue(valueSlider.getValue()));
            valueField.setText(Integer.toString(valueSlider.getValue()));
            currentLevel = valueSlider.getValue();
        });
        GridBagConstraints sliderConstraints = new GridBagConstraints();
        sliderConstraints.gridx = 1;
        sliderConstraints.gridy = 1;
        sliderConstraints.fill = GridBagConstraints.HORIZONTAL;
        sliderConstraints.weightx = 1.0; //the slider gets all the additional space
        add(new MenuWrapper(200, 60, DriveSimFrame.MENU_ORANGE, valueSlider), sliderConstraints);
	}

	private void addResetButton() {
		JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
        	valueSlider.setEnabled(true); //after reset the speed slider should be changeable, even if not afterwards while paused simulation
            valueSlider.setValue(SimulatorConfig.ROBOT_DEFAULT_SPEED_LEVEL);
            valueField.setText(Integer.toString(SimulatorConfig.getRobotDefaultSpeedLevel()));
            setter.setValue(toMagicSpeedValue(SimulatorConfig.getRobotDefaultSpeedLevel()));
            currentLevel = SimulatorConfig.getRobotDefaultSpeedLevel();
        });
        GridBagConstraints resetButtonConstraints = new GridBagConstraints();
        resetButtonConstraints.gridx = 0;
        resetButtonConstraints.gridy = 2;
        add(new MenuWrapper(60, 30, DriveSimFrame.MENU_ORANGE, resetButton), resetButtonConstraints);
	}

	private void addPlayButton() {
		loadIcons();

        playButton = new JButton();
        refresh();  // Refresh to set icon
        playButton.addActionListener(e -> {
        	world.toggleRunning();
        	valueSlider.setEnabled(world.isRunning());
        });
        GridBagConstraints playButtonConstraints = new GridBagConstraints();
        playButtonConstraints.gridx = 2;
        playButtonConstraints.gridy = 1;
        add(new MenuWrapper(60, 60, DriveSimFrame.MENU_ORANGE, playButton), playButtonConstraints);
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
