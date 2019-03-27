package de.hpi.mod.sim.env.view.panels;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.sim.SimulationWorld; 

public class TimerPanel extends JPanel {
	private Timer timer;
	private SimulationWorld world;
	private float time;
	private JTextField valueField;
	
	public TimerPanel(SimulationWorld world){
		this.world = world;
		
		setLayout(new GridBagLayout());
		
		addValueField();
		addSpacer();
		
		initializeTimer();
	}
	
	private void addValueField() {
		valueField = newValueField();
        GridBagConstraints valueFieldConstraints = new GridBagConstraints();
        valueFieldConstraints.gridx = 0;
        valueFieldConstraints.gridy = 1;
        valueFieldConstraints.anchor = GridBagConstraints.LINE_START;
        add(new MenuWrapper(60, 60, DriveSimFrame.MAIN_MENU_COLOR, valueField), valueFieldConstraints);
	}
	
	private JTextField newValueField() {
		JTextField valueField = new JTextField();
        valueField.setEditable(false);
        valueField.setPreferredSize(new Dimension(50, 0));
        valueField.setHorizontalAlignment(JTextField.CENTER);
        valueField.setText("0:00");
        return valueField;
	}

	private void addSpacer() {
		//The spacer takes up additional space and pushed the timer field to the left
		JPanel spacer = new JPanel();
        spacer.setBackground(DriveSimFrame.MAIN_MENU_COLOR);
        GridBagConstraints spacerConstraints = new GridBagConstraints();
        spacerConstraints.gridx = 1;
        spacerConstraints.gridy = 1;
        spacerConstraints.weightx = 1.0;
        add(new MenuWrapper(100, 60, DriveSimFrame.MAIN_MENU_COLOR, spacer), spacerConstraints);
	}
	
	private void initializeTimer() {
		timer = new Timer(1000,new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(world.isRunning()) {
			        time += 1 * SimulatorConfig.getRobotSpeedFactor();
			        displayNewTime();
				}
			}
		});
		stopTimer();
	}

	public void startNewClock() {
		time = 0.0f;
		displayNewTime();
		startTimer();
	}
	
	public void clearTimer() {
		stopTimer();
		time = 0.0f;
		displayNewTime();
	}

	public void startTimer() {
		timer.restart();
	}
	
	public void stopTimer() {
		timer.stop();
	}
	
	private void displayNewTime() {
		int passedMinutes = ((int) time)/60;
		int passedSeconds = ((int) time)%60;
		if (passedSeconds >= 10) {
			valueField.setText(Integer.toString(passedMinutes) + ":" + Integer.toString(passedSeconds));
		} else {
			valueField.setText(Integer.toString(passedMinutes) + ":0" + Integer.toString(passedSeconds));
		}
	}
}
