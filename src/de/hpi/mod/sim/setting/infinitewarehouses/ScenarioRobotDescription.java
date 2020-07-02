package de.hpi.mod.sim.setting.infinitewarehouses;

import de.hpi.mod.sim.setting.grid.Orientation;
import de.hpi.mod.sim.setting.grid.Position;
import de.hpi.mod.sim.setting.infinitewarehouses.env.RobotManagement;
import de.hpi.mod.sim.setting.robot.Robot;

public class ScenarioRobotDescription extends RobotDescription {
	private Position position;
	private Orientation facing;
	private int delay = 0;
	private boolean isAlone = false;
	
	public ScenarioRobotDescription(RobotManagement robots, Position position, Orientation facing) {
		super(robots);
		this.position = position;
		this.facing = facing;
	}
	
	public ScenarioRobotDescription(RobotManagement robots, Position position, Orientation facing, int delay) {
		super(robots);
		this.position = position;
		this.facing = facing;
		this.delay = delay;
	}

	@Override
	public Robot getRobot(RobotManagement robots) {
		Robot robot = robots.addRobotInScenario(position, facing, delay);
		robot.setIsAlone(isAlone);
		return robot;
	}

	@Override
	public void refreshEntity() {
	}

	public void setIsAlone(boolean b) {
		this.isAlone = b;
	}	
}
