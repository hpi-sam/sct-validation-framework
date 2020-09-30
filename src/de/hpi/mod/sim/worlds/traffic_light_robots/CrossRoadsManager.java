package de.hpi.mod.sim.worlds.traffic_light_robots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.ICellType;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.RobotGridManager;

/**
 * Represents the Map and contains all logic which is dependant of the
 * implementation of the map. The Position (0, 0) is the upper left block of a
 * station. (2, 0) is therefore the first loading position on the right side of
 * the coordinate system. All cells which have a Y value below or equal to 0 are
 * in a station.
 */
public class CrossRoadsManager extends RobotGridManager {

    /**
     * Contains all traffic lights, from lines from bottom to top and from left to right
     */
    private List<TrafficLight> lights = new ArrayList<>(0);
    private int lightsPerRow, lightsPerCol;

    public CrossRoadsManager() {
        super();
        updateFieldSize(TrafficLightsConfiguration.getFieldWidth(), TrafficLightsConfiguration.getFieldHeight());
    }


    public void updateFieldSize(int width, int height) {
        lightsPerRow = width / 3;
        lightsPerCol = height / 3;
        lights = new ArrayList<>(lightsPerCol * lightsPerRow);
    }
    
    private TrafficLight getLightForCrossroad(int x, int y) {
        int index = y * lightsPerRow + x;
        if (lights.size() > index)
            return lights.get(index);
        return null;
    }

    /**
     * The {@link ICellType} of the given Position.
     * 
     * @param position The Position
     * @return ICellType
     */
    @Override
    public CellType cellType(Position position) {
        if (position.getY() >= 0 && position.getY() < TrafficLightsConfiguration.getFieldHeight()
         && position.getX() >= 0 && position.getX() < TrafficLightsConfiguration.getFieldWidth()) {

            // Each third
            if (position.getY() % 3 == 0 && position.getX() % 3 == 0)
                return CellType.BLOCK;
            if (isWaypoint(position.getX(), position.getY()))
                return getWaypointCellType(position.getX(), position.getY());
            return CellType.CROSSROAD;
        } else {
            return CellType.BLOCK;
        }
    }

    private boolean isWaypoint(int x, int y) {
        return (x % 3 == 0) ^ (y % 3 == 0);
    }

    private boolean isArrivalPoint(int x, int y) {
        return (x == 0 && y % 3 == 2) 
            || (x % 3 == 1 && y == 0) 
            || (x == TrafficLightsConfiguration.getFieldWidth()-1 && y % 3 == 1) 
            || (x % 3 == 2 && y == TrafficLightsConfiguration.getFieldHeight()-1);
    }

    /**
     * Returns true if and only if there is a traffic light at position x,y and it is green
     */
    private CellType getWaypointCellType(int x, int y) {
        if (isArrivalPoint(x, y))
            return CellType.ARRIVAL_POINT;

        if (x % 3 == 2 && y % 3 == 0) {
            TrafficLight light = getLightForCrossroad(x / 3, y / 3);
            if (light == null)
                return CellType.PURE_WAYPOINT;
            if (light.isGreenSouth())
                return CellType.TRAFFIC_LIGHT_GREEN_SOUTH;
            return CellType.TRAFFIC_LIGHT_RED_SOUTH;
        }
        if (x % 3 == 0 && y % 3 == 1) {
             TrafficLight light = getLightForCrossroad(x / 3, y / 3);
            if (light == null)
                return CellType.PURE_WAYPOINT;
            if (light.isGreenWest())
                return CellType.TRAFFIC_LIGHT_GREEN_WEST;
            return CellType.TRAFFIC_LIGHT_RED_WEST;
        }
        if (x % 3 == 1 && y % 3 == 0) {
             TrafficLight light = getLightForCrossroad(x / 3, y / 3 - 1);
            if (light == null)
                return CellType.PURE_WAYPOINT;
            if (light.isGreenNorth())
                return CellType.TRAFFIC_LIGHT_GREEN_NORTH;
            return CellType.TRAFFIC_LIGHT_RED_NORTH;
        }
        if (x % 3 == 0 && y % 3 == 2) {
             TrafficLight light = getLightForCrossroad(x / 3 - 1, y / 3);
            if (light == null)
                return CellType.PURE_WAYPOINT;
            if (light.isGreenEast())
                return CellType.TRAFFIC_LIGHT_GREEN_EAST;
            return CellType.TRAFFIC_LIGHT_RED_EAST;
        }
        return null;     
    }

