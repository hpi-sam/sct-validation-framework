package de.hpi.mod.sim.env.robot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.ITimer;
import de.hpi.mod.sim.drivesystem.DrivesystemStatemachine;
import de.hpi.mod.sim.drivesystem.IDrivesystemStatemachine;
import de.hpi.mod.sim.env.model.*;

/**
 * Handles calls to the statechard.
 * This should be the only file with logic depending on the statechard implementation.
 */
public class DriveSystemWrapper implements IDrivesystemStatemachine.SCIDataOperationCallback, IDrivesystemStatemachine.SCIRawDataOperationCallback, IDriveSystem {

    /**
     * The generated Statemachine
     */
    private IDrivesystemStatemachine machine;

    private ISensor data;
    private IRobotActors actors;
    private IProcessor processor;

    private RobotTimerService timer = null;


    public DriveSystemWrapper(ISensor data, IRobotActors actors, IProcessor processor) {
        DrivesystemStatemachine machine = new DrivesystemStatemachine();
        Method methodToFind = null;
        timer = new RobotTimerService();
        try {
        	  methodToFind = DrivesystemStatemachine.class.getMethod("setTimer", new Class[] {ITimer.class});
        	} catch (NoSuchMethodException | SecurityException e) {
        	}
        if(methodToFind != null) {
        	try {
				methodToFind.invoke(machine, new Object[] {timer});
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
        }
        this.data = data;
        this.actors = actors;
        this.processor = processor;
        this.machine = machine;
        this.machine.getSCIData().setSCIDataOperationCallback(this);
        this.machine.getSCIRawData().setSCIRawDataOperationCallback(this);
        this.machine.init();
        this.machine.enter();
    }

    /**
     * Does contain {@link IDrivesystemStatemachine} so tests can set mockups
     */
    public DriveSystemWrapper(IDrivesystemStatemachine machine, ISensor data, IRobotActors actors, IProcessor processor) {
        this.data = data;
        this.actors = actors;
        this.processor = processor;
        this.machine = machine;
        this.machine.getSCIData().setSCIDataOperationCallback(this);
        this.machine.getSCIRawData().setSCIRawDataOperationCallback(this);
        this.machine.init();
        this.machine.enter();
    }

    /**
     * Runs a cycle of the statemachine and checks if any functions got fired
     */
    private void update() {
        if (machine.getSCIActors().isRaisedDriveForward())
            actors.driveForward();
        if (machine.getSCIActors().isRaisedDriveBackward())
        	actors.driveBackward();
        if (machine.getSCIActors().isRaisedStartUnload())
            actors.startUnloading();
        if (machine.getSCIActors().isRaisedTurnLeft())
            actors.turnLeft();
        if (machine.getSCIActors().isRaisedTurnRight())
            actors.turnRight();
        if (machine.getSCIProcessor().isRaisedArrived())
            processor.arrived();
    }

    @Override
    public void getUpdate() {
    	update();
    }
    
    @Override
    public void dataRefresh() {
        machine.getSCInterface().raiseDataRefresh();
        update();
    }

    @Override
    public void unload() {
        machine.getSCInterface().raiseUnload();
        update();
    }

    @Override
    public void newTarget() {
        machine.getSCInterface().raiseNewTarget();
        update();
    }
    
    @Override
    public void newUnloadingTarget() {
    	machine.getSCInterface().raiseNewUnloadingTarget();
    	update();
    }
    
    @Override
    public void newChargingTarget() {
    	machine.getSCInterface().raiseNewChargingTarget();
    	update();
    }

    @Override
    public void actionCompleted() {
        machine.getSCInterface().raiseActionCompleted();
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
                return machine.getSCIPositionType().getWAYPOINT();
            case STATION:
                return machine.getSCIPositionType().getSTATION();
            case CROSSROAD:
                return machine.getSCIPositionType().getCROSSROAD();
            default:
                throw new IllegalArgumentException();
        }
    }

    private long toSCIOrientation(Orientation orient) {
        switch (orient) {
            case NORTH:
                return machine.getSCIOrientation().getNORTH();
            case EAST:
                return machine.getSCIOrientation().getEAST();
            case SOUTH:
                return machine.getSCIOrientation().getSOUTH();
            case WEST:
                return machine.getSCIOrientation().getWEST();
            default:
                throw new IllegalArgumentException();
        }
    }

    private long toSCIDirection(Direction dir) {
        switch (dir) {
            case LEFT:
                return machine.getSCIDirection().getLEFT();
            case AHEAD:
                return machine.getSCIDirection().getAHEAD();
            case RIGHT:
                return machine.getSCIDirection().getRIGHT();
            case BEHIND:
                return machine.getSCIDirection().getBEHIND();
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
    public void close() {
        timer.cancel();
    }
    
    @Override
    public String getMachineState() {
    	try {
	    	List<String> activeStates = new ArrayList<>();
	    	for(DrivesystemStatemachine.State state : DrivesystemStatemachine.State.values()) {
	    		/*
	    		* This is not intended by the YAKINDU implementation and source generation. 
	    		* Officially, the YAKINDU interface does not support this, which is why we have 
	    		* to cast to the actual DrivesystemStatemachine object.
	    		*/
	    		if(((DrivesystemStatemachine) machine).isStateActive(state)) {
	    			activeStates.add(state.toString());
	    		}
	    		
	    		
	    	}
    		return activeStates.get(activeStates.size() - 1);
    	} catch (Exception e) { //avoid problems with version changes
    		return "";
    	}
    }

	@Override
	public boolean canChargeAtTarget() {
		return data.canChargeAtTarget();
	}

	public void updateTimer() {
		if(this.timer != null) {
			this.timer.update();
			update();
		}
	}
}
