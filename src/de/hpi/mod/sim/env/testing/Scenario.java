package de.hpi.mod.sim.env.testing;

import de.hpi.mod.sim.env.Setting;
import de.hpi.mod.sim.env.testing.scenarios.ScenarioManager;

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
    protected String description = "No description available";
    protected boolean resizable = false;


    protected Scenario() {

    }

    public String getName() {
        return name;
    }
    
    public String getDescription() {
    	return description;
    }

    protected abstract List<RobotDescription> initializeScenario();

    public void loadScenario(Setting setting) { 
        List<RobotDescription> newRobots = initializeScenario();
        newRobots.forEach(robot -> robot.register(setting));
    }

	public boolean isResizable() {
		return resizable;
	};
}
