package de.hpi.mod.sim.worlds.traffic_light_robots;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.abstract_grid.Position;

public class ScenarioGenerator {

    private TrafficLightWorld world;

    public ScenarioGenerator(TrafficLightWorld world) {
        this.world = world;
    }

    private class SingleLightScenario extends Scenario {
        public SingleLightScenario() {
            name = "Single Traffic Light";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            list.add(new TrafficLightSpecification(new Position(0, 2), world));
            return list;
        }
    }

    private List<EntitySpecification<? extends Entity>> trafficLightsForEveryCrossRoad() {
        int lightsPerCol = TrafficLightsConfiguration.getFieldHeight() / 3;
        int lightsPerRow = TrafficLightsConfiguration.getFieldWidth() / 3;
        List<EntitySpecification<? extends Entity>> lights = new ArrayList<>(lightsPerCol * lightsPerRow);
        for (int y = 0; y < lightsPerCol; y++) {
            for (int x = 0; x < lightsPerRow; x++) {
                lights.add(new TrafficLightSpecification(new Position(x * 3 + 2, y * 3), world));
            }
        }
        return lights;
    }

    private class NoRobotsScenario extends Scenario {
        public NoRobotsScenario() {
            name = "No robots";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            return trafficLightsForEveryCrossRoad();
        }
    }

    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new SingleLightScenario());
        scenarios.add(new NoRobotsScenario());
        return scenarios;
    }
}
