package de.hpi.mod.sim.env.model;

import de.hpi.mod.sim.env.robot.DriveSystemWrapper;

/**
 * Represents the IProcessor interface in the Statechard.
 * It is used for communication between the Environment
 * and the Statechard-Wrapper ({@link DriveSystemWrapper})
 */
public interface IProcessor {

    void arrived();
}
