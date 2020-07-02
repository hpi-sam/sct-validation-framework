package de.hpi.mod.sim.setting.robot.model;

import de.hpi.mod.sim.setting.grid.Position;

public interface IRobotController {
    boolean isBlockedByRobot(Position pos);
}
