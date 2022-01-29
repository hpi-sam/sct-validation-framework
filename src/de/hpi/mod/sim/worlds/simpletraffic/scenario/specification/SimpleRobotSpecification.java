package de.hpi.mod.sim.worlds.simpletraffic.scenario.specification;

import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotSpecification;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;
import de.hpi.mod.sim.worlds.simpletraffic.entities.SimpleTrafficRobot;

public class SimpleRobotSpecification extends RobotSpecification<SimpleTrafficRobot, TrafficGridManager> {

    public SimpleRobotSpecification(TrafficGridManager m) {
		super(m);
	}

	@Override
    public SimpleTrafficRobot createRobot(TrafficGridManager networkManager) {
		SimpleTrafficRobot robot = new SimpleTrafficRobot(Robot.incrementID(), networkManager, SimpleTrafficWorldConfiguration.getIdleRobotsPosition(), SimpleTrafficWorldConfiguration.getIdleRobotsOrientation(), null);
    	networkManager.makeRobotIdle(robot);
    	return robot;
    }   
	    
}
