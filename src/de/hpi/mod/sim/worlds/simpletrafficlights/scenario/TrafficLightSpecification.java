package de.hpi.mod.sim.worlds.simpletrafficlights.scenario;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightWorld;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TrafficLightStatechartWrapper;

public class TrafficLightSpecification implements EntitySpecification<TrafficLightStatechartWrapper> {

    private Position pos;
    private SimpleTrafficLightWorld world;

    public TrafficLightSpecification(Position p, SimpleTrafficLightWorld w) {
        this.pos = p;
        this.world = w;
    }

    @Override
    public TrafficLightStatechartWrapper createEntity() {
        return world.getCrossRoadManager().addTrafficLight(new TrafficLightStatechartWrapper(pos));
    }
    
}
