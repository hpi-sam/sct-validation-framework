package de.hpi.mod.sim.env.robot;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.model.IRobotActors;
import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Calculates the real x and y position, angle and battery
 */
public class DriveManager implements IRobotActors {
	
	private DriveListener listener;

    private float x, y, angle;
    private long delay = Integer.MAX_VALUE;
    private long now = System.currentTimeMillis();

    private Position currentPosition;
    private Orientation targetFacing;

    private boolean loading = false;
    private boolean isUnloading = false;
    private boolean isTurningLeft = false;
    private boolean isTurningRight = false;
    private boolean isMoving = false;

    private long unloadingStartTime;
    private Position oldPosition;
    private Orientation oldFacing;

    private long unloadingTime = SimulatorConfig.getDefaultUnloadingTime();
    
    private float battery = ThreadLocalRandom.current().nextInt((int) (0.6*SimulatorConfig.getBatteryFull()), (int) SimulatorConfig.getBatteryFull()+1);
    
    private boolean inHardcoreMode = false;
	private boolean isWaitingToMove = false;
	private boolean isWaitingToTurningLeft = false;
	private boolean isWaitingToTurningRight = false;
	private boolean isWaitingToUnloading = false;


    public DriveManager(DriveListener listener, Position position, Orientation facing) {
        this.listener = listener;
        currentPosition = position;
        oldPosition = position;
        targetFacing = facing;
        oldFacing = facing;
        x = position.getX();
        y = position.getY();
        angle = facing.getAngle();
    }

    public void update(float delta) {
    	if(inHardcoreMode) {
    		if(delay + now <= System.currentTimeMillis()) {
    			if (isWaitingToMove) {
    				performDriveForward();
    	            isMoving = true;
    	            isWaitingToMove = false;
    	        } else if (isWaitingToTurningLeft ) { 
    	            performTurnLeft();
    	            isTurningLeft = true;
    	            isWaitingToTurningLeft = false;
    	        } else if (isWaitingToTurningRight) {
    	            performTurnRight();
    	            isTurningRight = true;
    	            isWaitingToTurningRight = false;
    	        } else if (isWaitingToUnloading ) {
    	            performUnload();
    	            isUnloading = true;
    	            isWaitingToUnloading = false;
    	        }
    		}
    	} 
    	
    	if (isMoving) {
            move(delta);
        } else if (isTurningLeft) { 
            turnLeft(delta);
        } else if (isTurningRight) {
            turnRight(delta);
        } else if (isUnloading) {
            unload();
        }

        if (loading) {
            loadBattery(delta);
        }
    }

	private void loadBattery(float delta) {
		battery = Math.min(battery + delta * SimulatorConfig.getBatteryChargingSpeed(), 100);
	}

	private void unload() {
		if (System.currentTimeMillis() - unloadingStartTime > unloadingTime) {
		    isUnloading = false;
		    listener.unloadingCompleted();
		}
	}

	private void turnRight(float delta) {
		float deltaAngle = targetFacing.getAngle() - oldFacing.getAngle();
		while (deltaAngle < 0) deltaAngle += 360;

		angle += Math.copySign(getRotationSpeed() * delta, deltaAngle);
		while (angle < 0) angle += 360;

		if (angle >= targetFacing.getAngle()) {
		    angle = targetFacing.getAngle();
		    oldFacing = targetFacing;
		    isTurningRight = false;
		    listener.actionCompleted();
		}
	}

	private void turnLeft(float delta) {
		float deltaAngle = targetFacing.getAngle() - oldFacing.getAngle();
		while (deltaAngle > 0) deltaAngle -= 360;

		angle += Math.copySign(getRotationSpeed() * delta, deltaAngle);

		if (angle <= targetFacing.getAngle()) {
		    angle = targetFacing.getAngle();
		    oldFacing = targetFacing;
		    isTurningLeft = false;
		    listener.actionCompleted();
		}
	}

