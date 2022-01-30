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
	            return new Color(180, 222, 255);
            case ARRIVAL_POINT:
                return new Color(180, 222, 255);
            case WALL:
                return new Color(63, 63, 63);
            case EMPTY:
                return Color.WHITE;
            case STREET:
                return new Color(216, 216, 216);
            case CROSSROAD:
                return new Color(255, 242, 204);
            case CROSSROAD_WAITING_POINT:
            	return new Color(251, 215, 187);
            default:
                return new Color(63, 63, 63);
        }
    }

    @Override
    public boolean hasBorder(Orientation orientation) {
    	return false;
    }
    
    @Override
    public Color borderColor(Orientation orientation) {
    	return Color.BLACK;
    }
    
    @Override
    public float borderWidth(Orientation orientation) {
        return 2;
    }
}
