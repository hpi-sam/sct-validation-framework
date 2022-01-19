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

    private class FourTrafficLightsNoRobotsScenario extends SimpleTrafficLightScenario {
        public FourTrafficLightsNoRobotsScenario() {
        	super();
        	gridMode = SimpleTrafficLightsConfiguration.GridMode.TWO_CROSSROADS;
            name = "Four Traffic Lights, No Robots";
        }
        
		@Override
		protected List<EntitySpecification<?>> getScenarioEntities() {
			return getDefaultEnvironmentEntities();
		}
    }

//    private class NoRobotsScenario extends Scenario {
//        public SingleLightScenario() {
//            name = "Multiple Traffic Light, No Robots";
//        }
//        
//        @Override
//        public void loadScenario(World world) {
//        	super.loadScenario(world);
//        	world.
//        }
//    }
//    
//    private class OneRobotScenario extends Scenario {
//        public OneRobotScenario() {
//            name = "One robot";
//        }
//
//        @Override
//        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
//            List<EntitySpecification<?>> specs = trafficLightsForEveryCrossRoad();
//            specs.addAll(getRandomRobots(1));
//            return specs;
//        }
//    }
//
//    private class FewRobotsScenario extends Scenario {
//        public FewRobotsScenario() {
//            name = "Few robots";
//        }
//
//        @Override
//        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
//            List<EntitySpecification<?>> specs = trafficLightsForEveryCrossRoad();
//            specs.addAll(getRandomRobots(getMaximumPossibleRobots()/5));
//            return specs;
//        }
//    }
//
//    private class AverageRobotsScenario extends Scenario {
//        public AverageRobotsScenario() {
//            name = "Average number of robots";
//        }
//
//        @Override
//        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
//            List<EntitySpecification<?>> specs = trafficLightsForEveryCrossRoad();
//            specs.addAll(getRandomRobots(getMaximumPossibleRobots() / 3));
//            return specs;
//        }
//    }
//
//    private class ManyRobotsScenario extends Scenario {
//        public ManyRobotsScenario() {
//            name = "Many robots";
//        }
//
//        @Override
//        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
//            List<EntitySpecification<?>> specs = trafficLightsForEveryCrossRoad();
//            specs.addAll(getRandomRobots(getMaximumPossibleRobots() / 2));
//            return specs;
//        }
//    }
//
//    private List<SimpleRobotSpecification> getRandomRobots(int count) {
//        List<Position> positions = StreetNetworkManager.getAllPossiblePositions();
//        List<SimpleRobotSpecification> robots = new ArrayList<>(count);
//
//        if (count > positions.size())  
//            throw new IllegalArgumentException();
//        
//        for (int i = 0; i < count; i++) {
//            int index = ThreadLocalRandom.current().nextInt(positions.size());
//            Position pos = positions.get(index);
//            positions.remove(index);
//            robots.add(new SimpleRobotSpecification(world, pos,
//                    StreetNetworkManager.getSuitableRobotOrientationForPosition(pos), StreetNetworkManager.getRandomDestination()));
//        }
//        return robots;
//    }
//
//    private int getMaximumPossibleRobots() {
//        return 2 * ((SimpleTrafficLightsConfiguration.getFieldWidth() - 1) / 3 + 
//        (SimpleTrafficLightsConfiguration.getFieldHeight() -1 ) / 3);
//    }


    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new SingleTrafficLightNoRobotsScenario());
        scenarios.add(new FourTrafficLightsNoRobotsScenario());
//        scenarios.add(new NoRobotsScenario());
//        scenarios.add(new OneRobotScenario());
//        scenarios.add(new FewRobotsScenario());
//        scenarios.add(new AverageRobotsScenario());
//        scenarios.add(new ManyRobotsScenario());
        return scenarios;
    }
}
