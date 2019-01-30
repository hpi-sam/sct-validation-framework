package de.hpi.mod.sim.env.robot;

import de.hpi.mod.sim.env.model.*;
import java.util.concurrent.TimeUnit;
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
    private int robotID;
    private int stationID;
    private int packageID;

    private RobotState state = RobotState.TO_BATTERY;
    private boolean driving = false;
    private boolean hasPackage = false;

    private boolean hasReservedBattery = false;
    
    private long now = 0;
    private long delay = 0;


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
     */
    public Robot(int robotID, int stationID, ISensorDataProvider grid,
                 IRobotStationDispatcher dispatcher, ILocation location, IScanner scanner,
                 Position startPosition, Orientation startFacing, Position target) {
        this(robotID, stationID, grid, dispatcher, location, scanner, startPosition, startFacing);
        this.target = target;
        state = RobotState.SCENARIO;
    }

    /**
     * Handles state changes and refreshes the State-Machine
     */
    public void refresh() {
        if (!driving) {
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
            } else if (state == RobotState.SCENARIO && !manager.currentPosition().equals(target)) {
                startDriving();
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
        target = location.getLoadingPositionAtStation(stationID);
        state = RobotState.TO_LOADING;
        startDriving();
    }

    private void handleArriveAtLoading() {
    }

    private void handleFinishedLoading() {
    	if(now == 0) {
    		delay = ThreadLocalRandom.current().nextLong(500,5000);
    		now = System.currentTimeMillis();
    	}
    	
    	if(now < System.currentTimeMillis() - delay)
        {
    		packageID = scanner.getPackageID(stationID);
    		hasPackage = true;
            target = location.getUnloadingPositionFromID(packageID);
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
        boolean needsLoading = manager.getBattery() < DriveManager.BATTERY_LOW;
        stationID = dispatcher.getReservationNextForStation(robotID, needsLoading);
        target = location.getArrivalPositionAtStation(stationID);
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
        boolean[] blockedFieldsFromNewPosition = grid.blocked(manager.currentFacing(), manager.currentPosition());
        boolean[] blockedFieldsFromOldPosition = grid.blocked(manager.currentFacing(), manager.getOldPosition());
        
        if(blockedFieldsFromNewPosition.length == blockedFieldsFromOldPosition.length) {
        	boolean[] blockedFieldsRes = new boolean[blockedFieldsFromNewPosition.length];
        	
        	for(int i=0; i<blockedFieldsFromNewPosition.length; i++) {
        		blockedFieldsRes[i] = blockedFieldsFromNewPosition[i] || blockedFieldsFromOldPosition[i];
        	}
        	
        	return blockedFieldsRes;
        	
        } else {
        	return blockedFieldsFromNewPosition;
        }
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
        boolean[] blockedFieldsFromNewPosition = grid.blockedWaypoint(manager.currentFacing(), manager.currentPosition());
        boolean[] blockedFieldsFromOldPosition = grid.blockedWaypoint(manager.currentFacing(), manager.getOldPosition());
        
        if(blockedFieldsFromNewPosition.length == blockedFieldsFromOldPosition.length) {
        	boolean[] blockedFieldsRes = new boolean[blockedFieldsFromNewPosition.length];
        	
        	for(int i=0; i<blockedFieldsFromNewPosition.length; i++) {
        		blockedFieldsRes[i] = blockedFieldsFromNewPosition[i] || blockedFieldsFromOldPosition[i];
        	}
        	
        	return blockedFieldsRes;
        	
        } else {
        	return blockedFieldsFromNewPosition;
        }
    }

    @Override
    public boolean blockedCrossroadAhead() {
        boolean blockedCrossroadAheadNew = grid.blockedCrossroadAhead(manager.currentFacing(), manager.currentPosition());
        boolean blockedCrossroadAheadOld = grid.blockedCrossroadAhead(manager.currentFacing(), manager.getOldPosition());
        
        return blockedCrossroadAheadNew || blockedCrossroadAheadOld;
    }

    @Override
    public boolean blockedCrossroadRight() {
        boolean blockedCrossroadRightNew = grid.blockedCrossroadAhead(manager.currentFacing(), manager.currentPosition());
        boolean blockedCrossroadRightOld = grid.blockedCrossroadAhead(manager.currentFacing(), manager.getOldPosition());
        
        return blockedCrossroadRightNew || blockedCrossroadRightOld;
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

    private enum RobotState {
        TO_BATTERY, TO_QUEUE, TO_LOADING, TO_UNLOADING, TO_STATION, SCENARIO
    }
}
