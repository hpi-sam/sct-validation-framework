package de.hpi.mod.sim.worlds.infinitewarehouse;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.Detector;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.simulation.SimulationRunner;
import de.hpi.mod.sim.core.view.SimulatorFrame;
import de.hpi.mod.sim.worlds.abstract_grid.GridManager;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotWorld;
import de.hpi.mod.sim.worlds.abstract_robots.detectors.CollisionDetector;
import de.hpi.mod.sim.worlds.abstract_robots.detectors.DeadlockDetector;
import de.hpi.mod.sim.worlds.infinitewarehouse.detectors.*;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.StationManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.WarehouseManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.scenario.RobotSpecification;
import de.hpi.mod.sim.worlds.infinitewarehouse.scenario.ScenarioGenerator;
import de.hpi.mod.sim.worlds.infinitewarehouse.scenario.TestCaseGenerator;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.RobotRenderer;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;

public class InfiniteWarehouse extends RobotWorld {

    private WarehouseManager warehouseManager;

    private List<Detector> detectors;

    private ScenarioManager scenarioManager;

    private RobotRenderer robotRenderer;

    private Iterable<WarehouseRobot> robots = new Iterable<WarehouseRobot>(){
        private Iterator<Robot> robots = getWarehouseManager().getRobots().iterator();

        @Override
        public Iterator<WarehouseRobot> iterator() {
            return new Iterator<WarehouseRobot>() {
                @Override
                public boolean hasNext() {
                    return robots.hasNext();
                }

                @Override
                public WarehouseRobot next() {
                    return (WarehouseRobot) robots.next();
                }
            };
        }
    };
    
    @Override
    protected GridManager createGridManager() {
        int chargingStations = InfiniteWarehouseConfiguration.getChargingStationsInUse();
        StationManager stationManager = new StationManager(chargingStations);
        return new WarehouseManager(stationManager);
    }

    @Override
    public void initialize(SimulatorFrame frame, SimulationRunner simulationRunner) {
        super.initialize(frame, simulationRunner);
        scenarioManager = new ScenarioManager(this);
        robotRenderer = new RobotRenderer(getSimulationBlockView(), getWarehouseManager());
        initializeDetectors();
    }

    public Iterable<WarehouseRobot> getWarehouseRobots() {
        return robots;
    }

    protected void initializeDetectors() {
        detectors = new ArrayList<>();
        detectors.add(new DeadlockDetector(this));
        detectors.add(new CollisionDetector(this));
        detectors.add(new InvalidPositionDetector(this));
        detectors.add(new InvalidUnloadingDetector(this));
        detectors.add(new InvalidTurningDetector(this));

    }

    public WarehouseManager getWarehouseManager() {
        return (WarehouseManager) getGridManager();
    }

    @Override
    public List<Detector> getDetectors() {
        return detectors;
    }

    @Override
    public void updateEntities(float delta) {
        for (WarehouseRobot robot : getWarehouseRobots()) {
            if (robot.getRobotSpecificDelay() == 0 || !robot.isInTest()) {
                robot.getDriveManager().update(delta);
            } else {
                robot.getDriveManager().update(delta, robot.getRobotSpecificDelay());
            }

        }
    }

    @Override
    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    @Override
    public List<Scenario> getScenarios() {
        return new ScenarioGenerator(getWarehouseManager()).getScenarios();
    }

    @Override
    public Map<String, List<TestScenario>> getTestGroups() {
        return TestCaseGenerator.getAllTestCases(getWarehouseManager());
    }

    @Override
    public void renderEntities(Graphics graphics) {
        robotRenderer.render(graphics, getSimulationBlockView().getBlockSize());
    }

    @Override
    public void refreshSimulationProperties(int currentHeight, int currentWidth) {

        int chargingStations = warehouseManager.chargingStationsInUse(currentHeight, currentWidth);
            
        if (InfiniteWarehouseConfiguration.getChargingStationsInUse() != chargingStations) {                
            InfiniteWarehouseConfiguration.setChargingStationsInUse(chargingStations);
            getWarehouseManager().createNewStationManager(InfiniteWarehouseConfiguration.getChargingStationsInUse());
        }
    
        InfiniteWarehouseConfiguration.setUnloadingRange(warehouseManager.unloadingRange(currentHeight, currentWidth));
    }
    
    public void reportInvalidPosition(WarehouseRobot robot1, String reason) {
        getFrame().displayWarningMessage(reason);
        getSimulationRunner().setRunForbidden(true);
    }
    
    public void reportInvalidTurning(WarehouseRobot robot, String reason) {
        getFrame().displayWarningMessage(reason);
        getSimulationRunner().setRunForbidden(true);
    }

    public void reportInvalidUnloading(WarehouseRobot robot, String reason) {
        getFrame().displayWarningMessage(reason);
    }

    public WarehouseRobot getRobotFromSpecification(RobotSpecification specification) {
        return specification.getRobot(getWarehouseManager());
    }
    
    @Override
    public IHighlightable getHighlightAtPosition(int x, int y) {
        Position pos = getSimulationBlockView().toGridPosition(x, y);
        for (WarehouseRobot robot : getWarehouseRobots()) {
            if (robot.getDriveManager().currentPosition().equals(pos)
                    || robot.getDriveManager().getOldPosition().equals(pos))
                return robot;
        }
        return null;
    }
    
    @Override
    public void refreshEntities() { 
        getWarehouseManager().refresh();
    }

    @Override
    public void resetScenario() {
        getWarehouseManager().releaseAllLocks();
    }

    @Override
    public void close() {
        getWarehouseManager().close();
    }
}