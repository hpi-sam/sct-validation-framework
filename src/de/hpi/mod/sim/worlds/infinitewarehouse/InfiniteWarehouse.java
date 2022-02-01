package de.hpi.mod.sim.worlds.infinitewarehouse;

import java.util.List;
import java.util.Map;

import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.worlds.abstract_grid.GridConfiguration;
import de.hpi.mod.sim.worlds.abstract_grid.GridManager;
import de.hpi.mod.sim.worlds.abstract_robots.RobotWorld;
import de.hpi.mod.sim.worlds.infinitewarehouse.detectors.*;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.StationManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.WarehouseManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.scenario.ScenarioGenerator;
import de.hpi.mod.sim.worlds.infinitewarehouse.scenario.TestCaseGenerator;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;

public class InfiniteWarehouse extends RobotWorld {
    
    public InfiniteWarehouse() {
		super();
		publicName = "Infinite Warehouse World";
	}

    @Override
    public void initialize() {
    	super.initialize();
    	// Moved here from static initialization of configuration class, until a better solution from the configuration if found.
    	GridConfiguration.setOriginOffsetY(InfiniteWarehouseConfiguration.getQueueSize());
    }

	@Override
    protected GridManager createGridManager() {
        int chargingStations = InfiniteWarehouseConfiguration.getChargingStationsInUse();
        StationManager stationManager = new StationManager(chargingStations);
        return new WarehouseManager(stationManager);
    }
    
    @Override
    protected List<Detector> createDetectors() {
        List<Detector> detectors = super.createDetectors();
        detectors.add(new InvalidPositionDetector(this));
        detectors.add(new InvalidUnloadingDetector(this));
        detectors.add(new InvalidTurningDetector(this));
        return detectors;
    }

    public WarehouseManager getWarehouseManager() {
        return (WarehouseManager) getGridManager();
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
    public void refreshSimulationSize(int currentHeight, int currentWidth) {

        int chargingStations = getWarehouseManager().chargingStationsInUse(currentHeight, currentWidth);
            
        if (InfiniteWarehouseConfiguration.getChargingStationsInUse() != chargingStations) {                
            InfiniteWarehouseConfiguration.setChargingStationsInUse(chargingStations);
            getWarehouseManager().createNewStationManager(InfiniteWarehouseConfiguration.getChargingStationsInUse());
        }
    
        InfiniteWarehouseConfiguration.setUnloadingRange(
                getWarehouseManager().unloadingRange(currentHeight, currentWidth));
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
    @Override
    public void resetScenario() {
        getWarehouseManager().releaseAllLocks();
    }
}