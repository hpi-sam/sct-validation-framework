package de.hpi.mod.sim.env.robot;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.model.IRobotActors;
import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;

/**
 * Calculates the real x and y position, angle and battery
 */
public class DriveManager implements IRobotActors {

    public static final float
            DEFAULT_ROTATION_SPEED = .5f;
    public static final long DEFAULT_UNLOADING_TIME = 1000;

    public static final float BATTERY_FULL = 100;
    public static final float BATTERY_LOW = 20;
    public static final float BATTERY_LOSS = .1f;
    public static final float BATTERY_LOADING_SPEED = .02f;

    private DriveListener listener;

    private float x, y, angle;
    private float battery = BATTERY_FULL;

    private Position targetPosition;
    private Orientation targetFacing;

    private boolean loading = false;
    private boolean isUnloading = false;
    private boolean isTurningLeft = false;
    private boolean isTurningRight = false;
    private boolean isMoving = false;

    private long unloadingStartTime;
    private Position oldPosition;
    private Orientation oldFacing;

    private float rotationSpeed = DEFAULT_ROTATION_SPEED;
    private long unloadingTime = DEFAULT_UNLOADING_TIME;


    public DriveManager(DriveListener listener, Position pos, Orientation facing) {
        this.listener = listener;
        targetPosition = pos;
        oldPosition = pos;
        targetFacing = facing;
        oldFacing = facing;
        x = pos.getX();
        y = pos.getY();
        angle = facing.getAngle();
    }

    public void update(float delta) {
        if (isMoving) {  // Moving
            int deltaX = targetPosition.getX() - oldPosition.getX();
            int deltaY = targetPosition.getY() - oldPosition.getY();

            if (deltaY != 0) {
                y += Math.copySign(SimulatorConfig.getRobotMoveSpeed() * delta, deltaY);

                // If y moved over target
                // TODO Remove code repetition (Not DRY enough)
                if (deltaY > 0 && y >= targetPosition.getY() ||
                        deltaY < 0 && y <= targetPosition.getY()) {
                    y = targetPosition.getY();
                    oldPosition = targetPosition;
                    isMoving = false;
                    listener.actionCompleted();
                }
            } else if (deltaX != 0) {
                x += Math.copySign(SimulatorConfig.getRobotMoveSpeed() * delta, deltaX);

                // If x moved over target
                if (deltaX > 0 && x >= targetPosition.getX() ||
                        deltaX < 0 && x <= targetPosition.getX()) {
                    x = targetPosition.getX();
                    oldPosition = targetPosition;
                    isMoving = false;
                    listener.actionCompleted();
                }
            }
        } else if (isTurningLeft) {  // Turning Left
            float deltaAngle = targetFacing.getAngle() - oldFacing.getAngle();
            while (deltaAngle > 0) deltaAngle -= 360;

            angle += Math.copySign(rotationSpeed * delta, deltaAngle);

            if (angle <= targetFacing.getAngle()) {
                angle = targetFacing.getAngle();
                oldFacing = targetFacing;
                isTurningLeft = false;
                listener.actionCompleted();
            }
        } else if (isTurningRight) {  // Turning Right
            float deltaAngle = targetFacing.getAngle() - oldFacing.getAngle();
            while (deltaAngle < 0) deltaAngle += 360;

            angle += Math.copySign(rotationSpeed * delta, deltaAngle);
            while (angle < 0) angle += 360;

            if (angle >= targetFacing.getAngle()) {
                angle = targetFacing.getAngle();
                oldFacing = targetFacing;
                isTurningRight = false;
                listener.actionCompleted();
            }
        } else if (isUnloading) {  // Unloading
            if (System.currentTimeMillis() - unloadingStartTime > unloadingTime) {
                isUnloading = false;
                listener.unloadingCompleted();
            }
        }

        if (loading) {
            battery = Math.min(battery + delta * BATTERY_LOADING_SPEED, 100);
        }
    }

    @Override
    public void driveForward() {
        decreaseBattery();
        if (hasPower()) {
            oldPosition = targetPosition;
            targetPosition = Position.nextPositionInOrientation(targetFacing, oldPosition);
            battery -= BATTERY_LOSS;
            isMoving = true;
        }
    }

    @Override
    public void turnLeft() {
        decreaseBattery();
        if (hasPower()) {
            oldFacing = targetFacing;
            targetFacing = targetFacing.getTurnedLeft();
            isTurningLeft = true;
        }
    }

    @Override
    public void turnRight() {
        decreaseBattery();
        if (hasPower()) {
            oldFacing = targetFacing;
            targetFacing = targetFacing.getTurnedRight();
            isTurningRight = true;
        }
    }

    @Override
    public void startUnloading() {
        unloadingStartTime = System.currentTimeMillis();
        isUnloading = true;
    }

    private void decreaseBattery() {
        battery -= BATTERY_LOSS;
        if (battery < 0)
            battery = 0;
    }

    private boolean hasPower() {
        return battery > 0;
    }

    public boolean isBatteryLow() { return battery < BATTERY_LOW; }

    public boolean isBatteryFull() { return battery >= BATTERY_FULL; }

    public float getBattery() {
        return battery;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    /**
     * @return The ceiling of the Position of the robot
     */
    public Position currentPosition() {
        return targetPosition;
    }

    public Orientation currentFacing() {
        return oldFacing;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public boolean isUnloading() {
        return isUnloading;
    }

    public boolean isTurningLeft() {
        return isTurningLeft;
    }

    public boolean isTurningRight() {
        return isTurningRight;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public long getUnloadingStartTime() {
        return unloadingStartTime;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public long getUnloadingTime() {
        return unloadingTime;
    }

    public void setUnloadingTime(long unloadingTime) {
        this.unloadingTime = unloadingTime;
    }
}