    /**
     * Whether the robot can stand on this position
     * 
     * @param position The position of the Robot
     */
    @Override
    public boolean isBlockedByMap(Position position) {
        return cellType(position) == CellType.BLOCK;
    }

    /**
     * Returns the direction the target is pointing to
     * 
     * @param facing   The orientation the robot is looking at
     * @param position The position of the robot
     * @param target   The position of the target
     * 
     * @return the direction
     */
    @Override
    public Direction targetDirection(Orientation facing, Position current, Position target) {

        // Catch special case: If robot is already ON target position, return a random
        // direction
        if (current.equals(target)) {
            return Direction.random();
        }

        ICellType currentType = cellType(current);
        ICellType targetType = cellType(target);

        if (currentType == CellType.CROSSROAD) {
            Position target_crossroad = getSouthwestCornerOfCrossroad(target);
            Position current_crossroad = getSouthwestCornerOfCrossroad(current);

            // B1: If target is also on the same crossroad...
            if (targetType == CellType.CROSSROAD && target_crossroad.equals(current_crossroad)) {
                // ...then forward/backward with priority over left/right)
                return targetDirectionPrioForward(facing, current, target);

                // B2: Otherwise...
            } else {
                return targetDirectionOnCrossroad(facing, current_crossroad, target);
            }

            // CASE C: Robot on waypoint...
        } else if (CellType.isWaypoint((CellType) currentType)) {
            Position target_waypoint = getSouthwestCornerOfWaypoint(target);
            Position current_waypoint = getSouthwestCornerOfWaypoint(current);

            // C1: If target is also on the same waypoint...
            if (CellType.isWaypoint((CellType) targetType) && target_waypoint.equals(current_waypoint)) {
                // ...then handle it as if it were a station
                return targetDirectionPrioForward(facing, current, target);

                // C2: If robot is facing a croassroad...
            } else if (cellType(Position.nextPositionInOrientation(facing, current)) == CellType.CROSSROAD) {
                Position target_crossroad = getSouthwestCornerOfCrossroad(target);
                Position upcoming_crossroad = getSouthwestCornerOfCrossroad(
                        Position.nextPositionInOrientation(facing, current));

                // C2.1: If target is on the faced crossroad ...
                if (targetType == CellType.CROSSROAD && target_crossroad.equals(upcoming_crossroad)) {
                    return Direction.AHEAD;

                    // C2.2: Target is elsewehere
                } else {
                    return targetDirectionOnCrossroad(facing, upcoming_crossroad, target);
                }

                // C3: If robot is sideways on the waypoint...
            } else {
                int steps_ahead = forwardStepsToTarget(facing, current, target);
                int steps_right = rightStepsToTarget(facing, current, target);

                // If on front pieve of waypoint, increase
                if (!CellType.isWaypoint((CellType) cellType(Position.nextPositionInOrientation(facing, current)))) {
                    steps_ahead += 1;
                }

                // Return correct direction (relative to
                if (steps_right < 0) {
                    if (steps_ahead > 2) {
                        return Direction.AHEAD;
                    } else if (steps_ahead < 0) {
                        return Direction.BEHIND;
                    } else {
                        return Direction.LEFT;
                    }
                } else if (steps_right > 0) {
                    if (steps_ahead > 1) {
                        return Direction.AHEAD;
                    } else if (steps_ahead < -1) {
                        return Direction.BEHIND;
                    } else {
                        return Direction.RIGHT;
                    }
                } else {
                    if (steps_ahead > 0) {
                        return Direction.AHEAD;
                    } else {
                        return Direction.BEHIND;
                    }
                }
            }

        }

        // If nothing applies: return random direction
        return Direction.random();

    }

