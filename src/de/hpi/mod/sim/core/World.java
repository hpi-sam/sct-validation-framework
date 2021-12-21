package de.hpi.mod.sim.core;

import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.simulation.SimulationRunner;
import de.hpi.mod.sim.core.view.SimulatorFrame;
import de.hpi.mod.sim.core.view.panels.AnimationPanel;

public abstract class World {
	
	protected String publicName;

    private SimulatorFrame frame;

    private SimulationRunner simulationRunner;

    private ScenarioManager scenarioManager;

    private List<Detector> detectors;

    private AnimationPanel panel;

    protected abstract List<Detector> createDetectors();

    public List<Detector> getDetectors() {
        return detectors;
    }

    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    public SimulationRunner getSimulationRunner() {
        return simulationRunner;
    }
    
    public SimulatorFrame getFrame() {
        return frame;
    }
    
    public final void initialize(SimulatorFrame frame, SimulationRunner simulationRunner) {
        this.frame = frame;
        this.simulationRunner = simulationRunner;
        initialize();
        //As the scenarioManager depends on the lists of scenarios being prepared, it is created after the initialize() call
        this.scenarioManager = new ScenarioManager(this);
        this.detectors = createDetectors();
    }


    protected abstract void initialize();

    public abstract void clearEntities();

    public abstract void updateEntities(float delta);

    public void runScenario(Scenario scenario) {
        getSimulationRunner().setRunForbidden(false);
        frame.setResizable(scenario.isResizable());
        getSimulationRunner().reset();
        getSimulationRunner().playScenario(scenario);
        resetScenario();
        
        // prepare detectors
        for (Detector detector : getDetectors()) {
        	// reset
        	detector.reset();
        	
        	// activate if needed
        	if(scenario.isDetectorNeeded(detector))
            	detector.activate();
        	else
        		detector.deactivate();
        		
        }
            
        if (!getSimulationRunner().isRunning())
            getSimulationRunner().toggleRunning();

        frame.resetAnimationPanel();
    }

    public abstract void resetScenario();
    
    public void deactivateDetectors() {
        for (Detector detector : getDetectors())
			if(detector.isActivated())
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

    public AnimationPanel createAnimationPanel() {
        return new AnimationPanel(this);
    }

    @Override
	public String toString() {
		return getPublicName()+ " (" + this.getClass().getName() + ")";
	}

	public AnimationPanel getAnimationPanel() {
        return panel;
    }

	public String getPublicName() {
		return publicName;
	}

	public String getInternalName() {
		return this.getClass().getName().replace(" ", "").toLowerCase();
	}

	public void updateDetectors() {
		List<? extends Entity> entities = this.getEntities();
		for (Detector detector : this.getDetectors())
			if(detector.isActivated())
				detector.update(entities);
	}

	

}