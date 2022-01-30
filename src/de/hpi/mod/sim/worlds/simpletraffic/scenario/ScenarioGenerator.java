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
    
    private abstract class SimpleTrafficScenario extends Scenario {
    	
    	SimpleTrafficWorldConfiguration.GridMode gridMode;
    	boolean statistics = false;
    
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
        		SimpleTrafficWorldConfiguration.setShowStatistics(statistics);
        		world.configurationChanged(); 
        	}
        	
        	super.loadScenario(world);
        }
    }
    
	private class NoRobotsScenario extends SimpleTrafficScenario {
	    public NoRobotsScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.TWO_CROSSROADS;
	        name = "No Robots";
	        statistics = false;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			return list;
		}
	}
    
	private class SingleRobotSingleCrossroadScenario extends SimpleTrafficScenario {
	    public SingleRobotSingleCrossroadScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.SINGLE_CROSSROAD;
	        name = "Single Robot + Single Crossrad";
	        statistics = false;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class SingleRobotManyCrossroadsScenario extends SimpleTrafficScenario {
	    public SingleRobotManyCrossroadsScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "Single Robot";
	        statistics = false;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class FewRobotManyCrossroadsScenario extends SimpleTrafficScenario {
	    public FewRobotManyCrossroadsScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "Few Robots";
	        statistics = false;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints() ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ManyRobotManyCrossroadsScenario extends SimpleTrafficScenario {
	    public ManyRobotManyCrossroadsScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "Many Robots";
	        statistics = false;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints()*(SimpleTrafficWorldConfiguration.getStreetLength()-1) ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ThroughputChallengeWithSingleCrossroad extends SimpleTrafficScenario {
	    public ThroughputChallengeWithSingleCrossroad() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.SINGLE_CROSSROAD;
	        name = "CHALLENGE (single crossroad)";
	        statistics = true;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithoutRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints()*SimpleTrafficWorldConfiguration.getStreetLength()*2 ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ThroughputChallengeWithManyCrossroads extends SimpleTrafficScenario {
	    public ThroughputChallengeWithManyCrossroads() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "CHALLENGE (multiple crossroads)";
	        statistics = true;
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
