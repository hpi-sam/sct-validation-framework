package de.hpi.mod.sim.setting.robot.model;

import de.hpi.mod.sim.setting.grid.Position;

public interface IScanner {
    boolean hasPackage(int stationID);
    int getPackageID(int stationID, Position robotPos);
}
