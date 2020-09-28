package de.hpi.mod.sim.worlds.traffic_light_robots;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.Graphics;

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
    public void updateEntities(float delta) {
        // for (Robot generalRobot : getRobots()) {
        //     TrafficLightRobot robot = (TrafficLightRobot) generalRobot;
        //     if (robot.getRobotSpecificDelay() == 0 || !robot.isInTest()) {
        //         robot.getDriveManager().update(delta);
        //     } else {
        //         robot.getDriveManager().update(delta, robot.getRobotSpecificDelay());
        //     }
        // }
            //TODO 
    }

    @Override
    public void resetScenario() {}

    @Override
    public List<Scenario> getScenarios() {
        // TODO Auto-generated method stub
        return new java.util.ArrayList<>();
    }

    @Override
    public Map<String, List<TestScenario>> getTestGroups() {
        // TODO Auto-generated method stub
        return new java.util.Hashtable<>();
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        for (TrafficLight light : getCrossRoadManager().getTrafficLights())
            light.render(graphics, (SimulationBlockView) getAnimationPanel());
    }

    @Override
    public void refreshSimulationProperties(int currentHeight, int currentWidth) {
        float blockSize = ((SimulationBlockView) getAnimationPanel()).getBlockSize();
        int width = (int) ((currentWidth / blockSize) / 3) * 3 - 6;
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
