package de.hpi.mod.sim.env.robot;

import de.hpi.mod.sim.env.model.*;

public class Robot implements IProcessor, ISensor, DriveListener, IRobot {

    private static int idCount = 0;

    private DriveManager manager;
    private IDriveSystem drive;
    private ISensorDataProvider grid;
    private IRobotDispatcher dispatcher;
    private ILocation location;
    private IScanner scanner;

    private Position target;
    private int robotID;
    private int stationID;
    private int packageID;

    private RobotState state = RobotState.TO_BATTERY;
    private boolean driving = false;
    private boolean hasPackage = false;


    public Robot(int robotID, int stationID, ISensorDataProvider grid,
                  IRobotDispatcher dispatcher, ILocation location, IScanner scanner,
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

    public Robot(int robotID, int stationID, ISensorDataProvider grid,
                 IRobotDispatcher dispatcher, ILocation location, IScanner scanner,
                 Position startPosition, Orientation startFacing, Position target) {
        this(robotID, stationID, grid, dispatcher, location, scanner, startPosition, startFacing);
        this.target = target;
        state = RobotState.SCENARIO;
    }

    @Override
    public void refresh() {
        if (!driving) {
            if (state == RobotState.TO_BATTERY && manager.getBattery() == DriveManager.BATTERY_FULL) {
                manager.setLoading(false);
                if (dispatcher.requestEnqueueAtStation(robotID, stationID)) {
                    target = location.getLoadingPositionAtStation(stationID);
                    state = RobotState.TO_LOADING;
                    driving = true;
                    drive.newTarget();
                }

            } else if (state == RobotState.TO_LOADING && scanner.hasPackage(stationID)) {
                packageID = scanner.getPackageID(stationID);
                hasPackage = true;
                target = location.getUnloadingPositionFromID(packageID);
                dispatcher.reportLeaveStation(robotID, stationID);
                state = RobotState.TO_UNLOADING;
                driving = true;
                drive.newTarget();

            } else if (state == RobotState.TO_UNLOADING && !hasPackage) {
                boolean needsLoading = manager.getBattery() < DriveManager.BATTERY_LOW;
                stationID = dispatcher.requestNextStation(robotID, needsLoading);
                target = location.getArrivalPositionAtStation(stationID);
                state = RobotState.TO_STATION;
                driving = true;
                drive.newTarget();

            } else if (state == RobotState.TO_STATION && manager.getBattery() < DriveManager.BATTERY_LOW) {
                int optionalBattery = dispatcher.requestFreeChargerAtStation(robotID, stationID);
                if (optionalBattery >= 0) {
                    target = location.getChargerPositionAtStation(stationID, optionalBattery);
                    state = RobotState.TO_BATTERY;
                    driving = true;
                    drive.newTarget();
                }

            } else if (state == RobotState.TO_STATION) {
                target = location.getLoadingPositionAtStation(stationID);
                state = RobotState.TO_LOADING;
                driving = true;
                drive.newTarget();

            } else if (state == RobotState.SCENARIO && !manager.currentPosition().equals(target)) {
                driving = true;
                drive.newTarget();
            }
        }
        drive.dataRefresh();
    }

    @Override
    public void stop() {
        drive.stop();
    }

    @Override
    public void arrived() {
        driving = false;
        if (state == RobotState.TO_UNLOADING) {
            drive.unload();
        } else if (state == RobotState.TO_BATTERY) {
            dispatcher.reportChargingAtStation(robotID, stationID);
            manager.setLoading(true);
        }
    }

    @Override
    public void unloaded() {
        manager.startUnloading();
        dispatcher.reportUnloadingPackage(robotID, packageID);
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

    @Override
    public int getID() {
        return robotID;
    }

    @Override
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
        TO_BATTERY, TO_LOADING, TO_UNLOADING, TO_STATION, SCENARIO
    }
}
