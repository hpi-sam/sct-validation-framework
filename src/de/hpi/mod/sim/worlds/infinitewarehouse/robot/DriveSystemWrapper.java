package de.hpi.mod.sim.worlds.infinitewarehouse.robot;

import de.hpi.mod.sim.drivesystem.DrivesystemStatemachine;
import de.hpi.mod.sim.drivesystem.IDrivesystemStatemachine;
import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.PositionType;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IDriveSystem;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IProcessor;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IRobotActors;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.ISensor;
import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;

/**
 * Handles calls to the statechard.
 * This should be the only file with logic depending on the statechard implementation.
 */
public class DriveSystemWrapper extends StateChartWrapper<DrivesystemStatemachine.State> implements IDrivesystemStatemachine.SCIDataOperationCallback, IDrivesystemStatemachine.SCIRawDataOperationCallback, IDriveSystem {

    private ISensor data;
    private IRobotActors actors;
    private IProcessor processor;

    public DriveSystemWrapper(ISensor data, IRobotActors actors, IProcessor processor) {
        super();
        this.data = data;
        this.actors = actors;
        this.processor = processor;
        start();
    }

    /**
     * Does contain {@link IDrivesystemStatemachine} so tests can set mockups
     */
    public DriveSystemWrapper(IDrivesystemStatemachine chart, ISensor data, IRobotActors actors, IProcessor processor) {
        this.data = data;
        this.actors = actors;
        this.processor = processor;
        this.chart = chart;
        start();
    }

    @Override
    public IStatemachine createStateMachine() {
        return new DrivesystemStatemachine();
    }

    @Override
    public void start() {
        getStatemachine().getSCIData().setSCIDataOperationCallback(this);
        getStatemachine().getSCIRawData().setSCIRawDataOperationCallback(this);
        super.start();
    }

    private DrivesystemStatemachine getStatemachine() {
        return (DrivesystemStatemachine) chart;
    }

    /**
     * Runs a cycle of the statechart and checks if any functions got fired
     */
    @Override
    public void update() {
        if (getStatemachine().getSCIActors().isRaisedDriveForward())
            actors.driveForward();
        if (getStatemachine().getSCIActors().isRaisedDriveBackward())
        	actors.driveBackward();
        if (getStatemachine().getSCIActors().isRaisedStartUnload())
            actors.startUnloading();
        if (getStatemachine().getSCIActors().isRaisedTurnLeft())
            actors.turnLeft();
        if (getStatemachine().getSCIActors().isRaisedTurnRight())
            actors.turnRight();
        if (getStatemachine().getSCIProcessor().isRaisedArrived())
            processor.arrived();
    }
    
    @Override
    public void dataRefresh() {
        getStatemachine().getSCInterface().raiseDataRefresh();
        update();
    }
    
    @Override
    public void newTarget() {
        getStatemachine().getSCInterface().raiseNewTarget();
        update();
    }
    
    @Override
    public void newUnloadingTarget() {
    	getStatemachine().getSCInterface().raiseNewUnloadingTarget();
    	update();
    }
    
    @Override
    public void newChargingTarget() {
    	getStatemachine().getSCInterface().raiseNewChargingTarget();
    	update();
    }

    @Override
    public void actionCompleted() {
        getStatemachine().getSCInterface().raiseActionCompleted();
        update();
    }

    @Override
    public long posOrientation() {
        return toSCIOrientation(data.posOrientation());
    }

    @Override
    public long posType() {
        return toSCIPositionType(data.posType());
    }

    @Override
    public long targetDirection() {
        return toSCIDirection(data.targetDirection());
    }

    @Override
    public long posX() {
        return data.posX();
    }

    @Override
    public long posY() {
        return data.posY();
    }

    @Override
    public long targetX() {
        return data.targetX();
    }

    @Override
    public long targetY() {
        return data.targetY();
    }
    
    private long toSCIPositionType(PositionType type) {
        switch (type) {
            case WAYPOINT:
                return getStatemachine().getSCIPositionType().getWAYPOINT();
            case STATION:
                return getStatemachine().getSCIPositionType().getSTATION();
            case CROSSROAD:
                return getStatemachine().getSCIPositionType().getCROSSROAD();
            default:
                throw new IllegalArgumentException();
        }
    }

    private long toSCIOrientation(Orientation orient) {
        switch (orient) {
            case NORTH:
                return getStatemachine().getSCIOrientation().getNORTH();
            case EAST:
                return getStatemachine().getSCIOrientation().getEAST();
            case SOUTH:
                return getStatemachine().getSCIOrientation().getSOUTH();
            case WEST:
                return getStatemachine().getSCIOrientation().getWEST();
            default:
                throw new IllegalArgumentException();
        }
    }

    private long toSCIDirection(Direction dir) {
        switch (dir) {
            case LEFT:
                return getStatemachine().getSCIDirection().getLEFT();
            case AHEAD:
                return getStatemachine().getSCIDirection().getAHEAD();
            case RIGHT:
                return getStatemachine().getSCIDirection().getRIGHT();
            case BEHIND:
                return getStatemachine().getSCIDirection().getBEHIND();
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isOnTarget() {
        return data.isOnTarget();
    }

    @Override
    public boolean canUnloadToTarget() {
        return data.canUnloadToTarget();
    }


    @Override
    public boolean blockedLeft() {
        return data.blockedLeft();
    }

    @Override
    public boolean blockedFront() {
        return data.blockedFront();
    }

    @Override
    public boolean blockedRight() {
        return data.blockedRight();
    }

    @Override
    public boolean blockedWaypointAhead() {
        return data.blockedWaypointAhead();
    }

    @Override
    public boolean blockedWaypointLeft() {
        return data.blockedWaypointLeft();
    }

    @Override
    public boolean blockedWaypointRight() {
        return data.blockedWaypointRight();
    }

    @Override
    public boolean blockedCrossroadAhead() {
        return data.blockedCrossroadAhead();
    }

    @Override
    public boolean blockedCrossroadRight() {
        return data.blockedCrossroadRight();
    }

	@Override
    public boolean canChargeAtTarget() {
        return data.canChargeAtTarget();
    }
    
    @Override
    public boolean isActive(DrivesystemStatemachine.State state) {
        /*
        * This is not intended by the YAKINDU implementation and source generation. 
        * Officially, the YAKINDU interface does not support this, which is why we have 
        * to cast to the actual DrivesystemStateChart object.
        */
        return ((DrivesystemStatemachine) chart).isStateActive(state);
    }

    @Override
    public DrivesystemStatemachine.State[] getStates() {
        return DrivesystemStatemachine.State.values();
    }
}
