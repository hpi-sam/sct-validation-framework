package de.hpi.mod.sim.setting.infinitewarehouses;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.Detector;
import de.hpi.mod.sim.core.scenario.EntityDescription;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Simulation;
import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.IHighlightable;
import de.hpi.mod.sim.setting.infinitewarehouses.detectors.*;
import de.hpi.mod.sim.setting.robot.DriveManager;
import de.hpi.mod.sim.setting.robot.IRobotController;
import de.hpi.mod.sim.setting.robot.Robot;
import de.hpi.mod.sim.core.view.SimulatorFrame;
import de.hpi.mod.sim.setting.GridSetting;
import de.hpi.mod.sim.setting.IGrid;
import de.hpi.mod.sim.setting.Position;
import java.awt.geom.Point2D;

public class InfiniteWarehousesSetting extends GridSetting implements IRobotController {

    private GridManagement grid;

    private List<Detector> detectors;

    private RobotDispatcher robotDispatcher;

    private ScenarioManager scenarioManager;

    private RobotRenderer robotRenderer;
    private ExplosionRenderer explosionRenderer;

    public InfiniteWarehousesSetting() {
        super();
        grid = new GridManagement(this);
    }

    @Override
    public void initialize(SimulatorFrame frame, Simulation simulation) {
        super.initialize(frame, simulation);
        robotDispatcher = new RobotDispatcher(grid,
                new StationManager(InfiniteWarehouseSimConfig.getChargingStationsInUse()));
        scenarioManager = new ScenarioManager(this);
        robotRenderer = new RobotRenderer(robotDispatcher, getSimulationBlockView());
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

    public GridManagement getGridManagement() {
        return grid;
    }

    @Override
    public IGrid getGrid() {
        return getGridManagement();
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
        return robotDispatcher.isBlockedByRobot(pos);
    }

    @Override
    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    @Override
    public List<? extends Entity> getEntities() {
        return getRobots();
    }

    public RobotDispatcher getRoboterDispatch() {
        return robotDispatcher;
    }

    @Override
    public void onSimulationPropertyRefresh() {
        getRoboterDispatch().createNewStationManager(InfiniteWarehouseSimConfig.getChargingStationsInUse());
    }

    public List<Robot> getRobots() {
        return getRoboterDispatch().getRobots();
    }

    @Override
    public List<Scenario> getScenarios() {
        return ScenarioGenerator.getScenarios();
    }

    @Override
    public Map<String, List<TestScenario>> getTestGroups() {
        return TestCaseGenerator.getAllTestCases();
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        robotRenderer.render(graphics, getSimulationBlockView().getBlockSize());
        explosionRenderer.render(graphics);
    }

    @Override
    public void refreshSimulationProperties(int currentHeight, int currentWidth) { // TODO this is not nice
        float blockSize = InfiniteWarehouseSimConfig.getDefaultBlockSize();
        int heightBlocks = (int) (currentHeight / blockSize);
        int widthBlocks = (int) (currentWidth / blockSize);

        int chargingStations = widthBlocks / InfiniteWarehouseSimConfig.getSpaceBetweenChargingStations();
        if (chargingStations % 2 != 0)
            chargingStations--;
        InfiniteWarehouseSimConfig.setChargingStationsInUse(chargingStations);
        int unloadingRange = (widthBlocks / 3) * ((heightBlocks - InfiniteWarehouseSimConfig.getQueueSize()) / 3);
        InfiniteWarehouseSimConfig.setUnloadingRange(unloadingRange);
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        explosionRenderer.mousePressed(e);
    }

    public void reportCollision(Robot r1, Robot r2, String reason) {
        getFrame().displayWarningMessage(reason);
        getSimulation().setRunForbidden(true);
        renderExplosion(r1);
    }

    public void reportInvalidPosition(Robot robot1, String reason) {
        getFrame().displayWarningMessage(reason);
        getSimulation().setRunForbidden(true);
    }

    public void reportInvalidTurning(Robot robot, String reason) {
        getFrame().displayWarningMessage(reason);
        getSimulation().setRunForbidden(true);
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
    public void resetView() {
        explosionRenderer.reset();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Entity> E fromDescription(EntityDescription<E> description) { //TODO very ugly, beautify
        if (description instanceof RobotDescription) {
            return (E) ((RobotDescription) description).getRobot(getRoboterDispatch());
        } else {
            return null;
        }
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
        getRoboterDispatch().refresh();
    }

    @Override
    public void resetScenario() {
        getRoboterDispatch().releaseAllLocks();
    }

    @Override
    public void close() {
        getRoboterDispatch().close();
    }
}