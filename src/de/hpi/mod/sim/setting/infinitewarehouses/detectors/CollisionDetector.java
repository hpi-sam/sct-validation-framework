package de.hpi.mod.sim.setting.infinitewarehouses.detectors;

import java.util.List;

import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehousesSetting;
import de.hpi.mod.sim.setting.robot.Robot;
import de.hpi.mod.sim.setting.robot.RobotDetector;

public class CollisionDetector extends RobotDetector {

	public CollisionDetector(InfiniteWarehousesSetting setting) {
		super(setting);
	}

	boolean collisionReported = false;
	

	@Override
	public void robotUpdate(List<Robot> robots) {
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
		setting.reportCollision(robot1, robot2, reason);
		report(reason, robot1, robot2);
	}
}
