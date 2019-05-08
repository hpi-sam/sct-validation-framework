package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the Map and contains all logic which is dependant of the
 * implementation of the map. The Position (0, 0) is the upper left block of a
 * station. (2, 0) is therefore the first loading position on the right side of
 * the coordinate system. All cells which have a Y value below or equal to 0 are
 * in a station.
 */
public class ServerGridManagement implements ISensorDataProvider {

	/**
	 * Because the Map doesn't have direct access to newRobots, it needs a
	 * controller to find out if a cells is blocked by a robot.
	 */
	private IRobotController control;
	private ArrayList<Position> invalidPositions = new ArrayList<Position>();

	public ServerGridManagement(IRobotController control) {
		this.control = control;
	}

	/**
	 * The {@link CellType} of the given Position.
	 * 
	 * @param position The Position
	 * @return CellType
	 */
	@Override
	public CellType cellType(Position position) {

		// Not in Station
		if (position.getY() > 0) {

			// Each third
			if (position.getY() % 3 == 0 && position.getX() % 3 == 0)
				return CellType.BLOCK;
			if (position.getX() % 3 == 0 || position.getY() % 3 == 0)
				return CellType.WAYPOINT;
			return CellType.CROSSROAD;
		} else {
			if (position.getX() % 3 == 0 && position.getY() < -1 && position.getY() > -5)
				return CellType.BATTERY;
			if (position.getX() % 3 == 0 || position.getY() < -SimulatorConfig.QUEUE_SIZE)
				return CellType.BLOCK;
			if (position.getY() == 0 && Math.floorMod(position.getX(), 3) == 2)
				return CellType.LOADING;
			if (position.getY() < 0 && position.getY() > -SimulatorConfig.QUEUE_SIZE
					&& Math.floorMod(position.getX(), 3) == 2)
				return CellType.QUEUE;
			return CellType.STATION;
		}
	}
	
	public boolean isInvalid(Position position) {
		CellType cellType = cellType(position);
		if(cellType == CellType.BLOCK) {
			return true;
		}
		return false;
	}
	
	public boolean invalidManoeuvre(Position oldPos, Position pos) {
		if(pos.getY() >= 1) {
			return false;
		}
		if(pos.getX() % 3 == 2 && oldPos.getX() % 3 == 0) {
			return true;
		}
		if(oldPos.getX() % 3 == 1 && oldPos.getY() > -5) {
			if(pos.getX() % 3 == 2) {
				return true;
			}
		}
		return false;
	};

	/**
	 * Is another Robot on this position?
	 * 
	 * @param position The position to check
	 */
	public boolean isBlockedByRobot(Position position) {
		return control.isBlockedByRobot(position);
	}

	/**
	 * Whether the robot can stand on this position
	 * 
	 * @param pos The position of the Robot
	 */
	public boolean isBlockedByMap(Position pos) {
		boolean isBlock = cellType(pos) == CellType.BLOCK;
		boolean isInvalid = false;
		for(int i=0; i<invalidPositions.size(); i++) {
			if(invalidPositions.get(i).is(pos)) {
				isInvalid = true;
				break;
			}
		}
		
		return isBlock || isInvalid;
	}
	
	public void makePositionInvalid(Position pos) {
		invalidPositions.add(pos);	
	}

	/**
	 * The type of cell the Robot stands on
	 * 
	 * @param position The position of the Robot
	 */
	@Override
	public PositionType posType(Position position) {
		return cellType(position).toPositionType();
	}

