package de.hpi.mod.sim.env.setting.infinitestations;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.env.model.IGrid;
import de.hpi.mod.sim.env.model.IRobotController;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.setting.Setting;
import de.hpi.mod.sim.env.setting.infinitestations.detectors.CollisionDetector;
import de.hpi.mod.sim.env.setting.infinitestations.detectors.DeadlockDetector;
import de.hpi.mod.sim.env.setting.infinitestations.detectors.InvalidPositionDetector;
import de.hpi.mod.sim.env.setting.infinitestations.detectors.InvalidTurningDetector;
import de.hpi.mod.sim.env.setting.infinitestations.detectors.InvalidUnloadingDetector;
import de.hpi.mod.sim.env.simulation.SimulatorConfig;
import de.hpi.mod.sim.env.testing.Detector;
import de.hpi.mod.sim.env.testing.scenarios.ScenarioManager;
import de.hpi.mod.sim.env.view.DriveSimFrame;

public class InfiniteStationsSetting extends Setting implements IRobotController {

    private GridManagement grid;

    private List<Detector> detectors;

    private RobotDispatcher robotDispatcher;

    private ScenarioManager scenarioManager;

    public InfiniteStationsSetting(DriveSimFrame frame) {
        super(frame);
        grid = new GridManagement(this);
        robotDispatcher = new RobotDispatcher(grid, new StationManager(SimulatorConfig.getChargingStationsInUse()));
        scenarioManager = new ScenarioManager(this);
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
    public void updateRobots(float delta) {
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
    public List<Robot> getRobots() {
        return getRoboterDispatcher().getRobots();
    }
    
    @Override
    public RobotDispatcher getRoboterDispatcher() {
        return robotDispatcher;
    }
}