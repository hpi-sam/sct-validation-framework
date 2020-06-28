package de.hpi.mod.sim.setting.infinitewarehouses.detectors;

import java.util.List;

import de.hpi.mod.sim.setting.infinitewarehouses.CellType;
import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehousesSetting;
import de.hpi.mod.sim.setting.robot.Robot;

public class InvalidTurningDetector extends RobotDetector {
	public InvalidTurningDetector(InfiniteWarehousesSetting setting) {
		super(setting);
	}

	boolean invalidTurningReported = false;
	
	
	@Override
	public void robotUpdate(List<Robot> robots) {
		if (!invalidTurningReported) {
			for (int i = 0; i < robots.size(); i++) {
				Robot robot = robots.get(i);
				if (setting.getGrid().cellType(robot.pos()) == CellType.BATTERY
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
		String reason = "Robot destroyed charging apparature because robot turned on battery!";
		setting.reportInvalidTurning(robot, reason);
		report(reason, robot);
	}

}
