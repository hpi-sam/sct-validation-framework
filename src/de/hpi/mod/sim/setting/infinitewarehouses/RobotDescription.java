package de.hpi.mod.sim.setting.infinitewarehouses;

import de.hpi.mod.sim.core.scenario.EntityDescription;
import de.hpi.mod.sim.setting.robot.Robot;

public abstract class RobotDescription extends EntityDescription<Robot> {
    public abstract Robot getRobot(RobotDispatcher roboteDispatcher);
}