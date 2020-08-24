package de.hpi.mod.sim.worlds.abstract_robots.detectors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.worlds.abstract_grid.GridConfiguration;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotWorld;

public class DeadlockDetector extends RobotDetector {

	public DeadlockDetector(RobotWorld world) {
		super(world);
		getRobotPositions();
	}

	private long currentTime = System.currentTimeMillis();
	private long defaultOffset = 12000;
	private long offset = 7000;
	private Map<Integer, Position> robotPositions = new HashMap<>();

	@Override
	public void robotUpdate(List<Robot> robots) {
		if (!world.getSimulationRunner().isRunning() || !isActivated()) {
			return;
		}

		offset = (long) Math.max(defaultOffset, defaultOffset / GridConfiguration.getEntitySpeedFactor());
		if (currentTime + offset <= System.currentTimeMillis()) {
			checkForDeadlock();
			getRobotPositions();
			getCurrentTime();
		}
	}

	private void checkForDeadlock() {
		List<Robot> robotList = world.getRobots();

		if (robotList.isEmpty()) {
			return;
		}

		for (Robot robot : robotList) {
			Position oldPosition = robotPositions.get(robot.getID());
			if (oldPosition == null) {
				return;
			}
			if (!(oldPosition.is(robot.pos()))) {
				return;
			}
		}
		reportDeadlock();
	}

	private void reportDeadlock() {
		deactivate();
		String reason = "Deadlock detected!";
		world.reportDeadlock(reason);
		if (world.getScenarioManager().isRunningTest()) {
			world.getScenarioManager().failCurrentTest(reason);
		}
	}

	private void getCurrentTime() {
		currentTime = System.currentTimeMillis();
	}

	private void getRobotPositions() {
		List<Robot> robotList = world.getRobots();
		for (Robot robot : robotList) {
			robotPositions.put(robot.getID(), robot.pos());
		}
	}

	@Override
	public void reset() {
		activate();
	}
}
