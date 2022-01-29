package de.hpi.mod.sim.worlds.simpletraffic.scenario.specification;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;
import de.hpi.mod.sim.worlds.simpletraffic.entities.TransitPoint;

public abstract class TransitPointSpecification<P extends TransitPoint> implements EntitySpecification<TransitPoint> {
	
	protected int id;
    protected TrafficGridManager grid;
    protected boolean randomizeWitingTimes = true;
	
	public TransitPointSpecification(int i, TrafficGridManager g, boolean r) {
        this.id = i;
        this.grid = g;
        this.randomizeWitingTimes = r;
	}
	
    public Orientation getOrientation() {   	
    	// Orientation facing Inwards
    	
    	// West Side?
    	int i = id;
    	if(i < SimpleTrafficWorldConfiguration.getHorizontalStreets()) 
    		return Orientation.EAST;
    	
    	// North Side?
    	i -= SimpleTrafficWorldConfiguration.getHorizontalStreets();
    	if(i < SimpleTrafficWorldConfiguration.getVerticalStreets()) 
    		return Orientation.SOUTH;

    	// East Side?
    	i -= SimpleTrafficWorldConfiguration.getVerticalStreets();
    	if(i < SimpleTrafficWorldConfiguration.getHorizontalStreets()) 
    		return Orientation.WEST;
    	
    	// South Side? (Default)
   		return Orientation.NORTH;
   		
    }
}