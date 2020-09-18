package de.hpi.mod.sim.core.scenario;

import de.hpi.mod.sim.core.simulation.Entity;

public interface EntitySpecification<E extends Entity> {

    public E createEntity(); 
}