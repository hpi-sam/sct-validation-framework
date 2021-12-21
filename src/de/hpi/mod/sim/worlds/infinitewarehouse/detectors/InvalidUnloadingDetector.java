package de.hpi.mod.sim.worlds.infinitewarehouse.detectors;

import java.util.List;

import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.detectors.RobotDetector;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;

public class InvalidUnloadingDetector extends RobotDetector {

	public InvalidUnloadingDetector(InfiniteWarehouse world) {
		super(world);
	}
	
	@Override
	public void robotUpdate(List<Robot> robots) {
		for (int i = 0; i < robots.size(); i++) {
			Robot general_robot = robots.get(i);
			if (!(general_robot instanceof WarehouseRobot))
				continue;
			WarehouseRobot robot = (WarehouseRobot) general_robot;
			if (robot.getInvalidUnloadingPosition() != null) {
				robot.resetInvalidUnloadingPosition();
				this.deactivate();
				reportInvalidPosition(robot, robot.pos());
				getWorld().getWarehouseManager().makePositionInvalid(robot.pos());
			}
		}
	}
	
	@Override
	public void reset() {
		getWorld().getWarehouseManager().clearInvalidPositions();
	}

	private void reportInvalidPosition(WarehouseRobot robot, Position invalidPosition) {
		String reason = "Robot at (" + String.valueOf(invalidPosition.getX()) + ","
				+ String.valueOf(invalidPosition.getY()) + ") unloaded to illegal position!";
		getWorld().reportInvalidUnloading(robot, reason);
		report(reason, robot);
	}
	
	private InfiniteWarehouse getWorld() {
		return (InfiniteWarehouse) world;
	}

}