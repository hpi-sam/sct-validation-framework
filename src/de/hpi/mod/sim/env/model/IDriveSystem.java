package de.hpi.mod.sim.env.model;

import de.hpi.mod.sim.env.robot.DriveSystemWrapper;
import de.hpi.mod.sim.env.robot.Robot;

/**
 * Represents the Default interface in the Statechart.
 * It is used for communication between the Environment ({@link Robot})
 * and the Statechard-Wrapper ({@link DriveSystemWrapper})
 */
public interface IDriveSystem {

    void unload();
    void stop();
    void dataRefresh();
    void newTarget();
    void actionCompleted();
    void close();
    String getMachineState();
}
