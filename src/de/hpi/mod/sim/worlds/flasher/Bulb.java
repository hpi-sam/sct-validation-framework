package de.hpi.mod.sim.worlds.flasher;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.flasher.FlasherStatemachine;
import de.hpi.mod.sim.flasher.FlasherStatemachine.State;

public class Bulb extends StateChartWrapper<FlasherStatemachine.State> implements StateChartEntity, EntitySpecification<Bulb> {

	private BufferedImage bulbOn, bulbOff;
	private boolean bulbIsLightning = false;

	public Bulb() {
		super();
		loadImages();
		start();
	}

	public void bulbRender(Graphics graphics) {
		if (bulbIsLightning) {
			graphics.drawImage(bulbOn, 0, 0, null);
		} else {
			graphics.drawImage(bulbOff, 0, 0, null);
		}
	}

	private void loadImages() {
		try {
			bulbOn = ImageIO.read(new File(FlasherConfiguration.getStringPathBulbOn()));
			bulbOff = ImageIO.read(new File(FlasherConfiguration.getStringPathBulbOff()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FlasherStatemachine getStatemachine() {
		return (FlasherStatemachine) chart;
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
		bulbIsLightning = true;
	}

	public void turnOff() {
		bulbIsLightning = false;
	}

	@Override
	public IStatemachine createStateMachine() {
		return new FlasherStatemachine();
	}

	@Override
	protected State[] getStates() {
		return FlasherStatemachine.State.values();
	}

	@Override
	protected boolean isActive(State state) {
		/*
		 * This is not intended by the YAKINDU implementation and source generation.
		 * Officially, the YAKINDU interface does not support this, which is why we have
		 * to cast to the actual DrivesystemStateChart object.
		 */
		return ((FlasherStatemachine) chart).isStateActive(state);
	}

	public void start(int n) {
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
	public Bulb get() {
		return this;
	}
}
