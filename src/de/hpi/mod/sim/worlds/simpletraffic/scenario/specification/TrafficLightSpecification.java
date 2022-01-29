package de.hpi.mod.sim.worlds.simpletraffic.scenario.specification;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;
import de.hpi.mod.sim.worlds.simpletraffic.entities.RelativePosition;
import de.hpi.mod.sim.worlds.simpletraffic.entities.TrafficLight;


public class TrafficLightSpecification implements EntitySpecification<TrafficLight> {

    private RelativePosition relativePosition;
    private TrafficGridManager grid;

    public TrafficLightSpecification(RelativePosition p, TrafficGridManager g) {
    	this.relativePosition = p;
        this.grid = g;
    }

    public Position getCoordinates() {
    	int x = SimpleTrafficWorldConfiguration.getFieldBorderWidth() + SimpleTrafficWorldConfiguration.getStreetLength() 
    			+ (relativePosition.getX() * (SimpleTrafficWorldConfiguration.getStreetLength()  + SimpleTrafficWorldConfiguration.getCrossroadLength()));
    	int y = SimpleTrafficWorldConfiguration.getFieldBorderWidth() + SimpleTrafficWorldConfiguration.getStreetLength() 
				+ (relativePosition.getY() * (SimpleTrafficWorldConfiguration.getStreetLength()  + SimpleTrafficWorldConfiguration.getCrossroadLength()));
    	return new Position(x,y);
    }
    
    @Override
    public TrafficLight createEntity() {
    	TrafficLight entity = new TrafficLight(relativePosition, getCoordinates(), grid);
        this.grid.addTrafficLight(entity);
		return entity;
    }
    
}
