package de.hpi.mod.sim.core.model;

/**
 * Similar to {@link PositionType}, but more specific.
 * Is used in the {@link de.hpi.mod.sim.core.model.IGrid} and when drawing in the view
 */
public enum CellType {
    WAYPOINT, CROSSROAD, BLOCK, BATTERY, LOADING, STATION, QUEUE;

    private PositionType posType;

    static {
        WAYPOINT.posType = PositionType.WAYPOINT;
        CROSSROAD.posType = PositionType.CROSSROAD;
        BLOCK.posType = PositionType.BLOCKED;
        BATTERY.posType = PositionType.STATION;
        LOADING.posType = PositionType.WAYPOINT;
        STATION.posType = PositionType.STATION;
        QUEUE.posType = PositionType.STATION;
    }

    public PositionType toPositionType() {
        return posType;
    }
}
