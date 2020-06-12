package de.hpi.mod.sim.env.setting.infinitestations;

import de.hpi.mod.sim.env.model.*;
import de.hpi.mod.sim.env.simulation.SimulatorConfig;
import de.hpi.mod.sim.env.simulation.robot.DriveListener;
import de.hpi.mod.sim.env.simulation.robot.DriveManager;
import de.hpi.mod.sim.env.simulation.robot.DriveSystemWrapper;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Controller for a Robot.
 * Is drawn by the view and managed by a Simulation.
 * A {@link IDriveSystem} is used through a Wrapper to access the State-Machine and
 * a {@link DriveManager} to calculate the real position.
 * It relays sensor-information from a {@link ISensorDataProvider} to the Drive-System.
 * It uses a {@link IRobotStationDispatcher} to drive in a station and {@link ILocation} to get new Targets.
 */
public class Robot implements IProcessor, ISensor, DriveListener {  //TODO draw as much as possible into core

    /**
     * Used so each Robot has an unique id
     */
    private static int idCount = 0;

    private DriveManager manager;
    private IDriveSystem drive;
    private ISensorDataProvider grid;
    private IRobotStationDispatcher dispatcher;
    private IComplexLocation location;
    private IScanner scanner;

    private Position target = null;
    private List<Position> testPositionTargets = null;
    private int robotID;
    private int stationID;
    private int packageID;
    private int robotSpecificDelay = 0;

    private RobotState state = RobotState.TO_QUEUE;
    private boolean robotHasDriveTarget = false;

    private boolean hasReservedBattery = false;
    
    private long now = 0;
    private long delay = 0;

	private boolean isInTest = false;
	private boolean isAlone = false;

	private boolean refreshing = false;

	private Position invalidUnloadingPosition = null;

	private int batteryID;

	private int initialDelay = 0;

	private long initialNow;

	private boolean fuzzyTestCompletion = false;
	private boolean requireUnloadingForTestCompletion = false;
	private boolean requireArrivedForTestCompletion = false;

	private boolean arrivedEventWasCalled = true;

    public Robot(int robotID, int stationID, ISensorDataProvider grid,
                 IRobotStationDispatcher dispatcher, IComplexLocation location, IScanner scanner,
                 Position startPosition, Orientation startFacing) {
        this.robotID = robotID;
        this.stationID = stationID;
        this.grid = grid;
        this.dispatcher = dispatcher;
        this.location = location;
        this.scanner = scanner;
        manager = new DriveManager(this, startPosition, startFacing);
        drive = new DriveSystemWrapper(this, manager, this);
        target = startPosition;
    }

    /**
     * This constructor is only used for test-scenarios and sets the Robots state to Scenario
     * @param fuzzyEnd 
     * @param hardArrivedConstraint 
     * @param hasReservedBattery2 
     * @param state 
     */
    public Robot(int robotID, int stationID, ISensorDataProvider grid,
                 IRobotStationDispatcher dispatcher, 
            IComplexLocation location, IScanner scanner,
                 Position startPosition, RobotState initialState, Orientation startFacing, List<Position> targets, int robotSpecificDelay, int initialDelay, boolean fuzzyEnd, boolean unloadingTest, boolean hasReservedBattery, boolean hardArrivedConstraint) {
        this(robotID, (int) startPosition.getX()/3, grid, dispatcher, location, scanner, startPosition, startFacing);
        testPositionTargets = targets;
        isInTest  = true;
        this.robotSpecificDelay = robotSpecificDelay;
        this.initialDelay  = initialDelay;
        this.initialNow = System.currentTimeMillis();
        this.state = initialState;
        this.fuzzyTestCompletion = fuzzyEnd;
        this.requireUnloadingForTestCompletion = unloadingTest;
        this.hasReservedBattery = hasReservedBattery;
        this.requireArrivedForTestCompletion = hardArrivedConstraint;
    }

    public Robot(int robotID2, int i, ISensorDataProvider grid2, IRobotStationDispatcher stations, 
            IComplexLocation simulator,
			IScanner simulator2, Position position, Orientation facing, int delay) {
		this(robotID2, i, grid2, stations, simulator, simulator2, position, facing);
		manager.setMaxDelay(delay);
	}

