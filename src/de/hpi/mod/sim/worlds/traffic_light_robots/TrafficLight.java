package de.hpi.mod.sim.worlds.traffic_light_robots;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.trafficlight.TrafficlightStatemachine;
import de.hpi.mod.sim.trafficlight.TrafficlightStatemachine.State;
import de.hpi.mod.sim.worlds.abstract_grid.Position;

public class TrafficLight extends StateChartWrapper<TrafficlightStatemachine.State>
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
    public TrafficLight(Position pos) {
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

    private TrafficlightStatemachine getStatemachine() {
        return (TrafficlightStatemachine) chart;
    }

    @Override
    public void update() {
        /**
         * Runs a cycle of the statechart and checks if any functions got fired
         */
        if (getStatemachine().getSCINorth().isRaisedOn())
            lightStates[3] = true;
        if (getStatemachine().getSCINorth().isRaisedOff())
            lightStates[3] = false;
        if (getStatemachine().getSCIEast().isRaisedOn())
            lightStates[2] = true;
        if (getStatemachine().getSCIEast().isRaisedOff())
            lightStates[2] = false;
        if (getStatemachine().getSCISouth().isRaisedOn())
            lightStates[0] = true;
        if (getStatemachine().getSCISouth().isRaisedOff())
            lightStates[0] = false;
        if (getStatemachine().getSCIWest().isRaisedOn())
            lightStates[1] = true;
        if (getStatemachine().getSCIWest().isRaisedOff())
            lightStates[1] = false;
    }

    @Override
    public IStatemachine createStateMachine() {
        return new TrafficlightStatemachine();
    }

    @Override
    protected State[] getStates() {
        return TrafficlightStatemachine.State.values();
    }

    @Override
    protected boolean isActive(TrafficlightStatemachine.State state) {
        /*
         * This is not intended by the YAKINDU implementation and source generation.
         * Officially, the YAKINDU interface does not support this, which is why we have
         * to cast to the actual DrivesystemStateChart object.
         */
        return ((TrafficlightStatemachine) chart).isStateActive(state);
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
