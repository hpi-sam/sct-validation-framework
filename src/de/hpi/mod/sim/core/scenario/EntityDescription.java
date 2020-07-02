package de.hpi.mod.sim.core.scenario;

import de.hpi.mod.sim.core.model.Entity;

public interface EntityDescription<E extends Entity> {

    public E get(); 
    public void refreshEntity();
}