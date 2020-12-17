package de.hpi.mod.sim.worlds.infinitewarehouse.environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.ICellType;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.RobotGridManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouseConfiguration;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.WarehouseRobot;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IRobotDispatch;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IRobotStationDispatcher;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IScanner;

/**
 * Represents the Map and contains all logic which is dependant of the
 * implementation of the map. The Position (0, 0) is the upper left block of a
 * station. (2, 0) is therefore the first loading position on the right side of
 * the coordinate system. All cells which have a Y value below or equal to 0 are
 * in a station.
 */
public class WarehouseManager extends RobotGridManager implements ISensorDataProvider, IRobotDispatch, ILocation, IScanner {

	private List<Position> invalidPositions = new ArrayList<Position>();
	private IRobotStationDispatcher stations;
	private int unloadingRange = InfiniteWarehouseConfiguration.getUnloadingRange();
	private int mapHeight = InfiniteWarehouseConfiguration.getMapHeight();
	private int[] heights = new int[mapHeight];

	public WarehouseManager(IRobotStationDispatcher stations) {
		this.stations = stations;
	}

	/**
	 * The {@link ICellType} of the given Position.
	 * 
	 * @param position The Position
	 * @return ICellType
	 */
	@Override
	public ICellType cellType(Position position) {

		// Not in Station
		if (position.getY() > 0) {

			// Each third
			if (position.getY() % 3 == 0 && position.getX() % 3 == 0)
				return CellType.BLOCK;
			if (position.getX() % 3 == 0 || position.getY() % 3 == 0)
				return CellType.WAYPOINT;
			return CellType.CROSSROAD;
		} else {
			// Unused stations should be drawn differently
			int stationCount = InfiniteWarehouseConfiguration.getChargingStationsInUse();
			boolean isUsed = position.getX() < stationCount * 3 / 2 - stationCount % 2
					&& position.getX() >= -stationCount * 3 / 2 + stationCount % 2;

			if (position.getX() % 3 == 0 && position.getY() < 0 && position.getY() > -4)
				return isUsed ? CellType.CHARGER : CellType.CHARGER_UNUSED;
			if (position.getX() % 3 == 0 || position.getY() < -InfiniteWarehouseConfiguration.getQueueSize())
				return CellType.BLOCK;
			if (position.getY() == 0 && Math.floorMod(position.getX(), 3) == 2)
				return isUsed ? CellType.LOADING : CellType.LOADING_UNUSED;
			if (position.getY() < 0 && position.getY() > -InfiniteWarehouseConfiguration.getQueueSize()
					&& Math.floorMod(position.getX(), 3) == 2)
				return isUsed ? CellType.QUEUE : CellType.QUEUE_UNUSED;
			return isUsed ? CellType.STATION : CellType.STATION_UNUSED;
		}
	}

	/**
	 * Whether the robot can stand on this position
	 * 
	 * @param position The position of the Robot
	 */
	@Override
	public boolean isBlockedByMap(Position position) {
		if (cellType(position) == CellType.BLOCK)
			return true;

		boolean isInvalid = false;
		for (int i = 0; i < invalidPositions.size(); i++) {
			if (invalidPositions.get(i).is(position)) {
				isInvalid = true;
				break;
			}
		}
		return isInvalid;
	}

