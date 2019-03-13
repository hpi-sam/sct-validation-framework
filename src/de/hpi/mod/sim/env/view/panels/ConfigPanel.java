package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel implements ITimeListener{

	private int currentLevel = SimulatorConfig.getDefaultRobotSpeedLevel();
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
        add(new MenuWrapper(390, 30, DriveSimFrame.MAIN_MENU_COLOR, label), labelConstraints);
	}

	private void addValueField() {
		valueField = newValueField();
        GridBagConstraints valueFieldConstraints = new GridBagConstraints();
        valueFieldConstraints.gridx = 0;
        valueFieldConstraints.gridy = 1;
        add(new MenuWrapper(60, 60, DriveSimFrame.MAIN_MENU_COLOR, valueField), valueFieldConstraints);
	}
	
	private JTextField newValueField() {
		JTextField valueField = new JTextField();
        valueField.setEditable(false);
        valueField.setText(Integer.toString(SimulatorConfig.getDefaultRobotSpeedLevel()));
        valueField.setHorizontalAlignment(JTextField.CENTER);
        return valueField;
	}

	private void addSlider() {
		valueSlider = newSpeedSlider();
        GridBagConstraints sliderConstraints = new GridBagConstraints();
        sliderConstraints.gridx = 1;
        sliderConstraints.gridy = 1;
        sliderConstraints.fill = GridBagConstraints.HORIZONTAL;
        sliderConstraints.weightx = 1.0; //the slider gets all the additional space
        sliderConstraints.insets = new Insets(0, 3, 0, 3);
        add(new MenuWrapper(200, 60, DriveSimFrame.MAIN_MENU_COLOR, valueSlider), sliderConstraints);
	}
	
	private JSlider newSpeedSlider() {
		JSlider valueSlider = new JSlider(SimulatorConfig.ROBOT_MIN_SPEED_LEVEL, SimulatorConfig.ROBOT_MAX_SPEED_LEVEL, SimulatorConfig.ROBOT_DEFAULT_SPEED_LEVEL);
        setter = SimulatorConfig::setRobotSpeedLevel;
        setter.setValue(SimulatorConfig.getDefaultRobotSpeedLevel());
        valueSlider.setToolTipText("Adjust Robot Speed");
        valueSlider.setEnabled(true); //in the begin the speed slider should be changeable, even if not afterwards while paused simulation
        valueSlider.addChangeListener(e -> {
        	setter.setValue(valueSlider.getValue());
            valueField.setText(Integer.toString(valueSlider.getValue()));
            currentLevel = valueSlider.getValue();
        });
        return valueSlider;
	}

	private void addResetButton() {
		JButton resetButton = newResetButton();
        GridBagConstraints resetButtonConstraints = new GridBagConstraints();
        resetButtonConstraints.gridx = 0;
        resetButtonConstraints.gridy = 2;
        resetButtonConstraints.insets = new Insets(3, 3, 3, 3);        
        add(new MenuWrapper(60, 24, DriveSimFrame.MAIN_MENU_COLOR, resetButton), resetButtonConstraints);
	}
	
	private JButton newResetButton() {
		JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            valueSlider.setValue(SimulatorConfig.ROBOT_DEFAULT_SPEED_LEVEL);
            valueField.setText(Integer.toString(SimulatorConfig.getDefaultRobotSpeedLevel()));
            setter.setValue(SimulatorConfig.getDefaultRobotSpeedLevel());
            currentLevel = SimulatorConfig.getDefaultRobotSpeedLevel();
        });
        return resetButton;
	}

	private void addPlayButton() {
		playButton = newPlayButton();
		refresh();  // Refresh to set icon
        GridBagConstraints playButtonConstraints = new GridBagConstraints();
        playButtonConstraints.gridx = 2;
        playButtonConstraints.gridy = 1;
        playButtonConstraints.insets = new Insets(3, 3, 3, 3);
        add(new MenuWrapper(60, 60, DriveSimFrame.MAIN_MENU_COLOR, playButton), playButtonConstraints);
	}
	
	private JButton newPlayButton() {
		loadIcons();

        JButton playButton = new JButton();
        playButton.addActionListener(e -> {
        	world.toggleRunning();
        });
        return playButton;
	}

	private void loadIcons() {
    	playIcon = new ImageIcon(SimulatorConfig.getStringPathToPlayIcon());
        pauseIcon = new ImageIcon(SimulatorConfig.getStringPathToPauseIcon());
	}

	public int getCurrentLevel() {
    	return currentLevel;
    }

	private interface ValueSetter {
        void setValue(int value);
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
