package de.hpi.mod.sim.worlds.abstract_grid;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The orientation an entity can face
 */
public enum Orientation {
    NORTH, EAST, SOUTH, WEST;

    private float angle;
    private Orientation left, right, inverse;

    static {
        NORTH.angle = 0;
        EAST.angle = 90;
        SOUTH.angle = 180;
        WEST.angle = 270;

        NORTH.left = WEST;
        EAST.left = NORTH;
        SOUTH.left = EAST;
        WEST.left = SOUTH;

        NORTH.right = EAST;
        EAST.right = SOUTH;
        SOUTH.right = WEST;
        WEST.right = NORTH;
        
        NORTH.inverse = SOUTH;
        EAST.inverse = WEST;
        SOUTH.inverse = NORTH;
        WEST.inverse = EAST;
    }

    public float getAngle() {
        return angle;
    }

    public Orientation getTurnedLeft() {
        return left;
    }

    public Orientation getTurnedRight() {
        return right;
    }

    public Orientation getInverse() {
        return inverse;
    }
    
    public static Orientation rotate(Orientation facing, Direction rotation) {
        switch (rotation) {
            case AHEAD:
                return facing;
            case LEFT:
                return facing.getTurnedLeft();
            case RIGHT:
                return facing.getTurnedRight();
            case BEHIND:
                return facing.getTurnedRight().getTurnedRight();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Direction difference(Orientation o1, Orientation o2) {
        int difference = Math.floorMod(Math.round(o1.getAngle() - o2.getAngle()), 360);
        switch (difference) {
            case 0: return Direction.AHEAD;
            case 90: return Direction.LEFT;
            case 270: return Direction.RIGHT;
            default: return Direction.BEHIND;
        }
    }
    
    public static Orientation random() {
    	return values()[ThreadLocalRandom.current().nextInt(values().length)];
    }
    
}
