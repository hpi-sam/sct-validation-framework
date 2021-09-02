package de.hpi.mod.sim.worlds.flasher.entities;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.flasher.FlashWorld;
import de.hpi.mod.sim.worlds.flasher.scenario.TestCaseExpectation;

public class LightBulbSpecification implements EntitySpecification<LightBulb> {

    private FlashWorld world;
    
    public LightBulbSpecification(FlashWorld world) {
        this.world = world;
    }

    @Override
    public LightBulb createEntity() {
        LightBulb bulb = new LightBulb();
        world.setBulb(bulb);
        return bulb;
    }

	public void addExpectedTestResult(TestCaseExpectation testCaseExpectation) {
		// TODO Auto-generated method stub
		
	}
    
}
