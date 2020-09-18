package de.hpi.mod.sim.worlds.infinitewarehouse.environment;

import java.awt.Color;

import de.hpi.mod.sim.worlds.abstract_grid.ICellType;

public enum CellType implements ICellType {
    WAYPOINT, CROSSROAD, BLOCK, CHARGER, LOADING, STATION, QUEUE, CHARGER_UNUSED, LOADING_UNUSED, QUEUE_UNUSED, STATION_UNUSED;

    boolean hasBorderLeft = false;
    boolean hasBorderRight = false;
    boolean hasBorderTop = false;
    boolean hasBorderBottom = false;

    Color color = Color.DARK_GRAY;

    static {
        CHARGER.hasBorderLeft = true;
        LOADING.hasBorderLeft = true;
        QUEUE.hasBorderLeft = true;
        
        WAYPOINT.color = Color.WHITE;
        CROSSROAD.color = Color.LIGHT_GRAY;
        CHARGER.color = new Color(0xe0d9f9);
        LOADING.color = new Color(0xc0e8ed);
        STATION.color = new Color(0xfff3e2);
        QUEUE.color = new Color(0xfff3e2);
    }

    @Override
    public Color getColor() {
        return color;
    }
    

    @Override
    public boolean borderLeft() {
        return hasBorderLeft;
    }

    @Override
    public boolean borderTop() {
        return hasBorderTop;
    }

    @Override
    public boolean borderRight() {
        return hasBorderRight;
    }

    @Override
    public boolean borderBottom() {
        return hasBorderBottom;
    }
}
