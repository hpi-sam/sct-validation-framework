package de.hpi.mod.sim.setting.infinitewarehouses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.model.Position;
import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.core.simulation.robot.Robot;
import de.hpi.mod.sim.core.testing.Detector;

public class DeadlockDetector extends Detector {

	public DeadlockDetector(Setting setting) {
		super(setting);
		getRobotPositions();
	}

	private long currentTime = System.currentTimeMillis();
	private long defaultOffset = 12000;
	private long offset = 7000;
	private Map<Integer, Position> robotPositions = new HashMap<>();
	private boolean deactivated = true;

	@Override
	public void update(List<Robot> robots) {
		if (!setting.getSimulation().isRunning() || deactivated) {
			return;
		}

		offset = (long) Math.max(defaultOffset, defaultOffset / SimulatorConfig.getRobotSpeedFactor());
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
		setting.getFrame().reportDeadlock(reason);
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
