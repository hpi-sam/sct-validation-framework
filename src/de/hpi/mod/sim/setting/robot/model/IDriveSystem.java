package de.hpi.mod.sim.setting.robot.model;

import de.hpi.mod.sim.setting.robot.DriveSystemWrapper;
import de.hpi.mod.sim.setting.robot.Robot;

/**
 * Represents the Default interface in the Statechart.
 * It is used for communication between the Environment ({@link Robot})
 * and the Statechart-Wrapper ({@link DriveSystemWrapper})
 */
public interface IDriveSystem {

    void dataRefresh();
    void newTarget();
    void newUnloadingTarget();
    void newChargingTarget();
    void actionCompleted();
}
