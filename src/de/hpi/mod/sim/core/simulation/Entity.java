package de.hpi.mod.sim.core.simulation;

public interface Entity {

    public default boolean hasPassedAllTestCriteria() {
        return true;
    }

}