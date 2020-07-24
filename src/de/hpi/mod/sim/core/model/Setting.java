package de.hpi.mod.sim.core.model;

import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.Detector;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Simulation;
import de.hpi.mod.sim.core.view.SimulatorFrame;
import de.hpi.mod.sim.core.view.panels.SimulationView;

public abstract class Setting {

    private SimulatorFrame frame;

    private Simulation simulation;

    public abstract List<Detector> getDetectors();

    public abstract ScenarioManager getScenarioManager();

    public Simulation getSimulation() {
        return simulation;
    }
    
    public SimulatorFrame getFrame() {
        return frame;
    }

    public void initialize(SimulatorFrame frame, Simulation simulation) {
        this.frame = frame;
        this.simulation = simulation;
    }

    public void clearEntities() {
        getEntities().clear();
    }

    public abstract void updateEntities(float delta);

    public void runScenario(Scenario scenario) {
        getSimulation().setRunForbidden(false);
        frame.setResizable(scenario.isResizable());
        getSimulation().reset();
        getSimulation().playScenario(scenario);
        resetScenario();
        for (Detector detector : getDetectors())
            detector.reset();
        if (!getSimulation().isRunning())
            getSimulation().toggleRunning();

        frame.resetSimulationView();
    }

    public abstract void resetScenario();
    
    public void deactivateDetectors() {
        for (Detector detector : getDetectors())
            detector.deactivate();
    }

    public abstract List<? extends Entity> getEntities();

    public abstract void refreshEntities();
    
    public abstract List<Scenario> getScenarios();

    public abstract Map<String, List<TestScenario>> getTestGroups();

    public abstract void render(java.awt.Graphics graphics);

    public abstract void refreshSimulationProperties(int currentHeight, int currentWidth);

    public void mousePressed(java.awt.event.MouseEvent e) { }

	public void resetView() {
	}

    public abstract IHighlightable getHighlightAtPosition(int x, int y);

    public abstract void close();

    public abstract SimulationView getView();

	

}