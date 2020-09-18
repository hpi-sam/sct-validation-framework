package de.hpi.mod.sim.worlds.flasher;

import java.util.List;

import de.hpi.mod.sim.core.scenario.EntitySpecification;

public class StarterSpecification implements EntitySpecification<Starter> {

    private List<Integer> blinks;
    private List<Float> pauses;
    private boolean onRepeat;
    private FlashWorld world;

    public StarterSpecification(List<Integer> blinks, List<Float> pauses, boolean onRepeat, FlashWorld world) {
        this.blinks = blinks;
        this.pauses = pauses;
        this.onRepeat = onRepeat;
        this.world = world;
    }

    @Override
    public Starter createEntity() {
        Starter starter = new Starter(blinks, pauses, onRepeat, world);
        world.setStarter(starter);
        return starter;
    }

}
