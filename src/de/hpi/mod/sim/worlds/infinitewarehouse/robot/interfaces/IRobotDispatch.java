package de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces;

import java.util.List;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.Robot;


public interface IRobotDispatch {

    public Robot addRobot();

    public Robot addRobotInScenario(Position position, Orientation facing, int delay);

    public Robot addRobotAtPosition(Position position, Robot.RobotState state, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzyEnd, boolean unloadingTest, boolean hasReservedCharger,
            boolean hardArrivedConstraint);

    public void refresh();

    public void close();

	public void releaseAllLocks();

	public void createNewStationManager(int chargingStationsInUse);
}