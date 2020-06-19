package de.hpi.mod.sim.core.model;

public interface IScanner {
    boolean hasPackage(int stationID);
    int getPackageID(int stationID, Position robotPos);
}
