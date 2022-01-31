package de.hpi.mod.sim.worlds.simpletraffic.scenario.specification;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;
import de.hpi.mod.sim.worlds.simpletraffic.entities.ArrivalPoint;

public class ArrivalPointSpecification extends TransitPointSpecification<ArrivalPoint> {
	
	public ArrivalPointSpecification(int id, TrafficGridManager gridManager, boolean randomizeTimes, boolean testMode) {
		super(id, gridManager, randomizeTimes, testMode);
	}
	
	public ArrivalPointSpecification(int id, TrafficGridManager gridManager, boolean randomizeTimes) {
		super(id, gridManager, randomizeTimes, false);
	}
	
	public ArrivalPointSpecification(int id, TrafficGridManager gridManager) {
		super(id, gridManager, true, false);
	}

    public Position getCoordinates() {
    	int stepSize = SimpleTrafficWorldConfiguration.getStreetLength()  + SimpleTrafficWorldConfiguration.getCrossroadLength();
    	
    	// Go through all sides (clockwise)
    	
    	// West Side?
    	int i = id;
    	if(i < SimpleTrafficWorldConfiguration.getHorizontalStreets()) 
    		return new Position(SimpleTrafficWorldConfiguration.getFieldBorderWidth() - 1, 
    				SimpleTrafficWorldConfiguration.getFieldBorderWidth() + SimpleTrafficWorldConfiguration.getStreetLength() + 1 + (i*stepSize));
    	
    	// North Side?
    	i -= SimpleTrafficWorldConfiguration.getHorizontalStreets();
    	if(i < SimpleTrafficWorldConfiguration.getVerticalStreets()) 
    		return new Position(SimpleTrafficWorldConfiguration.getFieldBorderWidth() + SimpleTrafficWorldConfiguration.getStreetLength() + 1 + (i*stepSize),
    				SimpleTrafficWorldConfiguration.getFieldHeight() - SimpleTrafficWorldConfiguration.getFieldBorderWidth());

    	// East Side?
    	i -= SimpleTrafficWorldConfiguration.getVerticalStreets();
    	if(i < SimpleTrafficWorldConfiguration.getHorizontalStreets()) 
    		return new Position(SimpleTrafficWorldConfiguration.getFieldWidth() - SimpleTrafficWorldConfiguration.getFieldBorderWidth(), 
    				SimpleTrafficWorldConfiguration.getFieldHeight() - SimpleTrafficWorldConfiguration.getFieldBorderWidth() - SimpleTrafficWorldConfiguration.getStreetLength() - 2 - (i*stepSize));
    	
    	// South Side?
    	i -= SimpleTrafficWorldConfiguration.getHorizontalStreets();
    	if(i < SimpleTrafficWorldConfiguration.getVerticalStreets()) 
    		return new Position(SimpleTrafficWorldConfiguration.getFieldWidth() - SimpleTrafficWorldConfiguration.getFieldBorderWidth() - SimpleTrafficWorldConfiguration.getStreetLength() - 2 - (i*stepSize),
    				SimpleTrafficWorldConfiguration.getFieldBorderWidth() - 1);
    	
    	return new Position(0, 0);
    }

    @Override
	public Orientation getOrientation() {
    	// Orientation facing outwards
    	return super.getOrientation().getInverse();
    }
    
	@Override
	public ArrivalPoint createEntity() {
		ArrivalPoint entity = new ArrivalPoint(this.id, getCoordinates(), getOrientation(), randomizeWitingTimes);
		this.grid.addArrivalPoint(entity);
		if(testMode)
			entity.activateSingleUseMode();
	    return entity;
	}

}
