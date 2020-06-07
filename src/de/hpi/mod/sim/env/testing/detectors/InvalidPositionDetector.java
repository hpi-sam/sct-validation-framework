package de.hpi.mod.sim.env.testing.detectors;

import java.util.List;

import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.simulation.robot.Robot;
import de.hpi.mod.sim.env.testing.scenarios.ScenarioManager;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

public class InvalidPositionDetector {
	SimulationWorld world;
	DriveSimFrame frame;
	ScenarioManager scenarioManager;
	boolean invalidPositionReported = false;
	
	public InvalidPositionDetector(ScenarioManager scenarioManager, SimulationWorld world, DriveSimFrame frame) {
		this.scenarioManager = scenarioManager;
		this.world = world;
		this.frame = frame;
	}
	
	public void update() {
		if(!invalidPositionReported) {
			List<Robot> robots = world.getRobots();
			for (int i= 0; i<robots.size(); i++) {
					Robot robot = robots.get(i);
					if (world.getSimulator().getServerGridManagement().isInvalid(robot.pos())) {
						invalidPositionReported = true;
						reportInvalidPosition(robot, robot.pos());
					}
					if (world.getSimulator().getServerGridManagement().isInvalid(robot.oldPos())) {
						invalidPositionReported = true;
						reportInvalidPosition(robot, robot.oldPos());
					}
					if(world.getSimulator().getServerGridManagement().invalidManoeuvre(robot.oldPos(), robot.pos())) {
						invalidPositionReported = true;
						reportInvalidPosition(robot, robot.pos());
					}
			}
		}
	}
	
	public void reset() {
		invalidPositionReported = false;
	}

	private void reportInvalidPosition(Robot robot, Position invalidPosition) {
		String reason = "Robot at invalid position at: (" + String.valueOf(invalidPosition.getX()) + 
				"," + String.valueOf(invalidPosition.getY()) + ")!";
		frame.reportInvalidPosition(robot, reason);
		world.setHighlightedRobot1(robot);
		if(world.isRunning())
			world.toggleRunning();
		if(scenarioManager.isRunningTest()) {
			scenarioManager.failCurrentTest(reason);
		}
	}

}
