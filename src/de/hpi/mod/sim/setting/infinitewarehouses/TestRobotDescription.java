package de.hpi.mod.sim.setting.infinitewarehouses;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.setting.Position;
import de.hpi.mod.sim.setting.robot.Robot;
import de.hpi.mod.sim.setting.robot.Robot.RobotState;

public class TestRobotDescription extends RobotDescription {
    private Position position;
    private RobotState state;
    private List<Position> targets = new ArrayList<Position>();
    private Orientation facing;
    private int robotSpecificDelay = 0;
    private int initialDelay = 0;
    private List<Position> targetsCopy = new ArrayList<Position>();
    private boolean fuzzyEnd = false;
    private boolean requireUnload = false;
    private boolean hasReservedBattery = false;
    private boolean hardArrivedConstraint = false;

    public void setRequireArrived(boolean hardArrivedConstraint) {
        this.hardArrivedConstraint = hardArrivedConstraint;
    }

    public void setFuzzyTargetCheck(boolean fuzzyEnd) {
        this.fuzzyEnd = fuzzyEnd;
    }

    public void setUnloadingRequired(boolean requireUnload) {
        this.requireUnload = requireUnload;
    }

    public void setBatteryReservation(boolean batteryReservation) {
        this.hasReservedBattery = batteryReservation;
    }

    public void setDelayBeforeStart(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public TestRobotDescription(Position position, RobotState startingState, Orientation facing,
            List<Position> targets) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = 0;
        this.initialDelay = SimulatorConfig.getDefaultWaitingTimeBeforeTest();
    }

    public TestRobotDescription(Position position, RobotState startingState, Orientation facing, List<Position> targets,
            int delay) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = delay;
        this.initialDelay = SimulatorConfig.getDefaultWaitingTimeBeforeTest();
    }

    public TestRobotDescription(Position position, RobotState startingState, Orientation facing, List<Position> targets,
            int delay, int initialDelay) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = delay;
        this.initialDelay = initialDelay;
    }

    public TestRobotDescription(Position position, RobotState startingState, Orientation facing, List<Position> targets,
            boolean fuzzy) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = 0;
        this.initialDelay = SimulatorConfig.getDefaultWaitingTimeBeforeTest();
        this.fuzzyEnd = fuzzy;
    }

    public TestRobotDescription(Position position, RobotState startingState, Orientation facing, List<Position> targets,
            boolean fuzzy, boolean initialBatteryStatus) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = 0;
        this.initialDelay = SimulatorConfig.getDefaultWaitingTimeBeforeTest();
        this.fuzzyEnd = fuzzy;
        this.hasReservedBattery = initialBatteryStatus;
    }

    public TestRobotDescription(Position position, RobotState startingState, Orientation facing, List<Position> targets,
            boolean fuzzy, boolean initialBatteryStatus, boolean hardArrivedConstraint) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = 0;
        this.initialDelay = SimulatorConfig.getDefaultWaitingTimeBeforeTest();
        this.fuzzyEnd = fuzzy;
        this.hasReservedBattery = initialBatteryStatus;
        this.hardArrivedConstraint = hardArrivedConstraint;
    }

    public TestRobotDescription(Position position, RobotState startingState, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzy) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = initialDelay;
        this.initialDelay = delay;
        this.fuzzyEnd = fuzzy;
    }

    public TestRobotDescription(Position position, RobotState startingState, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzy, boolean initialBatteryStatus) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = initialDelay;
        this.initialDelay = delay;
        this.fuzzyEnd = fuzzy;
        this.hasReservedBattery = initialBatteryStatus;
    }

    public TestRobotDescription(Position position, RobotState startingState, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzy, boolean initialBatteryStatus, boolean hardArrivedConstraint) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = initialDelay;
        this.initialDelay = delay;
        this.fuzzyEnd = fuzzy;
        this.hasReservedBattery = initialBatteryStatus;
        this.hardArrivedConstraint = hardArrivedConstraint;
    }

    @Override
    public void refreshEntity() {
        targets.clear();
        targets.addAll(targetsCopy);
    }

    @Override
    public Robot getRobot(RobotDispatcher robotDispatcher) {
        return robotDispatcher.addRobotAtPosition(position, state, facing, targets, robotSpecificDelay, initialDelay, fuzzyEnd,
                requireUnload, hasReservedBattery, hardArrivedConstraint);
    }
}
