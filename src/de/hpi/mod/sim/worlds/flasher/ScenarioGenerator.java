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
import de.hpi.mod.sim.worlds.flasher.entities.TaskProviderWithGenerator.ITaskGenerator;

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
    
    private class FibonacciBlinkScenario extends Scenario {

        public FibonacciBlinkScenario() {
            name = "Fibonacci Blinks";
        }
        
        private class FibonacciGenerator implements ITaskGenerator{
        	
        	int secondLastValue = 1;
        	int lastValue = 0;

			@Override
			public FlashTask next() {
				// Overwrite last and second last values
				// (The temporary value is a "hack" for the first iteration) 
				int temporaryReturnValue = this.lastValue + this.secondLastValue;
				this.secondLastValue = this.lastValue;
				this.lastValue = temporaryReturnValue;
				return new FlashTask(temporaryReturnValue, (temporaryReturnValue* 1000) + 3500);
			}
        		
        }        
        
        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {


            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            LightBulbSpecification bulb = new LightBulbSpecification(false, false, false, world);
            TaskProviderSpecification starter = new TaskProviderSpecification(new FibonacciGenerator(), world);

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
