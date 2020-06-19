package de.hpi.mod.sim.core.model;

import java.util.List;

import de.hpi.mod.sim.core.simulation.Simulation;
import de.hpi.mod.sim.core.simulation.robot.Robot;
import de.hpi.mod.sim.core.testing.Detector;
import de.hpi.mod.sim.core.testing.Scenario;
import de.hpi.mod.sim.core.testing.scenarios.ScenarioManager;
import de.hpi.mod.sim.core.view.DriveSimFrame;

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

    public abstract IRobotDispatch getRoboterDispatch();
    
    public abstract List<Robot> getRobots(); //TODO Think about where to put
    
    public synchronized Robot addRobot() {  // TODO Think about where to put addRobotStuff
        return getSimulation().addRobotRunner(() -> getRoboterDispatch().addRobot());
    }

    public Robot addRobotAtPosition(Position pos, Robot.RobotState initialState, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzyEnd, boolean unloadingTest, boolean hasReservedBattery,
            boolean hardArrivedConstraint) {

        return getSimulation()
                .addRobotRunner(() -> getRoboterDispatch().addRobotAtPosition(pos, initialState, facing,
                targets, delay, initialDelay, fuzzyEnd, unloadingTest, hasReservedBattery, hardArrivedConstraint));
    }

    public Robot addRobotInScenario(Position pos, Orientation facing, int delay) {
        return getSimulation().addRobotRunner(() -> getRoboterDispatch().addRobotInScenario(pos, facing, delay));
    }
    
    public void runScenario(Scenario scenario) {
        frame.allowRunning();
        frame.setResizable(scenario.isResizable());
        getSimulation().reset();
        getSimulation().playScenario(scenario);
        getRoboterDispatch().releaseAllLocks();
        resetDetectors();
        if (!getSimulation().isRunning())
            getSimulation().toggleRunning();

        frame.resetSimulationView();
    }

    public void resetDetectors() {
        for (Detector detector : getDetectors())
            detector.reset();
    }
}