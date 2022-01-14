package de.hpi.mod.sim.worlds.trafficlights.robot;

import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.trafficlights.CellType;

public interface IRobotData {

    public CellType.Type cellType();

    public Direction targetDirection();

    public boolean isOnTarget();

    public boolean blockedWaypointAhead();

    public boolean blockedWaypointLeft();

    public boolean blockedWaypointRight();

}
