package de.hpi.mod.sim.env.model;

public interface ISensorDataProvider {

    PositionType posType(Position pos);
    CellType cellType(Position pos);
    boolean[] blocked(Orientation facing, Position pos);
    boolean[] blockedWaypoint(Orientation facing, Position pos);
    boolean blockedCrossroadAhead(Orientation facing, Position pos);
    boolean blockedCrossroadRight(Orientation facing, Position pos);
    Orientation targetOrientation(Position current, Position target);
    Direction targetDirection(Orientation facing, Position current, Position target);
}
