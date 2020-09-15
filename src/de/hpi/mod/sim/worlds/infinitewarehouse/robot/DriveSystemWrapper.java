package de.hpi.mod.sim.worlds.infinitewarehouse.robot;

import de.hpi.mod.sim.drivesystem.DrivesystemStateChart;
import de.hpi.mod.sim.drivesystem.IDrivesystemStateChart;
import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.PositionType;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IDriveSystem;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IProcessor;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IRobotActors;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.ISensor;
import de.hpi.mod.sim.IStatechart;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;

/**
 * Handles calls to the statechard.
 * This should be the only file with logic depending on the statechard implementation.
 */
public class DriveSystemWrapper extends StateChartWrapper<DrivesystemStateChart.State> implements IDrivesystemStateChart.SCIDataOperationCallback, IDrivesystemStateChart.SCIRawDataOperationCallback, IDriveSystem {

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
     * Does contain {@link IDrivesystemStateChart} so tests can set mockups
     */
    public DriveSystemWrapper(IDrivesystemStateChart chart, ISensor data, IRobotActors actors, IProcessor processor) {
        this.data = data;
        this.actors = actors;
        this.processor = processor;
        this.chart = chart;
        start();
    }

    @Override
    public IStatechart createStateChart() {
        return new DrivesystemStateChart();
    }

    @Override
    public void start() {
        getStateChart().getSCIData().setSCIDataOperationCallback(this);
        getStateChart().getSCIRawData().setSCIRawDataOperationCallback(this);
        super.start();
    }

    private DrivesystemStateChart getStateChart() {
        return (DrivesystemStateChart) chart;
    }

    /**
     * Runs a cycle of the statechart and checks if any functions got fired
     */
    @Override
    public void update() {
        if (getStateChart().getSCIActors().isRaisedDriveForward())
            actors.driveForward();
        if (getStateChart().getSCIActors().isRaisedDriveBackward())
        	actors.driveBackward();
        if (getStateChart().getSCIActors().isRaisedStartUnload())
            actors.startUnloading();
        if (getStateChart().getSCIActors().isRaisedTurnLeft())
            actors.turnLeft();
        if (getStateChart().getSCIActors().isRaisedTurnRight())
            actors.turnRight();
        if (getStateChart().getSCIProcessor().isRaisedArrived())
            processor.arrived();
    }
    
    @Override
    public void dataRefresh() {
        getStateChart().getSCInterface().raiseDataRefresh();
        update();
    }
    
    @Override
    public void newTarget() {
        getStateChart().getSCInterface().raiseNewTarget();
        update();
    }
    
    @Override
    public void newUnloadingTarget() {
    	getStateChart().getSCInterface().raiseNewUnloadingTarget();
    	update();
    }
    
    @Override
    public void newChargingTarget() {
    	getStateChart().getSCInterface().raiseNewChargingTarget();
    	update();
    }

    @Override
    public void actionCompleted() {
        getStateChart().getSCInterface().raiseActionCompleted();
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
                return getStateChart().getSCIPositionType().getWAYPOINT();
            case STATION:
                return getStateChart().getSCIPositionType().getSTATION();
            case CROSSROAD:
                return getStateChart().getSCIPositionType().getCROSSROAD();
            default:
                throw new IllegalArgumentException();
        }
    }

    private long toSCIOrientation(Orientation orient) {
        switch (orient) {
            case NORTH:
                return getStateChart().getSCIOrientation().getNORTH();
            case EAST:
                return getStateChart().getSCIOrientation().getEAST();
            case SOUTH:
                return getStateChart().getSCIOrientation().getSOUTH();
            case WEST:
                return getStateChart().getSCIOrientation().getWEST();
            default:
                throw new IllegalArgumentException();
        }
    }

    private long toSCIDirection(Direction dir) {
        switch (dir) {
            case LEFT:
                return getStateChart().getSCIDirection().getLEFT();
            case AHEAD:
                return getStateChart().getSCIDirection().getAHEAD();
            case RIGHT:
                return getStateChart().getSCIDirection().getRIGHT();
            case BEHIND:
                return getStateChart().getSCIDirection().getBEHIND();
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
    public boolean isActive(DrivesystemStateChart.State state) {
        /*
        * This is not intended by the YAKINDU implementation and source generation. 
        * Officially, the YAKINDU interface does not support this, which is why we have 
        * to cast to the actual DrivesystemStateChart object.
        */
        return ((DrivesystemStateChart) chart).isStateActive(state);
    }

    @Override
    public DrivesystemStateChart.State[] getStates() {
        return DrivesystemStateChart.State.values();
    }
}
