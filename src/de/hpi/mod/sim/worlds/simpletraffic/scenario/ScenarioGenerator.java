package de.hpi.mod.sim.worlds.simpletraffic.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorld;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.entities.RelativePosition;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.ArrivalPointSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.DeparturePointSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.SimpleRobotSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.scenario.specification.TrafficLightSpecification;

public class ScenarioGenerator {

    private SimpleTrafficWorld world;

    public ScenarioGenerator(SimpleTrafficWorld world) {
        this.world = world;
    }
    
    private abstract class SimpleTrafficLightScenario extends Scenario {
    	
    	SimpleTrafficWorldConfiguration.GridMode gridMode;
    
		private List<EntitySpecification<?>> createDefaultEnvironmentEntities(boolean randomizeWaitingTimes) {
			// Initialize list
			List<EntitySpecification<?>> list = new ArrayList<>();
			
			// Add Traffic Light Specifications to list
			for(int x = 0 ; x<SimpleTrafficWorldConfiguration.getVerticalStreets() ; x++) {
				for(int y = 0 ; y<SimpleTrafficWorldConfiguration.getHorizontalStreets() ; y++) {
					list.add(new TrafficLightSpecification(new RelativePosition(x, y), world.getStreetNetworkManager()));
				}
			}
			
			// Add Arrival + Departure Point specifications to list
			for (int i=0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints() ; i++) {
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
        		SimpleTrafficWorldConfiguration.setCrossroadsMode(gridMode);
        		world.configurationChanged(); 
        	}
        	
        	super.loadScenario(world);
        }
    }
    
	private class NoRobotsScenario extends SimpleTrafficLightScenario {
	    public NoRobotsScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.TWO_CROSSROADS;
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
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.SINGLE_CROSSROAD;
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
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.MAXIMUM_CROSSROADS;
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
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "Few Robots";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints() ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ManyRobotManyCrossroadsScenario extends SimpleTrafficLightScenario {
	    public ManyRobotManyCrossroadsScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "Many Robots";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints()*(SimpleTrafficWorldConfiguration.getStreetLength()-1) ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ThroughputChallengeWithSingleCrossroad extends SimpleTrafficLightScenario {
	    public ThroughputChallengeWithSingleCrossroad() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.SINGLE_CROSSROAD;
	        name = "CHALLENGE (single crossroad)";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithoutRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints()*SimpleTrafficWorldConfiguration.getStreetLength()*2 ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ThroughputChallengeWithManyCrossroads extends SimpleTrafficLightScenario {
	    public ThroughputChallengeWithManyCrossroads() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "CHALLENGE (multiple crossroads)";
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithoutRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints()*SimpleTrafficWorldConfiguration.getStreetLength()*(SimpleTrafficWorldConfiguration.getVerticalStreets()+SimpleTrafficWorldConfiguration.getHorizontalStreets()) ; i++)
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
