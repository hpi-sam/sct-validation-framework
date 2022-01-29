package de.hpi.mod.sim.worlds.simpletraffic.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.yakindu.core.IStatemachine;

import de.hpi.mod.sim.statemachines.simpletraffic.SimpleTrafficLightStatechart;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;

public class TrafficLight extends StateChartWrapper<SimpleTrafficLightStatechart.State>
        implements StateChartEntity, IHighlightable {
    /**
     * The northern, eastern, southern and western positions
     */
	
	private RelativePosition relativePosition;
	private Position basePosition;
    
	// true if a light is green.
    private boolean lightStateWest = false;
    private boolean lightStateNorth = false;
    private boolean lightStateEast = false;
    private boolean lightStateSouth = false; 
    
    // 
    private TrafficLightOperationCallback centerCallback;
    private TrafficLightOperationCallback northCallback;
    private TrafficLightOperationCallback eastCallback;
    private TrafficLightOperationCallback southCallback;
    private TrafficLightOperationCallback westCallback;

    /**
     * Creates a traffic light. Each traffic light is responsible for a whole
     * crossroad and therefore has 4 positions.
     * 
     * @param pos The southern position of traffic light. All other positions are
     *            calculated from this one.
     */
    public TrafficLight(RelativePosition relative, Position absolute, TrafficGridManager manager) {
    	relativePosition = relative;
    	basePosition = absolute;
    	
        centerCallback = new TrafficLightOperationCallback(getCrossroadPositions(), manager);
        northCallback = new TrafficLightOperationCallback(getNorthWaitingPosition(), manager);
        eastCallback = new TrafficLightOperationCallback(getEastWaitingPosition(), manager);
        southCallback = new TrafficLightOperationCallback(getSouthWaitingPosition(), manager);
        westCallback = new TrafficLightOperationCallback(getWestWaitingPosition(), manager);
        
        start();
    }


    @Override
    public void start() {
        getStatemachine().centerSensor().setOperationCallback(centerCallback);
        getStatemachine().northSensor().setOperationCallback(northCallback);
        getStatemachine().eastSensor().setOperationCallback(eastCallback);
        getStatemachine().southSensor().setOperationCallback(southCallback);
        getStatemachine().westSensor().setOperationCallback(westCallback);
        super.start();
    }
    
    public Position getBottomLeftPosition() {
        return basePosition;
    }
    
    @Override
    public String getTopLevelRegionName() {
        return "_simpletrafficlight";
    }

    private SimpleTrafficLightStatechart getStatemachine() {
        return (SimpleTrafficLightStatechart) chart;
    }

    @Override
    public void update() {
        /**
         * Runs a cycle of the statechart and checks if any functions got fired
         */
        if (getStatemachine().northLamp().isRaisedRed())
            lightStateNorth = false;
        if (getStatemachine().northLamp().isRaisedGreen())
        	lightStateNorth = true;

        if (getStatemachine().eastLamp().isRaisedRed())
        	lightStateEast = false;
        if (getStatemachine().eastLamp().isRaisedGreen())
        	lightStateEast = true;

        if (getStatemachine().southLamp().isRaisedRed())
            lightStateSouth = false;
        if (getStatemachine().southLamp().isRaisedGreen())
        	lightStateSouth = true;

        if (getStatemachine().westLamp().isRaisedRed())
        	lightStateWest = false;
        if (getStatemachine().westLamp().isRaisedGreen())
        	lightStateWest = true;
    }

    @Override
    public IStatemachine createStateMachine() {
        return new SimpleTrafficLightStatechart();
    }

    @Override
    protected SimpleTrafficLightStatechart.State[] getStates() {
        return SimpleTrafficLightStatechart.State.values();
    }

    @Override
    protected boolean isActive(SimpleTrafficLightStatechart.State state) {
        /*
         * This is not intended by the YAKINDU implementation and source generation.
         * Officially, the YAKINDU interface does not support this, which is why we have
         * to cast to the actual DrivesystemStateChart object.
         */
        return ((SimpleTrafficLightStatechart) chart).isStateActive(state);
    }
    
    public Position getSouthWaitingPosition() {
    	return getBottomLeftPosition().getModified(1, -1);
    }
    
    public Position getNorthWaitingPosition() {
    	return getBottomLeftPosition().getModified(0, 2);
    }
    
    public Position getWestWaitingPosition() {
    	return getBottomLeftPosition().getModified(-1, 0);
    }
    
    public Position getEastWaitingPosition() {
    	return getBottomLeftPosition().getModified(2, 1);
    }

    public List<Position> getWaitingPositions() {
		return Arrays.asList(getNorthWaitingPosition(), getSouthWaitingPosition(), getEastWaitingPosition(), getWestWaitingPosition());
    }

    public List<Position> getCrossroadPositions() {
    	return  Arrays.asList(getBottomLeftPosition(), getBottomLeftPosition().getModified(0, 1), getBottomLeftPosition().getModified(1, 0), getBottomLeftPosition().getModified(1, 1));
    }

    public boolean isCrossroadPosition(Position position) {
    	return getCrossroadPositions().stream().anyMatch(p -> p.equals(position));
    }
    
    public boolean isWaitingPosition(Position position) {
    	return getWaitingPositions().stream().anyMatch(p -> p.equals(position));
    }

	public List<Position> getRelatedPositions() {
		return Stream.of(getCrossroadPositions(),getWaitingPositions()).flatMap(x -> x.stream()).collect(Collectors.toList());
	}

	public boolean isRelatedPosition(Position p) {
		return isWaitingPosition(p) || isCrossroadPosition(p);
	}

    public TrafficLightState getTrafficLightState(Position p) {
    	if(getSouthWaitingPosition().equals(p))
    		return isGreenSouth() ? TrafficLightState.TRAFFIC_LIGHT_GREEN : TrafficLightState.TRAFFIC_LIGHT_RED;
    	
    	if(getNorthWaitingPosition().equals(p))
    		return isGreenNorth() ? TrafficLightState.TRAFFIC_LIGHT_GREEN : TrafficLightState.TRAFFIC_LIGHT_RED;
    	
    	if(getEastWaitingPosition().equals(p))
    		return isGreenEast() ? TrafficLightState.TRAFFIC_LIGHT_GREEN : TrafficLightState.TRAFFIC_LIGHT_RED;
    	
    	if(getWestWaitingPosition().equals(p))
    		return isGreenWest() ? TrafficLightState.TRAFFIC_LIGHT_GREEN : TrafficLightState.TRAFFIC_LIGHT_RED;
    	
    	return TrafficLightState.NO_TRAFFIC_LIGHT;
    }
    
    public boolean isGreenNorth() {
        return lightStateNorth;
    }

    public boolean isGreenEast() {
        return lightStateEast;
    }

    public boolean isGreenWest() {
        return lightStateWest;
    }

    public boolean isGreenSouth() {
        return lightStateSouth;
    }

    @Override
    public List<String> getHighlightInfo() {
        List<String> infos = new ArrayList<>();
        infos.add("Index: " + getRelativePosition().stringify());
        infos.add("Position (bottom left): " + getBottomLeftPosition().stringify());
        infos.add("South: " + (isGreenSouth() ? "GREEN" : "red"));
        infos.add("West: " + (isGreenWest() ? "GREEN" : "red"));
        infos.add("East: " + (isGreenEast() ? "GREEN" : "red"));
        infos.add("North: " + (isGreenNorth() ? "GREEN" : "red"));
        return infos;
    }

	public RelativePosition getRelativePosition() {
		return relativePosition;
	}

}
