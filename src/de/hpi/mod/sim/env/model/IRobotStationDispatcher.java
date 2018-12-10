package de.hpi.mod.sim.env.model;

public interface IRobotStationDispatcher {
    int getReservationNextForStation(int robotID, boolean charge);
    int getReservedChargerAtStation(int robotID, int stationID);
    boolean requestEnteringStation(int robotID, int stationID);
    boolean requestLeavingBattery(int robotID, int stationID);
    void reportChargingAtStation(int robotID, int stationID);
    void reportEnteringQueueAtStation(int robotID, int stationID);
    void reportLeaveStation(int robotID, int stationID);
}
