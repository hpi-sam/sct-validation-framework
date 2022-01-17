package de.hpi.mod.sim.worlds.simpletrafficlights;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.worlds.abstract_grid.GridConfiguration;
import de.hpi.mod.sim.worlds.abstract_grid.GridManager;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.RobotWorld;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TrafficLightWithStatechart;
import de.hpi.mod.sim.worlds.simpletrafficlights.scenario.ScenarioGenerator;
import de.hpi.mod.sim.worlds.trafficlights.TrafficLightsConfiguration;
import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;

public class SimpleTrafficLightWorld extends RobotWorld {

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
        getCrossRoadManager().updateFieldSize();
    }
    
    @Override
    public void refreshEntities() {
        super.refreshEntities();
        for (TrafficLightWithStatechart light : getCrossRoadManager().getTrafficLights()) {
            light.updateTimer();
        }
    }

    @Override
    public void clearEntities() {
        super.clearEntities();
        getCrossRoadManager().getTrafficLights().clear();
    }
    
    @Override
    public List<? extends Entity> getEntities() {
        List<? extends Entity> superList = super.getEntities();
        List<TrafficLightWithStatechart> lights = getCrossRoadManager().getTrafficLights();
        List<Entity> list = new ArrayList<>(lights.size() + superList.size());
        list.addAll(superList);
        list.addAll(lights);
        return list;
    }

    public StreetNetworkManager getCrossRoadManager() {
        return (StreetNetworkManager) getGridManager();
    }

    @Override
    public IHighlightable getHighlightAtPosition(int x, int y) {
        IHighlightable highlight = super.getHighlightAtPosition(x, y);
        if (highlight != null)
            return highlight;
        
        Position pos = getSimulationBlockView().toGridPosition(x, y);
        return getCrossRoadManager().getLightForCrossroad(pos.getX() / 3, pos.getY() / 3);
        
    }
}