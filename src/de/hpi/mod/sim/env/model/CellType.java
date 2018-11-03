package de.hpi.mod.sim.env.model;

public enum CellType {
    WAYPOINT, CROSSROAD, BLOCK, BATTERY, LOADING, STATION;

    private PositionType posType;

    static {
        WAYPOINT.posType = PositionType.WAYPOINT;
        CROSSROAD.posType = PositionType.CROSSROAD;
        BLOCK.posType = null;
        BATTERY.posType = PositionType.STATION;
        LOADING.posType = PositionType.WAYPOINT;
        STATION.posType = PositionType.STATION;
    }

    public PositionType toPositionType() {
        return posType;
    }
}
