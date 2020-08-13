package de.hpi.mod.sim.worlds.infinitewarehouse.detectors;

import java.util.List;

import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.Robot;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.RobotDetector;

public class InvalidUnloadingDetector extends RobotDetector {

	public InvalidUnloadingDetector(InfiniteWarehouse world) {
		super(world);
	}

	boolean invalidUnloadingReported = false;
	
	@Override
	public void robotUpdate(List<Robot> robots) {
		if (!invalidUnloadingReported) {
			for (int i = 0; i < robots.size(); i++) {
				Robot robot = robots.get(i);
				if (robot.getInvalidUnloadingPosition() != null) {
					robot.resetInvalidUnloadingPosition();
					invalidUnloadingReported = true;
					reportInvalidPosition(robot, robot.pos());
					world.getGridManager().makePositionInvalid(robot.pos());
				}
			}
		}
	}
	
	@Override
	public void reset() {
		invalidUnloadingReported = false;
		world.getGridManager().clearInvalidPositions();
	}

	private void reportInvalidPosition(Robot robot, Position invalidPosition) {
		String reason = "Robot at (" + String.valueOf(invalidPosition.getX()) + 
				"," + String.valueOf(invalidPosition.getY()) + ") unloaded to illegal position!";
		world.reportInvalidUnloading(robot, reason);
		report(reason, robot);
	}

}