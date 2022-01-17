package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotGridManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.CellType;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;

public class SimpleRobot extends Robot implements IRobotData, IProcessor, StateChartEntity {

    private RobotStatechartWrapper control;

    public SimpleRobot(int robotID, RobotGridManager grid, Position startPosition, Orientation startFacing,
            Position destination) {
        super(robotID, grid, startPosition, startFacing);
        control = new RobotStatechartWrapper(getDriveManager(), this, this);
        setTarget(destination);
        control.newTarget();
    }

    @Override
    public void arrived() {
        super.arrived();
        Position pos = getCrossRoadsManager().getRandomStart();
        setRobotTo(pos);
        setFacingTo(StreetNetworkManager.getSuitableRobotOrientationForPosition(pos));
        setTarget(StreetNetworkManager.getRandomDestination());
        control.newTarget();
    }

    /**
     * Handles state changes and refreshes the State-Machine
     */
    @Override
    public void onRefresh() {
        control.onRefresh();
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

    private StreetNetworkManager getCrossRoadsManager() {
        return (StreetNetworkManager) grid;
    }

    @Override
    public String getMachineState() {
        return control.getChartState();
    }

    @Override
    public String getTopStateName() {
        return "tlRobot";
    }

    @Override
    public void actionCompleted() {
        super.actionCompleted();
        control.actionCompleted();
    }
    
}
