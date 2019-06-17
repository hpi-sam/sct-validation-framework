package de.hpi.mod.sim.env.view.model;

import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class TestScenario extends Scenario {

    private List<Robot> robots = new CopyOnWriteArrayList<>();
    private boolean hasRun = false;
    private boolean alreadyPrinted = false;

    @Override
    public void loadScenario(SimulationWorld sim) {
        robots.clear();
        List<NewRobot> newRobots = initializeScenario();
        for (NewRobot robot : newRobots) {
            robots.add(robot.register(sim));
        }
        hasRun = true;
    }

    public boolean isPassed() {
        return hasRun && robots.stream().allMatch(Robot::hasReachedAllTargets);
    }
    
    public void notifySuccessToUser(DriveSimFrame frame) {
    	if(!alreadyPrinted) {
    		frame.displayMessage("Finished test \"" + name + "\" succesfully", DriveSimFrame.MENU_GREEN);
    		alreadyPrinted = true;
    	}
    }
    
	public void resetTest() {
		hasRun = false;
		alreadyPrinted = false;
	}

	public void notifyFailToUser(DriveSimFrame frame) {
		if(!alreadyPrinted) {
    		frame.displayMessage("Test \"" + name + "\" failed!", DriveSimFrame.MENU_RED);
    		alreadyPrinted = true;
    	}
	}
}
