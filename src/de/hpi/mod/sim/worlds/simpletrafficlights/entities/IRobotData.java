package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.simpletrafficlights.CellType;

public interface IRobotData {

    public CellType.Type cellType();

    public Direction targetDirection();

    public boolean isOnTarget();

    public boolean blockedWaypointAhead();

    public boolean blockedWaypointLeft();

    public boolean blockedWaypointRight();

}
