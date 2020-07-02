package de.hpi.mod.sim.setting.infinitewarehouses.model;

import de.hpi.mod.sim.setting.grid.Direction;
import de.hpi.mod.sim.setting.grid.IGrid;
import de.hpi.mod.sim.setting.grid.Orientation;
import de.hpi.mod.sim.setting.grid.Position;

public interface ISensorDataProvider extends IGrid {

    boolean[] blocked(Orientation facing, Position pos);
    boolean[] blockedWaypoint(Orientation facing, Position pos);
    boolean blockedCrossroadAhead(Orientation facing, Position pos);
    boolean blockedCrossroadRight(Orientation facing, Position pos);
    Orientation targetOrientation(Position current, Position target);

    Direction targetDirection(Orientation facing, Position current, Position target);
}
