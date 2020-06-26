package de.hpi.mod.sim.core.model;

import java.awt.geom.Point2D;

import de.hpi.mod.sim.core.view.sim.SimulationWorld;

public interface IGrid {
    
    CellType cellType(Position pos);

    /**
     * The type of cell the Robot stands on
     * 
     * @param position The position of the Robot
     */
    
    public default PositionType posType(Position position) {
        return cellType(position).toPositionType();
    }
    
    public boolean isInvalid(Position pos);

    public boolean invalidManoeuvre(Position oldPos, Position newPos);

	public void clearInvalidPositions();

    public void makePositionInvalid(Position pos);

    /**
	 * Converts a draw-position to a grid-position
	 */
    public Position toGridPosition(int x, int y, SimulationWorld simWorld);

    /**
	 * Converts a grid-position to the draw-position
	 */
    public Point2D toDrawPosition(float x, float y, SimulationWorld simWorld);

    public boolean affects(IHighlightable highlight, Position pos);
}