package de.hpi.mod.sim.worlds.traffic_light_robots;

import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.trafficlight.ITrafficlightStatemachine;
import de.hpi.mod.sim.trafficlight.ITrafficlightStatemachine.SCInterfaceOperationCallback;
import de.hpi.mod.sim.trafficlight.TrafficlightStatemachine;
import de.hpi.mod.sim.trafficlight.TrafficlightStatemachine.State;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;

import java.awt.Color;
import java.awt.Graphics;

public class TrafficLight extends StateChartWrapper<TrafficlightStatemachine.State> implements StateChartEntity,
        ITrafficlightStatemachine.SCInterfaceOperationCallback {
    /**
     * The northern, eastern, southern and western positions
     */
    private Position[] positions = new Position[4];
    /**
     * The traffic lights are not drawn in the center of the field but with some
     * offset depending on whether it is the northern, eastern, ... part
     */
    private static final int[] X_OFFSETS = { -1, -1, 1, 1 };
    private static final int[] Y_OFFSETS = { -1, 1, 1, -1 };
    private boolean[] lightStates = { false, false, false, false }; // Which positions show green

    /**
     * Creates a traffic light. Each traffic light is responsible for a whole
     * crossroad and therefore has 4 positions.
     * 
     * @param pos The southern position of traffic light. All other positions are
     *            calculated from this one.
     */
    public TrafficLight(Position pos) {
        positions[0] = new Position(pos.getX() - 1, pos.getY() + 3);
        positions[1] = new Position(pos.getX() + 1, pos.getY() + 2);
        positions[2] = pos;
        positions[3] = new Position(pos.getX() - 2, pos.getY() + 1);
        start();
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
        if (getStatemachine().isRaisedSwitchNorth())
            lightStates[0] = !lightStates[0];
        if (getStatemachine().isRaisedSwitchEast())
            lightStates[1] = !lightStates[1];
        if (getStatemachine().isRaisedSwitchSouth())
            lightStates[2] = !lightStates[2];
        if (getStatemachine().isRaisedSwitchWest())
            lightStates[3] = !lightStates[3];
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

    public void render(Graphics graphics, SimulationBlockView panel) {
        for (int i = 0; i < positions.length; i++) {
            Position pos = positions[i];
            Color color = lightStates[i] ? Color.GREEN : Color.RED;
            graphics.setColor(color);
            java.awt.geom.Point2D point = panel.toDrawPosition(pos);
            float blockSize = panel.getBlockSize();
            graphics.fillOval((int) (point.getX() + X_OFFSETS[i] * blockSize / 3),
                    (int) (point.getY() + Y_OFFSETS[i] * blockSize / 3), (int) (blockSize / 6), (int) (blockSize / 6));
        }
    }

    @Override
    public boolean robotNorth() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean robotWest() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean robotSouth() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean robotEast() {
        // TODO Auto-generated method stub
        return false;
    }
}
