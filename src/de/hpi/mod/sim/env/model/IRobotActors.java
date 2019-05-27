package de.hpi.mod.sim.env.model;

import de.hpi.mod.sim.env.robot.DriveSystemWrapper;

/**
 * Represents the IRobotActors interface in the Statechard.
 * It is used for communication between the Environment
 * and the Statechard-Wrapper ({@link DriveSystemWrapper})
 */
public interface IRobotActors {

    void driveForward();
    void driveBackward();
    void turnLeft();
    void turnRight();
    void startUnloading();
}
