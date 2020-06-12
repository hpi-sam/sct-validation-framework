package de.hpi.mod.sim.env.testing;

import de.hpi.mod.sim.env.setting.infinitestations.Robot;
import de.hpi.mod.sim.env.simulation.World;

public abstract class RobotDescription {
    public abstract Robot register(World sim);
    public abstract void refreshRobot();
}