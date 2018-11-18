package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.*;
import de.hpi.mod.sim.env.robot.Robot;

import java.util.*;


/**
 * TODO doc
 * TODO tests
 */
public class Simulator implements IRobotController, IRobotDispatcher, ILocation, IScanner {

    public static final int DEFAULT_UNLOADING_RANGE = 10;

    private List<Robot> robots = new ArrayList<>();
    private ServerGridManagement grid;
    private SortedSet<Station> stations = new TreeSet<>();
    private int unloadingRange = DEFAULT_UNLOADING_RANGE;


    public Simulator() {
        grid = new ServerGridManagement(this);
    }

    public Robot addRobot() {
        Station station = getStationWithFreeBattery();
        return addRobot(station.getStationID());
    }

    public Robot addRobot(int stationID) {
        Station station = getStationByID(stationID);
        if (station.hasFreeBattery()) {
            int robotID = Robot.incrementID();
            requestNextStation(robotID, true);
            int batteryID = requestFreeChargerAtStation(robotID, stationID);

            Robot robot = new Robot(
                    robotID,
                    stationID,
                    grid, this, this, this,
                    getChargerPositionAtStation(stationID, batteryID),
                    Orientation.EAST);
            robots.add(robot);
            return robot;
        }
        return null;
    }

    public Robot addRobotAtWaypoint(Position pos, Orientation facing, Position target) {
        if (grid.posType(pos) == PositionType.WAYPOINT) {
            int robotID = Robot.incrementID();
            Robot robot = new Robot(
                    robotID,
                    getStationWithFreeBattery().getStationID(),
                    grid, this, this, this,
                    pos, facing, target);
            robots.add(robot);
            return robot;
        }
        return null;
    }

    public void refresh() {
        try {
            for (IRobot robot : robots)
                robot.refresh();
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isBlockedByRobot(Position pos) {
        for (IRobot r : robots) {
            if (r.pos().equals(pos))
                return true;
        }
        return false;
    }

    public ServerGridManagement getGrid() {
        return grid;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public int getUnloadingRange() {
        return unloadingRange;
    }

    public void setUnloadingRange(int unloadingRange) {
        this.unloadingRange = unloadingRange;
    }

    @Override
    public int requestNextStation(int robotID, boolean charge) {
        if (charge) {
            Station station = getStationWithFreeBattery();
            station.reserveBatteryForRobot(robotID);
            return station.getStationID();
        } else {
            return getStationWithMinQueue().getStationID();
        }
    }

    @Override
    public int requestFreeChargerAtStation(int robotID, int stationID) {
        Station station = getStationByID(stationID);
        return station.getBatteryReservedForRobot(robotID);
    }

    @Override
    public boolean requestEnqueueAtStation(int robotID, int stationID) {
        Station station = getStationByID(stationID);
        if (station.hasFreeQueuePosition()) {
            station.addToQueue();
            return true;
        }
        return false;
    }

    @Override
    public void reportChargingAtStation(int robotID, int stationID) {
        // No logic needed here except log
    }

    @Override
    public void reportEnqueueAtStation(int robotID, int stationID) {
        getStationByID(stationID).unregisterBatteryWithRobotIfPresent(robotID);
    }

    @Override
    public void reportLeaveStation(int robotID, int stationID) {
        getStationByID(stationID).takeFromQueue();
    }

    @Override
    public void reportUnloadingPackage(int robotID, int packageID) {

    }

    @Override
    public Position getArrivalPositionAtStation(int stationID) {
        return grid.getArrivalPositionAtStation(stationID);
    }

    @Override
    public Position getChargerPositionAtStation(int stationID, int chargerID) {
        return grid.getChargerPositionAtStation(stationID, chargerID);
    }

    @Override
    public Position getQueuePositionAtStation(int stationID) {
        Station station = getStationByID(stationID);
        return grid.getQueuePositionAtStation(stationID, station.getQueueSize());
    }

    @Override
    public Position getLoadingPositionAtStation(int stationID) {
        return grid.getLoadingPositionAtStation(stationID);
    }

    @Override
    public Position getUnloadingPositionFromID(int unloadingID) {
        return grid.getUnloadingPositionFromID(unloadingID);
    }

    private Station getStationWithFreeBattery() {
        return stations
                .stream()
                .filter(Station::hasFreeBattery)
                .findFirst()
                .orElseGet(this::addNewStation);
    }

    private int getRandomUnloadingID() {
        Random r = new Random();
        return r.nextInt(unloadingRange);
    }

    private Station getStationWithMinQueue() {
        Optional<Station> station = stations.stream()
                .min(Station::compareByQueueSize);
        if (station.isPresent()) {

            // Only returns station if free queue positions are available
            int queueSize = station.get().getQueueSize();
            if (queueSize < ServerGridManagement.QUEUE_SIZE)
                return station.get();
        }

        // If no free queue positions are available create new Stations
        return addNewStation();
    }

    private Station addNewStation() {
        Station created = Station.getInstance();
        stations.add(created);
        return created;
    }

    private Station getStationByID(int stationID) {
        Optional<Station> station = stations.stream()
                .filter(s -> s.getStationID() == stationID).findFirst();
        if (station.isPresent())
            return station.get();
        throw new NullPointerException("No Station with id " + stationID);
    }

    @Override
    public boolean hasPackage(int stationID) {
        return true;
    }

    @Override
    public int getPackageID(int stationID) {
        return getRandomUnloadingID();
    }

    public void close() {
        for (IRobot robot : robots)
            robot.close();
    }
}
