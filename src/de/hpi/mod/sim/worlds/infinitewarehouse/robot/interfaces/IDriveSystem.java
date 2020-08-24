package de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces;

import de.hpi.mod.sim.worlds.infinitewarehouse.robot.DriveSystemWrapper;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;

/**
 * Represents the Default interface in the Statechart.
 * It is used for communication between the Environment ({@link WarehouseRobot})
 * and the Statechart-Wrapper ({@link DriveSystemWrapper})
 */
public interface IDriveSystem {

    void dataRefresh();
    void newTarget();
    void newUnloadingTarget();
    void newChargingTarget();
    void actionCompleted();
}
