package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.IRobotStationDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
     * TODO Free unused stations back to default if not used
     */
    private List<Station> stations = new ArrayList<>();


    /**
     * Allocate the given amount of stations
     * @param defaultAmountOfStations How many stations to allocate
     */
    StationManager(int defaultAmountOfStations) {
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

    /**
     * Checks whether the robot is currently allowed to drive to the queue
     */
    @Override
    public boolean requestEnteringStation(int robotID, int stationID) {
        return requestDriveLock(stationID);
    }

    /**
     * Checks whether the robot is currently allowed to drive to the queue
     */
    @Override
    public boolean requestLeavingBattery(int robotID, int stationID) {
        return requestDriveLock(stationID);
    }

    private boolean requestDriveLock(int stationID) {
        Station station = getStationByID(stationID);
        // If Robot can enter it activates the lock
        return !station.isDriveLock() && station.toggleDriveLock();
    }

    @Override
    public void reportChargingAtStation(int robotID, int stationID) {
        getStationByID(stationID).resetDriveLock();
    }

    @Override
    public void reportEnteringQueueAtStation(int robotID, int stationID) {
        getStationByID(stationID).unregisterBatteryWithRobotIfPresent(robotID);
        getStationByID(stationID).resetDriveLock();
    }

    @Override
    public void reportLeaveStation(int robotID, int stationID) {
        getStationByID(stationID).decreaseQueue();
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
     * TODO: range = view
     * @param stationFilter
     * @return
     */
    private Station getRandomStationWithPredicate(Predicate<Station> stationFilter) {
        Random r = new Random();

        List<Station> filteredStations = stations.stream()
                .filter(stationFilter)  // Only get Stations where the predicate is true
                .collect(Collectors.toList());
        
        if (filteredStations.size() > SimulatorConfig.getChargingStationsInUse()) {
        	return filteredStations.isEmpty() ?
	                addNewStation() :
	                filteredStations.get(r.nextInt(SimulatorConfig.getChargingStationsInUse()));
        } else {
	        return filteredStations.isEmpty() ?
	                addNewStation() :
	                filteredStations.get(r.nextInt(filteredStations.size()));
        }
    }

    private Station addNewStation() {
        Station created = new Station(stations.size());
        stations.add(created);
        return created;
    }

    private Station getStationByID(int stationID) {
        Optional<Station> station = stations.stream()
                .filter(s -> s.getStationID() == stationID).findFirst();

        if (station.isPresent()) return station.get();
        throw new NullPointerException("No Station with id " + stationID);
    }
}
