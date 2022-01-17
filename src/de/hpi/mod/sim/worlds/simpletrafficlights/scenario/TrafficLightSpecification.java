package de.hpi.mod.sim.worlds.simpletrafficlights.scenario;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightWorld;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TrafficLightWithStatechart;

public class TrafficLightSpecification implements EntitySpecification<TrafficLightWithStatechart> {

    private Position pos;
    private SimpleTrafficLightWorld world;

    public TrafficLightSpecification(Position p, SimpleTrafficLightWorld w) {
        this.pos = p;
        this.world = w;
    }

    @Override
    public TrafficLightWithStatechart createEntity() {
        return world.getCrossRoadManager().addTrafficLight(new TrafficLightWithStatechart(pos));
    }
    
}
