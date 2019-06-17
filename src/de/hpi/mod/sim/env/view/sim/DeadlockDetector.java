package de.hpi.mod.sim.env.view.sim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.DriveSimFrame;

public class DeadlockDetector {
	
	private long currentTime = System.currentTimeMillis();
	private long defaultOffset = 7000;
	private long offset = 7000;
	private SimulationWorld simulationWorld;
	private ScenarioManager scenarioManager;
	private DriveSimFrame frame;
	private Map<Integer, Position> robotPositions = new HashMap<>();
	private boolean deactivated = true;

	public DeadlockDetector(SimulationWorld simulationWorld, ScenarioManager scenarioManager, DriveSimFrame frame) {
		this.scenarioManager = scenarioManager;
		this.simulationWorld = simulationWorld;
		this.frame = frame;
		getRobotPositions();
	}
	
	public void update(){
		if(!simulationWorld.isRunning() || deactivated) {
			return;
		}
		
		offset = (long) Math.max(defaultOffset, defaultOffset / SimulatorConfig.getRobotSpeedFactor());
		if(currentTime + offset <= System.currentTimeMillis()) {
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
		List<Robot> robotList = simulationWorld.getRobots();
		
		if(robotList.isEmpty()) {
			return;
		}
		
		for(Robot robot : robotList) {
			Position oldPosition = robotPositions.get(robot.getID());
			if (oldPosition == null) {
				return;
			}
			if(!(oldPosition.is(robot.pos()))) {
				return;
			}
		}	
		reportDeadlock();
	}

	private void reportDeadlock() {
		deactivate();
		String reason = "Deadlock detected!";
		frame.displayMessage(reason, DriveSimFrame.MENU_RED);
		if(scenarioManager.isRunningTest()) {
			scenarioManager.failCurrentTest(reason);
		}
	}

	private void getCurrentTime() {
		currentTime = System.currentTimeMillis();
	}

	private void getRobotPositions() {
		List<Robot> robotList = simulationWorld.getRobots();
		for(Robot robot : robotList){
			robotPositions.put(robot.getID(), robot.pos());
		}
	}
}
