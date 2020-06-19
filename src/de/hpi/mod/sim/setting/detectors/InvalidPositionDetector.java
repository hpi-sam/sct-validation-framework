package de.hpi.mod.sim.setting.detectors;

import java.util.List;

import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.model.Position;
import de.hpi.mod.sim.core.simulation.robot.Robot;
import de.hpi.mod.sim.core.testing.Detector;

public class InvalidPositionDetector extends Detector {
	public InvalidPositionDetector(Setting setting) {
		super(setting);
	}

	boolean invalidPositionReported = false;
	
	@Override
	public void update(List<Robot> robots) {
		if (!invalidPositionReported) {
			for (int i = 0; i < robots.size(); i++) {
				Robot robot = robots.get(i);
				if (setting.getGrid().isInvalid(robot.pos())) {
					invalidPositionReported = true;
					reportInvalidPosition(robot, robot.pos());
				}
				if (setting.getGrid().isInvalid(robot.oldPos())) {
					invalidPositionReported = true;
					reportInvalidPosition(robot, robot.oldPos());
				}
				if (setting.getGrid().invalidManoeuvre(robot.oldPos(), robot.pos())) {
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
		setting.getFrame().reportInvalidPosition(robot, reason);
		report(reason, robot);
	}

}
