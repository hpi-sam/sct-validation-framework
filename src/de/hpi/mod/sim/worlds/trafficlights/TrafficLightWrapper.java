package de.hpi.mod.sim.worlds.trafficlights;

import java.util.ArrayList;
import java.util.List;

import com.yakindu.core.IStatemachine;

import de.hpi.mod.sim.statecharts.trafficlights.TrafficLight;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.worlds.abstract_grid.Position;

public class TrafficLightWrapper extends StateChartWrapper<TrafficLight.State>
        implements StateChartEntity, IHighlightable {
    /**
     * The northern, eastern, southern and western positions
     */
    private Position[] positions = new Position[4];
    private boolean[] lightStates = { false, false, false, false }; // Which positions show green. Order: South, West,
                                                                    // East, North (by y-value)

    /**
     * Creates a traffic light. Each traffic light is responsible for a whole
     * crossroad and therefore has 4 positions.
     * 
     * @param pos The southern position of traffic light. All other positions are
     *            calculated from this one.
     */
    public TrafficLightWrapper(Position pos) {
        positions[0] = pos;
        positions[1] = new Position(pos.getX() - 2, pos.getY() + 1);
        positions[2] = new Position(pos.getX() + 1, pos.getY() + 2);
        positions[3] = new Position(pos.getX() - 1, pos.getY() + 3);
        start();
    }

    public Position getSouthPosition() {
        return positions[0];
    }

    @Override
    public String getMachineState() {
        return getChartState();
    }

    @Override
    public String getTopStateName() {
        return "trafficlight";
    }

    private TrafficLight getStatemachine() {
        return (TrafficLight) chart;
    }

    @Override
    public void update() {
        /**
         * Runs a cycle of the statechart and checks if any functions got fired
         */
        if (getStatemachine().north().isRaisedOn())
            lightStates[3] = true;
        if (getStatemachine().north().isRaisedOff())
            lightStates[3] = false;
        if (getStatemachine().east().isRaisedOn())
            lightStates[2] = true;
        if (getStatemachine().east().isRaisedOff())
            lightStates[2] = false;
        if (getStatemachine().south().isRaisedOn())
            lightStates[0] = true;
        if (getStatemachine().south().isRaisedOff())
            lightStates[0] = false;
        if (getStatemachine().west().isRaisedOn())
            lightStates[1] = true;
        if (getStatemachine().west().isRaisedOff())
            lightStates[1] = false;
    }

    @Override
    public IStatemachine createStateMachine() {
        return new TrafficLight();
    }

    @Override
    protected TrafficLight.State[] getStates() {
        return TrafficLight.State.values();
    }

    @Override
    protected boolean isActive(TrafficLight.State state) {
        /*
         * This is not intended by the YAKINDU implementation and source generation.
         * Officially, the YAKINDU interface does not support this, which is why we have
         * to cast to the actual DrivesystemStateChart object.
         */
        return ((TrafficLight) chart).isStateActive(state);
    }

    public boolean isGreenNorth() {
        return lightStates[3];
    }

    public boolean isGreenEast() {
        return lightStates[2];
    }

    public boolean isGreenWest() {
        return lightStates[1];
    }

    public boolean isGreenSouth() {
        return lightStates[0];
    }

    @Override
    public List<String> getHighlightInfo() {
        List<String> infos = new ArrayList<>();
        infos.add("Crossroad: " + new Position(positions[0].getX() / 3, positions[0].getY() / 3));
        infos.add("South: " + (isGreenSouth() ? "green" : "red"));
        infos.add("West: " + (isGreenWest() ? "green" : "red"));
        infos.add("East: " + (isGreenEast() ? "green" : "red"));
        infos.add("North: " + (isGreenNorth() ? "green" : "red"));
        return infos;
    }

}
