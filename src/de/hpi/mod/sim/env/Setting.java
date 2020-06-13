package de.hpi.mod.sim.env;

import java.util.List;

import de.hpi.mod.sim.env.model.IGrid;
import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.robot.Robot.RobotState;
import de.hpi.mod.sim.env.setting.infinitestations.RobotDispatcher;
import de.hpi.mod.sim.env.simulation.Simulation;
import de.hpi.mod.sim.env.testing.Detector;
import de.hpi.mod.sim.env.testing.scenarios.ScenarioManager;
import de.hpi.mod.sim.env.view.DriveSimFrame;

public abstract class Setting {

    private DriveSimFrame frame;

    private Simulation simulation;

    public abstract IGrid getGrid();

    public abstract List<Detector> getDetectors();

    public abstract ScenarioManager getScenarioManager();

    protected Setting(DriveSimFrame frame) {
        this.frame = frame;
        simulation = new Simulation(this);
    }

    public Simulation getSimulation() {
        return simulation;
    }
    
    public DriveSimFrame getFrame() {
        return frame;
    }

    public abstract void updateRobots(float delta);

    public abstract RobotDispatcher getRoboterDispatcher();
    
    public abstract List<Robot> getRobots(); //TODO Think about where to put
    
    public synchronized Robot addRobot() {  // TODO Think about where to put addRobotStuff
        return getSimulation().addRobotRunner(() -> getRoboterDispatcher().addRobot());
    }

    public Robot addRobotAtPosition(Position pos, RobotState initialState, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzyEnd, boolean unloadingTest, boolean hasReservedBattery,
            boolean hardArrivedConstraint) {

        return getSimulation()
                .addRobotRunner(() -> getRoboterDispatcher().addRobotAtPosition(pos, initialState, facing,
                targets, delay, initialDelay, fuzzyEnd, unloadingTest, hasReservedBattery, hardArrivedConstraint));
    }

    public Robot addRobotInScenario(Position pos, Orientation facing, int delay) {
        return getSimulation()
                .addRobotRunner(() -> getRoboterDispatcher().addRobotInScenario(pos, facing, delay));
    }
}