	/**
	 * Checks all four neighbors of the cell and returns if they are blocked,
	 * ordered by the facing
	 * 
	 * @param facing   The orientation of the robot
	 * @param position The Position of the robot
	 * @return Whether the neighbors are blocked (The directions are corresponding
	 *         to the order in enum Directions)
	 */
	@Override
	public boolean[] blocked(Orientation facing, Position position) {

		// A boolean storing all directions
		boolean[] blocked = new boolean[4];

		Direction[] directions = Direction.values();

		// All four orientations rotated by the facing of the Robot
		// Sides: Left, Ahead, Right, Bottom (Same order like in the enum Direction)
		Orientation[] sides = Arrays.stream(directions).map(dir -> Orientation.rotate(facing, dir))
				.toArray(Orientation[]::new);

		// All four neighbor positions of the robot in the same order as sides
		Position[] neighbors = Arrays.stream(sides).map(side -> Position.nextPositionInOrientation(side, position))
				.toArray(Position[]::new);

		// If a neighbor is blocked, its corresponding boolean is set to true
		for (int i = 0; i < blocked.length; i++) {
			blocked[i] = isBlockedByMap(neighbors[i]) || isBlockedByRobot(neighbors[i]);
		}

		// In the Station a robot cannot enter a battery from the west
		// so we have to check, if the eastern neighbor is battery
		if (cellType(Position.nextPositionInOrientation(Orientation.EAST, position)) == CellType.BATTERY) {
			int i = Arrays.asList(sides).indexOf(Orientation.EAST);
			blocked[i] = true;
		}

		// In a station a robot cannot shortcut to the queue, it has to drive to the
		// bottom
		// First, check from west to east
		if (Math.floorMod(position.getX(), 3) == 1 && position.getY() <= 0 && position.getY() > -5) {
			int i = Arrays.asList(sides).indexOf(Orientation.EAST);
			blocked[i] = true;
		}
		// Than check from east to west
		else if (Math.floorMod(position.getX(), 3) == 2 && position.getY() <= 0 && position.getY() > -5) {
			int i = Arrays.asList(sides).indexOf(Orientation.WEST);
			blocked[i] = true;
		}

		return blocked;
	}

