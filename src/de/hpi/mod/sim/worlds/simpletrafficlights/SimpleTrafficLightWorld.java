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
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TrafficLightStatechartWrapper;
import de.hpi.mod.sim.worlds.simpletrafficlights.scenario.ScenarioGenerator;
import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;

public class SimpleTrafficLightWorld extends RobotWorld {

    public SimpleTrafficLightWorld() {
		super();
		publicName = "Robots with Traffic Light World";
	}

    @Override
    public void initialize() {
    	super.initialize();
    	// Moved here from static initialization of configuration class, until a better solution from the configuration if found.
    	GridConfiguration.setOriginOffsetX(-SimpleTrafficLightsConfiguration.getFieldWidth() / 2 - 1);
    	GridConfiguration.setOriginOffsetY(2);
	}
    
    @Override
    protected GridManager createGridManager() {
        return new CrossRoadsManager();
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
    	SimulationBlockView blockView = (SimulationBlockView) getAnimationPanel();
    	if(blockView != null) {
	        float blockSize = blockView.getBlockSize();
	        int width = (int) ((currentWidth / blockSize) + SimpleTrafficLightsConfiguration.getOriginOffsetX() * 2) * 3
	                + 1;
	        int height = (int) (((currentHeight / blockSize) - SimpleTrafficLightsConfiguration.getOriginOffsetY() * 2) / 3) * 3
	                + 1;
	        SimpleTrafficLightsConfiguration.setFieldDimensions(width, height);
	        getCrossRoadManager().updateFieldSize(width, height);
    	}
    }
    
    @Override
    public void refreshEntities() {
        super.refreshEntities();
        for (TrafficLightStatechartWrapper light : getCrossRoadManager().getTrafficLights()) {
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
        List<TrafficLightStatechartWrapper> lights = getCrossRoadManager().getTrafficLights();
        List<Entity> list = new ArrayList<>(lights.size() + superList.size());
        list.addAll(superList);
        list.addAll(lights);
        return list;
    }

    public CrossRoadsManager getCrossRoadManager() {
        return (CrossRoadsManager) getGridManager();
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