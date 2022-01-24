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
    
		private List<EntitySpecification<?>> createDefaultEnvironmentEntities(boolean randomizeWaitingTimes) {
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
				list.add(new ArrivalPointSpecification(i, world.getStreetNetworkManager(), randomizeWaitingTimes));
				list.add(new DeparturePointSpecification(i, world.getStreetNetworkManager(), randomizeWaitingTimes));
			}
			
			// Return List
			return list; 
		}
		
		protected List<EntitySpecification<?>> createDefaultEnvironmentEntitiesWithRandomizedTimes() {
			return createDefaultEnvironmentEntities(true);
		}
		
		protected List<EntitySpecification<?>> createDefaultEnvironmentEntitiesWithoutRandomizedTimes() {
			return createDefaultEnvironmentEntities(false);
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
    
	private class NoRobotsScenario extends SimpleTrafficLightScenario {
	    public NoRobotsScenario() {
	    	super();
	    	gridMode = SimpleTrafficLightsConfiguration.GridMode.TWO_CROSSROADS;
	        name = "No Robots";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			return list;
		}
	}
    
	private class SingleRobotSingleCrossroadScenario extends SimpleTrafficLightScenario {
	    public SingleRobotSingleCrossroadScenario() {
	    	super();
	    	gridMode = SimpleTrafficLightsConfiguration.GridMode.SINGLE_CROSSROAD;
	        name = "Single Robot + Single Crossrad";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class SingleRobotManyCrossroadsScenario extends SimpleTrafficLightScenario {
	    public SingleRobotManyCrossroadsScenario() {
	    	super();
	    	gridMode = SimpleTrafficLightsConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "Single Robot";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class FewRobotManyCrossroadsScenario extends SimpleTrafficLightScenario {
	    public FewRobotManyCrossroadsScenario() {
	    	super();
	    	gridMode = SimpleTrafficLightsConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "Few Robots";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficLightsConfiguration.getNumberOfTransferPoints() ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ManyRobotManyCrossroadsScenario extends SimpleTrafficLightScenario {
	    public ManyRobotManyCrossroadsScenario() {
	    	super();
	    	gridMode = SimpleTrafficLightsConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "Many Robots";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficLightsConfiguration.getNumberOfTransferPoints()*(SimpleTrafficLightsConfiguration.getStreetLength()-1) ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ThroughputChallengeWithSingleCrossroad extends SimpleTrafficLightScenario {
	    public ThroughputChallengeWithSingleCrossroad() {
	    	super();
	    	gridMode = SimpleTrafficLightsConfiguration.GridMode.SINGLE_CROSSROAD;
	        name = "CHALLENGE (single crossroad)";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithoutRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficLightsConfiguration.getNumberOfTransferPoints()*SimpleTrafficLightsConfiguration.getStreetLength()*2 ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ThroughputChallengeWithManyCrossroads extends SimpleTrafficLightScenario {
	    public ThroughputChallengeWithManyCrossroads() {
	    	super();
	    	gridMode = SimpleTrafficLightsConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "CHALLENGE (multiple crossroads)";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithoutRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficLightsConfiguration.getNumberOfTransferPoints()*SimpleTrafficLightsConfiguration.getStreetLength()*(SimpleTrafficLightsConfiguration.getVerticalStreets()+SimpleTrafficLightsConfiguration.getHorizontalStreets()) ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
	
    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new NoRobotsScenario());
        scenarios.add(new SingleRobotSingleCrossroadScenario());
        scenarios.add(new SingleRobotManyCrossroadsScenario());
        scenarios.add(new FewRobotManyCrossroadsScenario());
        scenarios.add(new ManyRobotManyCrossroadsScenario());
        scenarios.add(new ThroughputChallengeWithSingleCrossroad());
        scenarios.add(new ThroughputChallengeWithManyCrossroads());
        return scenarios;
    }
}
