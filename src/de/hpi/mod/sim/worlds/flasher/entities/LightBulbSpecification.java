package de.hpi.mod.sim.worlds.flasher.entities;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.flasher.FlashWorld;
import de.hpi.mod.sim.worlds.flasher.scenario.TestCaseExpectation;

public class LightBulbSpecification implements EntitySpecification<LightBulb> {

    private FlashWorld world;
	private TestCaseExpectation expectation = null; 
    
    public LightBulbSpecification(FlashWorld world) {
        this.world = world;
    }
    
    public LightBulbSpecification(FlashWorld world, TestCaseExpectation expectation) {
        this.world = world;
        this.expectation = expectation;
    }

    @Override
    public LightBulb createEntity() {
    	LightBulb bulb;
    	if(this.expectation == null) {
    		bulb = new LightBulbWithExpectation(expectation);
    	}else {
    		bulb = new LightBulb();
    	}       
        world.setBulb(bulb);
        return bulb;
    }

	public void addExpectedTestResult(TestCaseExpectation testCaseExpectation) {
		// TODO Auto-generated method stub
		
	}
    
}
