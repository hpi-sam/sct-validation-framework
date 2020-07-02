package de.hpi.mod.sim.core.statechart;

import de.hpi.mod.sim.core.model.Entity;

public interface StateChartEntity extends Entity {

	String getMachineState();

	String getTopStateName();

}