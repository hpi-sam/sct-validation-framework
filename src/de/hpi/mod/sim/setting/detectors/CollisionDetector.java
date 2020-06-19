package de.hpi.mod.sim.setting.detectors;

import java.util.List;

import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.testing.Detector;
import de.hpi.mod.sim.setting.robot.Robot;

public class CollisionDetector extends Detector {

	public CollisionDetector(Setting setting) {
		super(setting);
	}

	boolean collisionReported = false;
	

	@Override
	public void update(List<Robot> robots) {
		if (!collisionReported) {
			for (Robot robot1 : robots) {
				for (Robot robot2 : robots) {
					if (robot1 != robot2 && (robot1.pos().is(robot2.pos()) || robot1.oldPos().is(robot2.pos())
							|| robot1.oldPos().is(robot2.oldPos())) && !collisionReported) {
						collisionReported = true;
						reportCollision(robot1, robot2);
					}
				}
			}
		}
	}
	
	@Override
	public void reset() {
		collisionReported = false;
	}

	private void reportCollision(Robot robot1, Robot robot2) {
		String reason = "Collision detected!";
		setting.getFrame().reportCollision(robot1, robot2, reason);
		report(reason, robot1, robot2);
	}
}
