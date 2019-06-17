package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.model.NewRobot;

public class NewScenarioRobot extends NewRobot{
	private Position position;
	private Orientation facing;
	private int delay = 0;
	
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
		return sim.addRobotInScenario(position, facing, delay);
	}

	@Override
	public void refreshRobot() {
	}	
}
