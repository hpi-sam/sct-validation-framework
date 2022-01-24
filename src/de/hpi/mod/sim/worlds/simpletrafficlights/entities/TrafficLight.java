package de.hpi.mod.sim.worlds.simpletrafficlights.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.yakindu.core.IStatemachine;

import de.hpi.mod.sim.statemachines.simpletrafficlights.TrafficLightStatechart;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.worlds.abstract_grid.Position;

public class TrafficLight extends StateChartWrapper<TrafficLightStatechart.State>
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

    /**
     * Creates a traffic light. Each traffic light is responsible for a whole
     * crossroad and therefore has 4 positions.
     * 
     * @param pos The southern position of traffic light. All other positions are
     *            calculated from this one.
     */
    public TrafficLight(RelativePosition relative, Position absolute) {
    	relativePosition = relative;
    	basePosition = absolute;
        start();
    }

    public Position getBottomLeftPosition() {
        return basePosition;
    }

    @Override
    public String getMachineState() {
        return getChartState();
    }

    @Override
    public String getTopStateName() {
        return "trafficlight";
    }

    private TrafficLightStatechart getStatemachine() {
        return (TrafficLightStatechart) chart;
    }

    @Override
    public void update() {
        /**
         * Runs a cycle of the statechart and checks if any functions got fired
         */
        if (getStatemachine().north().isRaisedGreen())
        	lightStateNorth = true;
        if (getStatemachine().north().isRaisedRed())
            lightStateNorth = false;
        
        if (getStatemachine().east().isRaisedGreen())
        	lightStateEast = true;
        if (getStatemachine().east().isRaisedRed())
        	lightStateEast = false;
        
        if (getStatemachine().south().isRaisedGreen())
        	lightStateSouth = true;
        if (getStatemachine().south().isRaisedRed())
            lightStateSouth = false;
        
        if (getStatemachine().west().isRaisedGreen())
        	lightStateWest = true;
        if (getStatemachine().west().isRaisedRed())
        	lightStateWest = false;
    }

    @Override
    public IStatemachine createStateMachine() {
        return new TrafficLightStatechart();
    }

    @Override
    protected TrafficLightStatechart.State[] getStates() {
        return TrafficLightStatechart.State.values();
    }

    @Override
    protected boolean isActive(TrafficLightStatechart.State state) {
        /*
         * This is not intended by the YAKINDU implementation and source generation.
         * Officially, the YAKINDU interface does not support this, which is why we have
         * to cast to the actual DrivesystemStateChart object.
         */
        return ((TrafficLightStatechart) chart).isStateActive(state);
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
        infos.add("Crossroad: " + new Position(basePosition.getX() / 3, basePosition.getY() / 3));
        infos.add("South: " + (isGreenSouth() ? "green" : "red"));
        infos.add("West: " + (isGreenWest() ? "green" : "red"));
        infos.add("East: " + (isGreenEast() ? "green" : "red"));
        infos.add("North: " + (isGreenNorth() ? "green" : "red"));
        return infos;
    }

	public RelativePosition getRelativePosition() {
		return relativePosition;
	}

}
