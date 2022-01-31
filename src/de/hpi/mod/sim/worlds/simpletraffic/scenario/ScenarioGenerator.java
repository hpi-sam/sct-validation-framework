package de.hpi.mod.sim.worlds.simpletraffic.scenario;

import java.util.ArrayList;
import java.util.List;

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
    
	private class SingleRobotSingleCrossroadScenario extends SimpleTrafficScenario {
	    public SingleRobotSingleCrossroadScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.SINGLE_CROSSROAD;
	        name = "Single Robot 1";
	        statistics = false;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class SingleRobotSomeCrossroadsScenario extends SimpleTrafficScenario {
	    public SingleRobotSomeCrossroadsScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.TWO_CROSSROADS;
	        name = "Single Robot 2";
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
	        name = "Single Robot 2";
	        statistics = false;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class FewRobotSomeCrossroadsScenario extends SimpleTrafficScenario {
	    public FewRobotSomeCrossroadsScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.TWO_CROSSROADS;
	        name = "Few Robots 1";
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
    
	private class FewRobotManyCrossroadsScenario extends SimpleTrafficScenario {
	    public FewRobotManyCrossroadsScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "Few Robots 2";
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
			for(int i = 0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints()*(SimpleTrafficWorldConfiguration.getStreetLength()-2) ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class NoRobotsScenario extends SimpleTrafficScenario {
	    public NoRobotsScenario() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.TWO_CROSSROADS;
	        name = "Only Crossroads, No Robots";
	        statistics = false;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithRandomizedTimes();
			return list;
		}
	}
    
	private class ThroughputChallengeWithSingleCrossroadAndFewRobots extends SimpleTrafficScenario {
	    public ThroughputChallengeWithSingleCrossroadAndFewRobots() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.SINGLE_CROSSROAD;
	        name = "Single Crossroad, Few Robots (CHALLENGE)";
	        statistics = true;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithoutRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints()*2 ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ThroughputChallengeWithSingleCrossroadAndManyRobots extends SimpleTrafficScenario {
	    public ThroughputChallengeWithSingleCrossroadAndManyRobots() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.SINGLE_CROSSROAD;
	        name = "Single Crossroad, Many Robots (CHALLENGE)";
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
    
	private class ThroughputChallengeWithFourCrossroadsAndFewRobots extends SimpleTrafficScenario {
	    public ThroughputChallengeWithFourCrossroadsAndFewRobots() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.TWO_CROSSROADS;
	        name = "Four Crossroad, Few Robots (CHALLENGE)";
	        statistics = true;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithoutRandomizedTimes();
			for(int i = 0 ; i<SimpleTrafficWorldConfiguration.getNumberOfTransferPoints()*2 ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ThroughputChallengeWithFourCrossroadsAndManyRobots extends SimpleTrafficScenario {
	    public ThroughputChallengeWithFourCrossroadsAndManyRobots() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.TWO_CROSSROADS;
	        name = "Four Crossroad, Many Robots (CHALLENGE)";
	        statistics = true;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithoutRandomizedTimes();
			for(int i=0 ; i < SimpleTrafficWorldConfiguration.getNumberOfTransferPoints()*3 ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
    
	private class ThroughputChallengeWithManyCrossroads extends SimpleTrafficScenario {
	    public ThroughputChallengeWithManyCrossroads() {
	    	super();
	    	gridMode = SimpleTrafficWorldConfiguration.GridMode.MAXIMUM_CROSSROADS;
	        name = "Many Crossroads (CHALLENGE)";
	        statistics = true;
	    }
	    
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			List<EntitySpecification<?>> list = createDefaultEnvironmentEntitiesWithoutRandomizedTimes();
			for(int i=0 ; i < SimpleTrafficWorldConfiguration.getNumberOfTransferPoints()*3 ; i++)
				list.add(new SimpleRobotSpecification(world.getStreetNetworkManager()));
			return list;
		}
	}
	
    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        
        scenarios.add(new SingleRobotSingleCrossroadScenario());
        scenarios.add(new SingleRobotSomeCrossroadsScenario());
        scenarios.add(new SingleRobotManyCrossroadsScenario());
        scenarios.add(new FewRobotManyCrossroadsScenario());
        scenarios.add(new FewRobotSomeCrossroadsScenario());
        scenarios.add(new ManyRobotManyCrossroadsScenario());
        
//        scenarios.add(new NoRobotsScenario());
//        scenarios.add(new ThroughputChallengeWithSingleCrossroadAndFewRobots());
//        scenarios.add(new ThroughputChallengeWithSingleCrossroadAndManyRobots());
//        scenarios.add(new ThroughputChallengeWithFourCrossroadsAndFewRobots());
//        scenarios.add(new ThroughputChallengeWithFourCrossroadsAndManyRobots());
//        scenarios.add(new ThroughputChallengeWithManyCrossroads());
        
        return scenarios;
    }
}
