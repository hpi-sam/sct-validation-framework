package de.hpi.mod.sim.worlds.infinitewarehouse.detectors;

import java.util.List;

import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.Robot;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.RobotDetector;

public class CollisionDetector extends RobotDetector {

	public CollisionDetector(InfiniteWarehouse world) {
		super(world);
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
		world.reportCollision(robot1, robot2, reason);
		report(reason, robot1, robot2);
	}
}
