package de.hpi.mod.sim.worlds.infinitewarehouse.detectors;

import java.util.List;

import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.CellType;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.Robot;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.RobotDetector;

public class InvalidTurningDetector extends RobotDetector {
	public InvalidTurningDetector(InfiniteWarehouse world) {
		super(world);
	}

	boolean invalidTurningReported = false;
	
	
	@Override
	public void robotUpdate(List<Robot> robots) {
		if (!invalidTurningReported) {
			for (int i = 0; i < robots.size(); i++) {
				Robot robot = robots.get(i);
				if (world.getGrid().cellType(robot.pos()) == CellType.CHARGER
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

	private void reportInvalidTurning(Robot robot) {
		String reason = "Robot destroyed charging apparature because robot turned on charger!";
		world.reportInvalidTurning(robot, reason);
		report(reason, robot);
	}

}
