package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import com.yakindu.core.IStatemachine;

import de.hpi.mod.sim.statemachines.simpletrafficlights.RobotStatechart;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.worlds.abstract_robots.IRobotActors;

public class RobotStatechartWrapper extends StateChartWrapper<RobotStatechart.State> implements RobotStatechart.Data.OperationCallback {

    private IRobotActors actors;
    private IProcessor processor;
    private IRobotData data;

    public RobotStatechartWrapper(IRobotActors actors, IProcessor processor, IRobotData data) {
        this.actors = actors;
        this.processor = processor;
        this.data = data;
        start();
    }

    @Override
    public void start() {
        getStatemachine().data().setOperationCallback(this);
        super.start();
    }

    private RobotStatechart getStatemachine() {
		return (RobotStatechart) chart;
	}

	@Override
    public void update() {
        /**
         * Runs a cycle of the statechart and checks if any functions got fired
         */
        if (getStatemachine().actors().isRaisedDriveForward())
            actors.driveForward();
        if (getStatemachine().actors().isRaisedTurnLeft())
            actors.turnLeft();
        if (getStatemachine().actors().isRaisedTurnRight())
            actors.turnRight();
        if (getStatemachine().processor().isRaisedArrived())
            processor.arrived();
    }

    @Override
    public IStatemachine createStateMachine() {
        return new RobotStatechart();
    }

    @Override
    protected RobotStatechart.State[] getStates() {
        return RobotStatechart.State.values();
    }

    @Override
    protected boolean isActive(RobotStatechart.State state) {
        /*
         * This is not intended by the YAKINDU implementation and source generation.
         * Officially, the YAKINDU interface does not support this, which is why we have
         * to cast to the actual DrivesystemStateChart object.
         */
        return ((RobotStatechart) chart).isStateActive(state);}

    @Override
    public long cellType() {
        switch (data.cellType()) {
            case CROSSROAD_WAITING_POINT:
                return getStatemachine().cellType().getTRAFFICLIGHT_GREEN();
//            case CROSSROAD_WAITING_POINT:
//                return getStatemachine().cellType().getTRAFFICLIGHT_RED();
            case CROSSROAD:
                return getStatemachine().cellType().getCROSSROAD();
            default:
                return getStatemachine().cellType().getBLOCKED();  
        }
    }

    @Override
    public long targetDirection() {
        switch (data.targetDirection()) {
            case AHEAD:
                return getStatemachine().direction().getAHEAD();
            case LEFT:
                return getStatemachine().direction().getLEFT();
            case RIGHT:
                return getStatemachine().direction().getRIGHT();
            default:
                return getStatemachine().direction().getBEHIND();
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
        updateTimer();
    }
    
    public void newTarget() {
        getStatemachine().raiseNewTarget();
    }

    public void actionCompleted() {
        getStatemachine().raiseActionCompleted();
    }
}
