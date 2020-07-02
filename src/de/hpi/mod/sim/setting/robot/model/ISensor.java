package de.hpi.mod.sim.setting.robot.model;

import de.hpi.mod.sim.setting.grid.Direction;
import de.hpi.mod.sim.setting.grid.Orientation;
import de.hpi.mod.sim.setting.infinitewarehouses.model.PositionType;
import de.hpi.mod.sim.setting.robot.DriveSystemWrapper;

/**
 * Represents the Data interface in the Statechard.
 * It is used for communication between the Environment
 * and the Statechard-Wrapper ({@link DriveSystemWrapper})
 */
public interface ISensor {

    Orientation posOrientation();

    PositionType posType();

    Direction targetDirection();
    
    int posX();
    
    int posY();
    
    int targetX();
    
    int targetY();

    boolean isOnTarget();

    boolean blockedLeft();

    boolean blockedFront();

    boolean blockedRight();

    boolean blockedWaypointAhead();

    boolean blockedWaypointLeft();

    boolean blockedWaypointRight();

    boolean blockedCrossroadAhead();

    boolean blockedCrossroadRight();

	boolean canUnloadToTarget();

	boolean canChargeAtTarget();
}
