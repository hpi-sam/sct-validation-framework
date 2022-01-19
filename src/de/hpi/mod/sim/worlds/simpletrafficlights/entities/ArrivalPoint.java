package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;

public class ArrivalPoint extends TransitPoint {
	
	List<Robot> awaitedRobots = new ArrayList<>();
	
	public ArrivalPoint(int i, Position p, Orientation o) {
		super(i, p, o);
	}

	public void addExpectedRobot(Robot robot) {
		awaitedRobots.add(robot);
	}

	public int getNumberOfExpectedRobots() {
		return awaitedRobots.size();
	}

}
