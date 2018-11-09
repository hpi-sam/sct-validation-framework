package de.hpi.mod.sim.env.view.model;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;

import java.util.ArrayList;
import java.util.List;

public class ScenarioManager {

    public static List<Scenario> scenarios = new ArrayList<>();

    static {
        scenarios.add(new EmptyScenario());
        scenarios.add(new FourRobotsOnCrossroadScenario());
    }

    private static class EmptyScenario extends Scenario {

        public EmptyScenario() {
            name = "Empty";
        }

        @Override
        public void initializeScenario() {}
    }

    private static class RobotInEachStationScenario extends Scenario {

        public RobotInEachStationScenario() {
            name = "Robot in each Station";
        }

        @Override
        public void initializeScenario() {

        }
    }

    private static class FourRobotsOnCrossroadScenario extends Scenario {

        public FourRobotsOnCrossroadScenario() {
            name = "4 Robots on Crossroad";
        }

        @Override
        public void initializeScenario() {
            newRobots.add(new NewWaypointRobot(new Position(6, 5), Orientation.WEST, new Position(0, 5)));
            newRobots.add(new NewWaypointRobot(new Position(5, 3), Orientation.NORTH, new Position(5, 9)));
            newRobots.add(new NewWaypointRobot(new Position(3, 4), Orientation.EAST, new Position(9, 4)));
            newRobots.add(new NewWaypointRobot(new Position(4, 6), Orientation.SOUTH, new Position(4, 0)));
        }
    }
}
