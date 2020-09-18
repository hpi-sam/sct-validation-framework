package de.hpi.mod.sim.worlds.infinitewarehouse.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouseConfiguration;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.WarehouseManager;

public class ScenarioGenerator {

    WarehouseManager robots;

    public ScenarioGenerator(WarehouseManager robots) {
        this.robots = robots;
    }

    private class OneRobotScenario extends Scenario {
        public OneRobotScenario() {
            name = "One Robot";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            int maxStations = InfiniteWarehouseConfiguration.getChargingStationsInUse();
            int station_number;
            int[] robotsAtStations = new int[maxStations];
            List<EntitySpecification<? extends Entity>> newRobots = new ArrayList<>();

            do {
                station_number = ThreadLocalRandom.current().nextInt(maxStations);
            } while (robotsAtStations[station_number] >= InfiniteWarehouseConfiguration.getMaxRobotsPerStation());

            ScenarioRobotSpecification singleRobot = new ScenarioRobotSpecification(robots, new Position(
                            (InfiniteWarehouseConfiguration.getFirstStationTop().getX()
                                    + station_number * InfiniteWarehouseConfiguration.getSpaceBetweenChargingStations()) + 2,
                            InfiniteWarehouseConfiguration.getFirstStationTop().getY() - robotsAtStations[station_number] + 2),
                    Orientation.NORTH);
            singleRobot.setIsAlone(true);
            newRobots.add(singleRobot);
            return newRobots;
        }
    }

    private class EasyScenario extends Scenario {
        public EasyScenario() {
            name = "Few Robots";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            int maxStations = InfiniteWarehouseConfiguration.getChargingStationsInUse();
            int station_number;
            int[] robotsAtStations = new int[maxStations];
            List<EntitySpecification<? extends Entity>> newRobots = new ArrayList<>();

            for (int i = 0; i < InfiniteWarehouseConfiguration.getChargingStationsInUse(); i++) {
                do {
                    station_number = ThreadLocalRandom.current().nextInt(maxStations);
                } while (robotsAtStations[station_number] >= InfiniteWarehouseConfiguration.getMaxRobotsPerStation());

                newRobots.add(new ScenarioRobotSpecification(robots,
                        new Position(
                        (InfiniteWarehouseConfiguration.getFirstStationTop().getX()
                                + station_number * InfiniteWarehouseConfiguration.getSpaceBetweenChargingStations()) + 2,
                        InfiniteWarehouseConfiguration.getFirstStationTop().getY() - robotsAtStations[station_number] + 2),
                        Orientation.NORTH));
                robotsAtStations[station_number]++;
            }
            return newRobots;
        }
    }

    private class MediumScenario extends Scenario {
        public MediumScenario() {
            name = "Average number of Robots";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            int maxStations = InfiniteWarehouseConfiguration.getChargingStationsInUse();
            int station_number;
            int[] robotsAtStations = new int[maxStations];
            List<EntitySpecification<? extends Entity>> newRobots = new ArrayList<>();

            for (int i = 0; i < InfiniteWarehouseConfiguration.getChargingStationsInUse()
                    * Math.ceil(((float) InfiniteWarehouseConfiguration.getRecommendedRobotsPerStation()) / 2); i++) {
                do {
                    station_number = ThreadLocalRandom.current().nextInt(maxStations);
                } while (robotsAtStations[station_number] >= InfiniteWarehouseConfiguration.getMaxRobotsPerStation());

                newRobots.add(new ScenarioRobotSpecification(robots,
                        new Position(
                        (InfiniteWarehouseConfiguration.getFirstStationTop().getX()
                                + station_number * InfiniteWarehouseConfiguration.getSpaceBetweenChargingStations()) + 2,
                        InfiniteWarehouseConfiguration.getFirstStationTop().getY() - robotsAtStations[station_number] + 2),
                        Orientation.NORTH));
                robotsAtStations[station_number]++;
            }
            return newRobots;
        }
    }

    private class HardScenario extends Scenario {
        public HardScenario() {
            name = "Many Robots";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            int maxStations = InfiniteWarehouseConfiguration.getChargingStationsInUse();
            int station_number;
            int[] robotsAtStations = new int[maxStations];
            List<EntitySpecification<? extends Entity>> newRobots = new ArrayList<>();

            for (int i = 0; i < InfiniteWarehouseConfiguration.getChargingStationsInUse()
                    * InfiniteWarehouseConfiguration.getRecommendedRobotsPerStation(); i++) {
                do {
                    station_number = ThreadLocalRandom.current().nextInt(maxStations);
                } while (robotsAtStations[station_number] >= InfiniteWarehouseConfiguration.getMaxRobotsPerStation());

                newRobots.add(new ScenarioRobotSpecification(robots,
                        new Position(
                        (InfiniteWarehouseConfiguration.getFirstStationTop().getX()
                                + station_number * InfiniteWarehouseConfiguration.getSpaceBetweenChargingStations()) + 2,
                        InfiniteWarehouseConfiguration.getFirstStationTop().getY() - robotsAtStations[station_number] + 2),
                        Orientation.NORTH));
                robotsAtStations[station_number]++;
            }
            return newRobots;
        }
    }

    private class HardcoreScenario extends Scenario {
        public HardcoreScenario() {
            name = "Many Robots + Reaction Delay [Extreme mode]";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            int maxStations = InfiniteWarehouseConfiguration.getChargingStationsInUse();
            int station_number;
            int[] robotsAtStations = new int[maxStations];
            List<EntitySpecification<? extends Entity>> newRobots = new ArrayList<>();

            for (int i = 0; i < InfiniteWarehouseConfiguration.getChargingStationsInUse()
                    * InfiniteWarehouseConfiguration.getRecommendedRobotsPerStation(); i++) {
                // for(int i=0; i<3; i++) {
                do {
                    station_number = ThreadLocalRandom.current().nextInt(maxStations);
                } while (robotsAtStations[station_number] >= InfiniteWarehouseConfiguration.getMaxRobotsPerStation());

                newRobots.add(new ScenarioRobotSpecification(robots,
                        new Position(
                        (InfiniteWarehouseConfiguration.getFirstStationTop().getX()
                                + station_number * InfiniteWarehouseConfiguration.getSpaceBetweenChargingStations()) + 2,
                        InfiniteWarehouseConfiguration.getFirstStationTop().getY() - robotsAtStations[station_number] + 2),
                        Orientation.NORTH, 1000));
                robotsAtStations[station_number]++;
            }
            return newRobots;
        }
    }

	public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new OneRobotScenario());
        scenarios.add(new EasyScenario());
        scenarios.add(new MediumScenario());
        scenarios.add(new HardScenario());
        scenarios.add(new HardcoreScenario());
        return scenarios;
	}
}