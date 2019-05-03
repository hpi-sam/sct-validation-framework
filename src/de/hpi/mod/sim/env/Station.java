package de.hpi.mod.sim.env;

import java.util.*;

/**
 * Manages the Queue, Drive-Lock and Batteries of a station
 */
public class Station {

    private int stationID;
    private long now;

    /**
     * How many robots want to drive or are in the queue.
     * Must not be greater than {@link ServerGridManagement#QUEUE_SIZE}
     */
    private int queueSize = 0;

    /**
     * There are {@link ServerGridManagement#BATTERIES_PER_STATION} batteries in this station.
     */
    private Battery[] batteries;

	private boolean blockedBattery = false;

	private boolean blockedQueue = false;


    public Station(int stationID) {
        this.stationID = stationID;
        initializeBatteries();
    }

    /**
     * Adds the batteries with IDs 0, 1, 2 to the station
     */
    private void initializeBatteries() {
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
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Station station = (Station) object;

        return stationID == station.stationID;
    }

	public void freeBattery() {
		blockedBattery = false;
	}

	public void blockBattery() {
		blockedBattery = true;
		now = System.currentTimeMillis();
	}

	public boolean blocked() {
		if(now+SimulatorConfig.getDefaultStationUnblockingTime()<=System.currentTimeMillis()) {
			blockedBattery = false;
			blockedQueue = false;
			return false;
		}
		return blockedBattery || blockedQueue;
	}

	public void blockQueue() {
		blockedQueue  = true;
		now = System.currentTimeMillis();
	}

	public void unblockQueue() {
		blockedQueue = false;
	}
}
