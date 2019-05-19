package de.hpi.mod.sim.env.view.sim;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.robot.Robot.RobotState;
import de.hpi.mod.sim.env.view.model.NewRobot;

public class NewTestRobot extends NewRobot{
	private Position position;
    private RobotState state;
    private List<Position> targets = new ArrayList<Position>();
    private Orientation facing;
    private int robotSpecificDelay = 0;
    private int initialDelay = 0;

    public NewTestRobot(Position position, RobotState startingState, Orientation facing, List<Position> targets) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.facing = facing;
        this.robotSpecificDelay = 0;
        this.initialDelay = 0;
    }
    
    public NewTestRobot(Position position, RobotState startingState, Orientation facing, List<Position> targets, int delay) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.facing = facing;
        this.robotSpecificDelay = delay;
        this.initialDelay = 0;
    }
    
    public NewTestRobot(Position position, RobotState startingState, Orientation facing, List<Position> targets, int delay, int initialDelay) {
        this.position = position;
        this.state = startingState;
        this.targets = targets;
        this.facing = facing;
        this.robotSpecificDelay = delay;
        this.initialDelay = initialDelay;
    }

    @Override
    public Robot register(SimulationWorld simulationWorld) {
        return simulationWorld.addRobotAtPosition(position, state, facing, targets, robotSpecificDelay, initialDelay);
    }

}
