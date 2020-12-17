package de.hpi.mod.sim.worlds.traffic_light_robots.scenario;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.traffic_light_robots.TrafficLight;
import de.hpi.mod.sim.worlds.traffic_light_robots.TrafficLightWorld;

public class TrafficLightSpecification implements EntitySpecification<TrafficLight> {

    private Position pos;
    private TrafficLightWorld world;

    public TrafficLightSpecification(Position pos, TrafficLightWorld world) {
        this.pos = pos;
        this.world = world;
    }

    @Override
    public TrafficLight createEntity() {
        return world.getCrossRoadManager().addTrafficLight(new TrafficLight(pos));
    }
    
}
