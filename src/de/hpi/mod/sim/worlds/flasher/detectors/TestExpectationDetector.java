package de.hpi.mod.sim.worlds.flasher.detectors;

import java.util.List;

import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.flasher.FlashWorld;
import de.hpi.mod.sim.worlds.flasher.entities.LightBulbWithExpectation;

public class TestExpectationDetector extends Detector {

	private boolean errorReported = false;
	
	protected TestExpectationDetector(FlashWorld world) {
		super(world);
	}

	@Override
	public void update(List<? extends Entity> entities) {
		if(!this.errorReported) {
			for(Entity entity:entities) {
				if (entity instanceof LightBulbWithExpectation) {
					LightBulbWithExpectation lightBulb = (LightBulbWithExpectation) entity;
					if(lightBulb.hasFailedTest()) {
						this.errorReported = true;
						if (lightBulb.isOn()) {
							report("Should be off.", lightBulb, null);
						}else {
							report("Should be on.", lightBulb, null);
						}
				}
			}		
		}
	}
}
	@Override
	public void reset() {
		this.errorReported = false;
	}

}