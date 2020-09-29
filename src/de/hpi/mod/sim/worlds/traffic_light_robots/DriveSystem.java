package de.hpi.mod.sim.worlds.traffic_light_robots;

import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.drivesystem.IDrivesystemStatemachine;
import de.hpi.mod.sim.tlrobot.ITlRobotStatemachine;
import de.hpi.mod.sim.tlrobot.TlRobotStatemachine;

public class DriveSystem extends StateChartWrapper<TlRobotStatemachine.State> implements ITlRobotStatemachine.SCIDataOperationCallback, ITlRobotStatemachine. {

    @Override
    public void start() {
        getStatemachine().getSCIData().setSCIDataOperationCallback(this);
        super.start();
    }

    private TlRobotStatemachine getStatemachine() {
		return (TlRobotStatemachine) chart;
	}

	@Override
    public void update() {
        /**
         * Runs a cycle of the statechart and checks if any functions got fired
         */
        if (getStatemachine().getSCIActors().isRaisedDriveForward())
            // TODO
        if (getStatemachine().getSCIActors().isRaisedTurnLeft())
            // TODO
        if (getStatemachine().getSCIActors().isRaisedTurnRight())
            // TODO
        if (getStatemachine().getSCIProcessor().isRaisedArrived())
            // TODO
    }

    @Override
    public IStatemachine createStateMachine() {
        return new TlRobotStatemachine();
    }

    @Override
    protected TlRobotStatemachine.State[] getStates() {
        return TlRobotStatemachine.State.values();
    }

    @Override
    protected boolean isActive(TlRobotStatemachine.State state) {
        /*
         * This is not intended by the YAKINDU implementation and source generation.
         * Officially, the YAKINDU interface does not support this, which is why we have
         * to cast to the actual DrivesystemStateChart object.
         */
        return ((TlRobotStatemachine) chart).isStateActive(state);}

    @Override
    public long posType() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long targetDirection() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isOnTarget() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean blockedArrivalpointAhead() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean blockedArrivalpointLeft() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean blockedArrivalpointRight() {
        // TODO Auto-generated method stub
        return false;
    }
    
    public void dataRefresh() {
        getStatemachine().raiseDataRefresh();
    }
    
    public void newTarget() {
        getStatemachine().raiseNewTarget();
    }

    public void actionCompleted() {
        getStatemachine().raiseActionCompleted();
    }
}
