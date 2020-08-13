package de.hpi.mod.sim.worlds.abstract_grid;

import de.hpi.mod.sim.core.simulation.IHighlightable;

public interface IGrid {

    public ICellType cellType(Position pos);

    public boolean affects(IHighlightable highlight, Position pos);
}