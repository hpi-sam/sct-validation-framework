package de.hpi.mod.sim.env.robot;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.model.*;
import java.util.concurrent.TimeUnit;
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
public class Robot implements IProcessor, ISensor, DriveListener {

    /**
     * Used so each Robot has an unique id
     */
    private static int idCount = 0;

    private DriveManager manager;
    private IDriveSystem drive;
    private ISensorDataProvider grid;
    private IRobotStationDispatcher dispatcher;
    private ILocation location;
    private IScanner scanner;

    private Position target;
    private List<Position> testPositionTargets = null;
    private int robotID;
    private int stationID;
    private int packageID;

    private RobotState state = RobotState.TO_BATTERY;
    private boolean driving = false;
    private boolean hasPackage = false;

    private boolean hasReservedBattery = false;
    
    private long now = 0;
    private long delay = 0;

	private boolean isInTest = false;


    public Robot(int robotID, int stationID, ISensorDataProvider grid,
                 IRobotStationDispatcher dispatcher, ILocation location, IScanner scanner,
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
     * @param state 
     */
    public Robot(int robotID, int stationID, ISensorDataProvider grid,
                 IRobotStationDispatcher dispatcher, ILocation location, IScanner scanner,
                 Position startPosition, RobotState initialState, Orientation startFacing, List<Position> targets) {
        this(robotID, (int) startPosition.getX()/3, grid, dispatcher, location, scanner, startPosition, startFacing);
        testPositionTargets = targets;
        isInTest  = true;
        this.state = initialState;
    }

    /**
     * Handles state changes and refreshes the State-Machine
     */
    public void refresh() {
        if (!driving) {
        	if(!isInTest || !testPositionTargets.isEmpty()) {
	            if (state == RobotState.TO_BATTERY && manager.isBatteryFull()) {
	                handleFinishedCharging();
	            } else if (state == RobotState.TO_LOADING && scanner.hasPackage(stationID)) {
	            	handleFinishedLoading();
	            } else if (state == RobotState.TO_UNLOADING && !hasPackage) {
	                handleFinishedUnloading();
	            } else if (state == RobotState.TO_STATION) {
	                handleArriveAtStation();
	            } else if (state == RobotState.TO_QUEUE) {
	                handleArriveAtQueue();
	            }
        	}
        }
        startDriving();
        drive.dataRefresh();
    }

    /**
     * Gets called by the Drive-System if the robot arrived at its target
     */
    @Override
    public void arrived() {
        driving = false;
        if (state == RobotState.TO_UNLOADING) {
            handleArriveAtUnloading();
        } else if (state == RobotState.TO_BATTERY) {
            handleArriveAtBattery();
        }
    }

    private void handleArriveAtStation() {
        if (dispatcher.requestEnteringStation(robotID, stationID)) {

            if (hasReservedBattery) {
        		target = location.getBatteryPositionAtStation(stationID,
                        dispatcher.getReservedChargerAtStation(robotID, stationID));
                state = RobotState.TO_BATTERY;
                hasReservedBattery = false;
                startDriving();

            } else {
                target = location.getQueuePositionAtStation(stationID);
                state = RobotState.TO_QUEUE;
            }

            startDriving();
        }
    }

    private void handleArriveAtBattery() {
        dispatcher.reportChargingAtStation(robotID, stationID);
        manager.setLoading(true);
    }

    /**
     * Requests leaving the battery.
     * If not allowed it does nothing and has to be called again at a later time
     */
    private void handleFinishedCharging() {
        manager.setLoading(false);

        if (dispatcher.requestLeavingBattery(robotID, stationID)) {
        	target = location.getQueuePositionAtStation(stationID);
            state = RobotState.TO_QUEUE;
            startDriving();
        }
    }

    private void handleArriveAtQueue() {
        dispatcher.reportEnteringQueueAtStation(robotID, stationID);
        if(!isInTest) {
        	target = location.getLoadingPositionAtStation(stationID);
        } else {
        	target = testPositionTargets.get(0);
    		testPositionTargets.remove(0);
        }
        state = RobotState.TO_LOADING;
        startDriving();
    }

    private void handleArriveAtLoading() {
    }

    private void handleFinishedLoading() {
    	if(now == 0) {
    		long minWaitTime = (long) (500 * SimulatorConfig.getRobotLevel5Speed() / SimulatorConfig.getRobotMoveSpeed());
    		long maxWaitTime = 10 * minWaitTime;
    		delay = ThreadLocalRandom.current().nextLong(minWaitTime, maxWaitTime);
    		now = System.currentTimeMillis();
    	}
    	
    	if(now < System.currentTimeMillis() - delay)
        {
    		packageID = scanner.getPackageID(stationID, this.pos());
    		hasPackage = true;
    		if(!isInTest) {
    			target = location.getUnloadingPositionFromID(packageID);
    		} else {
    			target = testPositionTargets.get(0);
        		testPositionTargets.remove(0);
    		}
            dispatcher.reportLeaveStation(robotID, stationID);
            state = RobotState.TO_UNLOADING;
            startDriving();
            now = 0;
        }
    }

    private void handleArriveAtUnloading() {
        drive.unload();
    }

    private void handleFinishedUnloading() {
        boolean needsLoading = manager.getBattery() < SimulatorConfig.BATTERY_LOW;
        stationID = dispatcher.getReservationNextForStation(robotID, needsLoading);
        if(!isInTest) {
        	target = location.getArrivalPositionAtStation(stationID);
        } else {
        	target = testPositionTargets.get(0);
    		testPositionTargets.remove(0);
    		stationID = dispatcher.getStationIDFromPosition(target);
        }
        state = RobotState.TO_STATION;
        hasReservedBattery = needsLoading;
        startDriving();
    }

    private void startDriving() {
        driving = true;
        drive.newTarget();
    }

    public void stop() {
        drive.stop();
    }

    @Override
    public void unloaded() {
        manager.startUnloading();
        // TODO unloading Dispatcher
        //dispatcher.reportUnloadingPackage(robotID, packageID);
        hasPackage = false;
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
        return Orientation.difference(
                manager.currentFacing(), grid.targetOrientation(manager.currentPosition(), target));
    }

    @Override
    public boolean isTargetReached() {
        return manager.currentPosition().equals(target);
    }
    
    public boolean hasReachedAllTargets() {
    	return testPositionTargets.isEmpty() && this.isTargetReached();
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

    @Override
    public void unloadingCompleted() {
        drive.unloaded();
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

    public boolean isHasPackage() {
        return hasPackage;
    }

    public static int incrementID() {
        return idCount++;
    }

    public enum RobotState {
        TO_BATTERY, TO_QUEUE, TO_LOADING, TO_UNLOADING, TO_STATION, SCENARIO
    }
}
