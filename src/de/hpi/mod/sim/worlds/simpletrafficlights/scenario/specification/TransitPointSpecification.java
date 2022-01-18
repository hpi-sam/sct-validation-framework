package de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TransitPoint;

public abstract class TransitPointSpecification<P extends TransitPoint> implements EntitySpecification<TransitPoint> {
	
	protected int id;
    protected StreetNetworkManager grid;
    
	public TransitPointSpecification(int i, StreetNetworkManager g) {
        this.id = i;
        this.grid = g;
	}
	
}