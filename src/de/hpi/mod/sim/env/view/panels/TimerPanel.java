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
	private float originalTime;
	private float remainingTime;
	private int passedMinutes = 0;
	private int passedSeconds = 0;
	private JTextField valueField;
	private DriveSimFrame frame;
	
	public TimerPanel(SimulationWorld world, DriveSimFrame frame){
		this.world = world;
		this.frame = frame;
		
		setLayout(new GridBagLayout());
		
		addValueField();
		addSpacer();
		
		initializeTimer();
	}
	
	private void addValueField() {
		valueField = new JTextField();
        valueField.setEditable(false);
        valueField.setPreferredSize(new Dimension(50, 0));
        valueField.setHorizontalAlignment(JTextField.CENTER);
        valueField.setText("0:00");
        GridBagConstraints valueFieldConstraints = new GridBagConstraints();
        valueFieldConstraints.gridx = 0;
        valueFieldConstraints.gridy = 1;
        valueFieldConstraints.anchor = GridBagConstraints.LINE_START;
        add(new MenuWrapper(60, 60, DriveSimFrame.MENU_ORANGE, valueField), valueFieldConstraints);
	}

	private void addSpacer() {
		//The spacer takes up additional space and pushed the timer field to the left
		JPanel spacer = new JPanel();
        spacer.setBackground(DriveSimFrame.MENU_ORANGE);
        GridBagConstraints spacerConstraints = new GridBagConstraints();
        spacerConstraints.gridx = 1;
        spacerConstraints.gridy = 1;
        spacerConstraints.weightx = 1.0;
        add(new MenuWrapper(100, 60, DriveSimFrame.MENU_ORANGE, spacer), spacerConstraints);
	}
	
	private void initializeTimer() {
		timer = new Timer(1000,new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(world.isRunning()) {
			        remainingTime -= (float)frame.getConfigPanel().getCurrentLevel()/(float)SimulatorConfig.getRobotDefaultSpeedLevel();
			        setClockTime();
			        if(remainingTime<=0) {
			        	frame.getScenarioPanel().scenarioPassed();
			        }
				}
			}
		});
		stopTimer();
	}

	public void startNewClock(int countdown) {
		originalTime = countdown;
		remainingTime = countdown;
		setClockTime();
		startTimer();
	}

	public void startTimer() {
		timer.restart();
	}
	
	public void stopTimer() {
		timer.stop();
	}
	
	public float getRemainingTime() {
		return remainingTime;
	}
	
	private void setClockTime() {
		calculateNewPassedtime();
        displayNewTime();
	}
	
	private void calculateNewPassedtime() {
		float deltaTime = originalTime-remainingTime;
		
		passedMinutes = ((int) deltaTime)/60;
		passedSeconds = ((int) deltaTime)%60;
	}
	
	private void displayNewTime() {
		if (passedSeconds >= 10) {
			valueField.setText(Integer.toString(passedMinutes) + ":" + Integer.toString(passedSeconds));
		} else {
			valueField.setText(Integer.toString(passedMinutes) + ":0" + Integer.toString(passedSeconds));
		}
	}
}
