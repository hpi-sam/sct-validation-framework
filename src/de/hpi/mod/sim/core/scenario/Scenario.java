package de.hpi.mod.sim.core.scenario;

import java.util.List;
import de.hpi.mod.sim.core.model.Setting;

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
        List<EntityDescription<?>> newEntities = initializeScenario();
        newEntities.forEach(e -> setting.getSimulation().addEntityRunner(() -> e.get()));
    }

	public boolean isResizable() {
		return resizable;
	};
}

