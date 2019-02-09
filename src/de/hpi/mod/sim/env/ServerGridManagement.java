package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.*;

import java.util.Arrays;

/**
 * Represents the Map and contains all logic which is dependant of the implementation of the map.
 * The Position (0, 0) is the upper left block of a station.
 * (2, 0) is therefore the first loading position on the right side of the coordinate system.
 * All cells which have a Y value below or equal to 0 are in a station.
 */
public class ServerGridManagement implements ISensorDataProvider {

    /**
     * Because the Map doesn't have direct access to newRobots,
     * it needs a controller to find out if a cells is blocked by a robot.
     */
    private IRobotController control;


    public ServerGridManagement(IRobotController control) {
        this.control = control;
    }

    /**
     * The {@link CellType} of the given Position.
     * @param pos The Position
     * @return CellType
     */
    @Override
    public CellType cellType(Position pos) {

        // Not in Station
        if (pos.getY() > 0) {

            // Each third
            if (pos.getY() % 3 == 0 && pos.getX() % 3 == 0)
                return CellType.BLOCK;
            if (pos.getX() % 3 == 0 || pos.getY() % 3 == 0)
                return CellType.WAYPOINT;
            return CellType.CROSSROAD;
        } else {
            if (pos.getX() % 3 == 0 && pos.getY() < -1 && pos.getY() > -5)
                return CellType.BATTERY;
            if (pos.getX() % 3 == 0 || pos.getY() < -SimulatorConfig.QUEUE_SIZE)
                return CellType.BLOCK;
            if (pos.getY() == 0 && Math.floorMod(pos.getX() , 3) == 2)
                return CellType.LOADING;
            return CellType.STATION;
        }
    }

    /**
     * Is another Robot on this position?
     * @param pos The position to check
     */
    public boolean isBlockedByRobot(Position pos) {
        return control.isBlockedByRobot(pos);
    }

    /**
     * Whether the robot can stand on this position
     * @param pos The position of the Robot
     */
    public boolean isBlockedByMap(Position pos) {
        return cellType(pos) == CellType.BLOCK;
    }

    /**
     * The type of cell the Robot stands on
     * @param pos The position of the Robot
     */
    @Override
    public PositionType posType(Position pos) {
        return cellType(pos).toPositionType();
    }

    /**
     * Checks all four neighbors of the cell and returns if they are blocked,
     * ordered by the facing
     * @param facing The orientation of the robot
     * @param pos The Position of the robot
     * @return Whether the neighbors are blocked (The directions are corresponding to the order in enum Directions)
     */
    @Override
    public boolean[] blocked(Orientation facing, Position pos) {

        // A boolean storing all directions
        boolean[] blocked = new boolean[4];

        Direction[] directions = Direction.values();

        // All four orientations rotated by the facing of the Robot
        // Sides: Left, Ahead, Right, Bottom (Same order like in the enum Direction)
        Orientation[] sides = Arrays.stream(directions)
                .map(dir -> Orientation.rotate(facing, dir))
                .toArray(Orientation[]::new);

        // All four neighbor positions of the robot in the same order as sides
        Position[] neighbors = Arrays.stream(sides)
                .map(side -> Position.nextPositionInOrientation(side, pos))
                .toArray(Position[]::new);

        // If a neighbor is blocked, its corresponding boolean is set to true
        for (int i = 0; i < blocked.length; i++) {
            blocked[i] = isBlockedByMap(neighbors[i]) || isBlockedByRobot(neighbors[i]);
        }

        // In the Station a robot cannot enter a battery from the west
        // so we have to check, if the eastern neighbor is battery
        if (cellType(Position.nextPositionInOrientation(Orientation.EAST, pos)) == CellType.BATTERY) {
            int i = Arrays.asList(sides).indexOf(Orientation.EAST);
            blocked[i] = true;
        }

        // In a station a robot cannot shortcut to the queue, it has to drive to the bottom
        // First, check from west to east
        if (Math.floorMod(pos.getX(), 3) == 1 && pos.getY() <= 0 && pos.getY() > -5) {
            int i = Arrays.asList(sides).indexOf(Orientation.EAST);
            blocked[i] = true;
        }
        // Than check from east to west
        else if (Math.floorMod(pos.getX(), 3) == 2 && pos.getY() <= 0 && pos.getY() > -5) {
            int i = Arrays.asList(sides).indexOf(Orientation.WEST);
            blocked[i] = true;
        }

        return blocked;
    }

