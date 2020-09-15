package de.hpi.mod.sim.worlds.flasher;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.view.panels.AnimationPanel;
import de.hpi.mod.sim.worlds.flasher.FlasherConfiguration;

public class FlashWorld extends World {

	private Bulb bulb;

	 
	 
	@Override
	public List<Detector> createDetectors() {
		return new ArrayList<>() ;
	}
	
	

	@Override
	protected void initialize() {
		bulb = new Bulb();
	}

	@Override
	public void updateEntities(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetScenario() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<? extends Entity> getEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshEntities() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Scenario> getScenarios() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<TestScenario>> getTestGroups() {
		// TODO Auto-generated method stub
		return null;
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

	
	
	
}
