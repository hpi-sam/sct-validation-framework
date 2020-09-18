package de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces;

import de.hpi.mod.sim.worlds.abstract_grid.Position;

/**
 * A Robot can use the Dispatcher to get through a Station.
 * To find more info about to interaction see <code>env/stations.md</code>
 */
public interface IRobotStationDispatcher {
    int getReservationNextForStation(int robotID, boolean charge);
    int getReservedChargerAtStation(int robotID, int stationID);
    int getStationIDFromPosition(Position target);
    boolean requestEnteringStation(int robotID, int stationID);
    boolean requestLeavingCharger(int robotID, int stationID, int chargerID);
    void reportChargingAtStation(int robotID, int stationID, int chargerID);
    void reportEnteringQueueAtStation(int robotID, int stationID);
    void reportLeaveStation(int robotID, int stationID);
	boolean requestEnteringCharger(int robotID, int stationID);
	void releaseAllLocks();
	int getUsedStations();
}
