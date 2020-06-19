package de.hpi.mod.sim.core.testing.tests;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.testing.RobotDescription;
import de.hpi.mod.sim.core.testing.Scenario;
import de.hpi.mod.sim.core.view.DriveSimFrame;
import de.hpi.mod.sim.setting.robot.Robot;

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
