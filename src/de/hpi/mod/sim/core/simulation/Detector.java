package de.hpi.mod.sim.core.simulation;

import java.util.List;

import de.hpi.mod.sim.core.World;

public abstract class Detector {

    protected World world;

    private boolean enabled = false;
    private boolean neededForTests = true;
    private boolean neededForScenarios = true; 

    protected Detector(World world, boolean forTests, boolean forScenarios) {
        this.world = world;
        this.neededForTests = forTests;
        this.neededForScenarios = forScenarios;
    }

    protected Detector(World world) {
    	this(world, true, true);
    }

    public void reportDetectedProblem(String reason, IHighlightable involved1, IHighlightable involved2) {
        if (involved1 != null)
            world.getSimulationRunner().getAnimationPanel().setHighlighted1(involved1);
        
        if (involved2 != null)
            world.getSimulationRunner().getAnimationPanel().setHighlighted2(involved2);

        if (world.getSimulationRunner().isRunning())
            world.getSimulationRunner().toggleRunning();
        
        if (world.getScenarioManager().isRunningTest())
            world.getScenarioManager().failCurrentTest(reason);
    }

    public void reportDetectedProblem(String reason, IHighlightable involved) {
        reportDetectedProblem(reason, involved, null);
    }

    public void reportDetectedProblem(String reason) {
        reportDetectedProblem(reason, null, null);
    }

    public abstract void update(List<? extends Entity> entities);

    public void reset() {};

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

	public boolean isNeededForTests() {
		return neededForTests;
	}

	public boolean isNeededForScenarios() {
		return neededForScenarios;
	}
}