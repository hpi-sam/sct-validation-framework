package de.hpi.mod.sim.env.model;

/**
 * Accesses the position of locations the robot needs to drive to.
 * The <code>stationID</code> can be positive and negative.
 * The <code>batteryID</code> is 0-2 (inclusive)
 */
public interface ILocation {
    Position getArrivalPositionAtStation(int stationID);
    Position getBatteryPositionAtStation(int stationID, int batteryID);
    Position getLoadingPositionAtStation(int stationID);
    Position getQueuePositionAtStation(int stationID);
    Position getUnloadingPositionFromID(int packageID);
}
