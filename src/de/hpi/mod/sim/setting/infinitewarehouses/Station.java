package de.hpi.mod.sim.setting.infinitewarehouses;

import java.util.*;

/**
 * Manages the Queue, Drive-Lock and Batteries of a station
 */
public class Station extends AbstractStation { 

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


    public Station(int stationID) {
        super(stationID);
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

    @Override
    public void clear() {
        blockedQueue = false;
        for (int i = 0; i < InfiniteWarehouseSimConfig.getBatteriesPerStation(); i++) {
            batteries[i].setRobotNotPresent();
        }
    }

    @Override
    public boolean lockAllowed() {
        return !blockedQueue;
    }

    @Override
    public void onLock() {
        blockQueue();
    }
}
