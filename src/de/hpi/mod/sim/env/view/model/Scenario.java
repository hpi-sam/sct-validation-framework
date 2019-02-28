package de.hpi.mod.sim.env.view.model;

import de.hpi.mod.sim.env.view.sim.ScenarioManager;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import java.util.List;

/**
 * Contains Positions of Robots which can be applied to the Simulation.
 * Some Scenarios are defined in {@link ScenarioManager}
 */
public abstract class Scenario {

    /**
     * The name of the Scenario
     */
    protected String name = "Unnamed";
    protected boolean resizable = false;


    protected Scenario() {

    }

    public String getName() {
        return name;
    }

    protected abstract List<NewRobot> initializeScenario();

    public void loadScenario(SimulationWorld sim) {
        List<NewRobot> newRobots = initializeScenario();
        newRobots.forEach(r -> r.register(sim));
    }

	public boolean isResizable() {
		return resizable;
	};
}
