package de.hpi.mod.sim.worlds.simpletrafficlights;

import java.awt.Color;

import de.hpi.mod.sim.worlds.abstract_grid.ICellType;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;

public class CellType implements ICellType {

    public static enum Type {
        WALL, EMPTY,
    	ARRIVAL_POINT, DEPARTURE_POINT,
    	STREET, 
        CROSSROAD, CROSSROAD_WAITING_POINT
    }

    public static final CellType DEPARTURE_POINT = new CellType(Type.DEPARTURE_POINT);
    public static final CellType ARRIVAL_POINT = new CellType(Type.ARRIVAL_POINT);
    public static final CellType STREET = new CellType(Type.STREET);
    public static final CellType WALL = new CellType(Type.WALL);
    public static final CellType EMPTY = new CellType(Type.EMPTY);
    public static final CellType CROSSROAD = new CellType(Type.CROSSROAD);
    public static final CellType CROSSROAD_WAITING_POINT = new CellType(Type.CROSSROAD_WAITING_POINT);
    
//    public static final CellType TRAFFIC_LIGHT_RED_NORTH = createTrafficLight(false, Orientation.NORTH);
//    public static final CellType TRAFFIC_LIGHT_RED_SOUTH = createTrafficLight(false, Orientation.SOUTH);
//    public static final CellType TRAFFIC_LIGHT_RED_EAST = createTrafficLight(false, Orientation.EAST);
//    public static final CellType TRAFFIC_LIGHT_RED_WEST = createTrafficLight(false, Orientation.WEST);
//    public static final CellType TRAFFIC_LIGHT_GREEN_NORTH = createTrafficLight(true, Orientation.NORTH);
//    public static final CellType TRAFFIC_LIGHT_GREEN_SOUTH = createTrafficLight(true, Orientation.SOUTH);
//    public static final CellType TRAFFIC_LIGHT_GREEN_EAST = createTrafficLight(true, Orientation.EAST);
//    public static final CellType TRAFFIC_LIGHT_GREEN_WEST = createTrafficLight(true, Orientation.WEST);

    public  final Type type;

    private Orientation borderOrientation; // For traffic lights only

    private CellType(Type type) {
        this.type = type;
    }

//    private static final CellType createTrafficLight(boolean green, Orientation orientation) {
//        CellType cell = new CellType(green ? Type.TRAFFIC_LIGHT_GREEN : Type.TRAFFIC_LIGHT_RED);
//        cell.borderOrientation = inverseOrientation(orientation);
//        return cell;
//    }

    private static Orientation inverseOrientation(Orientation orientation) {
        switch (orientation) {
            case EAST:
                return Orientation.WEST;
            case NORTH:
                return Orientation.SOUTH;
            case SOUTH:
                return Orientation.NORTH;
            default: //WEST
                return Orientation.EAST;
        }
    }
    
    @Override
    public Color getColor() {
        switch(type) {
	        case DEPARTURE_POINT:
	            return Color.GREEN; // new Color(235, 235, 235);
            case ARRIVAL_POINT:
                return Color.RED; //new Color(235, 235, 235);
            case WALL:
                return Color.DARK_GRAY;
            case EMPTY:
                return Color.WHITE;
            case STREET:
                return Color.LIGHT_GRAY;
            case CROSSROAD:
                return Color.YELLOW;
            case CROSSROAD_WAITING_POINT:
            	return Color.ORANGE;
            default:
                return Color.DARK_GRAY;
        }
    }

    @Override
    public boolean hasBorder(Orientation orientation) {
    	return false;
//        return (type.equals(Type.TRAFFIC_LIGHT_GREEN) || type.equals(Type.TRAFFIC_LIGHT_RED))
//         && borderOrientation.equals(orientation);
    }
    
    @Override
    public Color borderColor(Orientation orientation) {
    	return Color.RED;
//    	return type.equals(Type.TRAFFIC_LIGHT_GREEN) ? Color.GREEN : Color.RED;
    }

    public static boolean isWaypoint(CellType cell) {
    	return false;
//        return cell.type.equals(Type.ARRIVAL_POINT) || cell.type.equals(Type.TRAFFIC_LIGHT_GREEN)
//                || cell.type.equals(Type.TRAFFIC_LIGHT_RED) || cell.type.equals(Type.PURE_WAYPOINT);
    }
    
    @Override
    public float borderWidth(Orientation orientation) {
        return 2;
    }
}