	/**
     * Handles state changes and refreshes the State-Machine
     */
    public void refresh() {
    	
    	
    	if(System.currentTimeMillis() >= initialNow + initialDelay) {
    		if (!robotHasDriveTarget) {
            	if(!isInTest || !testPositionTargets.isEmpty()) {
    	            if (state == RobotState.TO_BATTERY && manager.isBatteryFull()) {
    	                handleFinishedCharging();
    	            } else if (state == RobotState.TO_LOADING && scanner.hasPackage(stationID)) {
    	            	handleFinishedLoading();
    	            } else if (state == RobotState.TO_UNLOADING && !manager.hasPackage()) {
    	                handleFinishedUnloading();
    	            } else if (state == RobotState.TO_STATION) {
    	                handleArriveAtStation();
    	            } else if (state == RobotState.TO_QUEUE) {
    	                handleArriveAtQueue();
    	            }
            	}
            }
            
            // send datarefresh() Signal
            drive.dataRefresh();
            
    		// Update timer
            drive.updateTimer();
    	}
    }

    /**
     * Gets called by the Drive-System if the robot arrived at its target
     */
    @Override
    public void arrived() {
    	if(isInTest) {
    		arrivedEventWasCalled = true;
    	}
        robotHasDriveTarget = false;
        if (state == RobotState.TO_UNLOADING) {
            handleArriveAtUnloading();
        } else if (state == RobotState.TO_BATTERY) {
            handleArriveAtBattery();
        }
    }

    private void handleArriveAtStation() {
        if (hasReservedBattery) {
        	startDrivingToCharging();
        	if(dispatcher.requestEnteringBattery(robotID, stationID)) {
        		if(!isInTest) {
        			batteryID = dispatcher.getReservedChargerAtStation(robotID, stationID);
        		} else {
        			// If in test: Translate coordinates from queue to battery id
        			target = testPositionTargets.get(0);
        			if(Math.abs(target.getY()) == 1){
        				batteryID = 2;
        			} else if (Math.abs(target.getY()) == 2) {
        				batteryID = 1;
        			} else {
        				batteryID = 0;
        			}
        			testPositionTargets.remove(0);
            		arrivedEventWasCalled = false;
        		}
        		target = location.getBatteryPositionAtStation(stationID,
                       batteryID);
                state = RobotState.TO_BATTERY;
                hasReservedBattery = false;
                startDriving();
        	}
        } else {
        	drive.newTarget();
        	if (dispatcher.requestEnteringStation(robotID, stationID)) {
        		target = location.getQueuePositionAtStation(stationID);
                state = RobotState.TO_QUEUE;
        	}
        }

        startDriving();
    }

    private void handleArriveAtBattery() {
        if(manager.currentFacing() == Orientation.EAST) {
        	dispatcher.reportChargingAtStation(robotID, stationID, batteryID);
        	manager.setLoading(true);
        }
    }

    /**
     * Requests leaving the battery.
     * If not allowed it does nothing and has to be called again at a later time
     */
    private void handleFinishedCharging() {
        manager.setLoading(false);

        if (dispatcher.requestLeavingBattery(robotID, stationID, batteryID)) {
        	drive.newTarget();
        	target = location.getQueuePositionAtStation(stationID);
            state = RobotState.TO_QUEUE;
            startDriving();
        }
    }

    private void handleArriveAtQueue() {
        dispatcher.reportEnteringQueueAtStation(robotID, stationID);
        drive.newTarget();
    	if(!isInTest) {
        	target = location.getLoadingPositionAtStation(stationID);
        } else {
        	if((manager.currentPosition().equals(target) || manager.getOldPosition().equals(target)) && (!requireArrivedForTestCompletion || arrivedEventWasCalled)) {
        		target = testPositionTargets.get(0);
        		testPositionTargets.remove(0);
        		arrivedEventWasCalled = false;
        	}
        }
        state = RobotState.TO_LOADING;
        startDriving();
    }

