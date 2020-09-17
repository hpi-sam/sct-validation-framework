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
            Starter starter = new Starter(bulb) {
                @Override
                protected List<Integer> getBlinkCounts() {
                    return Arrays.asList(1);
                }

                @Override
                protected List<Float> getWaitingTimes() {
                    return Arrays.asList(3000f);
                }
            };

            world.setBulb(bulb);
            world.setStarter(starter);

            list.add(bulb);
            list.add(starter);
            return list;
        }
    }

    private class OneBlinkScenario extends Scenario {
        public OneBlinkScenario() {
            name = "One Blink";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> initializeScenario() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            Bulb bulb = new Bulb();
            Starter starter = new Starter(bulb) {
                @Override
                protected List<Integer> getBlinkCounts() {
                    return Arrays.asList(1);
                }

                @Override
                protected List<Float> getWaitingTimes() {
                    return Arrays.asList(3000f);
                }
            };

            world.setBulb(bulb);
            world.setStarter(starter);

            list.add(bulb);
            list.add(starter);
            return list;
        }
    }
    
    private class FibonacciScenario extends Scenario {
        public OneBlinkScenario() {
            name = "One Blink";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> initializeScenario() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            Bulb bulb = new Bulb();
            Starter starter = new Starter(bulb) {
                @Override
                protected List<Integer> getBlinkCounts() {
                    return Arrays.asList(1);
                }

                @Override
                protected List<Float> getWaitingTimes() {
                    return Arrays.asList(3000f);
                }
            };

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
        return scenarios;
    }
}
