package de.hpi.mod.sim.env.model;

public class Position {

    private int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        return x == position.x && y == position.y;
    }
    
    public boolean fuzzyEquals(Position p) {
    	if(((Math.abs(this.getX() - p.getX()) <= 1 && Math.abs(this.getY() - p.getY()) == 0) ||
    			(Math.abs(this.getX() - p.getX()) == 0 && Math.abs(this.getY() - p.getY()) <= 1)) &&
    			this.getY()>0) {
    		return true;
    	}
    	if(this.equals(p)) {
    		return true;
    	}
    	return false;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
    
    public Position getModified(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    public static Position nextPositionInOrientation(Orientation facing, Position current) {
        switch (facing) {
            case NORTH:
                return new Position(current.getX(), current.getY() + 1);
            case EAST:
                return new Position(current.getX() + 1, current.getY());
            case SOUTH:
                return new Position(current.getX(), current.getY() - 1);
            case WEST:
                return new Position(current.getX() - 1, current.getY());
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public static Position nextPositionInOppositeOrientation(Orientation facing, Position current) {
    	switch (facing) {
        case NORTH:
            return new Position(current.getX(), current.getY() - 1);
        case EAST:
            return new Position(current.getX() - 1, current.getY());
        case SOUTH:
            return new Position(current.getX(), current.getY() + 1);
        case WEST:
            return new Position(current.getX() + 1, current.getY());
        default:
            throw new IllegalArgumentException();
    }
	}

	public boolean is(Position position) {
		if(position.getX() == x && position.getY() == y) {
			return true;
		} else {
			return false;
		}
	}
}
