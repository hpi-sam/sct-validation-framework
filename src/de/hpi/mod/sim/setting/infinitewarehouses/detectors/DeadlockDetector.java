package de.hpi.mod.sim.setting.infinitewarehouses.detectors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.setting.Position;
import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehouseSimConfig;
import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehousesSetting;
import de.hpi.mod.sim.setting.robot.Robot;

public class DeadlockDetector extends RobotDetector {

	public DeadlockDetector(InfiniteWarehousesSetting setting) {
		super(setting);
		getRobotPositions();
	}

	private long currentTime = System.currentTimeMillis();
	private long defaultOffset = 12000;
	private long offset = 7000;
	private Map<Integer, Position> robotPositions = new HashMap<>();
	private boolean deactivated = true;

	@Override
	public void robotUpdate(List<Robot> robots) {
		if (!setting.getSimulation().isRunning() || deactivated) {
			return;
		}

		offset = (long) Math.max(defaultOffset, defaultOffset / InfiniteWarehouseSimConfig.getEntitySpeedFactor());
		if (currentTime + offset <= System.currentTimeMillis()) {
			checkForDeadlock();
			getRobotPositions();
			getCurrentTime();
		}
	}

	public void deactivate() {
		deactivated = true;
	}

	public void reactivate() {
		deactivated = false;
	}

	private void checkForDeadlock() {
		List<Robot> robotList = setting.getRobots();

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
		setting.reportDeadlock(reason);
		if (setting.getScenarioManager().isRunningTest()) {
			setting.getScenarioManager().failCurrentTest(reason);
		}
	}

	private void getCurrentTime() {
		currentTime = System.currentTimeMillis();
	}

	private void getRobotPositions() {
		List<Robot> robotList = setting.getRobots();  
		for (Robot robot : robotList) {
			robotPositions.put(robot.getID(), robot.pos());
		}
	}

	@Override
	public void reset() {
		reactivate();
	}
}
