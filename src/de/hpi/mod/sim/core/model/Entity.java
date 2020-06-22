package de.hpi.mod.sim.core.model;

public interface Entity {

    public default boolean hasPassedAllTestCriteria() {
        return true;
    }

}