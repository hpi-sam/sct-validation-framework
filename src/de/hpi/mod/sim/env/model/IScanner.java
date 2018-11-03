package de.hpi.mod.sim.env.model;

public interface IScanner {
    boolean hasPackage(int stationID);
    int getPackageID(int stationID);
}
