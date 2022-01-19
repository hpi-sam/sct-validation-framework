package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.abstract_grid.Position;

public abstract class TransitPoint implements Entity {

	// Id of the Transition point, starting at 0, counted on a clockwise circle starting at (1,1) in the bottom left corner
	private int id; 
	
	// Coordinates of the transit point
	private Position position;
	
	public TransitPoint(int i, Position p) {
		this.id = i;
		this.position = p;
	}
	
	public int getId() {
		return id;
	}

	public Position getPosition() {
		return position;
	}	

	public boolean isNextTo(TransitPoint other) {
		return this.getId() == other.getId();
	}
	
}