	@Override
	public boolean[] blocked(Orientation facing, Position position) {
		boolean blocked[] = super.blocked(facing, position);

		Direction[] directions = Direction.values();
		// All four orientations rotated by the facing of the Robot
		// Sides: Left, Ahead, Right, Bottom (Same order like in the enum Direction)
		Orientation[] sides = Arrays.stream(directions).map(dir -> Orientation.rotate(facing, dir))
				.toArray(Orientation[]::new);

		// In the Station a robot cannot enter a charger from the west
		// so we have to check, if the eastern neighbor is charger
		if (cellType(Position.nextPositionInOrientation(Orientation.EAST, position)) == CellType.CHARGER) {
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
	
	public PositionType posType(Position position) {
		return PositionType.get(cellType(position));
	}
	
	public boolean isInvalid(Position position) {
		ICellType cellType = cellType(position);
		if(cellType == CellType.BLOCK) {
			return true;
		}
		return false;
	}
	
	public boolean invalidManoeuvre(Position oldPos, Position pos) {
		int x_coordinate = Math.abs(pos.getX() % 3);
		int x_coordinateOld = Math.abs(oldPos.getX() % 3);
		if(pos.getY() >= 1) {
			return false;
		}
		
		if(pos.getX() < 0 || oldPos.getX() < 0) {
			if((x_coordinate == 1 && x_coordinateOld == 0) || (x_coordinate == 0 && x_coordinateOld == 1)) {
				return true;
			}
		} else {
			if((x_coordinate == 2 && x_coordinateOld == 0) || (x_coordinate == 0 && x_coordinateOld == 2)) {
				return true;
			}
		}
		if(x_coordinateOld == 1 && oldPos.getY() > -5) {
			if(x_coordinate == 2) {
				return true;
			}
		}
		if(x_coordinate % 3 == 1 && pos.getY() > -5) {
			if(x_coordinateOld == 2) {
				return true;
			}
		}
		return false;
	};
	
	public void makePositionInvalid(Position pos) {
		invalidPositions.add(pos);	
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
		List<Position> crossroadCells = getCellsOfCrossroad(crossroad, new ArrayList<Position>(Arrays.asList(position)));
		
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
		if(steps_ahead > 0 && steps_ahead >= Math.abs(steps_right)) {
			return Direction.AHEAD;
		}else if(steps_ahead < 0 && steps_ahead <= -Math.abs(steps_right)) {
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
		if (current.equals(target)) {
			return Orientation.random();
		}

		// If Position is in Station
		if (posType(current) == PositionType.STATION) {
			if (cellType(current) == CellType.CHARGER)
				return Orientation.EAST;
			if (current.getY() > target.getY())
				return Orientation.SOUTH;
			if (current.getX() > target.getX())
				return Orientation.WEST;
			if (current.getX() < target.getX()) {
				if (current.getY() > -InfiniteWarehouseConfiguration.getQueueSize())
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

	@Override
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

	@Override
	public Position getQueuePositionAtStation(int stationID) {
		int x;
		if((stationID & 1) == 0) {
			//even station ID means the station is on the right
			x = stationID/2 * 3 + 2;
		} else {
			//odd station ID means the station is on the left
			x = -(stationID + 1)/2 * 3 + 2;
		}
		return new Position(x, - InfiniteWarehouseConfiguration.getQueueSize());
	}

	@Override
	public Position getChargerPositionAtStation(int stationID, int chargerID) {
		int x;
		if((stationID % 2) == 0) {
			//even station ID means the station is on the right
			x = stationID/2 * 3;
		} else {
			//odd station ID means the station is on the left
			x = -(stationID + 1)/2 * 3;
		}
		int y = -1 - chargerID;
		return new Position(x, y);
	}

	@Override
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

	@Override
	public Position getUnloadingPositionFromID(int unloadingID) {
		int x, y;

		y = (Math.abs(unloadingID) % InfiniteWarehouseConfiguration.getMapHeight()) * 3 + 3;
		y += 3* InfiniteWarehouseConfiguration.getNotUsedRows();
		x = unloadingID / InfiniteWarehouseConfiguration.getMapHeight() * 3;
		
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
	 * Returns the "lower left" cell of the crossroad to which the position
	 * points relative to the current orientation.
	 * Only returns a valid Position if the given position is on a crossroad
	 * 
	 * @param facing   The orientation the Robot is facing
	 * @param position A Position on a Crossroad
	 * @return The (relative) lower left cell of the Crossroad
	 */
	public Position getLowerLeftCornerOfCrossroad(Orientation facing, Position position) {
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
	public Position getSouthwestCornerOfCrossroad(Position position) {
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
	public Position getSouthwestCornerOfWaypoint(Position position) {
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
	List<Position> getCellsOfCrossroad(Position position) {
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
	List<Position> getCellsOfCrossroad(Position position, ArrayList<Position> ignorePositions) {
		// get the reference position of the corssroad (i.e. the southwest corner).
		Position crossroad = getSouthwestCornerOfCrossroad(position);
		// Get all four positions that should belong to a croassroad
		List<Position> cells = new ArrayList<Position>(
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

	@Override
	public boolean affects(IHighlightable highlight, Position position) {
		if (highlight == null || !(highlight instanceof WarehouseRobot))
			return false;
		WarehouseRobot r = (WarehouseRobot) highlight;
		return position.is(r.pos()) || position.is(r.oldPos());
	}

	public int chargingStationsInUse(int currentHeight, int currentWidth) {
		float blockSize = InfiniteWarehouseConfiguration.getDefaultBlockSize();
		int widthBlocks = (int) (currentWidth / blockSize);
		int chargingStations = widthBlocks / InfiniteWarehouseConfiguration.getSpaceBetweenChargingStations();
		if (chargingStations % 2 != 0)
			chargingStations--;
		return chargingStations;
	}

	public int unloadingRange(int currentHeight, int currentWidth) {
		float blockSize = InfiniteWarehouseConfiguration.getDefaultBlockSize();
		int heightBlocks = (int) (currentHeight / blockSize);
		int widthBlocks = (int) (currentWidth / blockSize);
		int unloadingRange = (widthBlocks / 3) * ((heightBlocks - InfiniteWarehouseConfiguration.getQueueSize()) / 3);
		return unloadingRange;
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
	public WarehouseRobot addRobotAtPosition(Position position, WarehouseRobot.RobotState state, Orientation facing,
			List<Position> targets, int delay, int initialDelay, boolean fuzzyEnd, boolean unloadingTest,
			boolean hasReservedCharger, boolean hardArrivedConstraint) {

		WarehouseRobot robot = new WarehouseRobot(this, stations, position, state, facing, targets, delay, initialDelay,
				fuzzyEnd, unloadingTest, hasReservedCharger, hardArrivedConstraint);
		addRobot(robot);
		return robot;
	}
	
	public WarehouseRobot createScenarioRobot(Position position, Orientation facing, int delay) {

		if (posType(position) == PositionType.STATION || posType(position) == PositionType.WAYPOINT) {
			int stationID = stations.getStationIDFromPosition(position);
			WarehouseRobot robot = new WarehouseRobot(WarehouseRobot.incrementID(), stationID, this, stations, position, facing, delay);
			return robot;
		} else {
			throw new IllegalStateException(
					"Illegal initial position for scenario robot. Please contact the mod chair");
		}
	}
	
	@Override
	public int getPackageID(int stationID, Position robotPosition) {
		return getRandomUnloadingID(robotPosition);
	}
	
	private int getRandomUnloadingID(Position robotPosition) {
		int id = ThreadLocalRandom.current().nextInt(100) + 1;
		int min_pos = 0;
		int minimum = Integer.MAX_VALUE;

		if (id > 70) {
			id = ThreadLocalRandom.current().nextInt(3 * unloadingRange / 4, unloadingRange);
		} else if (id > 55) {
			id = ThreadLocalRandom.current().nextInt(unloadingRange / 2, 3 * unloadingRange / 4);
		} else if (id > 40) {
			id = ThreadLocalRandom.current().nextInt(unloadingRange / 4, unloadingRange / 2);
		} else if (id > 15) {
			id = ThreadLocalRandom.current().nextInt(0, unloadingRange / 4);
		} else {
			id = ThreadLocalRandom.current().nextInt(-unloadingRange, 0);
		}
		for (int i = 0; i < heights.length; i++) {
			if (heights[i] < minimum) {
				minimum = heights[i];
				min_pos = i;
			}
			if (heights[i] >= 100) {
				heights[i] = 0;
			}
		}
		if (heights[Math.abs(id) / mapHeight] > minimum) {
			heights[min_pos]++;
			if (id < 0) {
				id = (min_pos * mapHeight + id % mapHeight) * -1;
			} else {
				id = min_pos * mapHeight + id % mapHeight;
			}
		} else {
			heights[Math.abs(id) / mapHeight]++;
		}

		return id;
	}
	
	@Override
	public boolean hasPackage(int stationID) {
		return true;
	}
	
	@Override
	public void releaseAllLocks() {
		stations.releaseAllLocks();
	}
	
	@Override
	public void createNewStationManager(int chargingStationsInUse) {
		if (chargingStationsInUse != stations.getUsedStations()) {
			stations = new StationManager(chargingStationsInUse);
		}
	}

	@Override
	public WarehouseRobot addRobot() {
		int robotID = WarehouseRobot.incrementID();
		int stationID = stations.getReservationNextForStation(robotID, true);
		int chargerID = stations.getReservedChargerAtStation(robotID, stationID);
		stations.reportChargingAtStation(robotID, stationID, chargerID);
		WarehouseRobot robot = new WarehouseRobot(robotID, stationID, this, stations, getChargerPositionAtStation(stationID, chargerID),
				Orientation.EAST);
		addRobot(robot);
		return robot;
	}

}
