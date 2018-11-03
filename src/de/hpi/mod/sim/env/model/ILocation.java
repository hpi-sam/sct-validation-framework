package de.hpi.mod.sim.env.model;

public interface ILocation {
    Position getArrivalPositionAtStation(int stationID);
    Position getChargerPositionAtStation(int stationID, int chargerID);
    Position getQueuePositionAtStation(int stationID);
    Position getLoadingPositionAtStation(int stationID);
    Position getUnloadingPositionFromID(int packageID);
}
