package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.IRobotStationDispatcher;
import de.hpi.mod.sim.env.model.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Manages stations and acts as dispatcher in the robot-reservation-protocol
 *
 */
public class StationManager implements IRobotStationDispatcher {

    /**
     * The stations in the range.
     * If more stations are needed a new stations gets allocated.
     */
    private List<Station> stations = new ArrayList<>();
	private int usedStations;


    /**
     * Allocate the given amount of stations
     * @param defaultAmountOfStations How many stations to allocate
     */
    StationManager(int defaultAmountOfStations) {
    	this.usedStations = defaultAmountOfStations;
        for (int i = 0; i < defaultAmountOfStations; i++)
            stations.add(new Station(i));
    }

    /**
     * If the robot needs a charge the method searches for a station with a free battery.
     * Else it searches for a stations with a free queue-position.
     * It reserves a battery or queue-position for the robot and returns the station-id.
     * It will always return a valid station
     *
     * @param robotID The robot which requests the reservation
     * @param charge Whether a battery or a queue-position should be reserved
     * @return a valid station id
     */
    @Override
    public int getReservationNextForStation(int robotID, boolean charge) {
        Station station;
        if (charge) {
            station = getRandomStationWithFreeBattery();
            station.reserveBatteryForRobot(robotID);
        } else {
            station = getRandomStationWithMinQueue();
        }
        station.increaseQueue();
        return station.getStationID();
    }

    /**
     * Return the reserved Battery-id of the robot.
     * Throws an error if no battery is reserved
     */
    @Override
    public int getReservedChargerAtStation(int robotID, int stationID) {
        return getStationByID(stationID).getBatteryReservedForRobot(robotID);
    }
    
    @Override
    public int getStationIDFromPosition(Position target) {
    	if(target.getX() >= 0) {
    		return target.getX() / 3 * 2;
    	} else {
    		return (-target.getX() - 1) / 3 * 2 + 1; 
    	}
    }

    /**
     * Checks whether the robot is currently allowed to drive to the queue
     */
    @Override
    public boolean requestEnteringStation(int robotID, int stationID) {
    	getStationByID(stationID).registerRequestLock(robotID);
    	return getStationByID(stationID).requestLock(robotID);
    }

	/**
     * Checks whether the robot is currently allowed to drive to the queue
     */
    @Override
    public boolean requestEnteringBattery(int robotID, int stationID) {
    	return true;
    }
    
    @Override
    public boolean requestLeavingBattery(int robotID, int stationID, int batteryID) {
    	getStationByID(stationID).registerRequestLock(robotID);
    	return getStationByID(stationID).requestLock(robotID);
    }

    @Override
    public void reportChargingAtStation(int robotID, int stationID, int chargerID) {
    	getStationByID(stationID).robotPresentOnCharger(chargerID);
    }

    @Override
    public void reportEnteringQueueAtStation(int robotID, int stationID) {
    	Station station = getStationByID(stationID);
        station.unregisterBatteryWithRobotIfPresent(robotID);
        station.unblockQueue();
    }

    @Override
    public void reportLeaveStation(int robotID, int stationID) {
        getStationByID(stationID).decreaseQueue();
    }
    
    @Override
    public void releaseAllLocks() {
    	for(int i=0; i<stations.size(); i++) {
    		stations.get(i).releaseLocks();
    	}
    }
    
    @Override
    public int getUsedStations() {
    	return usedStations;
    }

    public List<Station> getStations() {
        return stations;
    }

    /**
     * Returns a station with a free battery
     */
    private Station getRandomStationWithFreeBattery() {
        return getRandomStationWithPredicate(Station::hasFreeBattery);
    }

    /**
     * Returns a station with a free queue-position
     */
    private Station getRandomStationWithMinQueue() {
        return getRandomStationWithPredicate(Station::hasFreeQueuePosition);
    }

    /**
     * @param stationFilter
     * @return
     */
    private Station getRandomStationWithPredicate(Predicate<Station> stationFilter) {
        Random r = new Random();
        
        List<Station> filteredStations = stations.stream()
             .filter(stationFilter)  // Only get Stations where the predicate is true
             .collect(Collectors.toList());
        
        if(!filteredStations.isEmpty()) {
        	return filteredStations.get(r.nextInt(filteredStations.size()));
        } else {
        	return stations.get(0); //Fallback strategy
        }
    }

    private Station getStationByID(int stationID) {
    	stationID = stationID % stations.size();
    	Station station = null;
    	for(int i = 0; i<stations.size(); i++) {
    		if(stations.get(i).getStationID() == stationID) {
    			station = stations.get(i);
    		}
    	}
    	if(station != null) {
    		return station;
    	}
    	throw new IllegalStateException("Can't create station with ID: " + stationID);
    }
}
