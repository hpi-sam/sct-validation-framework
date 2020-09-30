package de.hpi.mod.sim.worlds.traffic_light_robots;

import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.tlrobot.ITlRobotStatemachine;
import de.hpi.mod.sim.tlrobot.TlRobotStatemachine;
import de.hpi.mod.sim.worlds.abstract_robots.IRobotActors;

public class DriveSystem extends StateChartWrapper<TlRobotStatemachine.State> implements ITlRobotStatemachine.SCIDataOperationCallback {

    private IRobotActors actors;
    private IProcessor processor;
    private IRobotData data;

    public DriveSystem(IRobotActors actors, IProcessor processor, IRobotData data) {
        this.actors = actors;
        this.processor = processor;
        this.data = data;
    }

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
            actors.driveForward();
        if (getStatemachine().getSCIActors().isRaisedTurnLeft())
            actors.turnLeft();
        if (getStatemachine().getSCIActors().isRaisedTurnRight())
            actors.turnRight();
        if (getStatemachine().getSCIProcessor().isRaisedArrived())
            processor.arrived();
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
    public long cellType() {
        switch (data.cellType()) {
            case TRAFFIC_LIGHT_GREEN:
                return getStatemachine().getSCICellType().getTRAFFICLIGHT_GREEN();
            case TRAFFIC_LIGHT_RED:
                return getStatemachine().getSCICellType().getTRAFFICLIGHT_GREEN();
            case CROSSROAD:
                return getStatemachine().getSCICellType().getCROSSROAD();
            default:
                return getStatemachine().getSCICellType().getBLOCKED();  
        }
    }

    @Override
    public long targetDirection() {
        switch (data.targetDirection()) {
            case AHEAD:
                return getStatemachine().getSCIDirection().getAHEAD();
            case LEFT:
                return getStatemachine().getSCIDirection().getLEFT();
            case RIGHT:
                return getStatemachine().getSCIDirection().getRIGHT();
            default:
                return getStatemachine().getSCIDirection().getBEHIND();
        }
    }

    @Override
    public boolean isOnTarget() {
        return data.isOnTarget();
    }

    @Override
    public boolean blockedArrivalpointAhead() {
        return data.blockedWaypointAhead();
    }

    @Override
    public boolean blockedArrivalpointLeft() {
        return data.blockedWaypointLeft();
    }

    @Override
    public boolean blockedArrivalpointRight() {
        return data.blockedWaypointRight();
    }
    
    public void onRefresh() {
        getStatemachine().raiseDataRefresh();
        updateTimer();
    }
    
    public void newTarget() {
        getStatemachine().raiseNewTarget();
    }

    public void actionCompleted() {
        getStatemachine().raiseActionCompleted();
    }
}