    /**
     * Returns the orientation the target is pointing to for robots that are inside
     * a station
     * 
     * Only returns a valid Direction if the given position is in a station
     * 
     * @param facing   The orientation the robot is looking at
     * @param position The position of the robot
     * @param target   The position of the target
     * 
     * @return the Direction
     */
    Direction targetDirectionPrioForward(Orientation facing, Position current, Position target) { 
        // Catch special case: If robot is already ON target position, return a random
        // direction
        if (current.equals(target)) {
            return Direction.random();
        }

        // Calculate steps to front / back and right / left
        int steps_ahead = forwardStepsToTarget(facing, current, target);
        int steps_right = rightStepsToTarget(facing, current, target);

        // Try to go forward or backward first
        if (steps_ahead > 0 && steps_ahead >= Math.abs(steps_right)) {
            return Direction.AHEAD;
        } else if (steps_ahead < 0 && steps_ahead <= -Math.abs(steps_right)) {
            return Direction.BEHIND;

            // If no forward/backword steps are needed, go left or right
        } else {
            if (steps_right < 0) {
                return Direction.LEFT;
            } else {
                return Direction.RIGHT;
            }
        }
    }

    /**
     * Returns the orientation the target is pointing to for robots on a corssroad.
     * 
     * Only returns a valid Direction if the given position is on a crossroad and th
     * target is not on the same crossroad.
     * 
     * @param facing   The orientation the robot is looking at
     * @param position The position of the robot
     * @param target   The position of the target
     * 
     * @return the Direction
     */
    Direction targetDirectionOnCrossroad(Orientation facing, Position current, Position target) {
        // Catch special case: If robot is already ON target position, return a random
        // direction
        if (current.equals(target)) {
            return Direction.random();
        }

        // Calculate steps to front / back and right / left (from crossraod lower left
        // point)
        Position crossroad_lowerleft = getLowerLeftCornerOfCrossroad(facing, current);
        int steps_ahead = forwardStepsToTarget(facing, crossroad_lowerleft, target);
        int steps_right = rightStepsToTarget(facing, crossroad_lowerleft, target);

        // Return correct direction (relative to
        if (steps_right < 0) {
            if (steps_ahead > 2) {
                return Direction.AHEAD;
            } else if (steps_ahead < 0) {
                return Direction.BEHIND;
            } else {
                return Direction.LEFT;
            }
        } else if (steps_right > 1) {
            if (steps_ahead > 1) {
                return Direction.AHEAD;
            } else if (steps_ahead < -1) {
                return Direction.BEHIND;
            } else {
                return Direction.RIGHT;
            }
        } else {
            if (steps_ahead > 0) {
                return Direction.AHEAD;
            } else {
                return Direction.BEHIND;
            }
        }
    }

    /**
     * Returns the number of steps ahead needed to reach the target
     * 
     * @param facing   The orientation the robot is looking at
     * @param position The position of the robot
     * @param target   The position of the target
     * 
     * @return the number of forward steps to the target "row" (backwards steps are
     *         negative values)
     */
    int forwardStepsToTarget(Orientation facing, Position current, Position target) {
        int delta_x = target.getX() - current.getX();
        int delta_y = target.getY() - current.getY();
        if (facing == Orientation.NORTH) {
            return delta_y;
        } else if (facing == Orientation.WEST) {
            return -delta_x;
        } else if (facing == Orientation.SOUTH) {
            return -delta_y;
        } else {
            return delta_x;
        }
    }

    /**
     * Returns the number of steps to the right needed to reach the target
     * 
     * @param facing   The orientation the robot is looking at
     * @param position The position of the robot
     * @param target   The position of the target
     * 
     * @return the number of right steps to the target "row" (left steps are
     *         negative values)
     */
    int rightStepsToTarget(Orientation facing, Position current, Position target) {
        int delta_x = target.getX() - current.getX();
        int delta_y = target.getY() - current.getY();
        if (facing == Orientation.NORTH) {
            return delta_x;
        } else if (facing == Orientation.WEST) {
            return delta_y;
        } else if (facing == Orientation.SOUTH) {
            return -delta_x;
        } else {
            return -delta_y;
        }
    }

