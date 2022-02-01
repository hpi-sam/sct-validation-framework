package de.hpi.mod.sim.worlds.abstract_grid;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The directions relative to an entity
 */
public enum Direction {
    LEFT, AHEAD, RIGHT, BEHIND;

    private float relativeAngle;
	private Direction left;
	private Direction right;
	private Direction inverse;

    static {
        LEFT.relativeAngle = 270;
        AHEAD.relativeAngle = 0;
        RIGHT.relativeAngle = 90;
        BEHIND.relativeAngle = 180;

        LEFT.left = AHEAD;
        AHEAD.left = RIGHT;
        RIGHT.left = BEHIND;
        BEHIND.left = LEFT;

        LEFT.right = BEHIND;
        AHEAD.right = LEFT;
        RIGHT.right = AHEAD;
        BEHIND.right = RIGHT;
        
        LEFT.inverse = RIGHT;
        AHEAD.inverse = BEHIND;
        RIGHT.inverse = LEFT;
        BEHIND.inverse = AHEAD;
    }

    public float rotateAngleToMin(float angle) {
        float rotated = angle + relativeAngle;
        if (rotated > 180)
            rotated -= 180;
        return rotated;
    }


    public Direction getTurnedLeft() {
        return left;
    }

    public Direction getTurnedRight() {
        return right;
    }

    public Direction getInverse() {
        return right;
    }
    
    public static Direction random() {
    	return values()[ThreadLocalRandom.current().nextInt(values().length)];
    }
}
