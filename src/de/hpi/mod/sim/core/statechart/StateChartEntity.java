package de.hpi.mod.sim.core.statechart;

import java.util.List;

import de.hpi.mod.sim.core.simulation.Entity;

public interface StateChartEntity extends Entity {

	String getActiveState();
	
    List<String> getActiveStates();

	String getTopLevelRegionName();

}