    /**
     * Returns the waypoint neighboring the given crossroad in the given side.<br>
     * 
     * @param cross   The bottom left cell of the crossroad
     * @param side    The side of the waypoint
     * @param onCross Switches Modes. If true all outgoing waypoints of the
     *                crossroad are considered, if false all incoming ones.
     * @return The waypoint
     */
    public Position getWaypointOfCrossroad(Position cross, Orientation side, boolean onCross) {
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
     * Returns the left bottom cell of the crossroad which the position and facing
     * points to.<br>
     * Position has to be on a waypoint.
     * 
     * @param facing   The orientation the Robot is facing
     * @param position The position of the waypoint
     * @return left bottom cell of the crossroad
     */
    public Position getSouthwestCornerOfUpcomingCrossroad(Orientation facing, Position position) {
        Position next = Position.nextPositionInOrientation(facing, position);
        return getSouthwestCornerOfCrossroad(next);
    }

    /**
     * Returns the "lower left" cell of the crossroad to which the position points
     * relative to the current orientation. Only returns a valid Position if the
     * given position is on a crossroad
     * 
     * @param facing   The orientation the Robot is facing
     * @param position A Position on a Crossroad
     * @return The (relative) lower left cell of the Crossroad
     */
    public Position getLowerLeftCornerOfCrossroad(Orientation facing, Position position) {
        Position crossroad_absolute = getSouthwestCornerOfCrossroad(position);
        if (facing == Orientation.NORTH) {
            return new Position(crossroad_absolute.getX(), crossroad_absolute.getY());
        } else if (facing == Orientation.EAST) {
            return new Position(crossroad_absolute.getX(), crossroad_absolute.getY() + 1);
        } else if (facing == Orientation.WEST) {
            return new Position(crossroad_absolute.getX() + 1, crossroad_absolute.getY());
        } else {
            return new Position(crossroad_absolute.getX() + 1, crossroad_absolute.getY() + 1);
        }
    }

    /**
     * Returns the left bottom cell of the crossroad to which the position points.
     * Only returns a valid Position if the given position is on a crossroad
     * 
     * @param position A Position on a Crossroad
     * @return The left bottom cell of the Crossroad
     */
    public Position getSouthwestCornerOfCrossroad(Position position) {
        int x = position.getX() - (Math.floorMod(position.getX(), 3) - 1);
        int y = (position.getY() / 3) * 3 + 1;
        return new Position(x, y);
    }

    /**
     * Returns the southwest (i.e. bottom left) cell of the waipint that the
     * position is on. Only returns a valid Position if the given position is on a
     * waypoint
     * 
     * @param position A Position on a Crossroad
     * @return The left bottom cell of the Crossroad
     */
    public Position getSouthwestCornerOfWaypoint(Position position) {
        int x = position.getX(), y = position.getY();
        if (Math.floorMod(x, 3) == 2 && y % 3 == 0) {
            x = x - 1;
        } else if (x % 3 == 0 && Math.floorMod(y, 3) == 2) {
            y = y - 1;
        }
        return new Position(x, y);
    }

    /**
     * All 4 position of cells in a Crossroad
     * 
     * @param any position on a crossroad
     * @return Cells in Crossroad
     */
    List<Position> getCellsOfCrossroad(Position position) {
        return getCellsOfCrossroad(position, new ArrayList<Position>());
    }

    /**
     * All 4 position of cells in a Crossroad with additional list of positions to
     * ignore
     * 
     * @param position        a position on a corssroad
     * @param ignorePositions Positions to ignore in output
     * @return Cells in Crossroad
     */
    List<Position> getCellsOfCrossroad(Position position, ArrayList<Position> ignorePositions) {
        // get the reference position of the corssroad (i.e. the southwest corner).
        Position crossroad = getSouthwestCornerOfCrossroad(position);
        // Get all four positions that should belong to a croassroad
        List<Position> cells = new ArrayList<Position>(
                Arrays.asList(crossroad, new Position(crossroad.getX() + 1, crossroad.getY()),
                        new Position(crossroad.getX() + 1, crossroad.getY() + 1),
                        new Position(crossroad.getX(), crossroad.getY() + 1)));

        // Remove any of the corssroad positions that are to be ignored
        cells.removeAll(ignorePositions);

        // Return filtered results
        return cells;
    }

    public List<TrafficLight> getTrafficLights() {
        return lights;
    }

    /**
     * If the waypoints left, ahead and right of the crossroad which the position
     * faces are blocked.<br>
     * Position has to be on a traffic light
     *
     * @param facing   The orientation the Robot is facing
     * @param position A position on a traffic light
     * @return The waypoints left, ahead and right of the next crossroad
     */
    public boolean[] blockedWaypoint(Orientation facing, Position position) {
        // All Directions except BEHIND
        Direction[] directions = new Direction[] { Direction.LEFT, Direction.AHEAD, Direction.RIGHT };
        Orientation[] sides = Arrays.stream(directions).map(direction -> Orientation.rotate(facing, direction))
                .toArray(Orientation[]::new);

        Position crossroad = getSouthwestCornerOfUpcomingCrossroad(facing, position);
        Position[] waypoints = Arrays.stream(sides).map(side -> getWaypointOfCrossroad(crossroad, side))
                .toArray(Position[]::new);

        // No stream because of primitive type array
        boolean[] blocked = new boolean[3];
        for (int i = 0; i < waypoints.length; i++) {
            blocked[i] = isBlockedByMap(waypoints[i]) || isBlockedByRobot(waypoints[i]);
        }
        return blocked;
    }
    
    /**
     * Returns the outhoings waypoints neighboring the given crossroad in the given side.<br>
     * 
     * @param cross   The bottom left cell of the crossroad
     * @param side    The side of the waypoint
     * @return The waypoint
     */
    public Position getWaypointOfCrossroad(Position cross, Orientation side) {
        switch (side) {
            case NORTH:
                return new Position(cross.getX() + 1, cross.getY() + 2);
            case EAST:
                return new Position(cross.getX() + 2, cross.getY());
            case SOUTH:
                return new Position(cross.getX(), cross.getY() - 1);
            case WEST:
                return new Position(cross.getX() - 1, cross.getY() + 1);
            default:
                throw new IllegalArgumentException();
        }
    }

    public TrafficLight addTrafficLight(TrafficLight trafficLight) {
        Position pos = trafficLight.getSouthPosition();
        int x = (pos.getX() - 2) / 3;
        int y = pos.getY() / 3;
        lights.add(y * lightsPerRow + x, trafficLight);
		return trafficLight;
	}

    /**
     * Creates and adds new Robot at given Position if it is a Waypoint, with given
     * Orientation and target. This should only be used for Debug-Scenarios, since
     * the Robots may be in an invalid state after reaching their targets
     *
     * @param position              The Waypoint where the Robot will be placed
     * @param state
     * @param facing                The Orientation of the Robot at its starting
     *                              position
     * @param fuzzyEnd              whether or not the robot is allowed to be near
     *                              the last target or has to be exactly on it
     * @param hasReservedCharger    whether or not the robot should drive to charger
     * @param hardArrivedConstraint whether
     * @param target                The target of the Robot to drive to
     * @return The added Robot or NULL if the Position is not a Waypoint
     */
    // public TrafficLightRobot addRobotAtPosition(Position position, Orientation facing,
    //         Position target, int delay, int initialDelay, boolean fuzzyEnd,
    //         boolean hardArrivedConstraint) {

    //     TrafficLightRobot robot = new TrafficLightRobot(this, position, facing, target, delay, initialDelay,
    //             fuzzyEnd, hardArrivedConstraint);
    //     addRobot(robot);
    //     return robot;
    // }

    // public TrafficLightRobot addRobotInScenario(Position position, Orientation facing, int delay) {

    //     if (posType(position) == PositionType.STATION || posType(position) == PositionType.WAYPOINT) {
    //         TrafficLightRobot robot = new TrafficLightRobot(TrafficLightRobot.incrementID(), this, position,
    //                 facing, target, delay);
    //         addRobot(robot);
    //         return robot;
    //     } else {
    //         throw new IllegalStateException(
    //                 "Illegal initial position for scenario robot. Please contact the mod chair");
    //     }
    // }


    // @Override
    // public TrafficLightRobot addRobot() {
    //     int robotID = TrafficLightRobot.incrementID();
    //     TrafficLightRobot robot = new TrafficLightRobot(robotID, this, Orientation.EAST);
    //     addRobot(robot);
    //     return robot;
    // }

}
