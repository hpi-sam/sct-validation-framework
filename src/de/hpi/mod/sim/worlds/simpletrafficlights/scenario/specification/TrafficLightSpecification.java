package de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsConfiguration;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.RelativePosition;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TrafficLight;


public class TrafficLightSpecification implements EntitySpecification<TrafficLight> {

    private RelativePosition relativePosition;
    private StreetNetworkManager grid;

    public TrafficLightSpecification(RelativePosition p, StreetNetworkManager g) {
    	this.relativePosition = p;
        this.grid = g;
    }

    public Position getCoordinates() {
    	int x = SimpleTrafficLightsConfiguration.getFieldBorderWidth() + SimpleTrafficLightsConfiguration.getStreetLength() 
    			+ (relativePosition.getX() * (SimpleTrafficLightsConfiguration.getStreetLength()  + SimpleTrafficLightsConfiguration.getCrossroadLength()));
    	int y = SimpleTrafficLightsConfiguration.getFieldBorderWidth() + SimpleTrafficLightsConfiguration.getStreetLength() 
				+ (relativePosition.getY() * (SimpleTrafficLightsConfiguration.getStreetLength()  + SimpleTrafficLightsConfiguration.getCrossroadLength()));
    	return new Position(x,y);
    }
    
    @Override
    public TrafficLight createEntity() {
    	TrafficLight entity = new TrafficLight(relativePosition, getCoordinates(), grid);
        this.grid.addTrafficLight(entity);
		return entity;
    }
    
}
