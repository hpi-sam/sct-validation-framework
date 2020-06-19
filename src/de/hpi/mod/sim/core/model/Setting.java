package de.hpi.mod.sim.core.model;

import java.util.List;

import de.hpi.mod.sim.core.simulation.Simulation;
import de.hpi.mod.sim.core.testing.Detector;
import de.hpi.mod.sim.core.testing.Scenario;
import de.hpi.mod.sim.core.testing.scenarios.ScenarioManager;
import de.hpi.mod.sim.core.view.DriveSimFrame;
import de.hpi.mod.sim.setting.robot.Robot;

public abstract class Setting {

    private DriveSimFrame frame;

    private Simulation simulation;

    public abstract IGrid getGrid();

    public abstract List<Detector> getDetectors();

    public abstract ScenarioManager getScenarioManager();

    public Simulation getSimulation() {
        return simulation;
    }
    
    public DriveSimFrame getFrame() {
        return frame;
    }

    public void initialize(DriveSimFrame frame) {
        this.frame = frame;
        simulation = new Simulation(this);
    }

    public void clearEntities() {
        getEntities().clear();
    }

    public abstract void updateEntities(float delta);

    public abstract IRobotDispatch getRoboterDispatch();
    
    public synchronized Entity addEntity() {  // TODO Think about where to put addRobotStuff
        return getSimulation().addEntityRunner(() -> getRoboterDispatch().addRobot());
    }

    public Entity addRobotAtPosition(Position pos, Robot.RobotState initialState, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzyEnd, boolean unloadingTest, boolean hasReservedBattery,
            boolean hardArrivedConstraint) {

        return getSimulation()
                .addEntityRunner(() -> getRoboterDispatch().addRobotAtPosition(pos, initialState, facing,
                targets, delay, initialDelay, fuzzyEnd, unloadingTest, hasReservedBattery, hardArrivedConstraint));
    }

    public Entity addRobotInScenario(Position pos, Orientation facing, int delay) {
        return getSimulation().addEntityRunner(() -> getRoboterDispatch().addRobotInScenario(pos, facing, delay));
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

    public abstract List<? extends Entity> getEntities();

    public void refreshEntities() {
        getRoboterDispatch().refresh();
	}

    public abstract void onSimulationPropertyRefresh();
}