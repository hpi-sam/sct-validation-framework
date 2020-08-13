package de.hpi.mod.sim.core.view.model;

/**
 * Has to be registered on {@link de.hpi.mod.sim.core.view.panels.AnimationPanel}.
 * Gets called if one of the highlighted Robots changes
 */
public interface IHighlightedListener {
    void onHighlightedChange();
}
