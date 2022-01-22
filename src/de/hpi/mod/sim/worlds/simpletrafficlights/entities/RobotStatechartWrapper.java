package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import com.yakindu.core.IStatemachine;

import de.hpi.mod.sim.statemachines.simpletrafficlights.RobotStatechart;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.worlds.abstract_robots.IRobotActors;

public class RobotStatechartWrapper extends StateChartWrapper<RobotStatechart.State> {

    private IRobotActors actors;
    private IRobotCallback server;
	private IRobotSensorData data;

    public RobotStatechartWrapper(IRobotActors actors, IRobotCallback callback, IRobotSensorData data) {
        this.actors = actors;
        this.server = callback;
        this.data = data;
        start();
    }

    @Override
    public void start() {
        getStatemachine().sensors().setOperationCallback(data);
        getStatemachine().navigation().setOperationCallback(data);
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
        if (getStatemachine().server().isRaisedArrived())
        	server.arrived();
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
        return ((RobotStatechart) chart).isStateActive(state);
    }
    
    public void onRefresh() {
        updateTimer();
    }
    
    public void newTarget() {
        getStatemachine().raiseNewTarget();
    }

    public void actionCompletedAndTrafficLightObserved() {
        getStatemachine().raiseActionCompletedAndTrafficLightObserved();
    }

    public void actionCompleted() {
        getStatemachine().raiseActionCompleted();
    }
}
