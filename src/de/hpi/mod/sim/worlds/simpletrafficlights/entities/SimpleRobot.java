package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager.TrafficLightState;

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
        super.arrived();
        Position pos = getCrossRoadsManager().getRandomStart();
        setRobotTo(pos);
        setFacingTo(StreetNetworkManager.getSuitableRobotOrientationForPosition(pos));
        setTarget(StreetNetworkManager.getRandomDestination());
        control.newTarget();
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
        super.actionCompleted();
        control.actionCompleted();
    }

	@Override
	public boolean isObstacleAhead() {
		return getCrossRoadsManager().isBlocked(Position.nextPositionInOrientation(posOrientation(), pos()));
	}

	@Override
	public boolean trafficLightIsGreen() {
		return getCrossRoadsManager().queryTrafficLight(pos()) == TrafficLightState.TRAFFIC_LIGHT_GREEN;
	}

	@Override
	public boolean trafficLightIsRed() {
		return getCrossRoadsManager().queryTrafficLight(pos()) == TrafficLightState.TRAFFIC_LIGHT_RED;
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
