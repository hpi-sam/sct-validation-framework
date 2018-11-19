package de.hpi.mod.sim.env.view.model;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains Positions of Robots which can be applied to the Simulation.
 * Some Scenarios are defined in {@link de.hpi.mod.sim.env.view.ScenarioManager}
 */
public abstract class Scenario {

    /**
     * List of Robots the add to the Simulation
     */
    protected List<NewRobot> newRobots = new ArrayList<>();

    /**
     * The name of the Scenario
     */
    protected String name = "Unnamed";


    protected Scenario() {
        loadScenario();
    }

    public String getName() {
        return name;
    }

    /**
     * Loads the values of the new Robots.
     * Has to be called before {@link #playScenario(SimulationWorld)}
     */
    public void loadScenario() {
        newRobots.clear();
        initializeScenario();
    }

    protected abstract void initializeScenario();

    public void playScenario(SimulationWorld sim) {
        newRobots.forEach(r -> r.register(sim));
    }

    public abstract class NewRobot {
        public abstract void register(SimulationWorld sim);
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
        public void register(SimulationWorld sim) {
            sim.addRobotAtWaypoint(pos, facing, target);
        }
    }
}
