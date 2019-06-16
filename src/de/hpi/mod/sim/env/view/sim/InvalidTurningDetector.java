package de.hpi.mod.sim.env.view.sim;

import java.util.List;

import de.hpi.mod.sim.env.model.CellType;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.DriveSimFrame;

public class InvalidTurningDetector {
	SimulationWorld world;
	DriveSimFrame frame;
	ScenarioManager scenarioManager;
	boolean invalidTurningReported = false;
	
	public InvalidTurningDetector(ScenarioManager scenarioManager, SimulationWorld world, DriveSimFrame frame) {
		this.scenarioManager = scenarioManager;
		this.world = world;
		this.frame = frame;
	}
	
	public void update() {
		if(!invalidTurningReported) {
			List<Robot> robots = world.getRobots();
			for (int i= 0; i<robots.size(); i++) {
					Robot robot = robots.get(i);
					if (world.getSimulator().getServerGridManagement().cellType(robot.pos()) == CellType.BATTERY && 
							(robot.getDriveManager().isTurningLeft() || robot.getDriveManager().isTurningRight())) {
						invalidTurningReported = true;
						reportInvalidTurning(robot);
					}
			}
		}
	}
	
	public void reset() {
		invalidTurningReported = false;
	}

	private void reportInvalidTurning(Robot robot) {
		frame.reportInvalidTurning(robot);
		world.setHighlightedRobot1(robot);
		if(world.isRunning())
			world.toggleRunning();
		if(scenarioManager.isRunningTest()) {
			scenarioManager.failCurrentTest();
		}
	}

}
