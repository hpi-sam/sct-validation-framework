package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.*;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.robot.Robot.RobotState;
import de.hpi.mod.sim.env.SimulatorConfig;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class Simulator implements IRobotController, ILocation, IScanner {

    private List<Robot> robots = new CopyOnWriteArrayList<>();
    private ServerGridManagement grid;
    private IRobotStationDispatcher stations;
    private int unloadingRange = SimulatorConfig.getUnloadingRange();


    public Simulator() {
        grid = new ServerGridManagement(this);
        stations = new StationManager(10);
    }

    /**
     * Creates a new Robot and adds it to a battery
     * @return The added Robot
     */
    public Robot addRobot() {
        // Get next unused ID
        int robotID = Robot.incrementID();
        int stationID = stations.getReservationNextForStation(robotID, true);
        int batteryID = stations.getReservedChargerAtStation(robotID, stationID);
        stations.reportChargingAtStation(robotID, stationID);

        Robot robot = new Robot(
                robotID,
                stationID,
                grid, stations, this, this,
                getBatteryPositionAtStation(stationID, batteryID),
                Orientation.EAST);
        robots.add(robot);
        return robot;
    }

    /**
     * Creates and adds new Robot at given Position if it is a Waypoint, with given Orientation and target.
     * This should only be used for Debug-Scenarios,
     * since the Robots may be in an invalid state after reaching their targets
     *
     * @param position The Waypoint where the Robot will be placed
     * @param state 
     * @param facing The Orientation of the Robot at its starting position
     * @param target The target of the Robot to drive to
     * @return The added Robot or NULL if the Position is not a Waypoint
     */
    public Robot addRobotAtPosition(Position position, RobotState state, Orientation facing, List<Position> targets) {
    	
        int robotID = Robot.incrementID();
        Robot robot = new Robot(
                robotID,
                0,
                grid, stations, this, this,
                position, state, facing, targets);
        robots.add(robot);
        return robot;
    }
    
    public Robot addRobotInScenarioHPI(Position position, Orientation facing, boolean inHardcoreMode) {

	if (grid.posType(position) == PositionType.STATION) {
        int robotID = Robot.incrementID();
        Robot robot = new Robot(
                robotID,
                (int) position.getX()/SimulatorConfig.getSpaceBetweenChargingStations(),
                grid, stations, this, this,
                position, facing, inHardcoreMode);
        robots.add(robot);
        return robot;
    } else {
    	throw new IllegalStateException("Illegal initial position for scenario robot. Please contact the mod chair");
    }
    }

    /**
     * Refreshes the Robots.
     */
    public void refresh() {
        for (Robot robot : robots)
            robot.refresh();
    }

    /**
     * Whether there is a Robot on the given Position
     * @param position The Position to check
     * @return true if there is a Robot on the Position
     */
    @Override
    public boolean isBlockedByRobot(Position position) {
        for (Robot robot : robots) {
            if (robot.pos().equals(position) || robot.oldPos().equals(position))
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
    public Position getArrivalPositionAtStation(int stationID) {
        return grid.getArrivalPositionAtStation(stationID);
    }

    @Override
    public Position getBatteryPositionAtStation(int stationID, int chargerID) {
        return grid.getChargerPositionAtStation(stationID, chargerID);
    }

    @Override
    public Position getLoadingPositionAtStation(int stationID) {
        return grid.getLoadingPositionAtStation(stationID);
    }

    @Override
    public Position getQueuePositionAtStation(int stationID) {
        return grid.getQueuePositionAtStation(stationID);
    }

    @Override
    public Position getUnloadingPositionFromID(int unloadingID) {
        return grid.getUnloadingPositionFromID(unloadingID);
    }

    private int getRandomUnloadingID(Position robotPosition) {
        int id = ThreadLocalRandom.current().nextInt(100) + 1;
        if(id > 70) {
        	id = ThreadLocalRandom.current().nextInt(3*unloadingRange/4, unloadingRange);
        } else if (id > 55) {
        	id = ThreadLocalRandom.current().nextInt(unloadingRange/2, 3*unloadingRange/4);
        } else if (id > 40) {
        	id = ThreadLocalRandom.current().nextInt(unloadingRange/4, unloadingRange/2);
        } else if (id > 20) {
        	id = ThreadLocalRandom.current().nextInt(0, unloadingRange/4);
        } else {
        	id = ThreadLocalRandom.current().nextInt(-unloadingRange, 0);
        }
        if(id!= 0) {
        	return id;
        } else {
        	return id + 1;
        }
    }

    @Override
    public boolean hasPackage(int stationID) {
        return true;
    }

    @Override
    public int getPackageID(int stationID, Position robotPosition) {
        return getRandomUnloadingID(robotPosition);
    }

    public void close() {
        for (Robot robot : robots)
            robot.close();
    }
}
