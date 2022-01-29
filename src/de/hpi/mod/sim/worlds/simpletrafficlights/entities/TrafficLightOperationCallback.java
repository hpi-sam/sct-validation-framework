package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import java.util.List;
import java.util.Optional;

import de.hpi.mod.sim.statemachines.simpletrafficlights.TrafficLightStatechart;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;

public class TrafficLightOperationCallback implements TrafficLightStatechart.CenterSensor.OperationCallback, TrafficLightStatechart.NorthSensor.OperationCallback, 
				TrafficLightStatechart.EastSensor.OperationCallback, TrafficLightStatechart.SouthSensor.OperationCallback, TrafficLightStatechart.WestSensor.OperationCallback {

	private List<Position> positions;
	private StreetNetworkManager manager;
	
	private static class Direction{
		protected static final int NOT_APPLICABLE = (int) TrafficLightStatechart.Direction.aHEAD;
		protected static final int AHEAD = (int) TrafficLightStatechart.Direction.aHEAD;
		protected static final int LEFT = (int) TrafficLightStatechart.Direction.lEFT;
		protected static final int RIGHT = (int) TrafficLightStatechart.Direction.rIGHT;
	}

	public TrafficLightOperationCallback(List<Position> positions, StreetNetworkManager manager) {
		this.positions = positions; 
		this.manager = manager;
	}

	public TrafficLightOperationCallback(Position position, StreetNetworkManager manager) {
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
		
		if(!(potentialRobot.get() instanceof SimpleRobot))
			return Direction.NOT_APPLICABLE;
		
		SimpleRobot robot = (SimpleRobot) potentialRobot.get();
		if(robot.isTargetAhead())
			return Direction.AHEAD;
		if(robot.isTargetLeft())
			return Direction.LEFT;
		if(robot.isTargetRight())
			return Direction.RIGHT;
		
		return Direction.NOT_APPLICABLE;
	}

}
