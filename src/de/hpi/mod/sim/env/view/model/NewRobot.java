package de.hpi.mod.sim.env.view.model;

import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

public abstract class NewRobot {
    public abstract Robot register(SimulationWorld sim);
}