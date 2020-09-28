package de.hpi.mod.sim.worlds.traffic_light_robots;

import java.awt.Color;

import de.hpi.mod.sim.worlds.abstract_grid.ICellType;

public enum CellType implements ICellType {
    WAYPOINT, CROSSROAD, BLOCK, TARGETBOX;

    Color color = Color.DARK_GRAY;

    static {
        WAYPOINT.color = Color.WHITE;
        CROSSROAD.color = Color.LIGHT_GRAY;
    }

    @Override
    public Color getColor() {
        return color;
    }
    

    @Override
    public boolean borderLeft() {
        return false;
    }

    @Override
    public boolean borderTop() {
        return false;
    }

    @Override
    public boolean borderRight() {
        return false;
    }

    @Override
    public boolean borderBottom() {
        return false;
    }
}
