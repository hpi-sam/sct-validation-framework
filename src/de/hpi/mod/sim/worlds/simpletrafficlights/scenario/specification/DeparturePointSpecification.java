package de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification;

import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.DeparturePoint;


public class DeparturePointSpecification extends TransitPointSpecification<DeparturePoint> {

	public DeparturePointSpecification(int i, StreetNetworkManager g) {
		super(i, g);
	}

	@Override
	public DeparturePoint createEntity() {
		
		DeparturePoint entity = new DeparturePoint(this.id, new Position(0, 0));
		this.grid.addDeparturePoint(entity);
	    return entity;
	}

}
