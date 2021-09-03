package de.hpi.mod.sim.worlds.flasher.entities;

import java.awt.Color;

import java.awt.Font;
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
import de.hpi.mod.sim.worlds.flasher.config.FlasherConfiguration;
import de.hpi.mod.sim.worlds.flasher.scenario.TestCaseExpectation;
import de.hpi.mod.sim.Flasher;
import de.hpi.mod.sim.Flasher.State;

public class LightBulb extends StateChartWrapper<Flasher.State>
		implements StateChartEntity, IHighlightable {

	private BufferedImage lightBulbOnImage, lightBulbOffImage;
	private boolean isOn = false;
	private int currentTaskBlinks = 0;

	private int blinksSinceLastTask = -1;
	
	private Long lastOn, lastOff;
	
	public LightBulb() {
		super();
		loadImages();
		start();
	}
	
	public void render(Graphics graphics, int panelWidth, int panelHeight) {
		BufferedImage lightBulbImage = isOn ? lightBulbOnImage : lightBulbOffImage;
		
		int maximumPermittedImageWidth = (int) (panelWidth * FlasherConfiguration.getLightBulbImageMaximumWidthRate());
		int maximumPermittedImageHeight = (int) (panelHeight * FlasherConfiguration.getLightBulbImageMaximumHeightRate());
		
		int imageWidth = lightBulbImage.getWidth();
		int imageHeight = lightBulbImage.getHeight();
		
		// Scale image if original width is too large 
		if(imageWidth > maximumPermittedImageWidth) {
			imageWidth = maximumPermittedImageWidth;
			imageHeight = (imageWidth * lightBulbImage.getHeight()) / lightBulbImage.getWidth();
		}

		// Scale image if original height is too large
		if(imageHeight > maximumPermittedImageHeight) {
			imageHeight = maximumPermittedImageHeight;
			imageWidth = (imageHeight * lightBulbImage.getWidth()) / lightBulbImage.getHeight();
		}		
		
		graphics.drawImage(lightBulbImage, 
				(panelWidth - imageWidth) / 2, (panelHeight - imageHeight) / 2, // Image Position (top left)
				imageWidth, imageHeight, // Image Size (scaled)
				null);
		
	}

	private void loadImages() {
		try {
			lightBulbOnImage = ImageIO.read(new File(FlasherConfiguration.getStringPathBulbOn()));
			lightBulbOffImage = ImageIO.read(new File(FlasherConfiguration.getStringPathBulbOff()));
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
		blinksSinceLastTask++;
		isOn = true;
		lastOn = System.currentTimeMillis();
		if(lastOff == null)
			return;
	}

	public void turnOff() {
		if (!isOn)
			return;
		isOn = false;
		lastOff = System.currentTimeMillis();
		if (lastOn == null)
			return;
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
		return getStatemachine().isStateActive(state);
	}

	public void doBlinkingTask(int n) {
		// Save task parameters
		currentTaskBlinks = n;
		blinksSinceLastTask = 0;
		
		// Send command to state machine and have it run for a cycle.
		getStatemachine().raiseStart(n); 
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
		return Arrays.asList("Times to blink " + currentTaskBlinks, "Blinks since last tasks: "  + blinksSinceLastTask, "Lamp is on: " + isOn());
	}

	public boolean isOn() {
		return isOn;
	}

	public int getTimesToBlink() {
		return currentTaskBlinks;
	}

	public int getRemainingBlinks() {
		return blinksSinceLastTask;
	}

	public void update(float delta) {
		
	}
}
