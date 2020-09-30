package de.hpi.mod.sim.worlds.traffic_light_robots;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.abstract_grid.GridManager;
import de.hpi.mod.sim.worlds.abstract_robots.RobotWorld;
import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;

public class TrafficLightWorld extends RobotWorld {

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
        float blockSize = ((SimulationBlockView) getAnimationPanel()).getBlockSize();
        int width = (int) ((currentWidth / blockSize) / 3) * 3 - 5;
        int height = (int) (((currentHeight / blockSize) - TrafficLightsConfiguration.getOriginOffsetY() * 2) / 3) * 3
                + 1;
        TrafficLightsConfiguration.setFieldDimensions(width, height);
        getCrossRoadManager().updateFieldSize(width, height);
    }
    
    @Override
    public void refreshEntities() {
        super.refreshEntities();
        for (TrafficLight light : getCrossRoadManager().getTrafficLights()) {
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
        List<TrafficLight> lights = getCrossRoadManager().getTrafficLights();
        List<Entity> list = new ArrayList<>(lights.size() + superList.size());
        list.addAll(superList);
        list.addAll(lights);
        return list;
    }

    public CrossRoadsManager getCrossRoadManager() {
        return (CrossRoadsManager) getGridManager();
    }
}
