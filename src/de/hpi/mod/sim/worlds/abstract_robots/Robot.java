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

    private Position position;
    private Orientation facing;

    private float battery;

    public Robot(int robotID, RobotGridManager grid, Position startPosition, Orientation startFacing) {
        this.robotID = robotID; //TODO handle case of already given robotID
        this.grid = grid;
        position = startPosition;
        target = startPosition;
        facing = startFacing;
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
                 Position startPosition, Orientation startFacing, List<Position> targets, int robotSpecificDelay, int initialDelay, boolean fuzzyEnd, boolean hardArrivedConstraint) {
        this(grid, startPosition, startFacing);
        testTargets = targets;
        isInTest  = true;
        this.robotSpecificDelay = robotSpecificDelay;
        this.initialDelay  = initialDelay;
        this.initialNow = System.currentTimeMillis();
        this.fuzzyTestCompletion = fuzzyEnd;
        this.requireArrivedForTestCompletion = hardArrivedConstraint;
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
    
    public int posX() {
    	return this.pos().getX();
    }
    
    public int posY() {
    	return this.pos().getY();
    }
    
    public int targetX() {
    	return this.target.getX();
    }
    
    public int targetY() {
    	return this.target.getY();
    }

    public boolean isOnTarget() {
        if (this.fuzzyTestCompletion) {
            return isOnTargetFuzzy() && this.pos().equals(this.oldPos());
        } else {
            return isOnTargetOrNearby() && this.pos().equals(this.oldPos());
        }
    }
    
    private Position currentPosition() {
        return position;
    }

    public abstract Position oldPos(); //TODO

	private boolean isOnTargetFuzzy() {
		return currentPosition().fuzzyEquals(target);
	}

	public boolean isOnTargetOrNearby() {
        return currentPosition().equals(target);
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
}