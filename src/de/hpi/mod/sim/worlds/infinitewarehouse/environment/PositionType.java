package de.hpi.mod.sim.worlds.infinitewarehouse.environment;

import de.hpi.mod.sim.worlds.abstract_grid.ICellType;

/**
 * The types of cells the robot can stand on
 */
public enum PositionType {
    WAYPOINT, STATION, CROSSROAD, BLOCKED;
    
    
    @Override
    public String toString() {
      switch (this) {
        case WAYPOINT:
          return "Waypoint";
        case CROSSROAD:
          return "Crossroad";
        case STATION:
          return "Station";
        default:
          return "Invalid Position";
      }
    }
    
    public static PositionType get(ICellType cell) {
      if (! (cell instanceof CellType) )
        return null;

      switch ((CellType) cell) {
        case CROSSROAD:
          return CROSSROAD;

        case LOADING:
        case LOADING_UNUSED:
        case WAYPOINT:
          return WAYPOINT;

        case CHARGER:
        case CHARGER_UNUSED:
        case QUEUE:
        case QUEUE_UNUSED:
        case STATION:
        case STATION_UNUSED:
          return STATION;

        default:
          return BLOCKED;
      }
    }
}