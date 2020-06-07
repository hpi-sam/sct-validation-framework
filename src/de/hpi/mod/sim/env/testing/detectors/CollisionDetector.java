package de.hpi.mod.sim.env.testing.detectors;

import java.util.List;

import de.hpi.mod.sim.env.simulation.robot.Robot;
import de.hpi.mod.sim.env.testing.scenarios.ScenarioManager;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.world.MetaWorld;

public class CollisionDetector {
	MetaWorld world;
	DriveSimFrame frame;
	ScenarioManager scenarioManager;
	boolean collisionReported = false;
	
	public CollisionDetector(ScenarioManager scenarioManager, MetaWorld world, DriveSimFrame frame) {
		this.scenarioManager = scenarioManager;
		this.world = world;
		this.frame = frame;
	}

	public void update() {
		if(!collisionReported) {
			List<Robot> robots = world.getRobots();
			for (Robot robot1 : robots) {
				for (Robot robot2: robots) {
					if (robot1 != robot2
						&& (robot1.pos().is(robot2.pos()) || robot1.oldPos().is(robot2.pos()) || robot1.oldPos().is(robot2.oldPos()))
						&& !collisionReported) {
						collisionReported = true;
						reportCollision(robot1, robot2);
					}
				}
			}
		}
	}
	
	public void reset() {
		collisionReported = false;
	}

	private void reportCollision(Robot robot1, Robot robot2) {
		String reason = "Collision detected!";
		frame.reportCollision(robot1, robot2, reason);
		world.setHighlightedRobot1(robot1);
		world.setHighlightedRobot2(robot2);
		if(world.isRunning())
			world.toggleRunning();
		if(scenarioManager.isRunningTest()) {
			scenarioManager.failCurrentTest(reason);
		}
	}
}
