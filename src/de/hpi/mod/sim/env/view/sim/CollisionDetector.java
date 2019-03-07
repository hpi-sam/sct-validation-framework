package de.hpi.mod.sim.env.view.sim;

import java.util.List;

import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.DriveSimFrame;

public class CollisionDetector {
	SimulationWorld world;
	DriveSimFrame frame;
	ScenarioManager scenarioManager;
	boolean collisionReported = false;
	
	public CollisionDetector(ScenarioManager scenarioManager, SimulationWorld world, DriveSimFrame frame) {
		this.scenarioManager = scenarioManager;
		this.world = world;
		this.frame = frame;
	}

	public void update() {
		if(!collisionReported) {
			List<Robot> robots = world.getRobots();
			for (Robot r1 : robots) {
				for (Robot r2: robots) {
					if (r1 != r2) {
						if (r1.pos().is(r2.pos()) || r1.oldPos().is(r2.pos()) || r1.oldPos().is(r2.oldPos()))
							reportCollision(r1, r2);
					}
				}
			}
		}
	}
	
	public void reset() {
		collisionReported = false;
	}

	private void reportCollision(Robot r1, Robot r2) {
		collisionReported = true;
		frame.reportCollision(r1, r2);
		if(scenarioManager.isRunningTest()) {
			scenarioManager.failCurrentTest();
		}
	}
}
