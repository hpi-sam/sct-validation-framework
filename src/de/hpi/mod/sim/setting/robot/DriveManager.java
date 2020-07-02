package de.hpi.mod.sim.setting.robot;

import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.setting.grid.Orientation;
import de.hpi.mod.sim.setting.grid.Position;
import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehouseSimConfig;
import de.hpi.mod.sim.setting.robot.model.DriveListener;
import de.hpi.mod.sim.setting.robot.model.IRobotActors;

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

    private long unloadingTime = InfiniteWarehouseSimConfig.getDefaultUnloadingTime();
    
    private float battery = ThreadLocalRandom.current().nextInt((int) (InfiniteWarehouseSimConfig.getMinBatteryRatio()* InfiniteWarehouseSimConfig.getBatteryFull()), (int) InfiniteWarehouseSimConfig.getBatteryFull()+1);
    
    private int maxDelay = 0;
	private boolean isWaitingToMoveForward = false;
	private boolean isWaitingToTurningLeft = false;
	private boolean isWaitingToTurningRight = false;
	private boolean isWaitingToUnloading = false;

	private boolean hasPackage = false;

	private boolean isWaitingToMoveBackward;
	
	private boolean hasUnloadedSomething = false;


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
    	decreasePerTick();
    	if(maxDelay > 0) {
    		if(delay + now <= System.currentTimeMillis()) {
    			if (isWaitingToMoveForward) {
    				performDriveForward();
    	            isMoving = true;
    	            isWaitingToMoveForward = false;
    			} else if (isWaitingToMoveBackward) {
    				performDriveBackward();
    				isMoving = true;
    				isWaitingToMoveBackward = false;
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
		battery = Math.min(battery + delta * InfiniteWarehouseSimConfig.getBatteryChargingSpeed(), 100);
	}

	private void calculateUnload() {
		if (System.currentTimeMillis() - unloadingStartTime > (unloadingTime/(InfiniteWarehouseSimConfig.getEntitySpeedLevel()+1))) {
			isUnloading = false;
			hasPackage = false;
		    listener.actionCompleted();
		}
	}

	private void calculateTurnRight(float delta) {
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

	private void calculateTurnLeft(float delta) {
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

	private void calculateMovement(float delta) {
		int deltaX = currentPosition.getX() - oldPosition.getX();
		int deltaY = currentPosition.getY() - oldPosition.getY();

		if (deltaY != 0) {
		    y += Math.copySign(InfiniteWarehouseSimConfig.getEntitySpeed() * delta, deltaY);

		    // If y moved over target
		    if (deltaY > 0 && y >= currentPosition.getY() ||
		            deltaY < 0 && y <= currentPosition.getY()) {
		        y = currentPosition.getY();
		        finishMovement();
		    }
		} else if (deltaX != 0) {
		    x += Math.copySign(InfiniteWarehouseSimConfig.getEntitySpeed() * delta, deltaX);

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
    	if(!animationRunning()) {  	
	        if (hasPower()) {
	        	if(maxDelay > 0) {
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
		if(!animationRunning()) {
	        if (hasPower()) {
	        	if(maxDelay > 0) {
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
		oldPosition = currentPosition;
		currentPosition = Position.nextPositionInOppositeOrientation(targetFacing, oldPosition);
		decreaseBattery();
		isMoving = true;
	}

	private long getCustomRandomisedDelay(int upperBound) {
		//We add 100 in order to guarantee that in all random functions lowerBound < upperBound
		upperBound = (upperBound/Math.max(1, InfiniteWarehouseSimConfig.getEntitySpeedLevel())) + 100;
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
		decreaseBattery();
		isMoving = true;
	}

    @Override
    public void turnLeft() {
    	if(!animationRunning()) {
            if (hasPower()) {
            	if(maxDelay > 0) {
            		delay = getCustomRandomisedDelay(maxDelay/3);
            		now = System.currentTimeMillis();
            		isWaitingToTurningLeft = true;
            	} else {
    	            performTurnLeft();
            	}
            }
    	}
    }

	private void performTurnLeft() {
		oldFacing = targetFacing;
		targetFacing = targetFacing.getTurnedLeft();
		decreaseBattery();
		isTurningLeft = true;
	}

    @Override
    public void turnRight() {
    	if(!animationRunning()) {
            if (hasPower()) {
            	if(maxDelay > 0) {
            		delay = getCustomRandomisedDelay(maxDelay/3);
            		now = System.currentTimeMillis();
            		isWaitingToTurningRight = true;
            	} else {
    	            performTurnRight();
            	}
            }
    	}
    }

	private void performTurnRight() {
		oldFacing = targetFacing;
		targetFacing = targetFacing.getTurnedRight();
		decreaseBattery();
		isTurningRight = true;
	}

    @Override
    public void startUnloading() {
    	if(maxDelay > 0) {
    		delay = getCustomRandomisedDelay(maxDelay/3);
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
        battery -= InfiniteWarehouseSimConfig.getBatteryLoss();
        if (battery < 0)
            battery = 0;
    }
    
    private void decreasePerTick() {
    	
    }

    private boolean hasPower() {
        return battery > 0;
    }

    public boolean isBatteryLow() { return battery < InfiniteWarehouseSimConfig.getBatteryLow(); }

    public boolean isBatteryFull() { return battery >= InfiniteWarehouseSimConfig.getBatteryFull(); }

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
        return InfiniteWarehouseSimConfig.getDefaultRotationSpeed() * InfiniteWarehouseSimConfig.getEntitySpeedFactor();
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
		if(currentPosition.getY() <= 1) {
			return false; 
		} else if(currentPosition.getY()%3 != 0 && currentPosition.getX()%3 != 0) {
			return false; 
		} else if((currentPosition.getX() % 3 == 2 || currentPosition.getX() % 3 == -1) && currentPosition.getY() % 3 == 0) {
			if(targetFacing == Orientation.NORTH) {
				return true;
			}
		} else if((currentPosition.getX() % 3 == 1 || currentPosition.getX() % 3 == -2) && currentPosition.getY() % 3 == 0) {
			if(targetFacing == Orientation.SOUTH) {
				return true;
			}
		} else if(currentPosition.getX() % 3 == 0 && currentPosition.getY() % 3 == 1) {
			if(targetFacing == Orientation.EAST) {
				return true;
			}
		} else if(currentPosition.getX() % 3 == 0 && currentPosition.getY() % 3 == 2) {
			if(targetFacing == Orientation.WEST) {
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
