package de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces;

import java.util.List;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;


public interface IRobotDispatch {

    public WarehouseRobot addRobot();

    public WarehouseRobot createScenarioRobot(Position position, Orientation facing, int delay);

    public WarehouseRobot addRobotAtPosition(Position position, WarehouseRobot.RobotState state, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzyEnd, boolean unloadingTest, boolean hasReservedCharger,
            boolean hardArrivedConstraint);

    public void refreshRobots();

    public void closeRobots();

	public void releaseAllLocks();

	public void createNewStationManager(int chargingStationsInUse);
}