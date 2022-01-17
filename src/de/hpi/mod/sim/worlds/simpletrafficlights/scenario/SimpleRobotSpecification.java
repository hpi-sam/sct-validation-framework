package de.hpi.mod.sim.worlds.simpletrafficlights.scenario;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotSpecification;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightsGridManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.SimpleTrafficLightWorld;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.SimpleRobot;

public class SimpleRobotSpecification extends RobotSpecification<SimpleRobot, SimpleTrafficLightsGridManager> {

    private Position pos;
    private Orientation facing;
    private Position destination;

    public SimpleRobotSpecification(SimpleTrafficLightWorld world, Position pos, Orientation facing, Position destination) {
        super(world.getCrossRoadManager());
        this.pos = pos;
        this.facing = facing;
        this.destination = destination;
    }

    @Override
    public SimpleRobot createRobot(SimpleTrafficLightsGridManager robots) {
        return new SimpleRobot(Robot.incrementID(), robots, pos, facing, destination);
    }
    
}
