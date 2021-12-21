package de.hpi.mod.sim.worlds.infinitewarehouse.detectors;

import java.util.List;

import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.detectors.RobotDetector;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;

public class InvalidPositionDetector extends RobotDetector {
	public InvalidPositionDetector(InfiniteWarehouse world) {
		super(world);
	}
	
	@Override
	public void robotUpdate(List<Robot> robots) {
		for (int i = 0; i < robots.size(); i++) {
			Robot generalRobot = robots.get(i);
			if (!(generalRobot instanceof WarehouseRobot))
				continue;
			WarehouseRobot robot = (WarehouseRobot) generalRobot;
			if (getWorld().getWarehouseManager().isInvalid(robot.pos())) {
				this.disable();
				reportInvalidPosition(robot, robot.pos());
			}
			if (getWorld().getWarehouseManager().isInvalid(robot.oldPos())) {
				this.disable();
				reportInvalidPosition(robot, robot.oldPos());
			}
			if (getWorld().getWarehouseManager().invalidManoeuvre(robot.oldPos(), robot.pos())) {
				this.disable();
				reportInvalidPosition(robot, robot.pos());
			}
		}
	}

	private void reportInvalidPosition(WarehouseRobot robot, Position invalidPosition) {
		String reason = "Robot at invalid position at: (" + String.valueOf(invalidPosition.getX()) + ","
				+ String.valueOf(invalidPosition.getY()) + ")!";
		getWorld().reportInvalidPosition(robot, reason);
		reportDetectedProblem(reason, robot);
	}
	
	private InfiniteWarehouse getWorld() {
		return (InfiniteWarehouse) world;
	}

}