    private void handleFinishedLoading() {
    	if(now == 0 && !isInTest) {
    		long minWaitTime = (long) (SimulatorConfig.getMinWaitingTimeBeforeLoading() / SimulatorConfig.getRobotSpeedFactor());
    		long maxWaitTime = (long) (SimulatorConfig.getMaxWaitingTimeBeforeLoading() / SimulatorConfig.getRobotSpeedFactor());
    		if(isAlone) {
    			delay = minWaitTime;
    		}else {
    			delay = ThreadLocalRandom.current().nextLong(minWaitTime, maxWaitTime);
    		}
    		now = System.currentTimeMillis();
    	}
    	
    	if(now < System.currentTimeMillis() - delay || isInTest)
        {
    		packageID = scanner.getPackageID(stationID, this.pos());
    		manager.setPackage(true);
    		if(!isInTest) {
    			startDrivingToUnload();
    			target = location.getUnloadingPositionFromID(packageID);
    		} else {
    			startDrivingToUnload();
    			if((manager.currentPosition().fuzzyEquals(target) || manager.getOldPosition().fuzzyEquals(target)) && (!requireArrivedForTestCompletion || arrivedEventWasCalled)) {
            		target = testPositionTargets.get(0);
            		testPositionTargets.remove(0);
            		arrivedEventWasCalled = false;
            	}
    		}
    		if (!isInTest) {
    			dispatcher.reportLeaveStation(robotID, stationID);
    		}
            state = RobotState.TO_UNLOADING;
            now = 0;
        }
    }

    private void startDrivingToUnload() {
		robotHasDriveTarget = true;
		drive.newUnloadingTarget();
	}

	private void handleArriveAtUnloading() {
    	// if(!manager.isInPositionToUnloadIntoShaft() && manager.hasPackage()) {
        if(isInTest && !manager.isInPositionToUnloadIntoShaft()) {
    		invalidUnloadingPosition  = manager.currentPosition();
    	}
    }

	private void handleFinishedUnloading() {
		if(!manager.isInPositionToUnloadIntoShaft()) {
    		invalidUnloadingPosition  = manager.currentPosition();
    	}
		
        boolean needsLoading = manager.getBattery() < SimulatorConfig.BATTERY_LOW;
        stationID = dispatcher.getReservationNextForStation(robotID, needsLoading);
        drive.newTarget();
        if(!isInTest) {
        	target = location.getArrivalPositionAtStation(stationID);
        } else {
        	if(((manager.currentPosition().fuzzyEquals(target) || manager.getOldPosition().fuzzyEquals(target)) && !manager.hasPackage()) && (!requireArrivedForTestCompletion || arrivedEventWasCalled)) {
        		target = testPositionTargets.get(0);
        		testPositionTargets.remove(0);
        		stationID = dispatcher.getStationIDFromPosition(target);
        		arrivedEventWasCalled = false;
        	}
        }
        state = RobotState.TO_STATION;
        hasReservedBattery = needsLoading;
        startDriving();
    }

    private void startDrivingToCharging() {
		robotHasDriveTarget = true;
		drive.newChargingTarget();		
	}

	private void startDriving() {
        robotHasDriveTarget = true;
        drive.getUpdate();
    }

    @Override
    public Orientation posOrientation() {
        return manager.currentFacing();
    }

    @Override
    public PositionType posType() {
        return grid.posType(manager.currentPosition());
    }

    @Override
    public Direction targetDirection() {
    	return grid.targetDirection(manager.currentFacing(), manager.currentPosition(), target);
    }
    
    @Override
    public int posX() {
    	return this.pos().getX();
    }
    
    @Override
    public int posY() {
    	return this.pos().getY();
    }
    
    @Override
    public int targetX() {
    	return this.target.getX();
    }
    
    @Override
    public int targetY() {
    	return this.target.getY();
    }

    @Override
    public boolean isOnTarget() {
    	if(this.fuzzyTestCompletion) {
    		return isOnTargetFuzzy() && this.pos().equals(this.oldPos()) && correctFacing(this.pos());
    	} else {
    		return isOnTargetOrNearby() && this.pos().equals(this.oldPos())  && correctFacing(this.pos());
    	}
    }

