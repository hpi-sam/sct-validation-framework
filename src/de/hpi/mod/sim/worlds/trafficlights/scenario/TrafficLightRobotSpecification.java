package de.hpi.mod.sim.worlds.trafficlights.scenario;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotSpecification;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;
import de.hpi.mod.sim.worlds.trafficlights.CrossRoadsManager;
import de.hpi.mod.sim.worlds.trafficlights.TrafficLightWorld;
import de.hpi.mod.sim.worlds.trafficlights.robot.TrafficLightRobot;

public class TrafficLightRobotSpecification extends RobotSpecification<TrafficLightRobot, CrossRoadsManager> {

    private Position pos;
    private Orientation facing;
    private Position destination;

    public TrafficLightRobotSpecification(TrafficLightWorld world, Position pos, Orientation facing, Position destination) {
        super(world.getCrossRoadManager());
        this.pos = pos;
        this.facing = facing;
        this.destination = destination;
    }


    @Override
    public TrafficLightRobot createRobot(CrossRoadsManager robots) {
    	TrafficLightRobot robot =  new TrafficLightRobot(Robot.incrementID(), robots, pos, facing, destination);
		robots.addRobot(robot);
		return robot;
    }
    
}
