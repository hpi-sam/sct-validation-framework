package de.hpi.mod.sim.worlds.traffic_light_robots.scenario;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.traffic_light_robots.TrafficLightWrapper;
import de.hpi.mod.sim.worlds.traffic_light_robots.TrafficLightWorld;

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