    private boolean correctFacing(Position position) {
		if(grid.cellType(position) == CellType.BATTERY) {
			if(manager.currentFacing() != Orientation.EAST) {
				return false;
			}
		}
		return true;
	}

	private boolean isOnTargetFuzzy() {
		return manager.currentPosition().fuzzyEquals(target);
	}

	public boolean isOnTargetOrNearby() {
        return manager.currentPosition().equals(target);
    }

    @Override
    public boolean canUnloadToTarget() {
        return this.pos().equals(this.oldPos()) && grid.posType(this.pos()) == PositionType.WAYPOINT &&  grid.cellType(target) == CellType.BLOCK &&
        		((manager.currentPosition().equals(target.getModified(-1,0)) ) ||
        		 (manager.currentPosition().equals(target.getModified(1,0)) ) ||
        		 (manager.currentPosition().equals(target.getModified(0,-1)) ) ||
        		 (manager.currentPosition().equals(target.getModified(0,1)) ));
    }
    
    @Override
    public boolean canChargeAtTarget() {
    	return this.pos().is(this.oldPos()) && grid.cellType(this.pos()) == CellType.STATION && 
    			grid.cellType(target) == CellType.BATTERY && manager.currentPosition().equals(target.getModified(1,0));
    }
    
    public boolean hasPassedAllTestCriteria() {
    	return testPositionTargets.isEmpty() && this.isOnTarget()  
    			&& (!requireArrivedForTestCompletion || arrivedEventWasCalled) 
    			&& (!requireUnloadingForTestCompletion || manager.hasUnloadedSomething());
    }

    @Override
    public boolean blockedLeft() {
        return blocked()[0];
    }

    @Override
    public boolean blockedFront() {
        return blocked()[1];
    }

    @Override
    public boolean blockedRight() {
        return blocked()[2];
    }

    private boolean[] blocked() {
    	return grid.blocked(manager.currentFacing(), manager.currentPosition());
    }

    @Override
    public boolean blockedWaypointAhead() {
        return blockedWaypoint()[1];
    }

    @Override
    public boolean blockedWaypointLeft() {
        return blockedWaypoint()[0];
    }

    @Override
    public boolean blockedWaypointRight() {
        return blockedWaypoint()[2];
    }

    private boolean[] blockedWaypoint() {
    	return grid.blockedWaypoint(manager.currentFacing(), manager.currentPosition());
    }

    @Override
    public boolean blockedCrossroadAhead() {
    	return grid.blockedCrossroadAhead(manager.currentFacing(), manager.currentPosition());
    }

    @Override
    public boolean blockedCrossroadRight() {
    	return grid.blockedCrossroadRight(manager.currentFacing(), manager.currentPosition());
    }

    @Override
    public void actionCompleted() {
        drive.actionCompleted();
    }

    public int getID() {
        return robotID;
    }

    public Position pos() {
        return manager.currentPosition();
    }
    
    public Position oldPos() {
    	return manager.getOldPosition();
    }

    public void close() {
        drive.close();
    }

    public DriveManager getDriveManager() {
        return manager;
    }

    void setTarget(Position target) {
        this.target = target;
    }

    public IDriveSystem getDrive() {
        return drive;
    }

    public float getBattery() {
        return manager.getBattery();
    }

    public Position getTarget() {
        return target;
    }

    public int getStationID() {
        return stationID;
    }

    public int getPackageID() {
        return packageID;
    }

    public boolean hasPackage() {
        return manager.hasPackage();
    }

    public static int incrementID() {
        return idCount++;
    }
    
    public String getMachineState() {
    	return drive.getMachineState();
    }

    public enum RobotState {
        TO_BATTERY, TO_QUEUE, TO_LOADING, TO_UNLOADING, TO_STATION, SCENARIO
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
	
	public Position getInvalidUnloadingPosition() {
		return invalidUnloadingPosition;
	}

	public void resetInvalidUnloadingPosition() {
		invalidUnloadingPosition = null;
	}

	public void setIsAlone(boolean b) {
		this.isAlone = b;
	}	
}
