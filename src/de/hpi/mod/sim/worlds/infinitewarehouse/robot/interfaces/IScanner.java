package de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces;

import de.hpi.mod.sim.worlds.abstract_grid.Position;

public interface IScanner {
    boolean hasPackage(int stationID);
    int getPackageID(int stationID, Position robotPos);
}
