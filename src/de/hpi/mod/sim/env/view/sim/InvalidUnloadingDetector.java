package de.hpi.mod.sim.env.view.sim;

import java.util.List;

import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.DriveSimFrame;

public class InvalidUnloadingDetector {
	SimulationWorld world;
	DriveSimFrame frame;
	ScenarioManager scenarioManager;
	boolean invalidUnloadingReported = false;
	
	public InvalidUnloadingDetector(ScenarioManager scenarioManager, SimulationWorld world, DriveSimFrame frame) {
		this.scenarioManager = scenarioManager;
		this.world = world;
		this.frame = frame;
	}
	
	public void update() {
		if(!invalidUnloadingReported) {
			List<Robot> robots = world.getRobots();
			for (int i= 0; i<robots.size(); i++) {
					Robot robot = robots.get(i);
					if (robot.getInvalidUnloadingPosition() != null) {
						robot.resetInvalidUnloadingPosition();
						invalidUnloadingReported = true;
						reportInvalidPosition(robot, robot.pos());
						world.getSimulator().getServerGridManagement().makePositionInvalid(robot.pos());
					}
			}
		}
	}
	
	public void reset() {
		invalidUnloadingReported = false;
		world.getSimulator().getServerGridManagement().clearInvalidPositions();
	}

	private void reportInvalidPosition(Robot robot, Position invalidPosition) {
		frame.reportInvalidUnloading(robot, invalidPosition);
		world.setHighlightedRobot1(robot);
		
		if(scenarioManager.isRunningTest()) {
			scenarioManager.failCurrentTest();
		}
	}

}