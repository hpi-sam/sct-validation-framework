package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;

public class DeparturePoint extends TransitPoint {
	
	Robot startingRobot;
	ArrivalPoint targetforStartingRobot;

	public DeparturePoint(int i, Position p, Orientation o) {
		super(i, p, o);
	}

	public void addStartingRobot(Robot robot, ArrivalPoint arrival) {
		startingRobot = robot;
		targetforStartingRobot = arrival;
	}

}
