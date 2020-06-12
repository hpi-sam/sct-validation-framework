package de.hpi.mod.sim.env.setting.infinitestations.detectors;

import java.util.List;

import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.setting.Setting;
import de.hpi.mod.sim.env.setting.infinitestations.Robot;
import de.hpi.mod.sim.env.testing.Detector;

public class InvalidUnloadingDetector extends Detector {

	public InvalidUnloadingDetector(Setting setting) {
		super(setting);
	}

	boolean invalidUnloadingReported = false;
	
	@Override
	public void update(List<Robot> robots) {
		if (!invalidUnloadingReported) {
			for (int i = 0; i < robots.size(); i++) {
				Robot robot = robots.get(i);
				if (robot.getInvalidUnloadingPosition() != null) {
					robot.resetInvalidUnloadingPosition();
					invalidUnloadingReported = true;
					reportInvalidPosition(robot, robot.pos());
					setting.getGrid().makePositionInvalid(robot.pos());
				}
			}
		}
	}
	
	@Override
	public void reset() {
		invalidUnloadingReported = false;
		setting.getGrid().clearInvalidPositions();
	}

	private void reportInvalidPosition(Robot robot, Position invalidPosition) {
		String reason = "Robot at (" + String.valueOf(invalidPosition.getX()) + 
				"," + String.valueOf(invalidPosition.getY()) + ") unloaded to illegal position!";
		setting.getFrame().reportInvalidUnloading(robot, reason);
		report(reason, robot);
	}

}