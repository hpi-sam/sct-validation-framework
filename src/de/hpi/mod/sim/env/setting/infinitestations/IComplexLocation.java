package de.hpi.mod.sim.env.setting.infinitestations;

import de.hpi.mod.sim.env.model.ILocation;
import de.hpi.mod.sim.env.model.Position;

/**
 * Accesses the position of locations the robot needs to drive to. The
 * <code>stationID</code> can be positive and negative. The
 * <code>batteryID</code> is 0-2 (inclusive)
 */
public interface IComplexLocation extends ILocation {

    Position getBatteryPositionAtStation(int stationID, int batteryID);

    Position getLoadingPositionAtStation(int stationID);

    Position getQueuePositionAtStation(int stationID);

    Position getUnloadingPositionFromID(int packageID);
}
