package de.hpi.mod.sim.core.scenario;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;

/**
 * An executable set up.
 * Some Scenarios are defined in {@link ScenarioManager}
 */
public abstract class Scenario {

    /**
     * The name of the Scenario
     */
    protected String name = "Unnamed";
    protected String description = "No description available";
    protected boolean resizable = false;
    protected List<Entity> entities = new CopyOnWriteArrayList<>();


    protected Scenario() {

    }

    public String getName() {
        return name;
    }
    
    public String getDescription() {
    	return description;
    }

    protected abstract List<EntitySpecification<?>> getScenarioEntities();

    public void loadScenario(World world) { 
        entities.clear();
        List<EntitySpecification<?>> specs = getScenarioEntities();
                
        specs.forEach(spec -> world.getSimulationRunner().addEntityRunner(() -> {
            Entity e = spec.createEntity();
            entities.add(e);
            return e;
        }));
    }

	public boolean isResizable() {
		return resizable;
	};
	
	public boolean isDetectorNeeded(Detector d) {
		return d.isNeededForScenarios();
	}
}

