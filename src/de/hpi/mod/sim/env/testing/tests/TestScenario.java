package de.hpi.mod.sim.env.testing.tests;

import de.hpi.mod.sim.env.Setting;
import de.hpi.mod.sim.env.simulation.robot.Robot;
import de.hpi.mod.sim.env.testing.RobotDescription;
import de.hpi.mod.sim.env.testing.Scenario;
import de.hpi.mod.sim.env.view.DriveSimFrame;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class TestScenario extends Scenario {

    private List<Robot> robots = new CopyOnWriteArrayList<>();
    private boolean hasRun = false;
    private boolean alreadyPrinted = false;

    @Override
    public void loadScenario(Setting setting) { 
        robots.clear();
        List<RobotDescription> newRobots = initializeScenario();
        for (RobotDescription robot : newRobots) {
            robots.add(robot.register(setting));
        }
        hasRun = true;
    }

    public boolean isPassed() {
        return hasRun && robots.stream().allMatch(Robot::hasPassedAllTestCriteria);
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

	public void notifyFailToUser(DriveSimFrame frame, String reason) {
		if(!alreadyPrinted) {
    		frame.displayMessage("Test \"" + name + "\" failed beacuse " + reason, DriveSimFrame.MENU_RED);
    		alreadyPrinted = true;
    	}
	}
}
