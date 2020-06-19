package de.hpi.mod.sim.core.model;

/**
 * The types of cells the robot can stand on
 */
public enum PositionType {
    WAYPOINT, STATION, CROSSROAD, BLOCKED;
    
    
    @Override
    public String toString() {
      switch(this) {
        case WAYPOINT: return "Waypoint";
        case CROSSROAD: return "Crossroad";
        case STATION: return "Station";
        default: return "Invalid Position";
      }
    }
}