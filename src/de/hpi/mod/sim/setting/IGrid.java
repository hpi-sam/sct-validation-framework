package de.hpi.mod.sim.setting;

import de.hpi.mod.sim.core.model.IHighlightable;

public interface IGrid {

    public ICellType cellType(Position pos);

    public boolean affects(IHighlightable highlight, Position pos);
}