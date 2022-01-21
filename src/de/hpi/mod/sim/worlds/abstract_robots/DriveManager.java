package de.hpi.mod.sim.worlds.abstract_robots;

import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouseConfiguration;

/**
 * Calculates the real x and y position, angle and battery
 */
public class DriveManager implements IRobotActors {

    private long delay = Integer.MAX_VALUE;
    private long now = System.currentTimeMillis();

    private boolean loading = false;
    private boolean isUnloading = false;
    private boolean isTurningLeft = false;
    private boolean isTurningRight = false;
    private boolean isMoving = false;

    private long unloadingStartTime;

    private long unloadingTime = InfiniteWarehouseConfiguration.getDefaultUnloadingTime();

    private float battery = ThreadLocalRandom.current().nextInt(
            (int) (RobotConfiguration.getMinBatteryRatio()
                    * RobotConfiguration.getBatteryFull()),
            (int) RobotConfiguration.getBatteryFull() + 1);

    private int maxDelay = 0;
    private boolean isWaitingToMoveForward = false;
    private boolean isWaitingToTurningLeft = false;
    private boolean isWaitingToTurningRight = false;
    private boolean isWaitingToMoveBackward;
    private boolean isWaitingToUnloading = false;
   
    private boolean hasPackage = false;

    private boolean hasUnloadedSomething = false;

    private Robot robot;

    public DriveManager(Robot robot) {
        this.robot = robot;
    }

    public void update(float delta) {
        decreasePerTick();
        if (maxDelay > 0) {
            if (delay + now <= System.currentTimeMillis()) {
                if (isWaitingToMoveForward) {
                    performDriveForward();
                    isMoving = true;
                    isWaitingToMoveForward = false;
                } else if (isWaitingToMoveBackward) {
                    performDriveBackward();
                    isMoving = true;
                    isWaitingToMoveBackward = false;
                } else if (isWaitingToTurningLeft) {
                    performTurnLeft();
                    isTurningLeft = true;
                    isWaitingToTurningLeft = false;
                } else if (isWaitingToTurningRight) {
                    performTurnRight();
                    isTurningRight = true;
                    isWaitingToTurningRight = false;
                } else if (isWaitingToUnloading) {
                    performUnload();
                    isUnloading = true;
                    isWaitingToUnloading = false;
                }
            }
        }

        if (isMoving) {
            calculateMovement(delta);
        } else if (isTurningLeft) {
            calculateTurnLeft(delta);
        } else if (isTurningRight) {
            calculateTurnRight(delta);
        } else if (isUnloading) {
            calculateUnload();
        }

        if (loading) {
            loadBattery(delta);
        }
    }

    public void update(float delta, int robotSpecificDelay) {
        this.maxDelay = robotSpecificDelay;
        update(delta);
    }

    private boolean animationRunning() {
        return isMoving || isTurningLeft || isTurningRight || isUnloading;
    }

    private void loadBattery(float delta) {
        battery = Math.min(battery + delta * RobotConfiguration.getBatteryChargingSpeed(), 100);
    }

    private void calculateUnload() {
        if (System.currentTimeMillis()
                - unloadingStartTime > (unloadingTime / (InfiniteWarehouseConfiguration.getEntitySpeedLevel() + 1))) {
            isUnloading = false;
            hasPackage = false;
            robot.actionCompleted();
        }
    }
    
    private void calculateTurnRight(float delta) {
        float deltaAngle = robot.targetFacing().getAngle() - robot.facing().getAngle();
        while (deltaAngle < 0)
            deltaAngle += 360;

        robot.increaseAngle(Math.copySign(getRotationSpeed() * delta, deltaAngle));
        while (robot.getAngle() < 0)
            robot.increaseAngle(360);

        if (robot.getAngle() >= robot.targetFacing().getAngle()) {
            robot.setFacingTo(robot.targetFacing());
            isTurningRight = false;
            robot.actionCompleted();
        }
    }

    private void calculateTurnLeft(float delta) {
        float deltaAngle = robot.targetFacing().getAngle() - robot.facing().getAngle();
        while (deltaAngle > 0)
            deltaAngle -= 360;

        robot.increaseAngle(Math.copySign(getRotationSpeed() * delta, deltaAngle));

        if (robot.getAngle() <= robot.targetFacing().getAngle()) {
            robot.setFacingTo(robot.targetFacing());
            isTurningLeft = false;
            robot.actionCompleted();
        }
    }

    private void calculateMovement(float delta) {
        int deltaX = robot.posX() - robot.oldPos().getX();
        int deltaY = robot.posY() - robot.oldPos().getY();

        if (deltaY != 0) {
            robot.increaseY(Math.copySign(RobotConfiguration.getEntitySpeed() * delta, deltaY));

            // If y moved over target
            if (deltaY > 0 && robot.y() >= robot.posY() || deltaY < 0 && robot.y() <= robot.posY()) {
                robot.setY(robot.posY());
                finishMovement();
            }
        } else if (deltaX != 0) {
            robot.increaseX(Math.copySign(RobotConfiguration.getEntitySpeed() * delta, deltaX));

            // If x moved over target
            if (deltaX > 0 && robot.x() >= robot.posX() || deltaX < 0 && robot.x() <= robot.posX()) {
                robot.setX(robot.posX());
                finishMovement();
            }
        }
    }

    private void finishMovement() {
        robot.setOldPos(robot.pos());
        isMoving = false;
        robot.actionCompleted();
    }