    /**
     * If the waypoints left, ahead and right of the crossroad which the position faces are blocked.<br>
     * Position has to be on a waypoint or a crossroad.
     *
     * @param facing The orientation the Robot is facing
     * @param pos A position on a crossroad or waypoint
     * @return The waypoints left, ahead and right of the next crossroad
     */
    @Override
    public boolean[] blockedWaypoint(Orientation facing, Position pos) {
        // All Directions except BEHIND
        Direction[] directions = new Direction[] {
                Direction.LEFT, Direction.AHEAD, Direction.RIGHT
        };
        Orientation[] sides = Arrays.stream(directions)
                .map(dir -> Orientation.rotate(facing, dir))
                .toArray(Orientation[]::new);

        boolean onCross = posType(pos) == PositionType.CROSSROAD;
        Position cross = onCross ? getCrossroad(pos) : getFacingCrossroad(facing, pos);
        Position[] waypoints = Arrays.stream(sides)
                .map(side -> getWaypointOfCrossroad(cross, side, onCross))
                .toArray(Position[]::new);

        // No stream because of primitive type array
        boolean[] blocked = new boolean[3];
        for (int i = 0; i < waypoints.length; i++) {
            blocked[i] = isBlockedByMap(waypoints[i]) || isBlockedByRobot(waypoints[i]);
        }
        return blocked;
    }

    /**
     * Whether the crossroad ahead of the robot is blocked by other newRobots.<br>
     * Position has to be on a waypoint or a crossroad.
     * If its a crossroad the current crossroad is checked
     * @param facing The orientation the robot is looking at
     * @param pos The position of the robot
     * @return Whether it's blocked
     */
    @Override
    public boolean blockedCrossroadAhead(Orientation facing, Position pos) {
        boolean onCross = posType(pos) == PositionType.CROSSROAD;
        Position cross = onCross ? getCrossroad(pos) : getFacingCrossroad(facing, pos);
        Position[] crossroadCells = getCellsOfCrossroad(cross);

        for (Position crossroadCell : crossroadCells) {
            if (isBlockedByRobot(crossroadCell) || isBlockedByMap(crossroadCell))
                return true;
        }
        return false;
    }

    /**
     * Whether the crossroad right of the robot is blocked by other newRobots.<br>
     * Position has to be on a waypoint.
     * @param facing The orientation the robot is looking at
     * @param pos The position of the robot
     * @return Whether it's blocked
     */
    @Override
    public boolean blockedCrossroadRight(Orientation facing, Position pos) {
        Orientation right = facing.getTurnedRight();
        return blockedCrossroadAhead(right, pos);
    }

    /**
     * Returns the orientation the target is pointing to
     */
    @Override
    public Orientation targetOrientation(Position current, Position target) {

        // If Position is in Station
        if (posType(current) == PositionType.STATION) {
            if (cellType(current) == CellType.BATTERY)
                return Orientation.EAST;
            if (current.getY() > target.getY())
                return Orientation.SOUTH;
            if (current.getX() > target.getX())
                return Orientation.WEST;
            if (current.getX() < target.getX()) {
                if (current.getY() > -SimulatorConfig.QUEUE_SIZE)
                    return Orientation.SOUTH;
                return Orientation.EAST;
            }
            return Orientation.NORTH;
        }

        // Special Case
        // If target exactly below return EAST
        if (Position.nextPositionInOrientation(Orientation.SOUTH, current).equals(target))
            return Orientation.EAST;

        // If Position is on Waypoint
        if (posType(current) == PositionType.WAYPOINT) {
            if (current.getY() < target.getY())
                return Orientation.NORTH;
            if (current.getX() > target.getX() + 1)
                return Orientation.WEST;
            if (current.getX() < target.getX())
                return Orientation.EAST;
            return Orientation.SOUTH;
        }

        // If Position is on Crossroad
        // Repeated code because both cases should have different behaviours
        if (posType(current) == PositionType.CROSSROAD) {
            if (current.getY() < target.getY())
                return Orientation.NORTH;
            if (current.getX() > target.getX() + 1)
                return Orientation.WEST;
            if (current.getX() < target.getX())
                return Orientation.EAST;
            return Orientation.SOUTH;
        }

        return Orientation.SOUTH;
    }

