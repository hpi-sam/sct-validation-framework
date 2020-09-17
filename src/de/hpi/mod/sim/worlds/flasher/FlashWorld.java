package de.hpi.mod.sim.worlds.flasher;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.view.panels.AnimationPanel;

public class FlashWorld extends World {

	private Bulb bulb;
	private Starter starter;
	private int width, height;
	 
	@Override
	public List<Detector> createDetectors() {
		Detector det = new Detector(this) {

			@Override
			public void update(List<? extends Entity> entities) {
				if (bulb != null && bulb.isOn() && bulb.getTimesToBlink() == 0)
					 report("The lamp was on but no flashing was requested (either no start signal or just start(0)).");
			}

			@Override
			public void reset() {}
		};
		return Arrays.asList(det);
	}

	

	@Override
	protected void initialize() {
	}

	@Override
	public void updateEntities(float delta) {
		if (starter != null)
			starter.update(delta);		
	}

	@Override
	public void resetScenario() {
		
	}

	@Override
	public List<? extends Entity> getEntities() {
		List<Entity> list = new ArrayList<>(1);
		list.add(bulb);
		list.add(starter);
		return list;
	}

	@Override
	public void refreshEntities() {
		if (bulb != null)
			bulb.updateTimer();
	}

	@Override
	public List<Scenario> getScenarios() {
		return new ScenarioGenerator(this).getScenarios();
	}

	@Override
	public Map<String, List<TestScenario>> getTestGroups() {
		return TestCaseGenerator.getAllTestCases(this);
	}

	@Override
	public void render(Graphics graphics) {
		if (bulb != null)
			bulb.bulbRender(graphics, width, height);
	}

	@Override
	public void refreshSimulationProperties(int currentHeight, int currentWidth) {
		this.width = currentWidth;
		this.height = currentHeight;
	}

	@Override
	public IHighlightable getHighlightAtPosition(int x, int y) {
		return null;
	}

	@Override
	public void close() {
		bulb.close();
	}

	@Override
	public AnimationPanel getAnimationPanel() {
		return new AnimationPanel(this) {

			
			private static final long serialVersionUID = -3511125389453371432L;

			@Override
			public void resetZoom() {
			}

			@Override
			public void zoomIn(float zoom) {
			}

			@Override
			public void zoomOut(float zoom) {
			}
			
		};
	}

	public void setBulb(Bulb bulb) {
		this.bulb = bulb;
	}

	public void setStarter(Starter starter) {
		this.starter = starter;
	}

	@Override
	public void clearEntities() {
		bulb = null;
		starter = null;
	}
	
	
	
}
