package de.hpi.mod.sim.worlds.traffic_light_robots;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.simulation.Entity;

public class ScenarioGenerator {

    private TrafficLightWorld world;

    public ScenarioGenerator(TrafficLightWorld world) {
        this.world = world;
    }

    private class NoRobotsScenario extends Scenario {
        public NoRobotsScenario() {
            name = "No robots";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            return list;
        }
    }

    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new NoRobotsScenario());
        return scenarios;
    }
}
