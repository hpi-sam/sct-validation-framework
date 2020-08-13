package de.hpi.mod.sim.worlds.infinitewarehouse.environment;

import de.hpi.mod.sim.worlds.abstract_grid.Position;

/**
 * Accesses the position of locations the robot needs to drive to.
 * The <code>stationID</code> can be positive and negative.
 * The <code>chargerID</code> is 0-2 (inclusive)
 */
public interface ILocation {
    Position getArrivalPositionAtStation(int stationID);
    
    Position getChargerPositionAtStation(int stationID, int chargerID);

    Position getLoadingPositionAtStation(int stationID);

    Position getQueuePositionAtStation(int stationID);

    Position getUnloadingPositionFromID(int packageID);
}
