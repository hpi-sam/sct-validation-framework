package de.hpi.mod.sim.worlds.flasher.entities;

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
import de.hpi.mod.sim.statemachines.flasher.FlashControl;

public class LightBulb extends StateChartWrapper<FlashControl.State>
		implements StateChartEntity, IHighlightable {

	private BufferedImage lightBulbOnImage, lightBulbOffImage;
	private boolean isOn = false;
	private int currentTaskBlinks = 0;

	private int actionsSinceLastTask = -1;
	
	private Long lastOn, lastOff;
	
	public LightBulb() {
		super();
		loadImages();
		start();
	}
	
	public void render(Graphics graphics, int panelWidth, int panelHeight) {
		BufferedImage lightBulbImage = isOn ? lightBulbOnImage : lightBulbOffImage;
		
		// Recude effective panel hight by the size required for the task display below.
		int availablePanelHight = panelHeight - FlasherConfiguration.getTaskDisplayReservedHeight();
		
		// Get maximum sizes of light bulb image based on total heigt/with and scaling factors 
		int maximumPermittedImageWidth = (int) (panelWidth * FlasherConfiguration.getLightBulbImageMaximumRelativeWidth());
		int maximumPermittedImageHeight = (int) (availablePanelHight * FlasherConfiguration.getLightBulbImageMaximumRelativeHeight());
		
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
				(panelWidth - imageWidth) / 2, (availablePanelHight - imageHeight) / 2, // Image Position (top left)
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

	private FlashControl getStatemachine() {
		return (FlashControl) chart;
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
		actionsSinceLastTask++;
		isOn = true;
		lastOn = System.currentTimeMillis();
		if(lastOff == null)
			return;
	}

	public void turnOff() {
		if (!isOn)
			return;
		actionsSinceLastTask++;
		isOn = false;
		lastOff = System.currentTimeMillis();
		if (lastOn == null)
			return;
	}

	@Override
	public IStatemachine createStateMachine() {
		return new FlashControl();
	}

	@Override
	protected FlashControl.State[] getStates() {
		return FlashControl.State.values();
	}

	@Override
	protected boolean isActive(FlashControl.State state) {
		return getStatemachine().isStateActive(state);
	}

	public void doBlinkingTask(int n) {
		// Save task parameters
		currentTaskBlinks = n;
		actionsSinceLastTask = 0;
		
		// Send command to state machine and have it run for a cycle.
		getStatemachine().raiseStart(n); 
		update();
	}

	@Override
	public String getTopLevelRegionName() {
		return "flasher";
	}

	@Override
	public List<String> getHighlightInfo() {
		return Arrays.asList("Blinks in latest task: " + this.getTimesToBlinkText(), "Blinks since task start: "  + this.getCurrentBlinkCounterText(), "", "Lamp is on: " + this.isOn());
	}

	public boolean isOn() {
		return isOn;
	}

	public int getTimesToBlink() {
		return currentTaskBlinks;
	}

	private String getTimesToBlinkText() {
		if(currentTaskBlinks < 0) {
			return "No Task.";
		}
		return Integer.toString(currentTaskBlinks);
	}

	public int getCurrentBlinkCounter() {
		return actionsSinceLastTask/2;
	}

	private String getCurrentBlinkCounterText() {
		if(this.getCurrentBlinkCounter() < 0) {
			return "No Task.";
		}
		return Integer.toString(this.getCurrentBlinkCounter());
	}

	public void update(float delta) {
		
	}
}
