package de.hpi.mod.sim.env.setting.infinitestations.detectors;

import java.util.List;

import de.hpi.mod.sim.env.Setting;
import de.hpi.mod.sim.env.model.CellType;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.testing.Detector;

public class InvalidTurningDetector extends Detector {
	public InvalidTurningDetector(Setting setting) {
		super(setting);
	}

	boolean invalidTurningReported = false;
	
	
	@Override
	public void update(List<Robot> robots) {
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
		setting.getFrame().reportInvalidTurning(robot, reason);
		report(reason, robot);
	}

}
