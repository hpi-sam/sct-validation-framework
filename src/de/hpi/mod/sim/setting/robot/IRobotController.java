package de.hpi.mod.sim.setting.robot;

import de.hpi.mod.sim.setting.grid.Position;

public interface IRobotController {
    boolean isBlockedByRobot(Position pos);
}
