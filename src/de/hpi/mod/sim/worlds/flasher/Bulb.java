package de.hpi.mod.sim.worlds.flasher;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import com.yakindu.core.IStatemachine;

import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.Flasher;
import de.hpi.mod.sim.Flasher.State;

public class Bulb extends StateChartWrapper<Flasher.State>
		implements StateChartEntity, IHighlightable {

	private BufferedImage bulbOn, bulbOff;
	private boolean isOn = false;
	private int timesToBlink = 0;

	private int remainingBlinks = -1;
	
	private boolean hasToBeOffForTests = false;
	private boolean checkOnTimeForTests = false;
	private boolean checkOffTimeForTests = false;
	private boolean onlyCorrectOnTimes = true;
	private boolean onlyCorrectOffTimes = true;

	
	
	private Long lastOn, lastOff;

	public Bulb() {
		super();
		loadImages();
		start();
	}
	
	public void bulbRender(Graphics graphics, int width, int height) {
		
		BufferedImage img = isOn ? bulbOn : bulbOff;
		graphics.drawImage(img, (width - img.getWidth()) / 2, (height - img.getHeight()) / 2, null);

		
	}

	private void loadImages() {
		try {
			bulbOn = ImageIO.read(new File(FlasherConfiguration.getStringPathBulbOn()));
			bulbOff = ImageIO.read(new File(FlasherConfiguration.getStringPathBulbOff()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Flasher getStatemachine() {
		return (Flasher) chart;
	}

	@Override
	public void update() {
		/**
		 * Runs a cycle of the statechart and checks if any functions got fired
		 */
		if (getStatemachine().isRaisedOn())
			turnOn();
		if (getStatemachine().isRaisedOff())
			turnOff();
	}

	public void turnOn() {
		if (isOn)
			return;
		remainingBlinks--;
		isOn = true;
		lastOn = System.currentTimeMillis();
		if(lastOff == null)
			return;
		long offtime = lastOn - lastOff;
		if (remainingBlinks > 0 && (offtime < 450 || offtime > 700)) {
			onlyCorrectOffTimes = false;
		}
	}

	public void turnOff() {
		if (!isOn)
			return;
		isOn = false;
		lastOff = System.currentTimeMillis();
		if (lastOn == null)
			return;
		long ontime = lastOff - lastOn; 
		if (ontime < 450 || ontime > 700)
			onlyCorrectOnTimes = false;
	}

	@Override
	public IStatemachine createStateMachine() {
		return new Flasher();
	}

	@Override
	protected State[] getStates() {
		return Flasher.State.values();
	}

	@Override
	protected boolean isActive(State state) {
		/*
		 * This is not intended by the YAKINDU implementation and source generation.
		 * Officially, the YAKINDU interface does not support this, which is why we have
		 * to cast to the actual DrivesystemStateChart object.
		 */
		return ((Flasher) chart).isStateActive(state);
	}

	public void start(int n) {
		getStatemachine().raiseStart(n);
		timesToBlink = n;
		remainingBlinks = n;
		update();
	}

	@Override
	public String getMachineState() {
		return getChartState();
	}

	@Override
	public String getTopStateName() {
		return "flasher";
	}

	@Override
	public List<String> getHighlightInfo() {
		return Arrays.asList("Times to blink " + timesToBlink, "Remaining blinks: "  + remainingBlinks, "Lamp is on: " + isOn());
	}

	@Override
	public boolean hasPassedAllTestCriteria() {
		return remainingBlinks == 0 && 
			   (!hasToBeOffForTests || !isOn) &&
			   (!checkOffTimeForTests || onlyCorrectOffTimes) &&
			   (!checkOnTimeForTests || onlyCorrectOnTimes);
	}

	public boolean isOn() {
		return isOn;
	}

	public int getTimesToBlink() {
		return timesToBlink;
	}

	public void setHasToBeOffForTests(boolean b) {
		this.hasToBeOffForTests = b;
	}
	
	public void setCheckOffTimeForTests(boolean b) {
		this.checkOffTimeForTests = b;
	}

	public void setCheckOnTimeForTests(boolean b) {
		this.checkOnTimeForTests = b;
	}

	public int getRemainingBlinks() {
		return remainingBlinks;
	}
}
