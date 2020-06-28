package de.hpi.mod.sim.setting.infinitewarehouses;

import de.hpi.mod.sim.setting.ICellType;
import java.awt.Color;

public enum CellType implements ICellType {
    WAYPOINT, CROSSROAD, BLOCK, BATTERY, LOADING, STATION, QUEUE, BATTERY_UNUSED, LOADING_UNUSED, QUEUE_UNUSED, STATION_UNUSED;

    boolean hasBorderLeft = false;
    Color color = Color.DARK_GRAY;

    static {
        BATTERY.hasBorderLeft = true;
        LOADING.hasBorderLeft = true;
        QUEUE.hasBorderLeft = true;

        WAYPOINT.color = Color.WHITE;
        CROSSROAD.color = Color.LIGHT_GRAY;
        BATTERY.color = new Color(0xe0d9f9);
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
}
