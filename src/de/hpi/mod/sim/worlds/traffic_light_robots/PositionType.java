package de.hpi.mod.sim.worlds.traffic_light_robots;

import de.hpi.mod.sim.worlds.abstract_grid.ICellType;

/**
 * The types of cells the robot can stand on
 */
public enum PositionType {
    WAYPOINT, CROSSROAD, BLOCKED;
    
    
    @Override
    public String toString() {
      switch (this) {
        case WAYPOINT:
          return "Waypoint";
        case CROSSROAD:
          return "Crossroad";
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

        case WAYPOINT:
          return WAYPOINT;

        default:
          return BLOCKED;
      }
    }
}