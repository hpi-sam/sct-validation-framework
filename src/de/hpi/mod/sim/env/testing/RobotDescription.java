package de.hpi.mod.sim.env.testing;

import de.hpi.mod.sim.env.simulation.robot.Robot;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

public abstract class RobotDescription {
    public abstract Robot register(SimulationWorld sim);
    public abstract void refreshRobot();
}