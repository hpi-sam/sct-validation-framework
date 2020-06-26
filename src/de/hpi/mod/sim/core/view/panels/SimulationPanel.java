package de.hpi.mod.sim.core.view.panels;

import javax.swing.*;

import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.simulation.Simulation;
import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.core.view.DriveSimFrame;
import de.hpi.mod.sim.core.view.model.ITimeListener;

import java.awt.*;

public class SimulationPanel extends JPanel implements ITimeListener{

	private static final long serialVersionUID = 4048135614810423369L;
	private int currentLevel;
	private Simulation simulation;
	private ScenarioManager scenarioManager;
	private JTextField valueField;
	private JButton playButton;
	private JButton stopButton;
	private JButton restartButton;
	private JSlider valueSlider;
	private ValueSetter setter;
	private ImageIcon playIcon;
	private ImageIcon pauseIcon;
	private ImageIcon stopIcon;
	private ImageIcon resetIcon;
	
	public SimulationPanel(Simulation simulation, ScenarioManager scenarioManager) {
		this.currentLevel = SimulatorConfig.getDefaultEntitySpeedLevel();
    	this.simulation = simulation;
    	this.scenarioManager = scenarioManager;
    	
    	// --------------------------------------------------------
    	// |  -------					 ------	 ------ --------- |
    	// |  |Value|   -------O-------- | |> |	 |Stop| |Restart| |
    	// |  -------					 ------	 ------ --------- |
    	// --------------------------------------------------------
    	
    	//We use a GridBagLayout for flexibility
        setLayout(new GridBagLayout());
        
        addValueField();
        addSlider();
        addPlayButton();
        addStopButton();
        addRestartButton();
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
        valueField.setText(Integer.toString(SimulatorConfig.getDefaultEntitySpeedLevel()));
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
        add(new MenuWrapper(100, 60, DriveSimFrame.MAIN_MENU_COLOR, valueSlider), sliderConstraints);
	}
	
	private JSlider newSpeedSlider() {
		JSlider valueSlider = new JSlider(SimulatorConfig.ENTITY_MIN_SPEED_LEVEL, SimulatorConfig.ENTITY_MAX_SPEED_LEVEL, SimulatorConfig.ENTITY_DEFAULT_SPEED_LEVEL);
        setter = SimulatorConfig::setEntitySpeedLevel;
        setter.setValue(SimulatorConfig.getDefaultEntitySpeedLevel());
        valueSlider.setToolTipText("Adjust Entity Speed");
        valueSlider.setEnabled(true); //in the begin the speed slider should be changeable, even if not afterwards while paused simulation
        valueSlider.setMajorTickSpacing(5);
        valueSlider.setPaintTicks(true);
        valueSlider.addChangeListener(e -> {
        	setter.setValue(valueSlider.getValue());
            valueField.setText(Integer.toString(valueSlider.getValue()));
            currentLevel = valueSlider.getValue();
        });
        return valueSlider;
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
        	simulation.toggleRunning();
        });
        return playButton;
	}
	
	private void addStopButton() {
		stopButton = newStopButton();
		GridBagConstraints stopButtonConstraints = new GridBagConstraints();
		stopButtonConstraints.gridx = 3;
		stopButtonConstraints.gridy = 1;
		stopButtonConstraints.insets = new Insets(3, 3, 3, 3);
        add(new MenuWrapper(60, 60, DriveSimFrame.MAIN_MENU_COLOR, stopButton), stopButtonConstraints);
	}
	
	private JButton newStopButton() {
		JButton stopButton = new JButton();
		stopButton.setIcon(stopIcon);
		stopButton.addActionListener(e -> {
        	scenarioManager.clearScenario();
        });
        return stopButton;
	}
	
	private void addRestartButton() {
		restartButton = newRestartButton();
		GridBagConstraints restartButtonConstraints = new GridBagConstraints();
		restartButtonConstraints.gridx = 4;
		restartButtonConstraints.gridy = 1;
		restartButtonConstraints.insets = new Insets(3, 3, 3, 3);
        add(new MenuWrapper(60, 60, DriveSimFrame.MAIN_MENU_COLOR, restartButton), restartButtonConstraints);
	}
	
	private JButton newRestartButton() {
		JButton restartButton = new JButton();
		restartButton.setIcon(resetIcon);
		restartButton.addActionListener(e -> {
        	scenarioManager.restartScenario();
        });
        return restartButton;
	}

	private void loadIcons() {
    	playIcon = new ImageIcon(SimulatorConfig.getStringPathToPlayIcon());
        pauseIcon = new ImageIcon(SimulatorConfig.getStringPathToPauseIcon());
    	stopIcon = new ImageIcon(SimulatorConfig.getStringPathToStopIcon());
    	resetIcon = new ImageIcon(SimulatorConfig.getStringPathToResetIcon());
	}

	public int getCurrentLevel() {
    	return currentLevel;
    }

	private interface ValueSetter {
        void setValue(int value);
    }

	@Override
    public void refresh() {
        if (simulation.isRunning()) {
        	playButton.setIcon(pauseIcon);
        } else {
        	playButton.setIcon(playIcon);
        }
    }
}
