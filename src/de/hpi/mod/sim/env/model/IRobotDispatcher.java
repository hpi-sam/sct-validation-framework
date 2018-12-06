package de.hpi.mod.sim.env.model;

public interface IRobotDispatcher {
    int requestNextStation(int robotID, boolean charge);
    int requestFreeChargerAtStation(int robotID, int stationID);
    boolean requestEnqueueAtStation(int robotID, int stationID);
    void reportChargingAtStation(int robotID, int stationID);
    void reportLeaveStation(int robotID, int stationID);
    void reportUnloadingPackage(int robotID, int packageID);
}
