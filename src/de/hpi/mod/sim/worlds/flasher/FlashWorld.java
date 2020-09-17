package de.hpi.mod.sim.worlds.flasher;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Hashtable;
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
	 
	@Override
	public List<Detector> createDetectors() {
		return new ArrayList<>() ;
	}

	@Override
	protected void initialize() {
	}

	@Override
	public void updateEntities(float delta) {
		starter.update(delta);		
	}

	@Override
	public void resetScenario() {
		// TODO Auto-generated method stub
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
		bulb.updateTimer();
	}

	@Override
	public List<Scenario> getScenarios() {
		return new ScenarioGenerator(this).getScenarios();
	}

	@Override
	public Map<String, List<TestScenario>> getTestGroups() {
		// TODO Auto-generated method stub
		return new Hashtable<>();
	}

	@Override
	public void render(Graphics graphics) {
		bulb.bulbRender(graphics);
	}

	@Override
	public void refreshSimulationProperties(int currentHeight, int currentWidth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IHighlightable getHighlightAtPosition(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AnimationPanel getAnimationPanel() {
		return new AnimationPanel(this) {

			
			private static final long serialVersionUID = -3511125389453371432L;

			@Override
			public void resetZoom() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void zoomIn(float zoom) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void zoomOut(float zoom) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

	public void setBulb(Bulb bulb) {
		this.bulb = bulb;
	}

	public void setStarter(Starter starter) {
		this.starter = starter;
	}

	
	
	
}
