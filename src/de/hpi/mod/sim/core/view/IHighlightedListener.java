package de.hpi.mod.sim.core.view;

/**
 * Has to be registered on {@link de.hpi.mod.sim.core.view.panels.AnimationPanel}.
 * Gets called if one of the highlighted enitity changes
 */
public interface IHighlightedListener {
    
	void onHighlightedEntitySelection();
	
	void onValueUpdate();
    
}
