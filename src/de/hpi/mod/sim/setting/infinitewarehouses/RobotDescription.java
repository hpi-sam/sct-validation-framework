package de.hpi.mod.sim.setting.infinitewarehouses;

import de.hpi.mod.sim.core.scenario.EntityDescription;
import de.hpi.mod.sim.setting.infinitewarehouses.env.RobotManagement;
import de.hpi.mod.sim.setting.robot.Robot;

public abstract class RobotDescription implements EntityDescription<Robot> {
    
    private RobotManagement robots;

    public RobotDescription(RobotManagement robots) {
        this.robots = robots;
    }

    public abstract Robot getRobot(RobotManagement robots);

    @Override 
    public Robot get() {
        return getRobot(robots);
    }
}