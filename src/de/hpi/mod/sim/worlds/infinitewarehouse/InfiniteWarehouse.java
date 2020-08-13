package de.hpi.mod.sim.worlds.infinitewarehouse;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.Detector;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.simulation.SimulationRunner;
import de.hpi.mod.sim.core.view.SimulatorFrame;
import de.hpi.mod.sim.worlds.abstract_grid.GridWorld;
import de.hpi.mod.sim.worlds.abstract_grid.IGrid;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.infinitewarehouse.detectors.*;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.GridManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.RobotManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.StationManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.scenario.RobotSpecification;
import de.hpi.mod.sim.worlds.infinitewarehouse.scenario.ScenarioGenerator;
import de.hpi.mod.sim.worlds.infinitewarehouse.scenario.TestCaseGenerator;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.DriveManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.Robot;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IRobotController;
import de.hpi.mod.sim.worlds.util.ExplosionRenderer;

import java.awt.geom.Point2D;

public class InfiniteWarehouse extends GridWorld implements IRobotController {

    private GridManager grid;

    private List<Detector> detectors;

    private RobotManager robots;

    private ScenarioManager scenarioManager;

    private RobotRenderer robotRenderer;
    private ExplosionRenderer explosionRenderer;

    public InfiniteWarehouse() {
        super();
        grid = new GridManager(this);
    }

    @Override
    public void initialize(SimulatorFrame frame, SimulationRunner simulationRunner) {
        super.initialize(frame, simulationRunner);
        robots = new RobotManager(grid,
                new StationManager(InfiniteWarehouseConfiguration.getChargingStationsInUse()));
        scenarioManager = new ScenarioManager(this);
        robotRenderer = new RobotRenderer(robots, getSimulationBlockView());
        explosionRenderer = new ExplosionRenderer();
        initializeDetectors();
    }

    protected void initializeDetectors() {
        detectors = new ArrayList<>();
        detectors.add(new DeadlockDetector(this));
        detectors.add(new CollisionDetector(this));
        detectors.add(new InvalidPositionDetector(this));
        detectors.add(new InvalidUnloadingDetector(this));
        detectors.add(new InvalidTurningDetector(this));

    }

    public GridManager getGridManager() {
        return grid;
    }

    @Override
    public IGrid getGrid() {
        return getGridManager();
    }

    @Override
    public List<Detector> getDetectors() {
        return detectors;
    }

    @Override
    public void updateEntities(float delta) {
        for (Robot robot : getRobots()) {
            if (robot.getRobotSpecificDelay() == 0 || !robot.isInTest()) {
                robot.getDriveManager().update(delta);
            } else {
                robot.getDriveManager().update(delta, robot.getRobotSpecificDelay());
            }

        }
    }

    @Override
    public boolean isBlockedByRobot(Position pos) {
        return robots.isBlockedByRobot(pos);
    }

    @Override
    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    @Override
    public List<? extends Entity> getEntities() {
        return getRobots();
    }

    public List<Robot> getRobots() {
        return robots.getRobots();
    }

    @Override
    public List<Scenario> getScenarios() {
        return new ScenarioGenerator(robots).getScenarios();
    }

    @Override
    public Map<String, List<TestScenario>> getTestGroups() {
        return TestCaseGenerator.getAllTestCases(robots);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        robotRenderer.render(graphics, getSimulationBlockView().getBlockSize());
        explosionRenderer.render(graphics);
    }

    @Override
    public void refreshSimulationProperties(int currentHeight, int currentWidth) {

        int chargingStations = grid.chargingStationsInUse(currentHeight, currentWidth);
            
        if (InfiniteWarehouseConfiguration.getChargingStationsInUse() != chargingStations) {                
            InfiniteWarehouseConfiguration.setChargingStationsInUse(chargingStations);
            robots.createNewStationManager(InfiniteWarehouseConfiguration.getChargingStationsInUse());
        }
    
        InfiniteWarehouseConfiguration.setUnloadingRange(grid.unloadingRange(currentHeight, currentWidth));
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        explosionRenderer.mousePressed(e);
    }

    public void reportCollision(Robot r1, Robot r2, String reason) {
        getFrame().displayWarningMessage(reason);
        getSimulationRunner().setRunForbidden(true);
        renderExplosion(r1);
    }

    public void reportInvalidPosition(Robot robot1, String reason) {
        getFrame().displayWarningMessage(reason);
        getSimulationRunner().setRunForbidden(true);
    }

    public void reportInvalidTurning(Robot robot, String reason) {
        getFrame().displayWarningMessage(reason);
        getSimulationRunner().setRunForbidden(true);
    }

    public void reportInvalidUnloading(Robot robot, String reason) {
        getFrame().displayWarningMessage(reason);
    }

    public void reportDeadlock(String reason) {
        getFrame().displayWarningMessage(reason);
    }

    public void renderExplosion(Robot robot) {
        DriveManager drive = robot.getDriveManager();
        Point2D drawPos = getSimulationBlockView().toDrawPosition(drive.getX(), drive.getY());
        explosionRenderer.showExplosion(drawPos);
    }

    @Override
    public void resetAnimationPanel() {
        explosionRenderer.reset();
    }

    public Robot getRobotFromSpecification(RobotSpecification specification) {
        return specification.getRobot(robots);
    }
    
    @Override
    public IHighlightable getHighlightAtPosition(int x, int y) {
        Position pos = getSimulationBlockView().toGridPosition(x, y);
        for (Robot robot : getRobots()) {
            if (robot.getDriveManager().currentPosition().equals(pos)
                    || robot.getDriveManager().getOldPosition().equals(pos))
                return robot;
        }
        return null;
    }
    
    @Override
    public void refreshEntities() { 
        robots.refresh();
    }

    @Override
    public void resetScenario() {
        robots.releaseAllLocks();
    }

    @Override
    public void close() {
        robots.close();
    }
}