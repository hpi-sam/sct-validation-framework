package de.hpi.mod.sim.setting.infinitewarehouses;

import de.hpi.mod.sim.setting.grid.Orientation;
import de.hpi.mod.sim.setting.grid.Position;
import de.hpi.mod.sim.setting.infinitewarehouses.env.RobotDispatcher;
import de.hpi.mod.sim.setting.robot.Robot;

public class ScenarioRobotDescription extends RobotDescription {
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
	public Robot getRobot(RobotDispatcher robotDispatcher) {
		Robot robot = robotDispatcher.addRobotInScenario(position, facing, delay);
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
