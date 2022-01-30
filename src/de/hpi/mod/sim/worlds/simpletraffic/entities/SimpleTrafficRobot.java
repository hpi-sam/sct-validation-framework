package de.hpi.mod.sim.worlds.simpletraffic.entities;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;

public class SimpleTrafficRobot extends Robot implements IRobotCallback, IRobotSensorData, StateChartEntity {

    private SimpleTrafficRobotStatechartWrapper control;
	private boolean arrivedFlag;

    public SimpleTrafficRobot(int robotID, TrafficGridManager gridManager, Position startPosition, Orientation startFacing,
            Position destination) {
    	
    	// Initialze robot itself (via parent)
        super(robotID, gridManager, startPosition, startFacing);
        
        // Initialize statechart
        control = new SimpleTrafficRobotStatechartWrapper(getDriveManager(), this, this);
        
        if(destination != null) {
        	// If a specific target is given, send it to statechart and notify statechart right away.
            setTarget(destination);
            control.newTarget();	
        }
    }

    /**
     * Handles state changes and refreshes the State-Machine
     */
    @Override
    public void onRefresh() {
        control.onRefresh();
    }

    public TrafficGridManager getCrossRoadsManager() {
        return (TrafficGridManager) grid;
    }

	public void setTargetAndNotify(Position position) {
		this.setTarget(position);
		this.getDriveManager().resetBattery();
        setArrivedEventWasCalled(false);
		this.control.newTarget();
	}
	
	public void moveToIdlePosition() {
	    setRobotTo(SimpleTrafficWorldConfiguration.getIdleRobotsPosition());
	    setTarget(SimpleTrafficWorldConfiguration.getIdleRobotsPosition());
	}
	
	@Override
	public void arrived() {
		super.arrived();
		this.arrivedFlag = true;
	}
	
	public void resetArrived() {
		this.arrivedFlag = false;
	}
    
    @Override
    public boolean hasPassedAllTestCriteria() {
        return false;
    }
	
	public boolean hasReportedArrive() {
		return arrivedFlag;
	}

    @Override
    public String getActiveState() {
        return control.getActiveState();
    }

    @Override
    public List<String> getActiveStates() {
        return control.getActiveStates();
    }

    @Override
    public String getTopLevelRegionName() {
        return "_simpletrafficrobot";
    }
    
    @Override
    public void actionCompleted() {
        if(getCrossRoadsManager().isInFrontOfTrafficLight(pos()) && pos().equals(oldPos())) {
        	control.actionCompletedAndTrafficLightAhead();
        }else {
        	control.actionCompleted();
        }
    }

	@Override
	public boolean isObstacleAhead() {
		return getCrossRoadsManager().isBlocked(Position.nextPositionInOrientation(posOrientation(), pos()));
	}

	@Override
	public boolean isTrafficLightAhead() {
		return getCrossRoadsManager().queryTrafficLight(pos()) != TrafficLightState.NO_TRAFFIC_LIGHT;
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
		return getCrossRoadsManager().getTargetDirections(pos(), facing(), getTarget()).contains(Direction.AHEAD);
	}

	@Override
	public boolean isTargetLeft() {
		return getCrossRoadsManager().getTargetDirections(pos(), facing(), getTarget()).contains(Direction.LEFT);
	}

	@Override
	public boolean isTargetRight() {
		return getCrossRoadsManager().getTargetDirections(pos(), facing(), getTarget()).contains(Direction.RIGHT);
	}

	@Override
	public boolean isTargetBehind() {
		return getCrossRoadsManager().getTargetDirections(pos(), facing(), getTarget()).contains(Direction.BEHIND);
	}

    @Override
    public List<String> getHighlightInfo() {
        List<String> infos = new ArrayList<>();
        infos.add("Position: " + pos().stringify());
        infos.add("Target: " + getTarget().stringify());
        infos.add("Facing: " + posOrientation().toString());
        return infos;
    }
    
}
