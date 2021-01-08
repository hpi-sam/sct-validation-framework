package de.hpi.mod.sim.worlds.flasher.entities;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.flasher.FlashWorld;

public class LightBulbSpecification implements EntitySpecification<LightBulb> {

    private boolean hasToBeOffForTests, checkOnTimeForTests, checkOffTimeForTests;
    private FlashWorld world;

    public LightBulbSpecification(boolean hasToBeOffForTests, boolean checkOnTimeForTests, boolean checkOffTimeForTests, FlashWorld world) {
        this.hasToBeOffForTests = hasToBeOffForTests;
        this.checkOnTimeForTests = checkOnTimeForTests;
        this.checkOffTimeForTests = checkOffTimeForTests;
        this.world = world;
    }

    @Override
    public LightBulb createEntity() {
        LightBulb bulb = new LightBulb();
        bulb.setCheckOffTimeForTests(checkOffTimeForTests);
        bulb.setCheckOnTimeForTests(checkOnTimeForTests);
        bulb.setHasToBeOffForTests(hasToBeOffForTests);
        world.setBulb(bulb);
        return bulb;
    }
    
}
