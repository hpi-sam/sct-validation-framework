package de.hpi.mod.sim.worlds.infinitewarehouse.robot;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouseConfiguration;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.CellType;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.ILocation;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.PositionType;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.WarehouseManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.*;


public class WarehouseRobot extends Robot implements IProcessor, ISensor, IDriveListener, StateChartEntity {

    private DriveSystemWrapper drive;
    private IRobotStationDispatcher robotDispatch;
    private ILocation location;
    private IScanner scanner;

    private StateChartWrapper<?> chart;

    private int stationID;
    private int packageID;
    
    private RobotState state = RobotState.TO_QUEUE;
    
    private boolean hasReservedCharger = false;

    private long now = 0;
    private long delay = 0;

    private boolean isAlone = false;

    private Position invalidUnloadingPosition = null;

    private int chargerID;
    
    public WarehouseRobot(int robotID, int stationID, WarehouseManager grid, IRobotStationDispatcher robots, Position startPosition, Orientation startFacing) {
        super(robotID, grid, startPosition, startFacing);
        
        this.stationID = stationID;
        this.robotDispatch = robots;
        this.location = grid;
        this.scanner = grid;
        DriveSystemWrapper drive = new DriveSystemWrapper(this, getDriveManager(), this);
        this.drive = drive;
        this.chart = drive;
    }

    /**
     * This constructor is only used for test-scenarios and sets the Robots state to
     * Scenario
     * 
     * @param fuzzyEnd
     * @param hardArrivedConstraint
     * @param hasReservedCharger
     * @param state
     */
    public WarehouseRobot(WarehouseManager grid, IRobotStationDispatcher robotDispatch, Position startPosition, RobotState initialState,
            Orientation startFacing, List<Position> targets, int robotSpecificDelay, int initialDelay, boolean fuzzyEnd,
            boolean unloadingTest, boolean hasReservedCharger, boolean hardArrivedConstraint) {

        super(grid, startPosition, startFacing, targets, robotSpecificDelay, initialDelay, fuzzyEnd,
                hardArrivedConstraint);

        this.stationID = (int) startPosition.getX() / 3;
        this.robotDispatch = robotDispatch;
        this.location = grid;
        this.scanner = grid;
        DriveSystemWrapper drive = new DriveSystemWrapper(this, getDriveManager(), this);
        this.drive = drive;
        this.chart = drive;

        this.state = initialState;
        this.requireUnloadingForTestCompletion = unloadingTest;
        this.hasReservedCharger = hasReservedCharger;    
    }

    public WarehouseRobot(int robotID, int stationID, 
            WarehouseManager grid, IRobotStationDispatcher stations, Position position, Orientation facing, int delay) {
		this(robotID, stationID, grid, stations, position, facing);
		getDriveManager().setMaxDelay(delay);
	}

	/**
     * Handles state changes and refreshes the State-Machine
     */
    @Override
    public void onRefresh() {
        drive.dataRefresh();
        chart.updateTimer();
    	if (!robotHasDriveTarget) {
            if(!isInTest() || !getTestTargets().isEmpty()) {
                if (state == RobotState.TO_CHARGER && getDriveManager().isBatteryFull()) {
                    handleFinishedCharging();
                } else if (state == RobotState.TO_LOADING && scanner.hasPackage(stationID)) {
                    handleFinishedLoading();
                } else if (state == RobotState.TO_UNLOADING && !getDriveManager().hasPackage()) {
                    handleFinishedUnloading();
                } else if (state == RobotState.TO_STATION) {
                    handleArriveAtStation();
                } else if (state == RobotState.TO_QUEUE) {
                    handleArriveAtQueue();
                }
            }
        }
    }

    /**
     * Gets called by the Drive-System if the robot arrived at its target
     */
    @Override
    public void arrived() {
        super.arrived();
        if (state == RobotState.TO_UNLOADING) {
            handleArriveAtUnloading();
        } else if (state == RobotState.TO_CHARGER) {
            handleArriveAtCharger();
        }
    }

