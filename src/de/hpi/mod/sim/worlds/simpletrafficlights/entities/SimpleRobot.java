package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsConfiguration;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;

public class SimpleRobot extends Robot implements IRobotCallback, IRobotSensorData, StateChartEntity {

    private RobotStatechartWrapper control;

    public SimpleRobot(int robotID, StreetNetworkManager gridManager, Position startPosition, Orientation startFacing,
            Position destination) {
    	
    	// Initialze robot itself (via parent)
        super(robotID, gridManager, startPosition, startFacing);
        
        // Initialize statechart
        control = new RobotStatechartWrapper(getDriveManager(), this, this);
        
        if(destination != null) {
        	// If a specific target is given, send it to statechart and notify statechart right away.
            setTarget(destination);
            control.newTarget();	
        }
    }

    @Override
    public void arrived() {
    	System.out.println("ARRIVED");
    	// #TODO: Link this to arrival points. Make sure false arrivals are caught.
        super.arrived();
        moveToIdlePosition();
        getCrossRoadsManager().makeRobotIdle(this);
    }

    /**
     * Handles state changes and refreshes the State-Machine
     */
    @Override
    public void onRefresh() {
        control.onRefresh();
    }

    private StreetNetworkManager getCrossRoadsManager() {
        return (StreetNetworkManager) grid;
    }

	public void setTargetAndNotify(Position position) {
		this.setTarget(position);
		this.control.newTarget();
	}
	
	public void moveToIdlePosition() {
	    setRobotTo(SimpleTrafficLightsConfiguration.getIdleRobotsPosition());
	    setTarget(SimpleTrafficLightsConfiguration.getIdleRobotsPosition());
	}

    @Override
    public String getMachineState() {
        return control.getChartState();
    }
    

    @Override
    public String getTopStateName() {
        return "simpleTrafficLightRobot";
    }

    @Override
    public void actionCompleted() {
        if(getCrossRoadsManager().isInFrontOfTrafficLight(pos())) {
        	control.actionCompletedAndTrafficLightObserved();
        }else {
        	control.actionCompleted();
        }
    }

	@Override
	public boolean isObstacleAhead() {
		return getCrossRoadsManager().isBlocked(Position.nextPositionInOrientation(posOrientation(), pos()));
	}

	@Override
	public boolean trafficLightIsGreen() {
		boolean value = getCrossRoadsManager().queryTrafficLight(pos()) == TrafficLightState.TRAFFIC_LIGHT_GREEN;
    	return value;
	}

	@Override
	public boolean trafficLightIsRed() {
		boolean value = getCrossRoadsManager().queryTrafficLight(pos()) == TrafficLightState.TRAFFIC_LIGHT_RED;
    	return value;
	}

	@Override
	public boolean isTargetAhead() {
		return getCrossRoadsManager().getTargetDirections(pos()).contains(Direction.AHEAD);
	}

	@Override
	public boolean isTargetLeft() {
		return getCrossRoadsManager().getTargetDirections(pos()).contains(Direction.LEFT);
	}

	@Override
	public boolean isTargetRight() {
		return getCrossRoadsManager().getTargetDirections(pos()).contains(Direction.RIGHT);
	}

	@Override
	public boolean isTargetBehind() {
		return getCrossRoadsManager().getTargetDirections(pos()).contains(Direction.BEHIND);
	}

}
