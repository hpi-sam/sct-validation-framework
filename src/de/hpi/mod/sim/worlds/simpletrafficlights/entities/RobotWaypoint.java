package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import de.hpi.mod.sim.core.simulation.Entity;

public abstract class RobotWaypoint implements Entity {
	
	private int x;
	private int y;
	
	public RobotWaypoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
}
