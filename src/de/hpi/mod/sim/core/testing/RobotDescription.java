package de.hpi.mod.sim.core.testing;

import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.setting.robot.Robot;

public abstract class RobotDescription {
    public abstract Robot register(Setting setting);
    public abstract void refreshRobot();
}