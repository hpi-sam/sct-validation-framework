package de.hpi.mod.sim.env;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

class Station implements Comparable<Station> {

    private static int idCounter = 0;

    private int stationID;
    private int queueSize = 0;
    private Battery[] batteries;


    static Station getInstance() {
        return new Station(idCounter++);
    }

    private Station(int stationID) {
        this.stationID = stationID;
        initBatteries();
    }

    private void initBatteries() {
        batteries = new Battery[ServerGridManagement.BATTERIES_PER_STATION];
        for (int i = 0; i < batteries.length; i++) {
            batteries[i] = new Battery(i);
        }
    }

    void addToQueue() {
        queueSize++;
    }

    public void takeFromQueue() {
        queueSize--;
    }

    int getStationID() {
        return stationID;
    }

    int getQueueSize() {
        return queueSize;
    }

    boolean hasFreeQueuePosition() {
        return queueSize < ServerGridManagement.QUEUE_SIZE;
    }

    boolean hasFreeBattery() {
        return Arrays.stream(batteries).anyMatch(b -> !b.isBlocked());
    }

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
        throw new IllegalArgumentException("Theres is no battery reserved for " + robotID);
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
    public int compareTo(@NotNull Station s) {
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
