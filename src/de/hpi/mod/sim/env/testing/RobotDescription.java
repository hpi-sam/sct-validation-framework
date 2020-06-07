package de.hpi.mod.sim.env.testing;

import de.hpi.mod.sim.env.simulation.robot.Robot;
import de.hpi.mod.sim.env.world.MetaWorld;

public abstract class RobotDescription {
    public abstract Robot register(MetaWorld sim);
    public abstract void refreshRobot();
}