package de.hpi.mod.sim.setting.robot;

/**
 * Represents the IProcessor interface in the Statechard.
 * It is used for communication between the Environment
 * and the Statechard-Wrapper ({@link DriveSystemWrapper})
 */
public interface IProcessor {

    void arrived();
}
