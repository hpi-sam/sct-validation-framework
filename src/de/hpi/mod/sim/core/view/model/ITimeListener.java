package de.hpi.mod.sim.core.view.model;

/**
 * Has to be registered on {@link de.hpi.mod.sim.core.view.panels.AnimationPanel}.
 * Gets called if the time flow of the Simulation changes
 */
public interface ITimeListener {
    void refresh();
}
