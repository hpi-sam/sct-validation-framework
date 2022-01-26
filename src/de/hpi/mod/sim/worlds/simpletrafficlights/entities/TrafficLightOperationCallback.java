package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import java.util.List;

import de.hpi.mod.sim.statemachines.simpletrafficlights.TrafficLightStatechart;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;

public class TrafficLightOperationCallback implements TrafficLightStatechart.CenterSensor.OperationCallback, TrafficLightStatechart.NorthSensor.OperationCallback, 
				TrafficLightStatechart.EastSensor.OperationCallback, TrafficLightStatechart.SouthSensor.OperationCallback, TrafficLightStatechart.WestSensor.OperationCallback {

	private List<Position> positions;
	private StreetNetworkManager manager;

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

}
