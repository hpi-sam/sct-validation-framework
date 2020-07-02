package de.hpi.mod.sim.setting.infinitewarehouses.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.scenario.EntityDescription;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.setting.grid.Orientation;
import de.hpi.mod.sim.setting.grid.Position;
import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehouseSimConfig;
import de.hpi.mod.sim.setting.infinitewarehouses.ScenarioRobotDescription;
import de.hpi.mod.sim.setting.infinitewarehouses.env.RobotManagement;

public class ScenarioGenerator {

    RobotManagement robots;

    public ScenarioGenerator(RobotManagement robots) {
        this.robots = robots;
    }

    private class OneRobotScenario extends Scenario {
        public OneRobotScenario() {
            name = "One Robot";
        }

        @Override
        public List<EntityDescription<? extends Entity>> initializeScenario() {
            int maxStations = InfiniteWarehouseSimConfig.getChargingStationsInUse();
            int station_number;
            int[] robotsAtStations = new int[maxStations];
            List<EntityDescription<? extends Entity>> newRobots = new ArrayList<>();

            do {
                station_number = ThreadLocalRandom.current().nextInt(maxStations);
            } while (robotsAtStations[station_number] >= InfiniteWarehouseSimConfig.getMaxRobotsPerStation());

            ScenarioRobotDescription singleRobot = new ScenarioRobotDescription(robots, new Position(
                            (InfiniteWarehouseSimConfig.getFirstStationTop().getX()
                                    + station_number * InfiniteWarehouseSimConfig.getSpaceBetweenChargingStations()) + 2,
                            InfiniteWarehouseSimConfig.getFirstStationTop().getY() - robotsAtStations[station_number] + 2),
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
        public List<EntityDescription<? extends Entity>> initializeScenario() {
            int maxStations = InfiniteWarehouseSimConfig.getChargingStationsInUse();
            int station_number;
            int[] robotsAtStations = new int[maxStations];
            List<EntityDescription<? extends Entity>> newRobots = new ArrayList<>();

            for (int i = 0; i < InfiniteWarehouseSimConfig.getChargingStationsInUse(); i++) {
                do {
                    station_number = ThreadLocalRandom.current().nextInt(maxStations);
                } while (robotsAtStations[station_number] >= InfiniteWarehouseSimConfig.getMaxRobotsPerStation());

                newRobots.add(new ScenarioRobotDescription(robots,
                        new Position(
                        (InfiniteWarehouseSimConfig.getFirstStationTop().getX()
                                + station_number * InfiniteWarehouseSimConfig.getSpaceBetweenChargingStations()) + 2,
                        InfiniteWarehouseSimConfig.getFirstStationTop().getY() - robotsAtStations[station_number] + 2),
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
        public List<EntityDescription<? extends Entity>> initializeScenario() {
            int maxStations = InfiniteWarehouseSimConfig.getChargingStationsInUse();
            int station_number;
            int[] robotsAtStations = new int[maxStations];
            List<EntityDescription<? extends Entity>> newRobots = new ArrayList<>();

            for (int i = 0; i < InfiniteWarehouseSimConfig.getChargingStationsInUse()
                    * Math.ceil(((float) InfiniteWarehouseSimConfig.getRecommendedRobotsPerStation()) / 2); i++) {
                do {
                    station_number = ThreadLocalRandom.current().nextInt(maxStations);
                } while (robotsAtStations[station_number] >= InfiniteWarehouseSimConfig.getMaxRobotsPerStation());

                newRobots.add(new ScenarioRobotDescription(robots,
                        new Position(
                        (InfiniteWarehouseSimConfig.getFirstStationTop().getX()
                                + station_number * InfiniteWarehouseSimConfig.getSpaceBetweenChargingStations()) + 2,
                        InfiniteWarehouseSimConfig.getFirstStationTop().getY() - robotsAtStations[station_number] + 2),
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
        public List<EntityDescription<? extends Entity>> initializeScenario() {
            int maxStations = InfiniteWarehouseSimConfig.getChargingStationsInUse();
            int station_number;
            int[] robotsAtStations = new int[maxStations];
            List<EntityDescription<? extends Entity>> newRobots = new ArrayList<>();

            for (int i = 0; i < InfiniteWarehouseSimConfig.getChargingStationsInUse()
                    * InfiniteWarehouseSimConfig.getRecommendedRobotsPerStation(); i++) {
                do {
                    station_number = ThreadLocalRandom.current().nextInt(maxStations);
                } while (robotsAtStations[station_number] >= InfiniteWarehouseSimConfig.getMaxRobotsPerStation());

                newRobots.add(new ScenarioRobotDescription(robots,
                        new Position(
                        (InfiniteWarehouseSimConfig.getFirstStationTop().getX()
                                + station_number * InfiniteWarehouseSimConfig.getSpaceBetweenChargingStations()) + 2,
                        InfiniteWarehouseSimConfig.getFirstStationTop().getY() - robotsAtStations[station_number] + 2),
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
        public List<EntityDescription<? extends Entity>> initializeScenario() {
            int maxStations = InfiniteWarehouseSimConfig.getChargingStationsInUse();
            int station_number;
            int[] robotsAtStations = new int[maxStations];
            List<EntityDescription<? extends Entity>> newRobots = new ArrayList<>();

            for (int i = 0; i < InfiniteWarehouseSimConfig.getChargingStationsInUse()
                    * InfiniteWarehouseSimConfig.getRecommendedRobotsPerStation(); i++) {
                // for(int i=0; i<3; i++) {
                do {
                    station_number = ThreadLocalRandom.current().nextInt(maxStations);
                } while (robotsAtStations[station_number] >= InfiniteWarehouseSimConfig.getMaxRobotsPerStation());

                newRobots.add(new ScenarioRobotDescription(robots,
                        new Position(
                        (InfiniteWarehouseSimConfig.getFirstStationTop().getX()
                                + station_number * InfiniteWarehouseSimConfig.getSpaceBetweenChargingStations()) + 2,
                        InfiniteWarehouseSimConfig.getFirstStationTop().getY() - robotsAtStations[station_number] + 2),
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