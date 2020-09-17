package de.hpi.mod.sim.worlds.flasher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.simulation.Entity;

public class ScenarioGenerator {

    private FlashWorld world;

    public ScenarioGenerator(FlashWorld world) {
        this.world = world;
    }

    private class OneBlinkScenario extends Scenario {
        public OneBlinkScenario() {
            name = "One Blink";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> initializeScenario() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            Bulb bulb = new Bulb();
            Starter starter = new Starter(bulb, Arrays.asList(1), Arrays.asList(3000f), true);

            world.setBulb(bulb);
            world.setStarter(starter);

            list.add(bulb);
            list.add(starter);
            return list;
        }
    }

    private class TwoBlinkScenario extends Scenario {
        public TwoBlinkScenario() {
            name = "Two Blinks";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> initializeScenario() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            Bulb bulb = new Bulb();
            Starter starter = new Starter(bulb, Arrays.asList(2), Arrays.asList(3000f), true);

            world.setBulb(bulb);
            world.setStarter(starter);

            list.add(bulb);
            list.add(starter);
            return list;
        }
    }
    
    private class FibonacciBlinkScenario extends Scenario {

        public FibonacciBlinkScenario() {
            name = "Fibonacci Blinks";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> initializeScenario() {

            List<Integer> fibonacci = Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21, 34, 55);
            List<Float> waitTimes = new ArrayList<>(fibonacci.size());
            for (int f : fibonacci) {
                waitTimes.add(f * 1000f + 3500f);
            }

            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            Bulb bulb = new Bulb();
            Starter starter = new Starter(bulb, fibonacci, waitTimes, true);

            world.setBulb(bulb);
            world.setStarter(starter);

            list.add(bulb);
            list.add(starter);
            return list;
        }
    }

    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new OneBlinkScenario());
        scenarios.add(new TwoBlinkScenario());
        scenarios.add(new FibonacciBlinkScenario());
        return scenarios;
    }
}
