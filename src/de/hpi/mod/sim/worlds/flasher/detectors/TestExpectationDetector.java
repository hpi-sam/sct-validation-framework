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
					System.out.println("try to detect failure");
					if(lightBulb.hasFailedTest()) {
						System.out.println("detected");
						this.errorReported = true;
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
	@Override
	public void reset() {
		this.errorReported = false;
	}

}