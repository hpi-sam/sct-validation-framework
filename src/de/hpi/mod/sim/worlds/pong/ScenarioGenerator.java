package de.hpi.mod.sim.worlds.pong;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.simulation.Entity;

public class ScenarioGenerator {

    private PongWorld world;

    public ScenarioGenerator(PongWorld world) {
        this.world = world;
    }

    private class ShowScenario extends Scenario {
        public ShowScenario() {
            name = "TODO";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            PaddleSpecification paddle = new PaddleSpecification(world);
            list.add(paddle);
            return list;
        }
    }

    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new ShowScenario());
        return scenarios;
    }
}
