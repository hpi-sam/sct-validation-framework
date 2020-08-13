package de.hpi.mod.sim.worlds.infinitewarehouse.detectors;

import java.util.List;

import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.Robot;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.RobotDetector;

public class InvalidPositionDetector extends RobotDetector {
	public InvalidPositionDetector(InfiniteWarehouse world) {
		super(world);
	}

	boolean invalidPositionReported = false;
	
	@Override
	public void robotUpdate(List<Robot> robots) {
		if (!invalidPositionReported) {
			for (int i = 0; i < robots.size(); i++) {
				Robot robot = robots.get(i);
				if (world.getGridManager().isInvalid(robot.pos())) {
					invalidPositionReported = true;
					reportInvalidPosition(robot, robot.pos());
				}
				if (world.getGridManager().isInvalid(robot.oldPos())) {
					invalidPositionReported = true;
					reportInvalidPosition(robot, robot.oldPos());
				}
				if (world.getGridManager().invalidManoeuvre(robot.oldPos(), robot.pos())) {
					invalidPositionReported = true;
					reportInvalidPosition(robot, robot.pos());
				}
			}
		}
	}
	
	@Override
	public void reset() {
		invalidPositionReported = false;
	}

	private void reportInvalidPosition(Robot robot, Position invalidPosition) {
		String reason = "Robot at invalid position at: (" + String.valueOf(invalidPosition.getX()) + 
				"," + String.valueOf(invalidPosition.getY()) + ")!";
		world.reportInvalidPosition(robot, reason);
		report(reason, robot);
	}

}
