package de.hpi.mod.sim.env.testing;

import de.hpi.mod.sim.env.Setting;
import de.hpi.mod.sim.env.robot.Robot;

public abstract class RobotDescription {
    public abstract Robot register(Setting setting);
    public abstract void refreshRobot();
}