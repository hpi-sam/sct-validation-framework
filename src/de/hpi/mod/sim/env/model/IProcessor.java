package de.hpi.mod.sim.env.model;

/**
 * Represents the IProcessor interface in the Statechard.
 * It is used for communication between the Environment
 * and the Statechard-Wrapper ({@link DriveSystemWrapper})
 */
public interface IProcessor {

    void arrived();
}
