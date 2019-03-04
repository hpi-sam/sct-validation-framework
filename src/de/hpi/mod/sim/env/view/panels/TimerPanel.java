package de.hpi.mod.sim.env.view.panels;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.sim.SimulationWorld; 

public class TimerPanel extends JPanel {
	private Timer timer;
	private SimulationWorld world;
	private int originalTime;
	private int remainingTime;
	private int passedMinutes = 0;
	private int passedSeconds = 0;
	private JTextField valueField;
	private static DriveSimFrame parent;
	
	public TimerPanel(SimulationWorld world){
		this.world = world;
		
		setLayout(new GridBagLayout());

        // TextField to show values
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
        
        JPanel spacer = new JPanel();
        spacer.setBackground(DriveSimFrame.MENU_ORANGE);
        GridBagConstraints spacerConstraints = new GridBagConstraints();
        spacerConstraints.gridx = 1;
        spacerConstraints.gridy = 1;
        spacerConstraints.weightx = 1.0;
        add(new MenuWrapper(100, 60, DriveSimFrame.MENU_ORANGE, spacer), spacerConstraints);
	}
	
	public void startNewClock(int countdown) {
		originalTime = countdown;
		remainingTime = countdown;
		setClockTime();
		
		timer=new Timer(1000,new ActionListener(){
		public void actionPerformed(ActionEvent e)
		{
			if(world.isRunning()) {
		        remainingTime -= 1*parent.getConfigPanel().getCurrentLevel();
		        setClockTime();
		        if(remainingTime<=0) {
		        	parent.getScenarioPanel().scenarioPassed();
		        }
			}
		}
		});
		startTimer();
	}

	public void startTimer() {
		timer.start();
	}
	
	public void stopTimer() {
		timer.stop();
	}
	
	public int getRemainingTime() {
		return remainingTime;
	}
	
	private void setClockTime() {
		calculateNewPassedtime(originalTime, remainingTime);
        displayNewTime();
	}
	
	private void calculateNewPassedtime(int originalTime, int remainingTime) {
		int deltaTime = originalTime-remainingTime;
		
		passedMinutes = deltaTime/60;
		passedSeconds = deltaTime%60;
	}
	
	private void displayNewTime() {
		if (passedSeconds >= 10) {
			valueField.setText(Integer.toString(passedMinutes) + ":" + Integer.toString(passedSeconds));
		} else {
			valueField.setText(Integer.toString(passedMinutes) + ":0" + Integer.toString(passedSeconds));
		}
	}

	public static void setParent(DriveSimFrame driveSimFrame) {
		parent = driveSimFrame;
		
	}

}
