package de.hpi.mod.sim.core;

import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.Detector;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.simulation.SimulationRunner;
import de.hpi.mod.sim.core.view.SimulatorFrame;
import de.hpi.mod.sim.core.view.panels.AnimationPanel;

public abstract class World {

    private SimulatorFrame frame;

    private SimulationRunner simulationRunner;

    public abstract List<Detector> getDetectors();

    public abstract ScenarioManager getScenarioManager();

    public SimulationRunner getSimulationRunner() {
        return simulationRunner;
    }
    
    public SimulatorFrame getFrame() {
        return frame;
    }

    public void initialize(SimulatorFrame frame, SimulationRunner simulationRunner) {
        this.frame = frame;
        this.simulationRunner = simulationRunner;
    }

    public void clearEntities() {
        getEntities().clear();
    }

    public abstract void updateEntities(float delta);

    public void runScenario(Scenario scenario) {
        getSimulationRunner().setRunForbidden(false);
        frame.setResizable(scenario.isResizable());
        getSimulationRunner().reset();
        getSimulationRunner().playScenario(scenario);
        resetScenario();
        for (Detector detector : getDetectors())
            detector.reset();
        if (!getSimulationRunner().isRunning())
            getSimulationRunner().toggleRunning();

        frame.resetAnimationPanel();
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

	public void resetAnimationPanel() { }

    public abstract IHighlightable getHighlightAtPosition(int x, int y);

    public abstract void close();

    public abstract AnimationPanel getAnimationPanel();

	

}