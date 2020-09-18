package de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces;

import de.hpi.mod.sim.worlds.abstract_grid.Position;

public interface IRobotController {
    boolean isBlockedByRobot(Position pos);
}
