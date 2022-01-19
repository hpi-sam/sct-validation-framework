package de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification;

import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsConfiguration;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.ArrivalPoint;

public class ArrivalPointSpecification extends TransitPointSpecification<ArrivalPoint> {
    
	public ArrivalPointSpecification(int i, StreetNetworkManager g) {
		super(i, g);
	}

    public Position getCoordinates() {
    	int stepSize = SimpleTrafficLightsConfiguration.getStreetLength()  + SimpleTrafficLightsConfiguration.getCrossroadLength();
    	
    	// Go through all sides (clockwise)
    	
    	// West Side?
    	int i = id;
    	if(i < SimpleTrafficLightsConfiguration.getHorizontalStreets()) 
    		return new Position(SimpleTrafficLightsConfiguration.getFieldBorderWidth() - 1, 
    				SimpleTrafficLightsConfiguration.getFieldBorderWidth() + SimpleTrafficLightsConfiguration.getStreetLength() + 1 + (i*stepSize));
    	
    	// North Side?
    	i -= SimpleTrafficLightsConfiguration.getHorizontalStreets();
    	if(i < SimpleTrafficLightsConfiguration.getVerticalStreets()) 
    		return new Position(SimpleTrafficLightsConfiguration.getFieldBorderWidth() + SimpleTrafficLightsConfiguration.getStreetLength() + 1 + (i*stepSize),
    				SimpleTrafficLightsConfiguration.getFieldHeight() - SimpleTrafficLightsConfiguration.getFieldBorderWidth());

    	// East Side?
    	i -= SimpleTrafficLightsConfiguration.getVerticalStreets();
    	if(i < SimpleTrafficLightsConfiguration.getHorizontalStreets()) 
    		return new Position(SimpleTrafficLightsConfiguration.getFieldWidth() - SimpleTrafficLightsConfiguration.getFieldBorderWidth(), 
    				SimpleTrafficLightsConfiguration.getFieldHeight() - SimpleTrafficLightsConfiguration.getFieldBorderWidth() - SimpleTrafficLightsConfiguration.getStreetLength() - 2 - (i*stepSize));
    	
    	// South Side?
    	i -= SimpleTrafficLightsConfiguration.getHorizontalStreets();
    	if(i < SimpleTrafficLightsConfiguration.getVerticalStreets()) 
    		return new Position(SimpleTrafficLightsConfiguration.getFieldWidth() - SimpleTrafficLightsConfiguration.getFieldBorderWidth() - SimpleTrafficLightsConfiguration.getStreetLength() - 2 - (i*stepSize),
    				SimpleTrafficLightsConfiguration.getFieldBorderWidth() - 1);
    	
    	return new Position(0, 0);
    }

	@Override
	public ArrivalPoint createEntity() {
		ArrivalPoint entity = new ArrivalPoint(this.id, getCoordinates(), getOrientation());
		this.grid.addArrivalPoint(entity);
	    return entity;
	}

}
