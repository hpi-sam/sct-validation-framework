package de.hpi.mod.sim.worlds.flasher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.flasher.entities.FlashTask;
import de.hpi.mod.sim.worlds.flasher.entities.LightBulbSpecification;
import de.hpi.mod.sim.worlds.flasher.entities.TaskProviderSpecification;

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
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            LightBulbSpecification bulb = new LightBulbSpecification(false, false, false, world);
            
            TaskProviderSpecification starter = new TaskProviderSpecification(Arrays.asList(new FlashTask(1, 3000)), true, world);
            //TaskProviderSpecification starter = new TaskProviderSpecification(Arrays.asList(1), Arrays.asList(3000f), true, world);

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
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            LightBulbSpecification bulb = new LightBulbSpecification(false, false, false, world);
            
            TaskProviderSpecification starter = new TaskProviderSpecification(Arrays.asList(new FlashTask(2, 3000)), true, world);
//            TaskProviderSpecification starter = new TaskProviderSpecification(Arrays.asList(2), Arrays.asList(3000f), true,
//                    world);

            list.add(bulb);
            list.add(starter);
            return list;
        }
    }
    
//    private class FibonacciBlinkScenario extends Scenario {
//
//        public FibonacciBlinkScenario() {
//            name = "Fibonacci Blinks";
//        }
//
//        @Override
//        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
//
//            List<Integer> fibonacci = Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21, 34, 55);
//            List<Float> waitTimes = new ArrayList<>(fibonacci.size());
//            for (int f : fibonacci) {
//                waitTimes.add(f * 1000f + 3500f);
//            }
//
//            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
//            LightBulbSpecification bulb = new LightBulbSpecification(false, false, false, world);
//            TaskProviderSpecification starter = new TaskProviderSpecification(fibonacci, waitTimes, true, world);
//
//            list.add(bulb);
//            list.add(starter);
//            return list;
//        }
//    }

    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new OneBlinkScenario());
        scenarios.add(new TwoBlinkScenario());
//        scenarios.add(new FibonacciBlinkScenario());
        return scenarios;
    }
}
