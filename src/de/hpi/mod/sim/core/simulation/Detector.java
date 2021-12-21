package de.hpi.mod.sim.core.simulation;

import java.util.List;

import de.hpi.mod.sim.core.World;

public abstract class Detector {

    protected World world;

    private boolean activated = false;
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

    public void report(String reason, IHighlightable involved1, IHighlightable involved2) {
        if (involved1 != null)
            world.getSimulationRunner().getAnimationPanel().setHighlighted1(involved1);
        
        if (involved2 != null)
            world.getSimulationRunner().getAnimationPanel().setHighlighted2(involved2);

        if (world.getSimulationRunner().isRunning())
            world.getSimulationRunner().toggleRunning();
        
        if (world.getScenarioManager().isRunningTest())
            world.getScenarioManager().failCurrentTest(reason);
    }

    public void report(String reason, IHighlightable involved) {
        report(reason, involved, null);
    }

    public void report(String reason) {
        report(reason, null, null);
    }

    public abstract void update(List<? extends Entity> entities);

    public void reset() {};

    public boolean isActivated() {
        return activated;
    }

    public void activate() {
    	System.out.println("ENABLE "+this.toString());
        activated = true;
    }

    public void deactivate() {
    	System.out.println("DISABLE "+this.toString());
        activated = false;
    }

	public boolean isNeededForTests() {
		return neededForTests;
	}

	public boolean isNeededForScenarios() {
		return neededForScenarios;
	}
}