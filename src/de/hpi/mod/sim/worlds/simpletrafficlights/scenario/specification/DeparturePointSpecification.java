package de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsConfiguration;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.DeparturePoint;


public class DeparturePointSpecification extends TransitPointSpecification<DeparturePoint> {

	public DeparturePointSpecification(int i, StreetNetworkManager g) {
		super(i, g);
	}

    public Position getCoordinates() {
    	int stepSize = SimpleTrafficLightsConfiguration.getStreetLength()  + SimpleTrafficLightsConfiguration.getCrossroadLength();
    	
    	// Go through all sides (clockwise)
    	
    	// West Side?
    	int i = id;
    	if(i < SimpleTrafficLightsConfiguration.getHorizontalStreets()) 
    		return new Position(SimpleTrafficLightsConfiguration.getFieldBorderWidth() - 1, 
    				SimpleTrafficLightsConfiguration.getFieldBorderWidth() + SimpleTrafficLightsConfiguration.getStreetLength() + (i*stepSize));
    	
    	// North Side?
    	i -= SimpleTrafficLightsConfiguration.getHorizontalStreets();
    	if(i < SimpleTrafficLightsConfiguration.getVerticalStreets()) 
    		return new Position(SimpleTrafficLightsConfiguration.getFieldBorderWidth() + SimpleTrafficLightsConfiguration.getStreetLength() + (i*stepSize),
    				SimpleTrafficLightsConfiguration.getFieldHeight() - SimpleTrafficLightsConfiguration.getFieldBorderWidth());

    	// East Side?
    	i -= SimpleTrafficLightsConfiguration.getVerticalStreets();
    	if(i < SimpleTrafficLightsConfiguration.getHorizontalStreets()) 
    		return new Position(SimpleTrafficLightsConfiguration.getFieldWidth() - SimpleTrafficLightsConfiguration.getFieldBorderWidth(), 
    				SimpleTrafficLightsConfiguration.getFieldHeight() - SimpleTrafficLightsConfiguration.getFieldBorderWidth() - SimpleTrafficLightsConfiguration.getStreetLength() - 1 - (i*stepSize));
    	
    	// South Side?
    	i -= SimpleTrafficLightsConfiguration.getHorizontalStreets();
    	if(i < SimpleTrafficLightsConfiguration.getVerticalStreets()) 
    		return new Position(SimpleTrafficLightsConfiguration.getFieldWidth() - SimpleTrafficLightsConfiguration.getFieldBorderWidth() - SimpleTrafficLightsConfiguration.getStreetLength() - 1 - (i*stepSize),
    				SimpleTrafficLightsConfiguration.getFieldBorderWidth() - 1);
    	
    	return new Position(0, 0);
    }
    
    @Override
	public Orientation getOrientation() {
    	// Orientation facing outwards
    	return super.getOrientation().getInverse();
    }
	
	@Override
	public DeparturePoint createEntity() {
		
		DeparturePoint entity = new DeparturePoint(this.id, getCoordinates(), getOrientation());
		this.grid.addDeparturePoint(entity);
	    return entity;
	}

}
