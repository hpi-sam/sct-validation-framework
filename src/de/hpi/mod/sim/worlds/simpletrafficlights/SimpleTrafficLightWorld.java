package de.hpi.mod.sim.worlds.simpletrafficlights;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.worlds.abstract_grid.GridConfiguration;
import de.hpi.mod.sim.worlds.abstract_grid.GridManager;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.RobotWorld;
import de.hpi.mod.sim.worlds.simpletrafficlights.scenario.ScenarioGenerator;
import de.hpi.mod.sim.worlds.trafficlights.TrafficLightsConfiguration;
import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;

public class SimpleTrafficLightWorld extends RobotWorld {

    private TrafficLightRenderer trafficLightRenderer;

	public SimpleTrafficLightWorld() {
		super();
		publicName = "Simple robots with Traffic Light World";
	}

    @Override
    public void initialize() {
    	super.initialize();
    	// Moved here from static initialization of configuration class, until a better solution from the configuration if found.
    	GridConfiguration.setOriginOffsetX(-TrafficLightsConfiguration.getFieldWidth() / 2 - 1);
    	SimpleTrafficLightsConfiguration.setOriginOffsetY(2);
    	
    	trafficLightRenderer = new TrafficLightRenderer(getSimulationBlockView(), getStreetNetworkManager());
	}
    
    @Override
    protected GridManager createGridManager() {
        return new StreetNetworkManager();
    }

    @Override
    public void resetScenario() {}

    @Override
    public List<Scenario> getScenarios() {
        return new ScenarioGenerator(this).getScenarios();
    }

    @Override
    public Map<String, List<TestScenario>> getTestGroups() {
        // TODO Add tests
        return new java.util.Hashtable<>();
    }

    @Override
    public void refreshSimulationProperties(int currentHeight, int currentWidth) {
    	
		// Transform pixes size to blocks
    	SimulationBlockView blockView = getSimulationBlockView();
        float blockSize = blockView.getBlockSize();
        int width = (int) (currentWidth / blockSize);
        int height = (int) (currentHeight / blockSize);
        
        // Update fild size in configuration 
        SimpleTrafficLightsConfiguration.setAvailableFieldDimensions(width, height);
        
        // Trigger update in GridManager
        getStreetNetworkManager().resetFieldDataStructures();
    }
    
    @Override
    public void runScenario(Scenario scenario){
    	// Start all Scenarios by 
    	super.runScenario(scenario);
    }
    
    @Override
    public void refreshEntities() {
        super.refreshEntities();
        getStreetNetworkManager().refreshEntities();
    }

    @Override
    public void clearEntities() {
        super.clearEntities();
    }

    @Override
    protected void renderEntities(Graphics graphics) {
    	super.renderEntities(graphics);
        trafficLightRenderer.render(graphics, getSimulationBlockView().getBlockSize());
    }
    
    @Override
    public List<? extends Entity> getEntities() {
        List<? extends Entity> superEntities = super.getEntities();
        List<? extends Entity> streetNetworkEntities = getStreetNetworkManager().getEntities();
        return Stream.concat(superEntities.stream(), streetNetworkEntities.stream()).collect(Collectors.toList());
    }

    public StreetNetworkManager getStreetNetworkManager() {
        return (StreetNetworkManager) getGridManager();
    }

    @Override
    public IHighlightable getHighlightAtPosition(int x, int y) {
    	
    	// Use super method to find if there is a robot at the targeted position
        IHighlightable highlight = super.getHighlightAtPosition(x, y);
        if (highlight != null)
            return highlight;
        
        // If there is no robot, check if there is a crossroad
        Position pos = getSimulationBlockView().toGridPosition(x, y);
        return getStreetNetworkManager().getLightForCrossroad(pos.getX() / 3, pos.getY() / 3);
        
    }
}