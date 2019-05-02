package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.IRobotStationDispatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StationManagerTest {

    private IRobotStationDispatcher stations;

    @Before
    public void setUp() {
        stations = new StationManager(3);
    }

    @Test
    public void testNoOtherRobotCanDriveIfDriveLockIsOn() {
        assertTrue(stations.requestEnteringStation(0, 0));
        //this call shall block the queue and get a lock
        stations.reportEnteringQueueAtStation(0, 0);
        assertFalse(stations.requestEnteringStation(1, 0));
        //Since a robot is not allowed to enter the station at all, if there is another
        //robot in the queue or at the battery it is always safe to leave the battery
        assertTrue(stations.requestLeavingBattery(1, 0));

        // Other Stations are not affected
        assertTrue(stations.requestEnteringStation(1, 1));
        assertTrue(stations.requestLeavingBattery(1, 2));
    }

    @Test
    public void testArrivalInQueueTurnsOfLock() {
        assertTrue(stations.requestEnteringStation(0, 0));
        //this call shall block the queue and get a lock
        stations.reportEnteringQueueAtStation(0, 0);
        assertFalse(stations.requestEnteringStation(0, 0));
        //it is always safe to leave battery
        assertTrue(stations.requestLeavingBattery(0, 0));
        //this shall unblock the queue
        stations.reportLeaveStation(0, 0);
        assertTrue(stations.requestEnteringStation(0, 0));
    }

}