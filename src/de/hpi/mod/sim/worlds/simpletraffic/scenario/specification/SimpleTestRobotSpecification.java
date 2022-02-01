package de.hpi.mod.sim.worlds.simpletraffic.scenario.specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorld;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;
import de.hpi.mod.sim.worlds.simpletraffic.entities.SimpleTrafficRobot;

public class SimpleTestRobotSpecification extends SimpleRobotSpecification {

    private Position startPosition;
    private Orientation startFacing;
    private List<Position> destinations = new CopyOnWriteArrayList<Position>();
    
	private int initialDelay = SimpleTrafficWorldConfiguration.getDefaultWaitingTimeBeforeTest();
    private boolean testExpectArrived = false;
    
    private Position testSecretDestination;
	private int testSecretDestinationEvaluationDelay;

    public SimpleTestRobotSpecification(TrafficGridManager robotManager, Position pos, Orientation facing) {
        super(robotManager);
        this.startPosition = pos;
        this.startFacing = facing;
    }


    public SimpleTestRobotSpecification(TrafficGridManager robotManager, Position pos, Orientation facing, Position destination) {
    	this(robotManager, pos, facing);
        this.destinations = new ArrayList<>(Arrays.asList(destination));
    }

    @Override
    public SimpleTrafficRobot createRobot(TrafficGridManager networkManager) {
    	List<Position> destinationsCopy = destinations.stream().collect(Collectors.toList());
        SimpleTrafficRobot robot = new SimpleTrafficRobot(
        		Robot.incrementID(), networkManager, startPosition, startFacing, destinationsCopy, initialDelay, testExpectArrived);
    	networkManager.addRobot(robot);
    	return robot;
    }
    
    
}
