package de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsConfiguration;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TransitPoint;

public abstract class TransitPointSpecification<P extends TransitPoint> implements EntitySpecification<TransitPoint> {
	
	protected int id;
    protected StreetNetworkManager grid;
    protected boolean randomizeWitingTimes = true;
	
	public TransitPointSpecification(int i, StreetNetworkManager g, boolean r) {
        this.id = i;
        this.grid = g;
        this.randomizeWitingTimes = r;
	}
	
    public Orientation getOrientation() {   	
    	// Orientation facing Inwards
    	
    	// West Side?
    	int i = id;
    	if(i < SimpleTrafficLightsConfiguration.getHorizontalStreets()) 
    		return Orientation.EAST;
    	
    	// North Side?
    	i -= SimpleTrafficLightsConfiguration.getHorizontalStreets();
    	if(i < SimpleTrafficLightsConfiguration.getVerticalStreets()) 
    		return Orientation.SOUTH;

    	// East Side?
    	i -= SimpleTrafficLightsConfiguration.getVerticalStreets();
    	if(i < SimpleTrafficLightsConfiguration.getHorizontalStreets()) 
    		return Orientation.WEST;
    	
    	// South Side? (Default)
   		return Orientation.NORTH;
   		
    }
}