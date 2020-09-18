package de.hpi.mod.sim.worlds.flasher;

import de.hpi.mod.sim.core.scenario.EntitySpecification;

public class BulbSpecification implements EntitySpecification<Bulb> {

    private boolean hasToBeOffForTests, checkOnTimeForTests, checkOffTimeForTests;
    private FlashWorld world;

    public BulbSpecification(boolean hasToBeOffForTests, boolean checkOnTimeForTests, boolean checkOffTimeForTests, FlashWorld world) {
        this.hasToBeOffForTests = hasToBeOffForTests;
        this.checkOnTimeForTests = checkOnTimeForTests;
        this.checkOffTimeForTests = checkOffTimeForTests;
        this.world = world;
    }

    @Override
    public Bulb createEntity() {
        Bulb bulb = new Bulb();
        bulb.setCheckOffTimeForTests(checkOffTimeForTests);
        bulb.setCheckOnTimeForTests(checkOnTimeForTests);
        bulb.setHasToBeOffForTests(hasToBeOffForTests);
        world.setBulb(bulb);
        return bulb;
    }
    
}