    public Position getArrivalPositionAtStation(int stationID) {
        int x = stationID * 3 + 1;
        return new Position(x, 0);
    }

    public Position getQueuePositionAtStation(int stationID) {
        int x = stationID * 3 + 2;
        return new Position(x, -SimulatorConfig.QUEUE_SIZE);
    }

    public Position getChargerPositionAtStation(int stationID, int chargerID) {
        int x = stationID * 3;
        int y = -2 - chargerID;
        return new Position(x, y);
    }

    public Position getLoadingPositionAtStation(int stationID) {
        int x = stationID * 3 + 2;
        return new Position(x, 0);
    }

    public Position getUnloadingPositionFromID(int unloadingID) {
        int y = (unloadingID % SimulatorConfig.MAP_HEIGHT) * 3 + 4;
        int x = unloadingID / SimulatorConfig.MAP_HEIGHT * 3;
        return new Position(x, y);
    }

    /**
     * Returns the waypoint neighboring the given crossroad in the given side.<br>
     * @param cross The bottom left cell of the crossroad
     * @param side The side of the waypoint
     * @param onCross Switches Modes. If true all outgoing waypoints of the crossroad are considered,
     *                if false all incoming ones.
     * @return The waypoint
     */
    Position getWaypointOfCrossroad(Position cross, Orientation side, boolean onCross) {
        switch (side) {
            case NORTH:
                return new Position(onCross ? cross.getX() + 1 : cross.getX(), cross.getY() + 2);
            case EAST:
                return new Position(cross.getX() + 2, onCross ? cross.getY() : cross.getY() + 1);
            case SOUTH:
                return new Position(onCross ? cross.getX() : cross.getX() + 1, cross.getY() - 1);
            case WEST:
                return new Position(cross.getX() - 1, onCross ? cross.getY() + 1 : cross.getY());
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the left bottom cell of the crossroad which the position and facing points to.<br>
     * Position has to be on a waypoint
     * @param facing The orientation the Robot is facing
     * @param pos The position of the waypoint
     * @return left bottom cell of the crossroad
     */
    Position getFacingCrossroad(Orientation facing, Position pos) {
        Position next = Position.nextPositionInOrientation(facing, pos);
        return getCrossroad(next);
    }

    /**
     * Returns the left bottom cell of the crossroad to which the position points.<br>
     * Only returns a valid Position if the given position is on a crossroad
     * @param pos A Position on a Crossroad
     * @return The left bottom cell of the Crossroad
     */
    Position getCrossroad(Position pos) { // -1, 2
        int x = pos.getX() - (Math.floorMod(pos.getX(), 3) - 1);
        int y = (pos.getY() / 3) * 3 + 1;
        return new Position(x, y);
    }

    /**
     * All 4 position of cells in a Crossroad
     * @param cross the bottom left position of a crossroad
     * @return Cells in Crossroad
     */
    Position[] getCellsOfCrossroad(Position cross) {
        return new Position[] {
                cross,
                new Position(cross.getX() + 1, cross.getY()),
                new Position(cross.getX() + 1, cross.getY() + 1),
                new Position(cross.getX(), cross.getY() + 1),
        };
    }
}
