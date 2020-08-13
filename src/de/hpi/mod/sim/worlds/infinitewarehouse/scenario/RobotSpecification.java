package de.hpi.mod.sim.worlds.infinitewarehouse.scenario;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.RobotManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.Robot;

public abstract class RobotSpecification implements EntitySpecification<Robot> {
    
    private RobotManager robots;

    public RobotSpecification(RobotManager robots) {
        this.robots = robots;
    }

    public abstract Robot getRobot(RobotManager robots);

    @Override 
    public Robot get() {
        return getRobot(robots);
    }
}