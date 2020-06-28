package de.hpi.mod.sim.setting.infinitewarehouses.detectors;

import java.util.List;

import de.hpi.mod.sim.setting.Position;
import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehousesSetting;
import de.hpi.mod.sim.setting.robot.Robot;

public class InvalidUnloadingDetector extends RobotDetector {

	public InvalidUnloadingDetector(InfiniteWarehousesSetting setting) {
		super(setting);
	}

	boolean invalidUnloadingReported = false;
	
	@Override
	public void robotUpdate(List<Robot> robots) {
		if (!invalidUnloadingReported) {
			for (int i = 0; i < robots.size(); i++) {
				Robot robot = robots.get(i);
				if (robot.getInvalidUnloadingPosition() != null) {
					robot.resetInvalidUnloadingPosition();
					invalidUnloadingReported = true;
					reportInvalidPosition(robot, robot.pos());
					setting.getGridManagement().makePositionInvalid(robot.pos());
				}
			}
		}
	}
	
	@Override
	public void reset() {
		invalidUnloadingReported = false;
		setting.getGridManagement().clearInvalidPositions();
	}

	private void reportInvalidPosition(Robot robot, Position invalidPosition) {
		String reason = "Robot at (" + String.valueOf(invalidPosition.getX()) + 
				"," + String.valueOf(invalidPosition.getY()) + ") unloaded to illegal position!";
		setting.reportInvalidUnloading(robot, reason);
		report(reason, robot);
	}

}