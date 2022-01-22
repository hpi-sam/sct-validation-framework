package de.hpi.mod.sim.worlds.simpletrafficlights.scenario.specification;

import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotSpecification;
import de.hpi.mod.sim.worlds.simpletrafficlights.StreetNetworkManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsConfiguration;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.SimpleRobot;

public class SimpleRobotSpecification extends RobotSpecification<SimpleRobot, StreetNetworkManager> {

    public SimpleRobotSpecification(StreetNetworkManager m) {
		super(m);
	}

	@Override
    public SimpleRobot createRobot(StreetNetworkManager networkManager) {
		SimpleRobot robot = new SimpleRobot(Robot.incrementID(), networkManager, SimpleTrafficLightsConfiguration.getIdleRobotsPosition(), SimpleTrafficLightsConfiguration.getIdleRobotsOrientation(), null);
    	networkManager.makeRobotIdle(robot);
    	return robot;
    }   
	    
}
