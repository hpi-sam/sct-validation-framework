package de.hpi.mod.sim.env.view.model;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import java.util.ArrayList;
import java.util.List;

public abstract class Scenario {

    protected List<NewRobot> newRobots = new ArrayList<>();
    private List<Robot> robots = new ArrayList<>();

    private boolean playing = false;
    protected boolean isATest = false;

    protected String name = "Unnamed";


    public Scenario() {
        loadScenario();
    }

    public String getName() {
        return name;
    }

    public void loadScenario() {
        newRobots.clear();
        initializeScenario();
    }

    protected abstract void initializeScenario();

    public void playScenario(SimulationWorld sim) {
        robots.clear();
        newRobots.forEach(r -> r.register(sim));
        // TODO add robots and targets to list
    }

    public boolean isScenarioFullfilled() {
        if (isATest) {
            // TODO implement check if Robots are on target
            return false;
        }
        return false;
    }

    public boolean isScenarioATest() {
        return isATest;
    }

    public void clear() {
        robots.clear();
    }


    public abstract class NewRobot {
        public abstract Robot register(SimulationWorld sim);
    }

    public class NewSimpleRobot extends NewRobot {

        @Override
        public Robot register(SimulationWorld sim) {
            return sim.addRobot();
        }
    }

    public class NewStationRobot extends NewRobot {

        private int stationID;

        public NewStationRobot(int stationID) {
            this.stationID = stationID;
        }

        @Override
        public Robot register(SimulationWorld sim) {
            return sim.addRobot(stationID);
        }
    }

    public class NewWaypointRobot extends NewRobot {

        private Position pos, target;
        private Orientation facing;

        public NewWaypointRobot(Position pos, Orientation facing, Position target) {
            this.pos = pos;
            this.target = target;
            this.facing = facing;
        }

        @Override
        public Robot register(SimulationWorld sim) {
            return sim.addRobotAtWaypoint(pos, facing, target);
        }
    }
}