    private void handleArriveAtStation() {
        if (hasReservedCharger) {
        	startDrivingToCharging();
        	if(robotDispatch.requestEnteringCharger(getID(), stationID)) {
        		if(!isInTest()) {
        			chargerID = robotDispatch.getReservedChargerAtStation(getID(), stationID);
        		} else {
        			// If in test: Translate coordinates from queue to charger id
        			setTarget(getTestTargets().get(0));
        			if(Math.abs(getTarget().getY()) == 1){
        				chargerID = 2;
        			} else if (Math.abs(getTarget().getY()) == 2) {
        				chargerID = 1;
        			} else {
        				chargerID = 0;
        			}
        			getTestTargets().remove(0);
            		setArrivedEventWasCalled(false);
        		}
        		setTarget(location.getChargerPositionAtStation(stationID,
                       chargerID));
                state = RobotState.TO_CHARGER;
                hasReservedCharger = false;
                startDriving();
        	}
        } else {
        	drive.newTarget();
        	if (robotDispatch.requestEnteringStation(getID(), stationID)) {
        		setTarget(location.getQueuePositionAtStation(stationID));
                state = RobotState.TO_QUEUE;
        	}
        }

        startDriving();
    }

    private void handleArriveAtCharger() {
        if(facing() == Orientation.EAST) {
        	robotDispatch.reportChargingAtStation(getID(), stationID, chargerID);
        	getDriveManager().setLoading(true);
        }
    }

    /**
     * Requests leaving the charger.
     * If not allowed it does nothing and has to be called again at a later time
     */
    private void handleFinishedCharging() {
        getDriveManager().setLoading(false);

        if (robotDispatch.requestLeavingCharger(getID(), stationID, chargerID)) {
        	drive.newTarget();
        	setTarget(location.getQueuePositionAtStation(stationID));
            state = RobotState.TO_QUEUE;
            startDriving();
        }
    }

    private void handleArriveAtQueue() {
        robotDispatch.reportEnteringQueueAtStation(getID(), stationID);
        drive.newTarget();
        if (!isInTest()) {
            setTarget(location.getLoadingPositionAtStation(stationID));
        } else {
            if ((pos().equals(getTarget()) || oldPos().equals(getTarget())) && arrivementFullfilled()) {
                setTarget(getTestTargets().get(0));
                getTestTargets().remove(0);
                setArrivedEventWasCalled(false);
            }
        }
        state = RobotState.TO_LOADING;
        startDriving();
    }

