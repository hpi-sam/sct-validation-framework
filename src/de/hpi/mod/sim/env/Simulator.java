package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.*;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.SimulatorConfig;

import java.util.*;


public class Simulator implements IRobotController, ILocation, IScanner {

    public static final int DEFAULT_UNLOADING_RANGE = 10;

    private List<Robot> robots = new ArrayList<>();
    private ServerGridManagement grid;
    private IRobotStationDispatcher stations;

    /**
     * TODO
     */
    private int unloadingRange = DEFAULT_UNLOADING_RANGE;


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
     * @param pos The Waypoint where the Robot will be placed
     * @param facing The Orientation of the Robot at its starting position
     * @param target The target of the Robot to drive to
     * @return The added Robot or NULL if the Position is not a Waypoint
     */
    public Robot addRobotAtWaypoint(Position pos, Orientation facing, Position target) {

        // Check if Position is a Waypoint
        if (grid.posType(pos) == PositionType.WAYPOINT) {
            int robotID = Robot.incrementID();
            Robot robot = new Robot(
                    robotID,
                    0,
                    grid, stations, this, this,
                    pos, facing, target);
            robots.add(robot);
            return robot;
        }
        return null;
    }
    
    public Robot addRobotInScenarioHPI(Position pos, Orientation facing) {

	if (grid.posType(pos) == PositionType.STATION) {
        int robotID = Robot.incrementID();
        Robot robot = new Robot(
                robotID,
                (int) pos.getX()/SimulatorConfig.getSpaceBetweenChargingStations(),
                grid, stations, this, this,
                pos, facing);
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
     * @param pos The Position to check
     * @return true if there is a Robot on the Position
     */
    @Override
    public boolean isBlockedByRobot(Position pos) {
        for (Robot r : robots) {
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

    private int getRandomUnloadingID() {
        Random r = new Random();
        return r.nextInt(unloadingRange);
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
        for (Robot robot : robots)
            robot.close();
    }
}
