package de.hpi.mod.sim.core.scenario;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.view.SimulatorFrame;

public abstract class TestScenario extends Scenario {

    private boolean hasRun = false;
    private boolean alreadyPrinted = false;

    @Override
    public void loadScenario(World world) {
        super.loadScenario(world);
        hasRun = true;
    }

    @Override
	public boolean isDetectorNeeded(Detector d) {
		return d.isNeededForTests();
	}

    public boolean isPassed() {
        return hasRun && entities.stream().allMatch(Entity::hasPassedAllTestCriteria);
    }
    
    public void notifySuccessToUser(SimulatorFrame frame) {
    	if(!alreadyPrinted) {
    		frame.displayMessage("Finished test \"" + name + "\" succesfully", SimulatorFrame.MENU_GREEN);
    		alreadyPrinted = true;
    	}
    }
    
	public void resetTest() {
		hasRun = false;
		alreadyPrinted = false;
	}

	public void notifyFailToUser(SimulatorFrame frame, String reason) {
		if(!alreadyPrinted) {
    		frame.displayMessage("Test \"" + name + "\" failed because " + reason, SimulatorFrame.MENU_RED);
    		alreadyPrinted = true;
    	}
	}
}
