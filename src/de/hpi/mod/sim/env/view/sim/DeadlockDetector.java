package de.hpi.mod.sim.env.view.sim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.DriveSimFrame;

public class DeadlockDetector {
	
	private long currentTime = System.currentTimeMillis();
	private int offset = 5000;
	private SimulationWorld simulationWorld;
	private ScenarioManager scenarioManager;
	private Map<Integer, Position> robotPositions = new HashMap<>();
	private boolean deactivated = true;
	private boolean isRunningTest = false;

	public DeadlockDetector(SimulationWorld simulationWorld, ScenarioManager scenarioManager) {
		this.scenarioManager = scenarioManager;
		this.simulationWorld = simulationWorld;
		getRobotPositions();
	}
	
	public void update(){
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
	
	public void setIsRunningTest(boolean isRunningTest) {
		this.isRunningTest = isRunningTest;
	}

	private void checkForDeadlock() {
		List<Robot> robotList = simulationWorld.getRobots();
		
		if(!simulationWorld.isRunning() || deactivated) {
			return;
		}
		if(robotList.isEmpty()) {
			return;
		}
		for(Robot robot : robotList) {
			Position oldPos = robotPositions.get(robot.getID());
			if (oldPos == null) {
				return;
			}
			if(!(oldPos.is(robot.pos()))) {
				return;
			}
		}	
		reportDeadlock();
	}

	private void reportDeadlock() {
		deactivate();
		DriveSimFrame.displayMessage("Deadlock detected!");
		if(isRunningTest) {
			scenarioManager.failCurrentTest();
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
