package de.hpi.mod.sim.core.testing.scenarios;

import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.model.Orientation;
import de.hpi.mod.sim.core.model.Position;
import de.hpi.mod.sim.core.testing.RobotDescription;
import de.hpi.mod.sim.setting.robot.Robot;

public class ScenarioRobotDescription extends RobotDescription{
	private Position position;
	private Orientation facing;
	private int delay = 0;
	private boolean isAlone = false;
	
	public ScenarioRobotDescription(Position position, Orientation facing) {
		this.position = position;
		this.facing = facing;
	}
	
	public ScenarioRobotDescription(Position position, Orientation facing, int delay) {
		this.position = position;
		this.facing = facing;
		this.delay = delay;
	}

	@Override
	public Robot register(Setting setting) {
		Robot robot = setting.addRobotInScenario(position, facing, delay);
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
