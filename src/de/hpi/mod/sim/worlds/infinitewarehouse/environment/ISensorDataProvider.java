package de.hpi.mod.sim.worlds.infinitewarehouse.environment;

import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.IGrid;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;

public interface ISensorDataProvider extends IGrid {

    boolean[] blocked(Orientation facing, Position pos);
    boolean[] blockedWaypoint(Orientation facing, Position pos);
    boolean blockedCrossroadAhead(Orientation facing, Position pos);
    boolean blockedCrossroadRight(Orientation facing, Position pos);
    Orientation targetOrientation(Position current, Position target);

    Direction targetDirection(Orientation facing, Position current, Position target);
}
