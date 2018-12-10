package de.hpi.mod.sim.env;

import java.util.*;

public class Station implements Comparable<Station> {

    private int stationID;
    private int queueSize = 0;
    private Battery[] batteries;
    private boolean driveLock = false;


    public Station(int stationID) {
        this.stationID = stationID;
        initBatteries();
    }

    private void initBatteries() {
        batteries = new Battery[ServerGridManagement.BATTERIES_PER_STATION];
        for (int i = 0; i < batteries.length; i++) {
            batteries[i] = new Battery(i);
        }
    }

    int getStationID() {
        return stationID;
    }

    void increaseQueue() {
        queueSize++;
    }

    void decreaseQueue() {
        queueSize--;
    }

    int getQueueSize() {
        return queueSize;
    }

    boolean hasFreeQueuePosition() {
        return getQueueSize() < ServerGridManagement.QUEUE_SIZE;
    }

    boolean hasFreeBattery() {
        return Arrays.stream(batteries).anyMatch(b -> !b.isBlocked());
    }

    boolean isDriveLock() { return driveLock; }

    boolean toggleDriveLock() { return (driveLock = !driveLock); }

    void resetDriveLock() { driveLock = false; }

    void reserveBatteryForRobot(int robotID) {
        getFreeBattery().setReservedForRobot(robotID);
    }

    void unregisterBatteryWithRobotIfPresent(int robotID) {
        Arrays
                .stream(batteries)
                .filter(b -> b.getReservedForRobot() == robotID)
                .findFirst()
                .ifPresent(battery1 -> battery1.setBlocked(false));
    }

    int getBatteryReservedForRobot(int robotID) {
        Optional<Battery> battery = Arrays.stream(batteries)
                .filter(b -> b.getReservedForRobot() == robotID)
                .findFirst();
        if (battery.isPresent())
            return battery.get().getBatteryID();
        throw new IllegalArgumentException("There is no battery reserved for " + robotID);
    }

    private Battery getFreeBattery() {
        Optional<Battery> battery = Arrays.stream(batteries)
                .filter(b -> !b.isBlocked()).findFirst();
        if (battery.isPresent())
            return battery.get();
        throw new IllegalStateException("There is no free battery");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        return stationID == station.stationID;
    }

    @Override
    public int hashCode() {
        return stationID;
    }

    @Override
    public int compareTo(Station s) {
        if (stationID == s.getStationID())
            return 0;
        return stationID > s.getStationID() ? 1 : -1;
    }

    static int compareByQueueSize(Station s1, Station s2) {
        if (s1.getQueueSize() == s2.getQueueSize())
            return 0;
        return s1.getQueueSize() > s2.getQueueSize() ? 1 : -1;
    }
}
