package de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification;

import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.ArrivalPoint;

public class ArrivalPointSpecification extends TransitPointSpecification<ArrivalPoint> {
    
	public ArrivalPointSpecification(int i, StreetNetworkManager g) {
		super(i, g);
	}


	@Override
	public ArrivalPoint createEntity() {
		
		ArrivalPoint entity = new ArrivalPoint(this.id, new Position(0, 0));
		this.grid.addArrivalPoint(entity);
	    return entity;
	}

}
