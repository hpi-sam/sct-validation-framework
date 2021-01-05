package de.hpi.mod.sim.worlds.traffic_light_robots.robot;

import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotGridManager;
import de.hpi.mod.sim.worlds.traffic_light_robots.CellType;
import de.hpi.mod.sim.worlds.traffic_light_robots.CrossRoadsManager;

public class TrafficLightRobot extends Robot implements IRobotData, IProcessor, StateChartEntity {

    private DriveSystemWrapper drive;

    public TrafficLightRobot(int robotID, RobotGridManager grid, Position startPosition, Orientation startFacing,
            Position destination) {
        super(robotID, grid, startPosition, startFacing);
        drive = new DriveSystemWrapper(getDriveManager(), this, this);
        setTarget(destination);
        drive.newTarget();
    }

    @Override
    public void arrived() {
        super.arrived();
        Position pos = getCrossRoadsManager().getRandomStart();
        setRobotTo(pos);
        setFacingTo(CrossRoadsManager.getSuitableRobotOrientationForPosition(pos));
        setTarget(CrossRoadsManager.getRandomDestination());
        drive.newTarget();
    }

    /**
     * Handles state changes and refreshes the State-Machine
     */
    @Override
    public void onRefresh() {
        drive.onRefresh();
    }

    @Override
    public boolean blockedWaypointAhead() {
        return blockedWaypoint()[1];
    }

    @Override
    public boolean blockedWaypointLeft() {
        return blockedWaypoint()[0];
    }

    @Override
    public boolean blockedWaypointRight() {
        return blockedWaypoint()[2];
    }

    private boolean[] blockedWaypoint() {
        return getCrossRoadsManager().blockedWaypoint(facing(), pos());
    }

    @Override
    public CellType.Type cellType() {
        return getCrossRoadsManager().cellType(pos()).type;
    }

    private CrossRoadsManager getCrossRoadsManager() {
        return (CrossRoadsManager) grid;
    }

    @Override
    public String getMachineState() {
        return drive.getChartState();
    }

    @Override
    public String getTopStateName() {
        return "tlRobot";
    }

    @Override
    public void actionCompleted() {
        super.actionCompleted();
        drive.actionCompleted();
    }
    
}
