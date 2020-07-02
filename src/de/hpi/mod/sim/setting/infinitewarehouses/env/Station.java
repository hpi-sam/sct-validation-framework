package de.hpi.mod.sim.setting.infinitewarehouses.env;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehouseSimConfig;

/**
 * Manages the Queue, Drive-Lock and Batteries of a station
 */
public class Station { 

    /**
     * How many robots want to drive or are in the queue.
     * Must not be greater than {@link GridManagement#QUEUE_SIZE}
     */
    private int queueSize = 0;

    /**
     * There are {@link GridManagement#BATTERIES_PER_STATION} batteries in this station.
     */
    private Battery[] batteries;

    private boolean blockedQueue = false;
    
    private int stationID;

    private CopyOnWriteArrayList<Integer> requestedLocks = new CopyOnWriteArrayList<Integer>();



    public Station(int stationID) {
        this.stationID = stationID;
        initializeBatteries();
    }

    /**
     * Adds the batteries with IDs 0, 1, 2 to the station
     */
    private void initializeBatteries() {
        batteries = new Battery[InfiniteWarehouseSimConfig.BATTERIES_PER_STATION];
        for (int i = 0; i < batteries.length; i++) {
            batteries[i] = new Battery(i);
        }
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
        return getQueueSize() < InfiniteWarehouseSimConfig.QUEUE_SIZE;
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
    	int freeID = 0;
        for(int i=0; i< InfiniteWarehouseSimConfig.getBatteriesPerStation(); i++) {
        	if(batteries[i].isFree()) {
        		freeID = i;
        	}
        }
        return batteries[freeID];
    }

	public boolean blocked() {
		return blockedQueue;
	}

	public void blockQueue() {
		blockedQueue  = true;
	}

	public void unblockQueue() {
		blockedQueue = false;
	}
	
	public void robotPresentOnCharger(int chargerID) {
		batteries[chargerID].setRobotPresent();
	}

    void robotNotPresent(int batteryID) {
        batteries[batteryID].setRobotNotPresent();
    }

    public void clear() {
        blockedQueue = false;
        for (int i = 0; i < InfiniteWarehouseSimConfig.getBatteriesPerStation(); i++) {
            batteries[i].setRobotNotPresent();
        }
    }

    public boolean lockAllowed() {
        return !blockedQueue;
    }

    public void onLock() {
        blockQueue();
    }

    public int getStationID() {
        return stationID;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;

        Station station = (Station) object;

        return stationID == station.stationID;
    }

    public void releaseLocks() {
        requestedLocks.clear();
        clear();
    }

    public void registerRequestLock(int robotID) {
        if (!requestedLocks.contains(robotID)) {
            requestedLocks.add(0, robotID);
        }
    }

    public boolean requestLock(int robotID) {
        if (requestedLocks.get(0) == robotID && lockAllowed()) {
            requestedLocks.remove(0);
            onLock();
            return true;
        } else {
            return false;
        }
    }
}
