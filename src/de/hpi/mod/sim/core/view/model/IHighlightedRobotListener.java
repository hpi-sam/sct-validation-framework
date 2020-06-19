package de.hpi.mod.sim.core.view.model;

/**
 * Has to be registered on {@link de.hpi.mod.sim.core.view.sim.SimulationWorld}.
 * Gets called if one of the highlighted Robots changes
 */
public interface IHighlightedRobotListener {
    void onHighlightedRobotChange();
}
