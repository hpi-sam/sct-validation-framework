package de.hpi.mod.sim.core.model;

import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.Detector;
import de.hpi.mod.sim.core.scenario.EntityDescription;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Simulation;
import de.hpi.mod.sim.core.view.DriveSimFrame;

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

    public void initialize(DriveSimFrame frame, Simulation simulation) {
        this.frame = frame;
        this.simulation = simulation;
    }

    public void clearEntities() {
        getEntities().clear();
    }

    public abstract void updateEntities(float delta);

    public abstract IRobotDispatch getRoboterDispatch();

    public void runScenario(Scenario scenario) {
        getSimulation().setRunForbidden(false);
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
    
    public abstract List<Scenario> getScenarios();

    public abstract Map<String, List<TestScenario>> getTestGroups();

    public abstract void render(java.awt.Graphics graphics);

    public abstract void refreshSimulationProperties(int currentHeight, int currentWidth);

    public void mousePressed(java.awt.event.MouseEvent e) { }

	public void resetView() {
	}

    public abstract <E extends Entity> E fromDescription(EntityDescription<E> e);

    public abstract IHighlightable getHighlightAtPosition(Position pos);

}