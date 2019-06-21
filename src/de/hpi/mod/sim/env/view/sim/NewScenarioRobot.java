package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.model.NewRobot;

public class NewScenarioRobot extends NewRobot{
	private Position position;
	private Orientation facing;
	private int delay = 0;
	private boolean isAlone = false;
	
	public NewScenarioRobot(Position position, Orientation facing) {
		this.position = position;
		this.facing = facing;
	}
	
	public NewScenarioRobot(Position position, Orientation facing, int delay) {
		this.position = position;
		this.facing = facing;
		this.delay = delay;
	}

	@Override
	public Robot register(SimulationWorld sim) {
		Robot robot = sim.addRobotInScenario(position, facing, delay);
		robot.setIsAlone(isAlone);
		return robot;
	}

	@Override
	public void refreshRobot() {
	}

	public void setIsAlone(boolean b) {
		this.isAlone = b;
	}	
}