	private void move(float delta) {
		int deltaX = currentPosition.getX() - oldPosition.getX();
		int deltaY = currentPosition.getY() - oldPosition.getY();

		if (deltaY != 0) {
		    y += Math.copySign(SimulatorConfig.getRobotSpeed() * delta, deltaY);

		    // If y moved over target
		    if (deltaY > 0 && y >= currentPosition.getY() ||
		            deltaY < 0 && y <= currentPosition.getY()) {
		        y = currentPosition.getY();
		        finishMovement();
		    }
		} else if (deltaX != 0) {
		    x += Math.copySign(SimulatorConfig.getRobotSpeed() * delta, deltaX);

		    // If x moved over target
		    if (deltaX > 0 && x >= currentPosition.getX() ||
		            deltaX < 0 && x <= currentPosition.getX()) {
		        x = currentPosition.getX();
		        finishMovement();
		    }
		}
	}
	
	private void finishMovement() {
		oldPosition = currentPosition;
        isMoving = false;
        listener.actionCompleted();
	}

    @Override
    public void driveForward() {
        decreaseBattery();
        if (hasPower()) {
        	if(inHardcoreMode) {
        		delay = getCustomRandomisedDelay(1000);
        		now = System.currentTimeMillis();
        		isWaitingToMove = true;
        	} else {
        		performDriveForward();
        	}
        }
    }

	private long getCustomRandomisedDelay(int upperBound) {
		//We add 100 in order to guarantee that in all random functions lowerBound < upperBound
		upperBound = (upperBound/SimulatorConfig.getRobotSpeedLevel()) + 100;
		int percentage = ThreadLocalRandom.current().nextInt(100);
		if(percentage < 50) {
			return ThreadLocalRandom.current().nextLong(upperBound/10);
		} else if (percentage < 70) {
			return ThreadLocalRandom.current().nextLong(upperBound/10, upperBound/8);
		} else if (percentage < 85) {
			return ThreadLocalRandom.current().nextLong(upperBound/8, upperBound/5);
		} else if (percentage < 95) {
			return ThreadLocalRandom.current().nextLong(upperBound/5, upperBound/2);
		} else {
			return ThreadLocalRandom.current().nextLong(upperBound/2, upperBound);
		}
	}

	private void performDriveForward() {
		oldPosition = currentPosition;
		currentPosition = Position.nextPositionInOrientation(targetFacing, oldPosition);
		battery -= SimulatorConfig.getBatteryLoss();
		isMoving = true;
	}

    @Override
    public void turnLeft() {
        decreaseBattery();
        if (hasPower()) {
        	if(inHardcoreMode) {
        		delay = getCustomRandomisedDelay(300);
        		now = System.currentTimeMillis();
        		isWaitingToTurningLeft = true;
        	} else {
	            performTurnLeft();
        	}
        }
    }

	private void performTurnLeft() {
		oldFacing = targetFacing;
		targetFacing = targetFacing.getTurnedLeft();
		isTurningLeft = true;
	}

    @Override
    public void turnRight() {
        decreaseBattery();
        if (hasPower()) {
        	if(inHardcoreMode) {
        		delay = getCustomRandomisedDelay(300);
        		now = System.currentTimeMillis();
        		isWaitingToTurningRight = true;
        	} else {
	            performTurnRight();
        	}
        }
    }

	private void performTurnRight() {
		oldFacing = targetFacing;
		targetFacing = targetFacing.getTurnedRight();
		isTurningRight = true;
	}

    @Override
    public void startUnloading() {
    	if(inHardcoreMode) {
    		delay = getCustomRandomisedDelay(300);
    		now = System.currentTimeMillis();
    		isWaitingToUnloading = true;
    	} else {
    		performUnload();
    	}
    }

	private void performUnload() {
		unloadingStartTime = System.currentTimeMillis();
		isUnloading = true;
	}

    private void decreaseBattery() {
        battery -= SimulatorConfig.getBatteryLoss();
        if (battery < 0)
            battery = 0;
    }

    private boolean hasPower() {
        return battery > 0;
    }

    public boolean isBatteryLow() { return battery < SimulatorConfig.getBatteryLow(); }

    public boolean isBatteryFull() { return battery >= SimulatorConfig.getBatteryFull(); }

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
        return currentPosition;
    }

    public Orientation currentFacing() {
        return oldFacing;
    }
    
    public Position getOldPosition() {
    	return oldPosition;
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
        return SimulatorConfig.getDefaultRotationSpeed() * SimulatorConfig.getRobotSpeedFactor();
    }

    public long getUnloadingTime() {
        return unloadingTime;
    }

    public void setUnloadingTime(long unloadingTime) {
        this.unloadingTime = unloadingTime;
    }

	public void activateHardcoreMode() {
		inHardcoreMode = true;
	}
}