	/**
	 * If the waypoints left, ahead and right of the crossroad which the position
	 * faces are blocked.<br>
	 * Position has to be on a waypoint or a crossroad.
	 *
	 * @param facing   The orientation the Robot is facing
	 * @param position A position on a crossroad or waypoint
	 * @return The waypoints left, ahead and right of the next crossroad
	 */
	@Override
	public boolean[] blockedWaypoint(Orientation facing, Position position) {
		// All Directions except BEHIND
		Direction[] directions = new Direction[] { Direction.LEFT, Direction.AHEAD, Direction.RIGHT };
		Orientation[] sides = Arrays.stream(directions).map(direction -> Orientation.rotate(facing, direction))
				.toArray(Orientation[]::new);

		boolean onCrossroad = posType(position) == PositionType.CROSSROAD;
		Position crossroad = onCrossroad ? getSouthwestCornerOfCrossroad(position) : getSouthwestCornerOfUpcomingCrossroad(facing, position);
		Position[] waypoints = Arrays.stream(sides).map(side -> getWaypointOfCrossroad(crossroad, side, onCrossroad))
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
	 * Position has to be on a waypoint or a crossroad. If its a crossroad the
	 * current crossroad is checked.
	 * 
	 * @param facing   The orientation the robot is looking at
	 * @param position The position of the robot
	 * @return Whether it's blocked
	 */
	@Override
	public boolean blockedCrossroadAhead(Orientation facing, Position position) {
		// Find out which crossroad to check
		Position crossroad = (posType(position) == PositionType.CROSSROAD) ? getSouthwestCornerOfCrossroad(position): getSouthwestCornerOfUpcomingCrossroad(facing, position);

		// Get all cells of croassroads and make sure that current position is ignored.
		ArrayList<Position> crossroadCells = getCellsOfCrossroad(crossroad, new ArrayList<Position>(Arrays.asList(position)));
		
		// check if any of of the cells are blocked.
		for (Position crossroadCell : crossroadCells) {
			if (isBlockedByRobot(crossroadCell) || isBlockedByMap(crossroadCell))
				return true;
		}
		return false;
	}

	/**
	 * Whether the crossroad right of the robot is blocked by other newRobots.<br>
	 * Position has to be on a waypoint.
	 * 
	 * @param facing   The orientation the robot is looking at
	 * @param position The position of the robot
	 * @return Whether it's blocked
	 */
	@Override
	public boolean blockedCrossroadRight(Orientation facing, Position position) {
		Orientation right = facing.getTurnedRight();
		return blockedCrossroadAhead(right, position);
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

		// Catch special case: If robot is already ON target position, return a random direction
		if(current.equals( target)) {
			return Direction.random();
		}
			
		
		// CASE A: Robot in Station...
		if(posType(current) == PositionType.STATION) {
			return targetDirectionInStation(facing, current, target);
		
			
		// CASE B: Robot on crossroad...
		} else if(posType(current) == PositionType.CROSSROAD) {
			Position target_crossroad = getSouthwestCornerOfCrossroad(target);
			Position current_crossroad = getSouthwestCornerOfCrossroad(current);
			
			// B1: If target is also on the same crossroad...
			if(posType(target) == PositionType.CROSSROAD && target_crossroad.equals(current_crossroad)) {
				// ...then handle it as if it were a station (i.e. forward/backward with priority over left/right)
				return targetDirectionInStation(facing, current, target);
				
			// B2: Otherwise...
			} else {
				return targetDirectionOnCrossroad(facing, current_crossroad, target);
			}
			
			
		// CASE C: Robot on waypoint...
		} else if(posType(current) == PositionType.WAYPOINT) {
			Position target_waypoint = getSouthwestCornerOfWaypoint(target);
			Position current_waypoint = getSouthwestCornerOfWaypoint(current);
			
			// C1: If target is also on the same waypoint...
			if(posType(target) == PositionType.WAYPOINT && target_waypoint.equals(current_waypoint)) {
				// ...then handle it as if it were a station
				return targetDirectionInStation(facing, current, target);
				
			// C2: If robot is facing a croassroad...
			}else if(posType(Position.nextPositionInOrientation(facing, current)) == PositionType.CROSSROAD){
				Position target_crossroad = getSouthwestCornerOfCrossroad(target);
				Position upcoming_crossroad = getSouthwestCornerOfCrossroad(Position.nextPositionInOrientation(facing, current));
				
				// C2.1: If target is on the faced crossroad ...
				if(posType(target) == PositionType.CROSSROAD && target_crossroad.equals(upcoming_crossroad)) {
					return Direction.AHEAD;
					
				// C2.2: Target is elsewehere
				} else {
					return targetDirectionOnCrossroad(facing, upcoming_crossroad, target);
				}
				
			// C3: If robot is sideways on the waypoint...
			}else{
				int steps_ahead = forwardStepsToTarget(facing, current, target);
				int steps_right = rightStepsToTarget(facing, current, target);
				
				// If on front pieve of waypoint, increase  
				if(posType(Position.nextPositionInOrientation(facing, current)) != PositionType.WAYPOINT) {
					steps_ahead += 1;
				}
				
				// Return correct direction (relative to 
				if(steps_right < 0) {
					if(steps_ahead > 2) {
						return Direction.AHEAD;
					}else if(steps_ahead < 0){
						return Direction.BEHIND;
					}else {
						return Direction.LEFT;
					}
				}else if(steps_right > 0){
					if(steps_ahead > 1) {
						return Direction.AHEAD;
					}else if(steps_ahead < -1){
						return Direction.BEHIND;
					}else {
						return Direction.RIGHT;
					}
				}else {
					if(steps_ahead > 0) {
						return Direction.AHEAD;
					}else {
						return Direction.BEHIND;
					}
				}
			}
			
		}
		
		// If nothing applies: return random direction
		return Direction.random();
		
	}
	
	/**
	 * Returns the orientation the target is pointing to for robots that are inside a station
	 * 
	 * Only returns a valid Direction if the given position is in a station
	 * 
	 * @param facing   The orientation the robot is looking at
	 * @param position The position of the robot
	 * @param target   The position of the target
	 * 
	 * @return the Direction
	 */
	Direction targetDirectionInStation(Orientation facing, Position current, Position target) {
		// Catch special case: If robot is already ON target position, return a random direction
		if(current.equals( target)) {
			return Direction.random();
		}
		
		// Calculate steps to front / back and right / left
		int steps_ahead = forwardStepsToTarget(facing, current, target);
		int steps_right = rightStepsToTarget(facing, current, target);
		
		// Try to go forward or backward first
		if(steps_ahead > 0) {
			return Direction.AHEAD;
		}else if(steps_ahead < 0) {
			return Direction.BEHIND;
			
		// If no forward/backword steps are needed, go left or right
		}else {
			if(steps_right < 0) {
				return Direction.LEFT;
			}else {
				return Direction.RIGHT;
			}
		}
	}
	
	/**
	 * Returns the orientation the target is pointing to for robots on a corssroad.
	 * 
	 * Only returns a valid Direction if the given position is on a crossroad and th target is 
	 * not on the same crossroad.
	 * 
	 * @param facing   The orientation the robot is looking at
	 * @param position The position of the robot
	 * @param target   The position of the target
	 * 
	 * @return the Direction
	 */
	Direction targetDirectionOnCrossroad(Orientation facing, Position current, Position target) {
		// Catch special case: If robot is already ON target position, return a random direction
		if(current.equals( target)) {
			return Direction.random();
		}
		
		// Calculate steps to front / back and right / left (from crossraod lower left point)
		Position crossroad_lowerleft = getLowerLeftCornerOfCrossroad(facing,current);
		int steps_ahead = forwardStepsToTarget(facing, crossroad_lowerleft, target);
		int steps_right = rightStepsToTarget(facing, crossroad_lowerleft, target);
		
		// Return correct direction (relative to 
		if(steps_right < 0) {
			if(steps_ahead > 2) {
				return Direction.AHEAD;
			}else if(steps_ahead < 0){
				return Direction.BEHIND;
			}else {
				return Direction.LEFT;
			}
		}else if(steps_right > 1){
			if(steps_ahead > 1) {
				return Direction.AHEAD;
			}else if(steps_ahead < -1){
				return Direction.BEHIND;
			}else {
				return Direction.RIGHT;
			}
		}else {
			if(steps_ahead > 0) {
				return Direction.AHEAD;
			}else {
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
	 * @return the number of forward steps to the target "row" (backwards steps are negative values)
	 */
	int forwardStepsToTarget(Orientation facing, Position current, Position target) {
		int delta_x = target.getX() - current.getX(); 
		int delta_y = target.getY() - current.getY(); 
		if(facing==Orientation.NORTH) {
			return delta_y; 
		}else if(facing==Orientation.WEST) {
			return -delta_x; 
		}else if(facing==Orientation.SOUTH) {
			return -delta_y; 
		}else {
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
	 * @return the number of right steps to the target "row" (left steps are negative values)
	 */
	 int rightStepsToTarget(Orientation facing, Position current, Position target) {
		int delta_x = target.getX() - current.getX(); 
		int delta_y = target.getY() - current.getY(); 
		if(facing==Orientation.NORTH) {
			return delta_x;
		}else if(facing==Orientation.WEST) {
			return delta_y;
		}else if(facing==Orientation.SOUTH) {
			return -delta_x;
		}else {
			return -delta_y;
		}
	}

	/**
	 * Returns the orientation the target is pointing to
	 */
	@Override
	@Deprecated
	public Orientation targetOrientation(Position current, Position target) {

		// If already ON target: return random orientation
		if(current.equals( target)) {
			return Orientation.random();
		}
		
		
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

		// If nothing applies :
		return Orientation.SOUTH;
	}

	public Position getArrivalPositionAtStation(int stationID) {
		int x;
		if((stationID % 2) == 0) {
			//even station ID means the station is on the right
			x = stationID/2 * 3 + 1;
		} else {
			//odd station ID means the station is on the left
			x = -(stationID + 1)/2 * 3 + 1;
		}
		return new Position(x, 0);
	}

	public Position getQueuePositionAtStation(int stationID) {
		int x;
		if((stationID & 1) == 0) {
			//even station ID means the station is on the right
			x = stationID/2 * 3 + 2;
		} else {
			//odd station ID means the station is on the left
			x = -(stationID + 1)/2 * 3 + 2;
		}
		return new Position(x, -SimulatorConfig.QUEUE_SIZE);
	}

	public Position getChargerPositionAtStation(int stationID, int chargerID) {
		int x;
		if((stationID % 2) == 0) {
			//even station ID means the station is on the right
			x = stationID/2 * 3;
		} else {
			//odd station ID means the station is on the left
			x = -(stationID + 1)/2 * 3;
		}
		int y = -2 - chargerID;
		return new Position(x, y);
	}

	public Position getLoadingPositionAtStation(int stationID) {
		int x;
		if((stationID % 2) == 0) {
			//even station ID means the station is on the right
			x = stationID/2 * 3 + 2;
		} else {
			//odd station ID means the station is on the left
			x = -(stationID + 1)/2 * 3 + 2;
		}
		return new Position(x, 0);
	}

	public Position getUnloadingPositionFromID(int unloadingID) {
		int x, y;

		y = (Math.abs(unloadingID) % SimulatorConfig.getMapHeight()) * 3 + 4;
		x = unloadingID / SimulatorConfig.getMapHeight() * 3;
		return new Position(x, y);
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
	 * Returns the left bottom cell of the crossroad which the position and facing
	 * points to.<br>
	 * Position has to be on a waypoint.
	 * 
	 * @param facing   The orientation the Robot is facing
	 * @param position The position of the waypoint
	 * @return left bottom cell of the crossroad
	 */
	Position getSouthwestCornerOfUpcomingCrossroad(Orientation facing, Position position) {
		Position next = Position.nextPositionInOrientation(facing, position);
		return getSouthwestCornerOfCrossroad(next);
	}

	/**
	 * Returns the "lower left" cell of the crossroad to which the position
	 * points relative to the current orientation.
	 * Only returns a valid Position if the given position is on a crossroad
	 * 
	 * @param facing   The orientation the Robot is facing
	 * @param position A Position on a Crossroad
	 * @return The (relative) lower left cell of the Crossroad
	 */
	Position getLowerLeftCornerOfCrossroad(Orientation facing, Position position) {
		Position crossroad_absolute = getSouthwestCornerOfCrossroad(position);
		if(facing == Orientation.NORTH) {
			return new Position(crossroad_absolute.getX(), crossroad_absolute.getY());
		}else if(facing == Orientation.EAST) {
			return new Position(crossroad_absolute.getX(), crossroad_absolute.getY()+1);
		}else if(facing == Orientation.WEST) {
			return new Position(crossroad_absolute.getX()+1, crossroad_absolute.getY());
		}else {
			return new Position(crossroad_absolute.getX()+1, crossroad_absolute.getY()+1);
		}
	}

	/**
	 * Returns the left bottom cell of the crossroad to which the position
	 * points.
	 * Only returns a valid Position if the given position is on a crossroad
	 * 
	 * @param position A Position on a Crossroad
	 * @return The left bottom cell of the Crossroad
	 */
	Position getSouthwestCornerOfCrossroad(Position position) {
		int x = position.getX() - (Math.floorMod(position.getX(), 3) - 1);
		int y = (position.getY() / 3) * 3 + 1;
		return new Position(x, y);
	}

	/**
	 * Returns the southwest (i.e. bottom left) cell of the waipint that the position
	 * is on.
	 * Only returns a valid Position if the given position is on a waypoint
	 * 
	 * @param position A Position on a Crossroad
	 * @return The left bottom cell of the Crossroad
	 */
	Position getSouthwestCornerOfWaypoint(Position position) {
		int x = position.getX(), y= position.getY();
		if(Math.floorMod(x, 3) == 2 && y % 3 == 0) {
			x = x - 1;
		}else if(x % 3 == 0 && Math.floorMod(y, 3) == 2) {
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
	ArrayList<Position> getCellsOfCrossroad(Position position) {
		return getCellsOfCrossroad(position, new ArrayList<Position>());
	}

	/**
	 * All 4 position of cells in a Crossroad with additional list of positions to
	 * ignore
	 * 
	 * @param position	a position on a corssroad
	 * @param ignorePositions	Positions to ignore in output
	 * @return Cells in Crossroad
	 */
	ArrayList<Position> getCellsOfCrossroad(Position position, ArrayList<Position> ignorePositions) {
		// get the reference position of the corssroad (i.e. the southwest corner).
		Position crossroad = getSouthwestCornerOfCrossroad(position);
		// Get all four positions that should belong to a croassroad
		ArrayList<Position> cells = new ArrayList<Position>(
				Arrays.asList(crossroad, 
						new Position(crossroad.getX() + 1, crossroad.getY()),
						new Position(crossroad.getX() + 1, crossroad.getY() + 1),
						new Position(crossroad.getX(), crossroad.getY() + 1)));
		
		// Remove any of the corssroad positions that are to be ignored
		cells.removeAll(ignorePositions);
		
		// Return filtered results
		return cells;
	}

	public void clearInvalidPositions() {
		invalidPositions.clear();
	}
}
