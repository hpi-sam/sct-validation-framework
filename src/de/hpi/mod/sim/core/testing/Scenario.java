package de.hpi.mod.sim.core.testing;

import java.util.List;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.testing.scenarios.ScenarioManager;

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

    protected abstract List<EntityDescription<?>> initializeScenario();

    public void loadScenario(Setting setting) { 
        List<EntityDescription<? extends Entity>> newEntities = initializeScenario();
        newEntities.forEach(e -> setting.getSimulation().addEntityRunner(() -> setting.fromDescription(e)));
    }

	public boolean isResizable() {
		return resizable;
	};
}

