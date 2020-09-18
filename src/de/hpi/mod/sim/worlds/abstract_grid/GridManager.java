package de.hpi.mod.sim.worlds.abstract_grid;

import java.util.Arrays;

import de.hpi.mod.sim.core.simulation.IHighlightable;

public abstract class GridManager {

    public abstract ICellType cellType(Position pos);

    public abstract boolean affects(IHighlightable highlight, Position pos);
    
    public Direction orientationToDirection(Orientation facing, Orientation target) {
        if (facing == target)
            return Direction.AHEAD;
        if ((facing == Orientation.SOUTH && target == Orientation.NORTH)
                || (facing == Orientation.NORTH && target == Orientation.SOUTH)
                || (facing == Orientation.WEST && target == Orientation.EAST)
                || (facing == Orientation.SOUTH && target == Orientation.WEST))
            return Direction.BEHIND;
        if ((facing == Orientation.NORTH && target == Orientation.WEST)
                || (facing == Orientation.WEST && target == Orientation.SOUTH)
                || (facing == Orientation.SOUTH && target == Orientation.EAST)
                || (facing == Orientation.EAST && target == Orientation.NORTH))
            return Direction.LEFT;
        return Direction.RIGHT;
    }

    public Direction targetDirection(Orientation facing, Position current, Position target) {
        Orientation targetOrientation;
        if (current.getX() > target.getX())
            targetOrientation = Orientation.WEST;
        else if (current.getX() < target.getX())
            targetOrientation = Orientation.EAST;
        else if (current.getY() > target.getY())
            targetOrientation = Orientation.SOUTH;
        else if (current.getY() < target.getY())
            targetOrientation = Orientation.NORTH;
        else // current equals target
            targetOrientation = Orientation.random();

        return orientationToDirection(facing, targetOrientation);
    }

    public Position[] getNeighbors(Orientation facing, Position position) {
        Direction[] directions = Direction.values();
        // All four orientations rotated by the facing of the Robot
        // Sides: Left, Ahead, Right, Bottom (Same order like in the enum Direction)
        Orientation[] sides = Arrays.stream(directions).map(dir -> Orientation.rotate(facing, dir))
                .toArray(Orientation[]::new);

        // All four neighbor positions of the robot in the same order as sides
        Position[] neighbors = Arrays.stream(sides).map(side -> Position.nextPositionInOrientation(side, position))
                .toArray(Position[]::new);
        return neighbors;
    }
}