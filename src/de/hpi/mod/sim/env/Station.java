package de.hpi.mod.sim.env;

import java.util.*;

/**
 * Manages the Queue, Drive-Lock and Batteries of a station
 */
public class Station {

    private int stationID;

    /**
     * How many robots want to drive or are in the queue.
     * Must not be greater than {@link ServerGridManagement#QUEUE_SIZE}
     */
    private int queueSize = 0;

    /**
     * There are {@link ServerGridManagement#BATTERIES_PER_STATION} batteries in this station.
     */
    private Battery[] batteries;

    /**
     * Whether a robot is driving from arrival or battery to queue-start.
     * Is set so no two robots are driving simultaneously
     */
    private boolean driveLock = false;


    public Station(int stationID) {
        this.stationID = stationID;
        initBatteries();
    }

    /**
     * Adds the batteries with IDs 0, 1, 2 to the station
     */
    private void initBatteries() {
        batteries = new Battery[SimulatorConfig.BATTERIES_PER_STATION];
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
        return getQueueSize() < SimulatorConfig.QUEUE_SIZE;
    }

    boolean hasFreeBattery() {
        return Arrays.stream(batteries).anyMatch(Battery::isFree);
    }

    boolean isDriveLock() { return driveLock; }

    boolean toggleDriveLock() { return (driveLock = !driveLock); }

    void resetDriveLock() { driveLock = false; }

    /**
     * Reserves a free battery for the robot.
     * Always check first if there are any free batteries
     */
    void reserveBatteryForRobot(int robotID) {
        getFreeBattery().setReservedForRobot(robotID);
    }

    /**
     * Removes a reservation of a robot from the batteries of this station if it is present
     * @param robotID The robot to remove
     */
    void unregisterBatteryWithRobotIfPresent(int robotID) {
        Arrays
                .stream(batteries)
                .filter(b -> b.getReservedForRobot() == robotID)
                .findFirst()
                .ifPresent(battery1 -> battery1.setBlocked(false));
    }

    /**
     * Returns the id of the Battery which is reserved for the given Robot or throws an error if no such battery exists
     * @return id of the battery
     */
    int getBatteryReservedForRobot(int robotID) {
        Optional<Battery> battery = Arrays.stream(batteries)
                .filter(b -> b.getReservedForRobot() == robotID)
                .findFirst();
        if (battery.isPresent())
            return battery.get().getBatteryID();
        throw new IllegalArgumentException("There is no battery reserved for " + robotID);
    }

    /**
     * Returns the first free battery or throws an error if no free exists
     * @return A free battery
     */
    private Battery getFreeBattery() {
        Optional<Battery> battery = Arrays.stream(batteries)
                .filter(Battery::isFree).findFirst();
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
}
