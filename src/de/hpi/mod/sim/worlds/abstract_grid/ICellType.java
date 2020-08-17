package de.hpi.mod.sim.worlds.abstract_grid;

import java.awt.Color;

public interface ICellType {
    
    public Color getColor();

    public boolean borderLeft();

    public boolean borderTop();

    public boolean borderRight();

    public boolean borderBottom();
}