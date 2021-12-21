package de.hpi.mod.sim.worlds.abstract_robots.detectors;

import java.util.List;

import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotWorld;

public class CollisionDetector extends RobotDetector {

	public CollisionDetector(RobotWorld world) {
		super(world);
	}

	@Override
	public void robotUpdate(List<Robot> robots) {
		for (Robot robot1 : robots) {
			for (Robot robot2 : robots) {
				if (robot1 != robot2 && (robot1.pos().is(robot2.pos()) || robot1.oldPos().is(robot2.pos())
						|| robot1.oldPos().is(robot2.oldPos()))) {
					this.deactivate();
					reportCollision(robot1, robot2);
				}
			}
		}
	}

	private void reportCollision(Robot robot1, Robot robot2) {
		String reason = "Collision detected!";
		world.reportCollision(robot1, robot2, reason);
		report(reason, robot1, robot2);
	}
}