    private void handleFinishedLoading() {
    	if(now == 0 && !isInTest()) {
    		long minWaitTime = (long) (InfiniteWarehouseConfiguration.getMinWaitingTimeBeforeLoading() / InfiniteWarehouseConfiguration.getEntitySpeedFactor());
    		long maxWaitTime = (long) (InfiniteWarehouseConfiguration.getMaxWaitingTimeBeforeLoading() / InfiniteWarehouseConfiguration.getEntitySpeedFactor());
    		if(isAlone) {
    			delay = minWaitTime;
    		}else {
    			delay = ThreadLocalRandom.current().nextLong(minWaitTime, maxWaitTime);
    		}
    		now = System.currentTimeMillis();
    	}
    	
    	if(now < System.currentTimeMillis() - delay || isInTest())
        {
    		packageID = scanner.getPackageID(stationID, this.pos());
    		getDriveManager().setPackage(true);
    		if(!isInTest()) {
    			startDrivingToUnload();
    			setTarget(location.getUnloadingPositionFromID(packageID));
    		} else {
    			startDrivingToUnload();
    			if((pos().fuzzyEquals(getTarget()) || oldPos().fuzzyEquals(getTarget())) && arrivementFullfilled()) {
            		setTarget(getTestTargets().get(0));
            		getTestTargets().remove(0);
            		setArrivedEventWasCalled(false);
            	}
    		}
    		if (!isInTest()) {
    			robotDispatch.reportLeaveStation(getID(), stationID);
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
    	// if(!getDriveManager().isInPositionToUnloadIntoShaft() && getDriveManager().hasPackage()) {
        if(isInTest() && !getDriveManager().isInPositionToUnloadIntoShaft()) {
    		invalidUnloadingPosition  = pos();
    	}
    }

	private void handleFinishedUnloading() {
		if(!getDriveManager().isInPositionToUnloadIntoShaft()) {
    		invalidUnloadingPosition  = pos();
    	}
		
        boolean needsLoading = getDriveManager().getBattery() < InfiniteWarehouseConfiguration.BATTERY_LOW;
        stationID = robotDispatch.getReservationNextForStation(getID(), needsLoading);
        drive.newTarget();
        if(!isInTest()) {
        	setTarget(location.getArrivalPositionAtStation(stationID));
        } else {
        	if(((pos().fuzzyEquals(getTarget()) || oldPos().fuzzyEquals(getTarget())) && !getDriveManager().hasPackage()) && arrivementFullfilled()) {
        		setTarget(getTestTargets().get(0));
        		getTestTargets().remove(0);
        		stationID = robotDispatch.getStationIDFromPosition(getTarget());
        		setArrivedEventWasCalled(false);
        	}
        }
        state = RobotState.TO_STATION;
        hasReservedCharger = needsLoading;
        startDriving();
    }

    private void startDrivingToCharging() {
        robotHasDriveTarget = true;
        drive.newChargingTarget();
    }

    @Override
    protected void startDriving() {
        super.startDriving();
        chart.update();
    }

    @Override
    public PositionType posType() {
        return PositionType.get(grid.cellType(pos()));
    }
    
    @Override
    public boolean isOnTarget() {
        return super.isOnTarget() && correctFacing(pos());
    }

    private boolean correctFacing(Position position) {
		if(grid.cellType(position) == CellType.CHARGER) {
			if(facing() != Orientation.EAST) {
				return false;
			}
		}
		return true;
	}

    @Override
    public boolean canUnloadToTarget() {
        return this.pos().equals(this.oldPos()) && posType() == PositionType.WAYPOINT &&  grid.cellType(getTarget()) == CellType.BLOCK &&
        		((pos().equals(getTarget().getModified(-1,0)) ) ||
        		 (pos().equals(getTarget().getModified(1,0)) ) ||
        		 (pos().equals(getTarget().getModified(0,-1)) ) ||
        		 (pos().equals(getTarget().getModified(0,1)) ));
    }
    
    @Override
    public boolean canChargeAtTarget() {
        return this.pos().is(this.oldPos()) && grid.cellType(this.pos()) == CellType.STATION
                && grid.cellType(getTarget()) == CellType.CHARGER
                && pos().equals(getTarget().getModified(1, 0));
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
    	return getWarehouseManager().blockedWaypoint(facing(), pos());
    }

    @Override
    public boolean blockedCrossroadAhead() {
    	return getWarehouseManager().blockedCrossroadAhead(facing(), pos());
    }

    @Override
    public boolean blockedCrossroadRight() {
    	return getWarehouseManager().blockedCrossroadRight(facing(), pos());
    }

    @Override
    public void close() {
        chart.close();
    }

    public int getStationID() {
        return stationID;
    }

    public int getPackageID() {
        return packageID;
    }

    public String getMachineState() {
    	return chart.getChartState();
    }

    public enum RobotState {
        TO_CHARGER, TO_QUEUE, TO_LOADING, TO_UNLOADING, TO_STATION, SCENARIO
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

    @Override
    public List<String> getHighlightInfo() {
        List<String> infos = super.getHighlightInfo();
        infos.get(2).concat(" (" + posType().toString() + ")");
        return infos;
    }

    @Override
    public String getTopStateName() {
        return "drive_system";
    }

    private WarehouseManager getWarehouseManager() {
        return (WarehouseManager) grid;
    }

    @Override
    public void actionCompleted() {
        super.actionCompleted();
        drive.actionCompleted();
    }
}