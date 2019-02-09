package de.hpi.mod.sim.env.view.model;

/**
 * Has to be registered on {@link de.hpi.mod.sim.env.view.sim.SimulationWorld}.
 * Gets called if one of the highlighted Robots changes
 */
public interface IHighlightedRobotListener {
    void onHighlightedRobotChange();
    void onHighlightedRobotChange2();
}
