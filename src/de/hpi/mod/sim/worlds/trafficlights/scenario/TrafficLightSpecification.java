package de.hpi.mod.sim.worlds.trafficlights.scenario;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.trafficlights.TrafficLightWorld;
import de.hpi.mod.sim.worlds.trafficlights.TrafficLightWrapper;

public class TrafficLightSpecification implements EntitySpecification<TrafficLightWrapper> {

    private Position pos;
    private TrafficLightWorld world;

    public TrafficLightSpecification(Position pos, TrafficLightWorld world) {
        this.pos = pos;
        this.world = world;
    }

    @Override
    public TrafficLightWrapper createEntity() {
        return world.getCrossRoadManager().addTrafficLight(new TrafficLightWrapper(pos));
    }
    
}
