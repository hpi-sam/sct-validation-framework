<<<<<<< HEAD
package de.hpi.mod.sim.worlds.infinitewarehouse.robot;

import com.yakindu.core.IStatemachine;

import de.hpi.mod.sim.core.statechart.StateChartWrapper;

import de.hpi.mod.sim.Drivesystem;

import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_robots.IRobotActors;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.PositionType;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IDriveSystem;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IProcessor;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.ISensor;

/**
 * Handles calls to the statechard.
 * This should be the only file with logic depending on the statechard implementation.
 */
public class DriveSystemWrapper extends StateChartWrapper<Drivesystem.State> implements Drivesystem.Data.OperationCallback, Drivesystem.RawData.OperationCallback {

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
    public DriveSystemWrapper(IStatemachine chart, ISensor data, IRobotActors actors, IProcessor processor) {
        this.data = data;
        this.actors = actors;
        this.processor = processor;
        this.chart = chart;
        start();
    }

    @Override
    public IStatemachine createStateMachine() {
        return new Drivesystem();
    }

    @Override
    public void start() {
        getStatemachine().data().setOperationCallback(this);
        getStatemachine().rawData().setOperationCallback(this);
        super.start();
    }

    private Drivesystem getStatemachine() {
        return (Drivesystem) chart;
    }

    /**
     * Runs a cycle of the statechart and checks if any functions got fired
     */
    @Override
    public void update() {
        if (getStatemachine().actors().isRaisedDriveForward())
            actors.driveForward();
        if (getStatemachine().actors().isRaisedDriveBackward())
        	actors.driveBackward();
        if (getStatemachine().actors().isRaisedStartUnload())
            actors.startUnloading();
        if (getStatemachine().actors().isRaisedTurnLeft())
            actors.turnLeft();
        if (getStatemachine().actors().isRaisedTurnRight())
            actors.turnRight();
        if (getStatemachine().processor().isRaisedArrived())
            processor.arrived();
    }
    
    public void dataRefresh() {
        getStatemachine().raiseDataRefresh();
        update();
    }
    
    public void newTarget() {
        getStatemachine().raiseNewTarget();
        update();
    }
    
    public void newUnloadingTarget() {
    	getStatemachine().raiseNewUnloadingTarget();
    	update();
    }
    
    public void newChargingTarget() {
    	getStatemachine().raiseNewChargingTarget();
    	update();
    }

