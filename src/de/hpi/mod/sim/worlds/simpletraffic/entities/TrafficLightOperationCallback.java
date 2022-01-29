package de.hpi.mod.sim.worlds.simpletraffic.entities;

import java.util.List;
import java.util.Optional;

import de.hpi.mod.sim.statemachines.simpletraffic.SimpleTrafficLightStatechart;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;

public class TrafficLightOperationCallback implements SimpleTrafficLightStatechart.CenterSensor.OperationCallback, SimpleTrafficLightStatechart.NorthSensor.OperationCallback, 
SimpleTrafficLightStatechart.EastSensor.OperationCallback, SimpleTrafficLightStatechart.SouthSensor.OperationCallback, SimpleTrafficLightStatechart.WestSensor.OperationCallback {

	private List<Position> positions;
	private TrafficGridManager manager;
	
	private static class Direction{
		protected static final int NOT_APPLICABLE = (int) SimpleTrafficLightStatechart.Direction.aHEAD;
		protected static final int AHEAD = (int) SimpleTrafficLightStatechart.Direction.aHEAD;
		protected static final int LEFT = (int) SimpleTrafficLightStatechart.Direction.lEFT;
		protected static final int RIGHT = (int) SimpleTrafficLightStatechart.Direction.rIGHT;
	}

	public TrafficLightOperationCallback(List<Position> positions, TrafficGridManager manager) {
		this.positions = positions; 
		this.manager = manager;
	}

	public TrafficLightOperationCallback(Position position, TrafficGridManager manager) {
		this.positions = List.of(position);
		this.manager = manager;
	}

	@Override
	public boolean isFree() {
		return !isOccupied();
	}

	@Override
	public boolean isOccupied() {
		return positions.stream().anyMatch(p -> manager.isBlockedByRobot(p));
	}

	@Override
	public long getDirection() {
		if(positions.size() != 1)
			return Direction.NOT_APPLICABLE;
		
		Optional<Robot> potentialRobot = manager.getRobotAt(positions.get(0));
		
		if(potentialRobot.isEmpty())
			return Direction.NOT_APPLICABLE;
		
		if(!(potentialRobot.get() instanceof SimpleTrafficRobot))
			return Direction.NOT_APPLICABLE;
		
		SimpleTrafficRobot robot = (SimpleTrafficRobot) potentialRobot.get();
		if(robot.isTargetAhead())
			return Direction.AHEAD;
		if(robot.isTargetLeft())
			return Direction.LEFT;
		if(robot.isTargetRight())
			return Direction.RIGHT;
		
		return Direction.NOT_APPLICABLE;
	}

}
