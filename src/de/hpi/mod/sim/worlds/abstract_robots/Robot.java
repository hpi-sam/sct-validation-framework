package de.hpi.mod.sim.worlds.abstract_robots;

import java.util.ArrayList;
import java.util.List;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;

/**
 * Controller for a Robot.
 */
public abstract class Robot implements Entity, IHighlightable {

    /**
     * Used so each Robot has an unique id
     */
    private static int idCount = 0;

    protected RobotGridManager grid;

    private Position target = null;
    private List<Position> testTargets = null;
    private int robotID;
    private int robotSpecificDelay = 0;

    protected boolean robotHasDriveTarget = false;

	private boolean isInTest = false;

	private boolean refreshing = false;

	private int initialDelay = 0;

	private long initialNow;

	private boolean fuzzyTestCompletion = false;
	private boolean requireArrivedForTestCompletion = false;

    private boolean arrivedEventWasCalled = true;

    private Orientation facing, targetFacing;

    private float battery;

    private float x, y, angle;

    private Position position, oldPosition;

    public Robot(int robotID, RobotGridManager grid, Position startPosition, Orientation startFacing) {
        this.robotID = robotID; //TODO handle case of already given robotID
        this.grid = grid;
        setRobotTo(startPosition);
        oldPosition = startPosition;
        target = startPosition;
        turnRobotTo(startFacing);
        setTargetFacing(startFacing);
    }

    public Robot(RobotGridManager grid, Position startPosition, Orientation startFacing) {
        this(incrementID(), grid, startPosition, startFacing);
    }

    /**
     * This constructor is only used for test-scenarios and sets the Robots state to Scenario
     * @param fuzzyEnd 
     * @param hardArrivedConstraint 
     */
    public Robot(RobotGridManager grid,
            Position startPosition, Orientation startFacing, List<Position> targets, int robotSpecificDelay,
            int initialDelay, boolean fuzzyEnd, boolean hardArrivedConstraint) {
        this(grid, startPosition, startFacing);
        testTargets = targets;
        isInTest = true;
        this.robotSpecificDelay = robotSpecificDelay;
        this.initialDelay = initialDelay;
        this.initialNow = System.currentTimeMillis();
        this.fuzzyTestCompletion = fuzzyEnd;
        this.requireArrivedForTestCompletion = hardArrivedConstraint;
    }
    
    private void setRobotTo(Position pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.position = pos;
    }

	/**
     * Handles state changes and refreshes the State-Machine
     */
    public void refresh() {
        if (System.currentTimeMillis() >= initialNow + initialDelay)
            onRefresh();
    }

    protected abstract void onRefresh();

    /**
     * Gets called by the Drive-System if the robot arrived at its target
     */
    public void arrived() {
    	if(isInTest) {
    		arrivedEventWasCalled = true;
    	}
        robotHasDriveTarget = false;
    }

	protected void startDriving() {
        robotHasDriveTarget = true;
    }
    
    public float x() {
        return x;
    }
    
    public float y() {
        return y;
    }

    public int posX() {
        return position.getX();
    }

    public int posY() {
        return position.getY();
    }
    
    public float getAngle() {
        return angle;
    }
    
    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }
    public void increaseY(float y) {
        setY(y() + y);
    }

    public void increaseX(float x) {
        setX(x() + x);
    }
     
    public int targetX() {
    	return this.target.getX();
    }
    
    public int targetY() {
    	return this.target.getY();
    }

    public Position oldPos() {
        return oldPosition;
    }

    public void setOldPos(Position pos) {
        this.oldPosition = pos;
    }

    public void setPos(Position pos) {
        this.position = pos;
    }
    
    public Orientation facing() {
        return facing;
    }

    public Orientation targetFacing() {
        return targetFacing;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void increaseAngle(float angle) {
        setAngle(getAngle() + angle);
    }

    public void turnRobotTo(Orientation facing) {
        setFacing(facing);
        setAngle(facing.getAngle());
    }

    public void setTargetFacing(Orientation facing) {
        this.targetFacing = facing;
    }

    public void setFacing(Orientation facing) {
        this.facing = facing;
    }

    public boolean isOnTarget() {
        if (this.fuzzyTestCompletion) {
            return isOnTargetFuzzy() && this.pos().equals(this.oldPos());
        } else {
            return isOnTargetOrNearby() && this.pos().equals(this.oldPos());
        }
    }


	private boolean isOnTargetFuzzy() {
		return pos().fuzzyEquals(target);
	}

	public boolean isOnTargetOrNearby() {
        return pos().equals(target);
    }
    
    @Override
    public boolean hasPassedAllTestCriteria() {
        return testTargets.isEmpty() && this.isOnTarget() && arrivementFullfilled();
    }
    
    public boolean arrivementFullfilled() {
        return !requireArrivedForTestCompletion || arrivedEventWasCalled;
    } 

    public boolean blockedLeft() {
        return blocked()[0];
    }

    public boolean blockedFront() {
        return blocked()[1];
    }

    public boolean blockedRight() {
        return blocked()[2];
    }

    private boolean[] blocked() {
    	return grid.blocked(posOrientation(), pos());
    }

    public int getID() {
        return robotID;
    }

    public Position pos() {
        return position;
    }
    
    public Orientation posOrientation() {
        return facing;
    }
    
    public Direction targetDirection() {
        return grid.targetDirection(posOrientation(), pos(), target);
    }

    public void setTarget(Position target) {
        this.target = target;
    }

    public float getBattery() {
        return battery;
    }

    public Position getTarget() {
        return target;
    }

    public static int incrementID() {
        return idCount++;
    }

    public void startingRefreshing() {
		refreshing  = true;
	}

	public boolean isRefreshing() {
		return refreshing;
	}

	public void finishRefreshing() {
		refreshing = false;
	}
	
	public int getRobotSpecificDelay() {
		return robotSpecificDelay;
	}
	
	public boolean isInTest() {
		return isInTest;
	}

    @Override
    public List<String> getHighlightInfo() {
        List<String> infos = new ArrayList<>();
        infos.add("ID: " + getID());
        infos.add("Battery: " + (int) getBattery());
        infos.add("Pos: " + pos().stringify());
        infos.add("Target: " + getTarget().stringify());
        infos.add("Facing: " + posOrientation().toString());
        infos.add("Target Direction: " + (isOnTargetOrNearby() ? "-" : targetDirection().toString()));
        return infos;
    }

    public void close() {
    }

    public List<Position> getTestTargets() {
        return testTargets;
    }

    public void setArrivedEventWasCalled(boolean b) {
        this.arrivedEventWasCalled = b;
    }

	public boolean hasPackage() {
		return false;
	}
}