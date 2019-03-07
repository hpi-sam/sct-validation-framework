package de.hpi.mod.sim.env.view.model;

import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import java.util.ArrayList;
import java.util.List;

public abstract class TestScenario extends Scenario {

    private List<Robot> robots = new ArrayList<>();
    private boolean hasRun = false;
    private boolean alreadyPrinted = false;
    private boolean active = false;


    @Override
    public void loadScenario(SimulationWorld sim) {
        robots.clear();
        List<NewRobot> newRobots = initializeScenario();
        for (NewRobot r : newRobots) {
            robots.add(r.register(sim));
        }
        hasRun = true;
    }

    public boolean isPassed() {
        return hasRun && robots.stream().allMatch(Robot::hasReachedAllTargets);
    }
    
    public void notifySuccessToUser() {
    	if(!alreadyPrinted) {
    		DriveSimFrame.displayMessage("Finished test \"" + name + "\" succesfully");
    		alreadyPrinted = true;
    	}
    }
    
    public void setActive(boolean active) {
    	this.active = active;
    }
    
    public boolean isActive() {
    	return active;
    }

	public void resetTest() {
		hasRun = false;
		alreadyPrinted = false;
	}

	public void notifyFailToUser() {
		if(!alreadyPrinted) {
    		DriveSimFrame.displayMessage("Test \"" + name + "\" failed!");
    		alreadyPrinted = true;
    	}
	}
}
