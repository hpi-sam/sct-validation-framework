package de.hpi.mod.sim.core.scenario;

import java.util.List;

import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.World;

public abstract class Detector {

    protected World world;

    private boolean activated = false;

    protected Detector(World world) {
        this.world = world;
    }

    public void report(String reason, IHighlightable involved1, IHighlightable involved2) {
        if (involved1 != null)
            world.getSimulationRunner().getAnimationPanel().setHighlighted1(involved1);
        if (involved2 != null)
            world.getSimulationRunner().getAnimationPanel().setHighlighted2(involved2);

        if (world.getSimulationRunner().isRunning())
            world.getSimulationRunner().toggleRunning();
        if (world.getScenarioManager().isRunningTest()) {
            world.getScenarioManager().failCurrentTest(reason);
        }
    }

    public void report(String reason, IHighlightable involved) {
        report(reason, involved, null);
    }

    public void report(String reason) {
        report(reason, null, null);
    }

    public abstract void update(List<? extends Entity> entities);

    public abstract void reset();

    public boolean isActivated() {
        return activated;
    }

    public void activate() {
        activated = true;
    }

    public void deactivate() {
        activated = false;
    }
}