    @Override
    public void driveForward() {
        if (!animationRunning()) {
            if (hasPower()) {
                if (maxDelay > 0) {
                    delay = getCustomRandomisedDelay(maxDelay);
                    now = System.currentTimeMillis();
                    isWaitingToMoveForward = true;
                } else {
                    performDriveForward();
                }
            }
        }
    }

    @Override
    public void driveBackward() {
        if (!animationRunning()) {
            if (hasPower()) {
                if (maxDelay > 0) {
                    delay = getCustomRandomisedDelay(maxDelay);
                    now = System.currentTimeMillis();
                    isWaitingToMoveBackward = true;
                } else {
                    performDriveBackward();
                }
            }
        }
    }

    private void performDriveBackward() {
        robot.setOldPos(robot.pos());
        robot.setPos(Position.nextPositionInOppositeOrientation(robot.targetFacing(), robot.oldPos()));
        decreaseBattery();
        isMoving = true;
    }

    private long getCustomRandomisedDelay(int upperBound) {
        // We add 100 in order to guarantee that in all random functions lowerBound <
        // upperBound
        upperBound = (upperBound / Math.max(1, RobotConfiguration.getEntitySpeedLevel())) + 100;
        int percentage = ThreadLocalRandom.current().nextInt(100);
        if (percentage < 50) {
            return ThreadLocalRandom.current().nextLong(upperBound / 10);
        } else if (percentage < 70) {
            return ThreadLocalRandom.current().nextLong(upperBound / 10, upperBound / 8);
        } else if (percentage < 85) {
            return ThreadLocalRandom.current().nextLong(upperBound / 8, upperBound / 5);
        } else if (percentage < 95) {
            return ThreadLocalRandom.current().nextLong(upperBound / 5, upperBound / 2);
        } else {
            return ThreadLocalRandom.current().nextLong(upperBound / 2, upperBound);
        }
    }

    private void performDriveForward() {
        robot.setOldPos(robot.pos());
        robot.setPos(Position.nextPositionInOrientation(robot.targetFacing(), robot.oldPos()));
        decreaseBattery();
        isMoving = true;
    }

    @Override
    public void turnLeft() {
        if (!animationRunning()) {
            if (hasPower()) {
                if (maxDelay > 0) {
                    delay = getCustomRandomisedDelay(maxDelay / 3);
                    now = System.currentTimeMillis();
                    isWaitingToTurningLeft = true;
                } else {
                    performTurnLeft();
                }
            }
        }
    }

    private void performTurnLeft() {
        robot.setFacing(robot.targetFacing());
        robot.setTargetFacing(robot.targetFacing().getTurnedLeft());
        decreaseBattery();
        isTurningLeft = true;
    }

    @Override
    public void turnRight() {
        if (!animationRunning()) {
            if (hasPower()) {
                if (maxDelay > 0) {
                    delay = getCustomRandomisedDelay(maxDelay / 3);
                    now = System.currentTimeMillis();
                    isWaitingToTurningRight = true;
                } else {
                    performTurnRight();
                }
            }
        }
    }

    private void performTurnRight() {
        robot.setFacing(robot.targetFacing());
        robot.setTargetFacing(robot.targetFacing().getTurnedRight());
        decreaseBattery();
        isTurningRight = true;
    }

    @Override
    public void startUnloading() {
        if (maxDelay > 0) {
            delay = getCustomRandomisedDelay(maxDelay / 3);
            now = System.currentTimeMillis();
            isWaitingToUnloading = true;
        } else {
            hasUnloadedSomething = true;
            performUnload();
        }
    }

    private void performUnload() {
        unloadingStartTime = System.currentTimeMillis();
        isUnloading = true;
    }

    private void decreaseBattery() {
        battery -= RobotConfiguration.getBatteryLoss();
        if (battery < 0)
            battery = 0;
    }

    private void decreasePerTick() {

    }

    private boolean hasPower() {
        return battery > 0;
    }

    public boolean isBatteryLow() {
        return battery < RobotConfiguration.getBatteryLow();
    }

    public boolean isBatteryFull() {
        return battery >= RobotConfiguration.getBatteryFull();
    }

    public float getBattery() {
        return battery;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
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
        return RobotConfiguration.getDefaultRotationSpeed()
                * RobotConfiguration.getEntitySpeedFactor();
    }

    public long getUnloadingTime() {
        return unloadingTime;
    }

    public void setUnloadingTime(long unloadingTime) {
        this.unloadingTime = unloadingTime;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }

    public boolean isInPositionToUnloadIntoShaft() {
        if (robot.posY() <= 1) {
            return false;
        } else if (robot.posY() % 3 != 0 && robot.posX() % 3 != 0) {
            return false;
        } else if ((robot.posX() % 3 == 2 || robot.posX() % 3 == -1) && robot.posY() % 3 == 0) {
            if (robot.targetFacing() == Orientation.NORTH) {
                return true;
            }
        } else if ((robot.posX() % 3 == 1 || robot.posX() % 3 == -2) && robot.posY() % 3 == 0) {
            if (robot.targetFacing() == Orientation.SOUTH) {
                return true;
            }
        } else if (robot.posX() % 3 == 0 && robot.posY() % 3 == 1) {
            if (robot.targetFacing() == Orientation.EAST) {
                return true;
            }
        } else if (robot.posX() % 3 == 0 && robot.posY() % 3 == 2) {
            if (robot.targetFacing() == Orientation.WEST) {
                return true;
            }
        }
        return false;
    }

    public void setPackage(boolean b) {
        hasPackage = b;
    }

    public boolean hasPackage() {
        return hasPackage;
    }

    public boolean hasUnloadedSomething() {
        return hasUnloadedSomething;
    }
}
