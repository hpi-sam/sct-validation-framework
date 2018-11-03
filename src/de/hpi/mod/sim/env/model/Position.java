package de.hpi.mod.sim.env.model;

import org.jetbrains.annotations.NotNull;

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

        if (x != position.x) return false;
        return y == position.y;
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

    public static Position nextPositionInOrientation(@NotNull Orientation facing, Position current) {
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
}
