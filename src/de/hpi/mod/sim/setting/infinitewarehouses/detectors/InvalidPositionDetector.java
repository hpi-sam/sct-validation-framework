package de.hpi.mod.sim.setting.infinitewarehouses.detectors;

import java.util.List;

import de.hpi.mod.sim.setting.Position;
import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehousesSetting;
import de.hpi.mod.sim.setting.robot.Robot;

public class InvalidPositionDetector extends RobotDetector {
	public InvalidPositionDetector(InfiniteWarehousesSetting setting) {
		super(setting);
	}

	boolean invalidPositionReported = false;
	
	@Override
	public void robotUpdate(List<Robot> robots) {
		if (!invalidPositionReported) {
			for (int i = 0; i < robots.size(); i++) {
				Robot robot = robots.get(i);
				if (setting.getGridManagement().isInvalid(robot.pos())) {
					invalidPositionReported = true;
					reportInvalidPosition(robot, robot.pos());
				}
				if (setting.getGridManagement().isInvalid(robot.oldPos())) {
					invalidPositionReported = true;
					reportInvalidPosition(robot, robot.oldPos());
				}
				if (setting.getGridManagement().invalidManoeuvre(robot.oldPos(), robot.pos())) {
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
		setting.reportInvalidPosition(robot, reason);
		report(reason, robot);
	}

}
