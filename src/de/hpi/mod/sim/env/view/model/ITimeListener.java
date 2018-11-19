package de.hpi.mod.sim.env.view.model;

/**
 * Has to be registered on {@link de.hpi.mod.sim.env.view.sim.SimulationWorld}.
 * Gets called if the time flow of the Simulation changes
 */
public interface ITimeListener {
    void refresh();
}
