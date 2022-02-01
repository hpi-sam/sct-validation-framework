package de.hpi.mod.sim.worlds.simpletraffic.entities;

import com.yakindu.core.IStatemachine;

import de.hpi.mod.sim.statemachines.simpletraffic.SimpleTrafficRobotStatechart;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.worlds.abstract_robots.IRobotActors;

public class SimpleTrafficRobotStatechartWrapper extends StateChartWrapper<SimpleTrafficRobotStatechart.State> {

    private IRobotActors actors;
    private IRobotCallback server;
	private IRobotSensorData data;

    public SimpleTrafficRobotStatechartWrapper(IRobotActors actors, IRobotCallback callback, IRobotSensorData data) {
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

    private SimpleTrafficRobotStatechart getStatemachine() {
		return (SimpleTrafficRobotStatechart) chart;
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
        if (getStatemachine().communication().isRaisedArrived())
        	server.arrived();
    }

    @Override
    public IStatemachine createStateMachine() {
        return new SimpleTrafficRobotStatechart();
    }

    @Override
    protected SimpleTrafficRobotStatechart.State[] getStates() {
        return SimpleTrafficRobotStatechart.State.values();
    }

    @Override
    protected boolean isActive(SimpleTrafficRobotStatechart.State state) {
        /*
         * This is not intended by the YAKINDU implementation and source generation.
         * Officially, the YAKINDU interface does not support this, which is why we have
         * to cast to the actual DrivesystemStateChart object.
         */
        return ((SimpleTrafficRobotStatechart) chart).isStateActive(state);
    }
    
    public void onRefresh() {
        updateTimer();
    }
    
    public void newTarget() {
        getStatemachine().raiseNewTarget();
    }

    public void actionCompletedAndTrafficLightAhead() {
        getStatemachine().raiseActionCompletedAndTrafficLightAhead();
    }

    public void actionCompleted() {
        getStatemachine().raiseActionCompleted();
    }
}
