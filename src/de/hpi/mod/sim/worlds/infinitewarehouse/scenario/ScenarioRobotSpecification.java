package de.hpi.mod.sim.worlds.infinitewarehouse.scenario;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.RobotSpecification;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.WarehouseManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;

public class ScenarioRobotSpecification extends RobotSpecification<WarehouseRobot, WarehouseManager> {
	private Position position;
	private Orientation facing;
	private int delay = 0;
	private boolean isAlone = false;
	
	public ScenarioRobotSpecification(WarehouseManager robots, Position position, Orientation facing) {
		super(robots);
		this.position = position;
		this.facing = facing;
	}
	
	public ScenarioRobotSpecification(WarehouseManager robots, Position position, Orientation facing, int delay) {
		super(robots);
		this.position = position;
		this.facing = facing;
		this.delay = delay;
	}

	@Override
	public WarehouseRobot createRobot(WarehouseManager robots) {
		WarehouseRobot robot = robots.createScenarioRobot(position, facing, delay);
		robot.setIsAlone(isAlone);
		return robot;
	}

	public void setIsAlone(boolean b) {
		this.isAlone = b;
	}	
}
