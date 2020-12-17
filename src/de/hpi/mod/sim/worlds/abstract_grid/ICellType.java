package de.hpi.mod.sim.worlds.abstract_grid;

import java.awt.Color;

public interface ICellType {
    
    public Color getColor();

    public default boolean hasBorder(Orientation orientation) {
        return false;
    }

    public default Color borderColor(Orientation orientation) {
        return Color.DARK_GRAY;
    }

    public default float borderWidth(Orientation orientation) {
        return 1;
    }
}