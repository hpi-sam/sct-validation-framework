package de.hpi.mod.sim.env.model;

/**
 * The directions relative to a robot
 */
public enum Direction {
    LEFT, AHEAD, RIGHT, BEHIND;

    private float relativeAngle;

    static {
        LEFT.relativeAngle = 270;
        AHEAD.relativeAngle = 0;
        RIGHT.relativeAngle = 90;
        BEHIND.relativeAngle = 180;
    }

    public float rotateAngleToMin(float angle) {
        float rotated = angle + relativeAngle;
        if (rotated > 180)
            rotated -= 180;
        return rotated;
    }
}
