package de.hpi.mod.sim.worlds.simpletraffic;

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
    
    public  final Type type;

    private CellType(Type type) {
        this.type = type;
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
    }
    
    @Override
    public Color borderColor(Orientation orientation) {
    	return Color.RED;
    }
    
    @Override
    public float borderWidth(Orientation orientation) {
        return 2;
    }
}
