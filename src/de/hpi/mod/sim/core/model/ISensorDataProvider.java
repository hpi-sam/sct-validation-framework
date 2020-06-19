package de.hpi.mod.sim.core.model;

public interface ISensorDataProvider extends IGrid {

    boolean[] blocked(Orientation facing, Position pos);
    boolean[] blockedWaypoint(Orientation facing, Position pos);
    boolean blockedCrossroadAhead(Orientation facing, Position pos);
    boolean blockedCrossroadRight(Orientation facing, Position pos);
    Orientation targetOrientation(Position current, Position target);

    Direction targetDirection(Orientation facing, Position current, Position target);
}
