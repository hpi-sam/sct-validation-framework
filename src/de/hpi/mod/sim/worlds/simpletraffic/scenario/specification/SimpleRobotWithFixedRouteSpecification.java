package de.hpi.mod.sim.worlds.simpletraffic.scenario.specification;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorld;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;
import de.hpi.mod.sim.worlds.simpletraffic.entities.SimpleTrafficRobot;

public class SimpleRobotWithFixedRouteSpecification extends SimpleRobotSpecification {

    private Position pos;
    private Orientation facing;
    private Position destination;

    public SimpleRobotWithFixedRouteSpecification(TrafficGridManager robotManager, Position pos, Orientation facing, Position destination) {
        super(robotManager);
        this.pos = pos;
        this.facing = facing;
        this.destination = destination;
    }

    @Override
    public SimpleTrafficRobot createRobot(TrafficGridManager networkManager) {
        SimpleTrafficRobot robot = new SimpleTrafficRobot(Robot.incrementID(), networkManager, pos, facing, destination);
    	networkManager.makeRobotIdle(robot);
    	return robot;
    }
    
}
