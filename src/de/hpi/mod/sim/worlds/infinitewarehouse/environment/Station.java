package de.hpi.mod.sim.worlds.infinitewarehouse.environment;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouseConfiguration;

/**
 * Manages the Queue, Drive-Lock and Chargers of a station
 */
public class Station { 

    /**
     * How many robots want to drive or are in the queue.
     * Must not be greater than {@link GridManager#QUEUE_SIZE}
     */
    private int queueSize = 0;

    /**
     * There are {@link GridManager#CHARGERS_PER_STATION} chargers in this station.
     */
    private Charger[] chargers;

    private boolean blockedQueue = false;
    
    private int stationID;

    private CopyOnWriteArrayList<Integer> requestedLocks = new CopyOnWriteArrayList<Integer>();



    public Station(int stationID) {
        this.stationID = stationID;
        initializeChargers();
    }

    /**
     * Adds the chargers with IDs 0, 1, 2 to the station
     */
    private void initializeChargers() {
        chargers = new Charger[InfiniteWarehouseConfiguration.CHARGERS_PER_STATION];
        for (int i = 0; i < chargers.length; i++) {
            chargers[i] = new Charger(i);
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
        return getQueueSize() < InfiniteWarehouseConfiguration.QUEUE_SIZE;
    }

    boolean hasFreeCharger() {
        return Arrays.stream(chargers).anyMatch(Charger::isFree);
    }

    /**
     * Reserves a free charger for the robot. Always check first if there are any
     * free chargers
     */
    void reserveChargerForRobot(int robotID) {
        getFreeCharger().setReservedForRobot(robotID);
    }

    /**
     * Removes a reservation of a robot from the chargers of this station if it is present
     * @param robotID The robot to remove
     */
    void unregisterChargerWithRobotIfPresent(int robotID) {
        Arrays
                .stream(chargers)
                .filter(b -> b.getReservedForRobot() == robotID)
                .findFirst()
                .ifPresent(charger -> charger.setBlocked(false));
    }

    /**
     * Returns the id of the charger which is reserved for the given Robot or throws an error if no such charger exists
     * @return id of the charger
     */
    int getChargerReservedForRobot(int robotID) {
        Optional<Charger> charger = Arrays.stream(chargers)
                .filter(b -> b.getReservedForRobot() == robotID)
                .findFirst();
        if (charger.isPresent())
            return charger.get().getChargerID();
        throw new IllegalArgumentException("There is no charger reserved for " + robotID);
    }

    /**
     * Returns the first free charger or throws an error if no free exists
     * 
     * @return A free charger
     */
    private Charger getFreeCharger() {
    	int freeID = 0;
        for(int i=0; i< InfiniteWarehouseConfiguration.getChargersPerStation(); i++) {
        	if(chargers[i].isFree()) {
        		freeID = i;
        	}
        }
        return chargers[freeID];
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
		chargers[chargerID].setRobotPresent();
	}

    void robotNotPresent(int chargerID) {
        chargers[chargerID].setRobotNotPresent();
    }

    public void clear() {
        blockedQueue = false;
        for (int i = 0; i < InfiniteWarehouseConfiguration.getChargersPerStation(); i++) {
            chargers[i].setRobotNotPresent();
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
