package de.hpi.mod.sim.worlds.simpletrafficlights.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightWorld;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsConfiguration;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.RelativePosition;
import de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification.ArrivalPointSpecification;
import de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification.DeparturePointSpecification;
import de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification.SimpleRobotSpecification;
import de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification.TrafficLightSpecification;

public class ScenarioGenerator {

    private SimpleTrafficLightWorld world;

    public ScenarioGenerator(SimpleTrafficLightWorld world) {
        this.world = world;
    }
    
    private abstract class SimpleTrafficLightScenario extends Scenario {
    	
    	SimpleTrafficLightsConfiguration.GridMode gridMode;

		protected List<EntitySpecification<?>> getDefaultEnvironmentEntities() {
			System.out.println("Create Entities!");
			// Initialize list
			List<EntitySpecification<?>> list = new ArrayList<>();
			
			// Add Traffic Light Specifications to list
			for(int x = 0 ; x<SimpleTrafficLightsConfiguration.getVerticalStreets() ; x++) {
				for(int y = 0 ; y<SimpleTrafficLightsConfiguration.getHorizontalStreets() ; y++) {
					list.add(new TrafficLightSpecification(new RelativePosition(x, y), world.getStreetNetworkManager()));
				}
			}
			
			// Add Arrival + Departure Point specifications to list
			for (int i=0 ; i<SimpleTrafficLightsConfiguration.getNumberOfTransferPoints() ; i++) {
				list.add(new ArrivalPointSpecification(i, world.getStreetNetworkManager()));
				list.add(new DeparturePointSpecification(i, world.getStreetNetworkManager()));
			}
			
			// Return List
			return list; 
		}
		
        @Override
        public void loadScenario(World world) {
        	if(gridMode != null) {
        		SimpleTrafficLightsConfiguration.setCrossroadsMode(gridMode);
        		world.configurationChanged(); 
        	}
        	
        	super.loadScenario(world);
        }
    }

    private class SingleTrafficLightNoRobotsScenario extends SimpleTrafficLightScenario {
        public SingleTrafficLightNoRobotsScenario() {
        	super();
        	gridMode = SimpleTrafficLightsConfiguration.GridMode.SINGLE_CROSSROAD;
            name = "Single Traffic Light, No Robots";
        }
        
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			return getDefaultEnvironmentEntities();
		}
    }
    
	private class SingleTrafficLightSingleRobotScenario extends SimpleTrafficLightScenario {
	    public SingleTrafficLightSingleRobotScenario() {
	    	super();
	    	gridMode = SimpleTrafficLightsConfiguration.GridMode.SINGLE_CROSSROAD;
	        name = "Single Traffic Light, Single Robot";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = getDefaultEnvironmentEntities();
			list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class SingleTrafficLightManyRobotsScenario extends SimpleTrafficLightScenario {
	    public SingleTrafficLightManyRobotsScenario() {
	    	super();
	    	gridMode = SimpleTrafficLightsConfiguration.GridMode.SINGLE_CROSSROAD;
	        name = "Single Traffic Light, Many Robot";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = getDefaultEnvironmentEntities();
			// TODO: Add many robot
			return list;
		}
	}
	
    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new SingleTrafficLightNoRobotsScenario());
        scenarios.add(new SingleTrafficLightSingleRobotScenario());
        scenarios.add(new SingleTrafficLightManyRobotsScenario());
//        scenarios.add(new NoRobotsScenario());
//        scenarios.add(new OneRobotScenario());
//        scenarios.add(new FewRobotsScenario());
//        scenarios.add(new AverageRobotsScenario());
//        scenarios.add(new ManyRobotsScenario());
        return scenarios;
    }
}
