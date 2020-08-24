package de.hpi.mod.sim.worlds.infinitewarehouse.detectors;

import java.util.List;

import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.detectors.RobotDetector;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.CellType;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;

public class InvalidTurningDetector extends RobotDetector {
	public InvalidTurningDetector(InfiniteWarehouse world) {
		super(world);
	}

	boolean invalidTurningReported = false;
	
	
	@Override
	public void robotUpdate(List<Robot> robots) {
		if (!invalidTurningReported) {
			for (int i = 0; i < robots.size(); i++) {
				Robot general_robot = robots.get(i);
				if (!(general_robot instanceof WarehouseRobot))
					continue;
				WarehouseRobot robot = (WarehouseRobot) general_robot;
				if (getWorld().getGridManager().cellType(robot.pos()) == CellType.CHARGER
						&& (robot.getDriveManager().isTurningLeft() || robot.getDriveManager().isTurningRight())) {
					invalidTurningReported = true;
					reportInvalidTurning(robot);
				}
			}
		}
	}
	
	@Override
	public void reset() {
		invalidTurningReported = false;
	}

	private void reportInvalidTurning(WarehouseRobot robot) {
		String reason = "Robot destroyed charging apparature because robot turned on charger!";
		getWorld().reportInvalidTurning(robot, reason);
		report(reason, robot);
	}
	
	private InfiniteWarehouse getWorld() {
		return (InfiniteWarehouse) world;
	}

}
