package de.hpi.mod.sim.worlds.flasher.detectors;

import java.util.List;

import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.flasher.FlashWorld;
import de.hpi.mod.sim.worlds.flasher.entities.LightBulbWithExpectation;

public class TestExpectationDetector extends Detector {
	
	public TestExpectationDetector(FlashWorld world) {
		super(world, true, false);
	}

	@Override
	public void update(List<? extends Entity> entities) {
		if(!isActivated())
			return;
		for(Entity entity:entities) {
			if (entity instanceof LightBulbWithExpectation) {
				LightBulbWithExpectation lightBulb = (LightBulbWithExpectation) entity;
				if(lightBulb.hasFailedTest()) {
					this.deactivate();
					if (lightBulb.isOn()) {
						report("light bulb should be off now.", lightBulb);
					}else {
						report("light bulb should be on now.", lightBulb);
					}
				}
			}		
		}
	}

}