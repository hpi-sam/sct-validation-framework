package de.hpi.mod.sim.setting.infinitewarehouses;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.scenario.Detector;
import de.hpi.mod.sim.core.scenario.EntityDescription;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Simulation;
import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.IGrid;
import de.hpi.mod.sim.core.model.IHighlightable;
import de.hpi.mod.sim.core.model.IRobotController;
import de.hpi.mod.sim.core.model.Position;
import de.hpi.mod.sim.setting.detectors.*;
import de.hpi.mod.sim.setting.robot.Robot;
import de.hpi.mod.sim.core.view.DriveSimFrame;

public class InfiniteWarehousesSetting extends Setting implements IRobotController {

    private GridManagement grid;

    private List<Detector> detectors;

    private RobotDispatcher robotDispatcher;

    private ScenarioManager scenarioManager;

    private GridRenderer gridRenderer;
    private RobotRenderer robotRenderer;
    private ExplosionRenderer explosionRenderer;

    public InfiniteWarehousesSetting() {
        grid = new GridManagement(this);
    }

    @Override
    public void initialize(DriveSimFrame frame, Simulation simulation) {
        super.initialize(frame, simulation);
        robotDispatcher = new RobotDispatcher(grid,
                new StationManager(InfiniteWarehouseSimConfig.getChargingStationsInUse()));
        scenarioManager = new ScenarioManager(this);
        gridRenderer = new GridRenderer(getSimulation().getSimulationWorld(), getGrid());
        robotRenderer = new RobotRenderer(robotDispatcher, getSimulation().getSimulationWorld());
        explosionRenderer = new ExplosionRenderer(getSimulation().getSimulationWorld());
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

    @Override
    public IGrid getGrid() {
        return grid;
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

    @Override
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
        gridRenderer.render(graphics);
        robotRenderer.render(graphics);
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
        explosionRenderer.showExplosion(robot);
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
	public IHighlightable getHighlightAtPosition(Position pos) {
        for (Robot robot : getRobots()) {
            if (robot.getDriveManager().currentPosition().equals(pos) || robot.getDriveManager().getOldPosition().equals(pos))
                return robot;
        }
        return null;
	}
}