package de.hpi.mod.sim.worlds.traffic_light_robots.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.traffic_light_robots.CrossRoadsManager;
import de.hpi.mod.sim.worlds.traffic_light_robots.TrafficLightWorld;
import de.hpi.mod.sim.worlds.traffic_light_robots.TrafficLightsConfiguration;

public class ScenarioGenerator {

    private TrafficLightWorld world;

    public ScenarioGenerator(TrafficLightWorld world) {
        this.world = world;
    }

    private class SingleLightScenario extends Scenario {
        public SingleLightScenario() {
            name = "Single Traffic Light";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            list.add(new TrafficLightSpecification(new Position(0, 2), world));
            return list;
        }
    }

    private List<EntitySpecification<? extends Entity>> trafficLightsForEveryCrossRoad() {
        int lightsPerCol = TrafficLightsConfiguration.getFieldHeight() / 3;
        int lightsPerRow = TrafficLightsConfiguration.getFieldWidth() / 3;
        List<EntitySpecification<? extends Entity>> lights = new ArrayList<>(lightsPerCol * lightsPerRow);
        for (int y = 0; y < lightsPerCol; y++) {
            for (int x = 0; x < lightsPerRow; x++) {
                lights.add(new TrafficLightSpecification(new Position(x * 3 + 2, y * 3), world));
            }
        }
        return lights;
    }

    private class NoRobotsScenario extends Scenario {
        public NoRobotsScenario() {
            name = "No robots";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            return trafficLightsForEveryCrossRoad();
        }
    }
    
    private class OneRobotScenario extends Scenario {
        public OneRobotScenario() {
            name = "One robot";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<?>> specs = trafficLightsForEveryCrossRoad();
            specs.addAll(getRandomRobots(1));
            return specs;
        }
    }

    private class FewRobotsScenario extends Scenario {
        public FewRobotsScenario() {
            name = "Few robots";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<?>> specs = trafficLightsForEveryCrossRoad();
            specs.addAll(getRandomRobots(getMaximumPossibleRobots()/5));
            return specs;
        }
    }

    private class AverageRobotsScenario extends Scenario {
        public AverageRobotsScenario() {
            name = "Average number of robots";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<?>> specs = trafficLightsForEveryCrossRoad();
            specs.addAll(getRandomRobots(getMaximumPossibleRobots() / 3));
            return specs;
        }
    }

    private class ManyRobotsScenario extends Scenario {
        public ManyRobotsScenario() {
            name = "Many robots";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<?>> specs = trafficLightsForEveryCrossRoad();
            specs.addAll(getRandomRobots(getMaximumPossibleRobots() / 2));
            return specs;
        }
    }

    private List<TrafficLightRobotSpecification> getRandomRobots(int count) {
        List<Position> positions = CrossRoadsManager.getAllPossiblePositions();
        List<TrafficLightRobotSpecification> robots = new ArrayList<>(count);

        if (count > positions.size())  
            throw new IllegalArgumentException();
        
        for (int i = 0; i < count; i++) {
            int index = ThreadLocalRandom.current().nextInt(positions.size());
            Position pos = positions.get(index);
            positions.remove(index);
            robots.add(new TrafficLightRobotSpecification(world, pos,
                    CrossRoadsManager.getSuitableRobotOrientationForPosition(pos), CrossRoadsManager.getRandomDestination()));
        }
        return robots;
    }

    private int getMaximumPossibleRobots() {
        return 2 * ((TrafficLightsConfiguration.getFieldWidth() - 1) / 3 + 
        (TrafficLightsConfiguration.getFieldHeight() -1 ) / 3);
    }


    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new SingleLightScenario());
        scenarios.add(new NoRobotsScenario());
        scenarios.add(new OneRobotScenario());
        scenarios.add(new FewRobotsScenario());
        scenarios.add(new AverageRobotsScenario());
        scenarios.add(new ManyRobotsScenario());
        return scenarios;
    }
}
