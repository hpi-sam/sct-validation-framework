package de.hpi.mod.sim.core.testing;

import de.hpi.mod.sim.core.model.Entity;

public abstract class EntityDescription<E extends Entity> {
    public abstract void refreshEntity();
}