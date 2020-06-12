package de.hpi.mod.sim.env.model;

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
}