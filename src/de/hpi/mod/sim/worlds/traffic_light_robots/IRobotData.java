package de.hpi.mod.sim.worlds.traffic_light_robots;

import de.hpi.mod.sim.worlds.abstract_grid.Direction;

public interface IRobotData {

    public CellType.Type cellType();

    public Direction targetDirection();

    public boolean isOnTarget();

    public boolean blockedWaypointAhead();

    public boolean blockedWaypointLeft();

    public boolean blockedWaypointRight();

}
