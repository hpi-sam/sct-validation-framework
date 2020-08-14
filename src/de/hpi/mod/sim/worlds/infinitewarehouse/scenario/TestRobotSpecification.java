package de.hpi.mod.sim.worlds.infinitewarehouse.scenario;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.RobotManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.Robot;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.Robot.RobotState;

public class TestRobotSpecification extends RobotSpecification {
    private Position position;
    private RobotState state;
    private List<Position> targets = new ArrayList<Position>();
    private Orientation facing;
    private int robotSpecificDelay = 0;
    private int initialDelay = 0;
    private List<Position> targetsCopy = new ArrayList<Position>();
    private boolean fuzzyEnd = false;
    private boolean requireUnload = false;
    private boolean hasReservedCharger = false;
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

    public void setChargerReservation(boolean chargerReservation) {
        this.hasReservedCharger = chargerReservation;
    }

    public void setDelayBeforeStart(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public TestRobotSpecification(RobotManager robots, Position position, RobotState startingState, Orientation facing,
            List<Position> targets) {
        super(robots);
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = 0;
        this.initialDelay = Configuration.getDefaultWaitingTimeBeforeTest();
    }

    public TestRobotSpecification(RobotManager robots, Position position, RobotState startingState, Orientation facing, List<Position> targets,
            int delay) {
        super(robots);
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = delay;
        this.initialDelay = Configuration.getDefaultWaitingTimeBeforeTest();
    }

    public TestRobotSpecification(RobotManager robots, Position position, RobotState startingState, Orientation facing, List<Position> targets,
            int delay, int initialDelay) {
        super(robots);
this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = delay;
        this.initialDelay = initialDelay;
    }

    public TestRobotSpecification(RobotManager robots, Position position, RobotState startingState, Orientation facing, List<Position> targets,
            boolean fuzzy) {
        super(robots);
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = 0;
        this.initialDelay = Configuration.getDefaultWaitingTimeBeforeTest();
        this.fuzzyEnd = fuzzy;
    }

    public TestRobotSpecification(RobotManager robots, Position position, RobotState startingState, Orientation facing, List<Position> targets,
            boolean fuzzy, boolean initialChargerStatus) {
        super(robots);
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = 0;
        this.initialDelay = Configuration.getDefaultWaitingTimeBeforeTest();
        this.fuzzyEnd = fuzzy;
        this.hasReservedCharger = initialChargerStatus;
    }

    public TestRobotSpecification(RobotManager robots, Position position, RobotState startingState, Orientation facing, List<Position> targets,
            boolean fuzzy, boolean initialChargerStatus, boolean hardArrivedConstraint) {
        super(robots);
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = 0;
        this.initialDelay = Configuration.getDefaultWaitingTimeBeforeTest();
        this.fuzzyEnd = fuzzy;
        this.hasReservedCharger = initialChargerStatus;
        this.hardArrivedConstraint = hardArrivedConstraint;
    }

    public TestRobotSpecification(RobotManager robots, Position position, RobotState startingState, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzy) {
        super(robots);
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = initialDelay;
        this.initialDelay = delay;
        this.fuzzyEnd = fuzzy;
    }

    public TestRobotSpecification(RobotManager robots, Position position, RobotState startingState, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzy, boolean initialChargerStatus) {
        super(robots);
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = initialDelay;
        this.initialDelay = delay;
        this.fuzzyEnd = fuzzy;
        this.hasReservedCharger = initialChargerStatus;
    }

    public TestRobotSpecification(RobotManager robots, Position position, RobotState startingState, Orientation facing, List<Position> targets,
            int delay, int initialDelay, boolean fuzzy, boolean initialChargerStatus, boolean hardArrivedConstraint) {
        super(robots);
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.targetsCopy.addAll(targets);
        this.facing = facing;
        this.robotSpecificDelay = initialDelay;
        this.initialDelay = delay;
        this.fuzzyEnd = fuzzy;
        this.hasReservedCharger = initialChargerStatus;
        this.hardArrivedConstraint = hardArrivedConstraint;
    }

    @Override
    public void refreshEntity() {
        targets.clear();
        targets.addAll(targetsCopy);
    }

    @Override
    public Robot getRobot(RobotManager robots) {
        return robots.addRobotAtPosition(position, state, facing, targets, robotSpecificDelay, initialDelay, fuzzyEnd,
                requireUnload, hasReservedCharger, hardArrivedConstraint);
    }
}