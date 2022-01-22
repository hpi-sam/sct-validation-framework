package de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightWorld;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsConfiguration;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.SimpleRobot;

public class SimpleRobotWithFixedRouteSpecification extends SimpleRobotSpecification {

    private Position pos;
    private Orientation facing;
    private Position destination;

    public SimpleRobotWithFixedRouteSpecification(StreetNetworkManager robotManager, Position pos, Orientation facing, Position destination) {
        super(robotManager);
        this.pos = pos;
        this.facing = facing;
        this.destination = destination;
    }

    @Override
    public SimpleRobot createRobot(StreetNetworkManager networkManager) {
        SimpleRobot robot = new SimpleRobot(Robot.incrementID(), networkManager, pos, facing, destination);
    	networkManager.makeRobotIdle(robot);
    	return robot;
    }
    
}