    public void actionCompleted() {
        getStatemachine().raiseActionCompleted();
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
                return getStatemachine().positionType().getWAYPOINT();
            case STATION:
                return getStatemachine().positionType().getSTATION();
            case CROSSROAD:
                return getStatemachine().positionType().getCROSSROAD();
            default:
                throw new IllegalArgumentException();
        }
    }

    private long toSCIOrientation(Orientation orient) {
        switch (orient) {
            case NORTH:
                return getStatemachine().orientation().getNORTH();
            case EAST:
                return getStatemachine().orientation().getEAST();
            case SOUTH:
                return getStatemachine().orientation().getSOUTH();
            case WEST:
                return getStatemachine().orientation().getWEST();
            default:
                throw new IllegalArgumentException();
        }
    }

    private long toSCIDirection(Direction dir) {
        switch (dir) {
            case LEFT:
                return getStatemachine().direction().getLEFT();
            case AHEAD:
                return getStatemachine().direction().getAHEAD();
            case RIGHT:
                return getStatemachine().direction().getRIGHT();
            case BEHIND:
                return getStatemachine().direction().getBEHIND();
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
    public boolean isActive(Drivesystem.State state) {
        /*
        * This is not intended by the YAKINDU implementation and source generation. 
        * Officially, the YAKINDU interface does not support this, which is why we have 
        * to cast to the actual DrivesystemStateChart object.
        */
        return ((Drivesystem) chart).isStateActive(state);
    }

    @Override
    public Drivesystem.State[] getStates() {
        return Drivesystem.State.values();
    }

}
=======
package de.hpi.mod.sim.worlds.infinitewarehouse.robot;

import com.yakindu.core.IStatemachine;

import de.hpi.mod.sim.core.statechart.StateChartWrapper;

import de.hpi.mod.sim.Drivesystem;

import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_robots.IRobotActors;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.PositionType;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IDriveSystem;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IProcessor;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.ISensor;

/**
 * Handles calls to the statechard.
 * This should be the only file with logic depending on the statechard implementation.
 */
public class DriveSystemWrapper extends StateChartWrapper<Drivesystem.State> implements Drivesystem.Data.OperationCallback, Drivesystem.RawData.OperationCallback {

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
    public DriveSystemWrapper(IStatemachine chart, ISensor data, IRobotActors actors, IProcessor processor) {
        this.data = data;
        this.actors = actors;
        this.processor = processor;
        this.chart = chart;
        start();
    }

    @Override
    public IStatemachine createStateMachine() {
        return new Drivesystem();
    }

    @Override
    public void start() {
        getStatemachine().data().setOperationCallback(this);
        getStatemachine().rawData().setOperationCallback(this);
        super.start();
    }

    private Drivesystem getStatemachine() {
        return (Drivesystem) chart;
    }

    /**
     * Runs a cycle of the statechart and checks if any functions got fired
     */
    @Override
    public void update() {
        if (getStatemachine().actors().isRaisedDriveForward())
            actors.driveForward();
        if (getStatemachine().actors().isRaisedDriveBackward())
        	actors.driveBackward();
        if (getStatemachine().actors().isRaisedStartUnload())
            actors.startUnloading();
        if (getStatemachine().actors().isRaisedTurnLeft())
            actors.turnLeft();
        if (getStatemachine().actors().isRaisedTurnRight())
            actors.turnRight();
        if (getStatemachine().processor().isRaisedArrived())
            processor.arrived();
    }
    
    public void dataRefresh() {
        getStatemachine().raiseDataRefresh();
        update();
    }
    
    public void newTarget() {
        getStatemachine().raiseNewTarget();
        update();
    }
    
    public void newUnloadingTarget() {
    	getStatemachine().raiseNewUnloadingTarget();
    	update();
    }
    
    public void newChargingTarget() {
    	getStatemachine().raiseNewChargingTarget();
    	update();
    }

    public void actionCompleted() {
        getStatemachine().raiseActionCompleted();
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
                return getStatemachine().positionType().getWAYPOINT();
            case STATION:
                return getStatemachine().positionType().getSTATION();
            case CROSSROAD:
                return getStatemachine().positionType().getCROSSROAD();
            default:
                throw new IllegalArgumentException();
        }
    }

    private long toSCIOrientation(Orientation orient) {
        switch (orient) {
            case NORTH:
                return getStatemachine().orientation().getNORTH();
            case EAST:
                return getStatemachine().orientation().getEAST();
            case SOUTH:
                return getStatemachine().orientation().getSOUTH();
            case WEST:
                return getStatemachine().orientation().getWEST();
            default:
                throw new IllegalArgumentException();
        }
    }

    private long toSCIDirection(Direction dir) {
        switch (dir) {
            case LEFT:
                return getStatemachine().direction().getLEFT();
            case AHEAD:
                return getStatemachine().direction().getAHEAD();
            case RIGHT:
                return getStatemachine().direction().getRIGHT();
            case BEHIND:
                return getStatemachine().direction().getBEHIND();
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
    public boolean isActive(Drivesystem.State state) {
        /*
        * This is not intended by the YAKINDU implementation and source generation. 
        * Officially, the YAKINDU interface does not support this, which is why we have 
        * to cast to the actual DrivesystemStateChart object.
        */
        return ((Drivesystem) chart).isStateActive(state);
    }

    @Override
    public Drivesystem.State[] getStates() {
        return Drivesystem.State.values();
    }

}
>>>>>>> 308c2f6b59a427da313ecd842c5fe5e5918e5f8c
