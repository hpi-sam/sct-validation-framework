package de.hpi.mod.sim.worlds.simpletrafficlights;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.ICellType;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotGridManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.ArrivalPoint;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.DeparturePoint;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.SimpleRobot;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TrafficLight;

/**
 * Represents the Map and contains all logic which is dependant of the
 * implementation of the map. The Position (0, 0) is the bottom left block of the 
 * whole grid and necessarily a wall.
 */
public class StreetNetworkManager extends RobotGridManager {

    private TrafficLight[] trafficLights;

    private ArrivalPoint[] arrivalPoints;
    List<ArrivalPoint> emptiestArrivalPoints = new ArrayList<>();
    
    private DeparturePoint[] departurePoints;
    List<DeparturePoint> emptyDeparturePoints = new ArrayList<>();
    List<DeparturePoint> occupiedDeparturePoints = new ArrayList<>();
    
    List<SimpleRobot> idleRobots = new ArrayList<>();

    public StreetNetworkManager() {
        super();
        resetFieldDataStructures();
    }


    public void resetFieldDataStructures() {
    	this.arrivalPoints = new ArrivalPoint[SimpleTrafficLightsConfiguration.getNumberOfTransferPoints()];
    	this.departurePoints = new DeparturePoint[SimpleTrafficLightsConfiguration.getNumberOfTransferPoints()];
    	this.trafficLights = new TrafficLight[SimpleTrafficLightsConfiguration.getNumberOfCrossroads()];
    }
    
    public TrafficLight getLightForCrossroad(int x, int y) {
        int index = y * SimpleTrafficLightsConfiguration.getVerticalStreets() + x;
        if (trafficLights.length > index)
            return trafficLights[index];
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
    	
    	// Is priamry field area
    	if (position.getY() >= 0 && position.getY() < SimpleTrafficLightsConfiguration.getFieldHeight()
         && position.getX() >= 0 && position.getX() < SimpleTrafficLightsConfiguration.getFieldWidth()) {

    		int blockSize = SimpleTrafficLightsConfiguration.getStreetLength() + SimpleTrafficLightsConfiguration.getCrossroadLength();
    		
    		// OPTION 1. Outside Border (except innermost line)
        	if (position.getY() < SimpleTrafficLightsConfiguration.getFieldBorderWidth() - 1 
        	        || position.getX() < SimpleTrafficLightsConfiguration.getFieldBorderWidth() - 1 
        			|| position.getY() > SimpleTrafficLightsConfiguration.getFieldHeight() - SimpleTrafficLightsConfiguration.getFieldBorderWidth() 
        	        || position.getX() > SimpleTrafficLightsConfiguration.getFieldWidth() - SimpleTrafficLightsConfiguration.getFieldBorderWidth() )
        		return CellType.WALL;
        	
    		int x2 = position.getX() - SimpleTrafficLightsConfiguration.getFieldBorderWidth();
    		int y2 = position.getY() - SimpleTrafficLightsConfiguration.getFieldBorderWidth();
    		        	
        	// OPTION 2. Street and/or Crossroad
    		if((x2+2) % blockSize <= 1 || (y2+2) % blockSize <= 1) {
    			
    			// OPTION 2.1.a Bottom Border (innermost line), including arrival and departure points 
            	if (position.getY() < SimpleTrafficLightsConfiguration.getFieldBorderWidth()){
            		if((x2+2) % blockSize == 0 && x2 < SimpleTrafficLightsConfiguration.getFieldWidth()-blockSize)
            			return CellType.ARRIVAL_POINT;
            		if((x2+2) % blockSize == 1 && x2 > 0)
            			return CellType.DEPARTURE_POINT;
            		return CellType.WALL;
            	}
    			// OPTION 2.1.b Top Border (innermost line), including arrival and departure points 
            	if (position.getY() >= SimpleTrafficLightsConfiguration.getFieldHeight() - SimpleTrafficLightsConfiguration.getFieldBorderWidth()){
            		if((x2+2) % blockSize == 0 && x2 < SimpleTrafficLightsConfiguration.getFieldWidth()-blockSize)
            			return CellType.DEPARTURE_POINT;
            		if((x2+2) % blockSize == 1 && x2 > 0)
            			return CellType.ARRIVAL_POINT;
            		return CellType.WALL;
            	}
    			// OPTION 2.1.c Left Border (innermost line), including arrival and departure points 
            	if (position.getX() < SimpleTrafficLightsConfiguration.getFieldBorderWidth()){
            		if((y2+2) % blockSize == 0 && y2 < SimpleTrafficLightsConfiguration.getFieldHeight()-blockSize)
            			return CellType.DEPARTURE_POINT;
            		if((y2+2) % blockSize == 1 && y2 > 0)
            			return CellType.ARRIVAL_POINT;
            		return CellType.WALL;
            	}
    			// OPTION 2.1.d Right Border (innermost line), including arrival and departure points 
            	if (position.getX() >= SimpleTrafficLightsConfiguration.getFieldWidth() - SimpleTrafficLightsConfiguration.getFieldBorderWidth()){
            		if((y2+2) % blockSize == 0 && y2 < SimpleTrafficLightsConfiguration.getFieldHeight()-blockSize)
            			return CellType.ARRIVAL_POINT;
            		if((y2+2) % blockSize == 1 && y2 > 0)
            			return CellType.DEPARTURE_POINT;
            		return CellType.WALL;
            	}
            	
    			// OPTION 2.2. Crossroad 
            	if((x2+2) % blockSize <= 1 && (y2+2) % blockSize <= 1)
            		return CellType.CROSSROAD;

    			// OPTION 2.3. Waiting Point before crossroad 
            	if(x2 % blockSize == 6 && y2 % blockSize == 4 && y2 < SimpleTrafficLightsConfiguration.getFieldWidth()-blockSize)
            		return CellType.CROSSROAD_WAITING_POINT;
            	if(x2 % blockSize == 0 && y2 % blockSize == 6 && x2 > 0)
            		return CellType.CROSSROAD_WAITING_POINT;
            	if(x2 % blockSize == 5 && y2 % blockSize == 0 && y2 > 0)
            		return CellType.CROSSROAD_WAITING_POINT;
            	if(x2 % blockSize == 4 && y2 % blockSize == 5 && x2 < SimpleTrafficLightsConfiguration.getFieldHeight()-blockSize)
            		return CellType.CROSSROAD_WAITING_POINT;

    			// OPTION 2.4. Generic Street (default) 
            	return CellType.STREET;
            }		   		
    		return CellType.WALL;
    		
        } else {
            return CellType.EMPTY;
        }
    }

    private boolean isWaypoint(int x, int y) {
        return (x % 3 == 0) ^ (y % 3 == 0);
    }

    private boolean isArrivalPoint(int x, int y) {
        return (x == 0 && y % 3 == 2) 
            || (x % 3 == 1 && y == 0) 
            || (x == SimpleTrafficLightsConfiguration.getFieldWidth()-1 && y % 3 == 1) 
            || (x % 3 == 2 && y == SimpleTrafficLightsConfiguration.getFieldHeight()-1);
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
                return CellType.STREET;
            if (light.isGreenSouth())
                return CellType.CROSSROAD_WAITING_POINT;
            return CellType.CROSSROAD_WAITING_POINT;
        }
        if (x % 3 == 0 && y % 3 == 1) {
        	TrafficLight light = getLightForCrossroad(x / 3, y / 3);
            if (light == null)
                return CellType.STREET;
            if (light.isGreenWest())
                return CellType.CROSSROAD_WAITING_POINT;
            return CellType.CROSSROAD_WAITING_POINT;
        }
        if (x % 3 == 1 && y % 3 == 0) {
        	TrafficLight light = getLightForCrossroad(x / 3, y / 3 - 1);
            if (light == null)
                return CellType.STREET;
            if (light.isGreenNorth())
                return CellType.CROSSROAD_WAITING_POINT;
            return CellType.CROSSROAD_WAITING_POINT;
        }
        if (x % 3 == 0 && y % 3 == 2) {
        	TrafficLight light = getLightForCrossroad(x / 3 - 1, y / 3);
            if (light == null)
                return CellType.STREET;
            if (light.isGreenEast())
                return CellType.CROSSROAD_WAITING_POINT;
            return CellType.CROSSROAD_WAITING_POINT;
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
        return cellType(position) == CellType.WALL;
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
        int currentCol = current.getX() / 3;
        int currentRow = current.getY() / 3;
        int targetCol = (target.getX() - 1) / 3;
        int targetRow = (target.getY() - 1) / 3;
        Orientation targetOrientation;

        if (currentCol < targetCol)
            targetOrientation = Orientation.EAST;
        else if (currentCol > targetCol)
            targetOrientation = Orientation.WEST;
        else if (currentRow < targetRow)
            targetOrientation = Orientation.NORTH;
        else if (currentRow > targetRow)
            targetOrientation = Orientation.SOUTH;
        else {
            if (current.getX() < target.getX())
                targetOrientation = Orientation.EAST;
            else if (current.getX() > target.getX())
                targetOrientation = Orientation.WEST;
            else if (current.getY() < target.getY())
                targetOrientation = Orientation.NORTH;
            else if (current.getY() > target.getY())
                targetOrientation = Orientation.SOUTH;
            else
                targetOrientation = Orientation.random();
        }
            return orientationToDirection(facing, targetOrientation);
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
    
	public List<TrafficLight> getTraffigLights() {
		return Arrays.stream(trafficLights).filter(x -> x != null).collect(Collectors.toList());
	}
	
    public List<Entity> getEntities() {    	
        return Stream.of(
        		Arrays.asList(trafficLights),
        		Arrays.asList(arrivalPoints),
        		Arrays.asList(departurePoints)
        		).filter(x -> x != null).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
	public void clearEntities() {
		// Stop robot statecharts and clear robot datastructure 
    	super.clearEntities();
		
		// Stop TrafficLight Statecharts
		for(TrafficLight trafficLight : this.trafficLights)
			if(trafficLight != null)
				trafficLight.stop();
		
		// Trigger reset of datastructures
		this.resetFieldDataStructures();
	}
    
	public void updateEntities(float delta) {
		// Start Robots
		startRobots();
		
		// Update (occupied) Departure Points to trigger timed robot starts.
		for(DeparturePoint point : this.departurePoints)
			point.update(delta);
		
		// Update Traffic Lights
        for (TrafficLight trafficLight : this.trafficLights) {
        	if(trafficLight != null)
        		trafficLight.updateTimer();
        }
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

    public void addTrafficLight(TrafficLight trafficLight) {
        Position relativePosition = trafficLight.getRelativePosition(); 
        int index = relativePosition.getY() * SimpleTrafficLightsConfiguration.getVerticalStreets() + relativePosition.getX();
        if(trafficLights.length > index)
        	trafficLights[index] = trafficLight;
    }


	public void addArrivalPoint(ArrivalPoint point) {		
        if(arrivalPoints.length > point.getId()) {
        	arrivalPoints[point.getId()] = point;
        	emptiestArrivalPoints.add(point);
        }
        	
	}   

	public void addDeparturePoint(DeparturePoint point) {	
        if(departurePoints.length > point.getId()) {
        	departurePoints[point.getId()] = point;
        	emptyDeparturePoints.add(point);
        }
	}   
    
    public static Orientation getSuitableRobotOrientationForPosition(Position pos) {
        if (pos.getX() == 0)
            return Orientation.EAST;
        if (pos.getY() == 0)
            return Orientation.NORTH;
        if (pos.getX() == SimpleTrafficLightsConfiguration.getFieldWidth() - 1)
            return Orientation.WEST;
        return Orientation.SOUTH;
    }

    public static List<Position> getAllPossiblePositions() {
        int width = SimpleTrafficLightsConfiguration.getFieldWidth();
        int height = SimpleTrafficLightsConfiguration.getFieldHeight();
        int positionsHorizontal = (width - 1) / 3;
        int positionsVertical = (height - 1) / 3;

        List<Position> list = new ArrayList<>(2 * (positionsHorizontal + positionsVertical));
        for (int x = 0; x < width / 3; x++) {
            list.add(new Position(3 * x + 2, 0));
        }
        for (int y = 0; y < height / 3; y++) {
            list.add(new Position(0, 3 * y + 1));
        }
        return list;
    }

    public static Position getRandomDestination() {
        int width = SimpleTrafficLightsConfiguration.getFieldWidth();
        int height = SimpleTrafficLightsConfiguration.getFieldHeight();
        int positionsHorizontal = (width - 1) / 3;
        int positionsVertical = (height - 1) / 3;

        int random = ThreadLocalRandom.current().nextInt(positionsHorizontal + positionsVertical);
        if (random < positionsHorizontal)
            return new Position(3*random + 2, height - 1);
        return new Position(width - 1, 3*(random - positionsHorizontal) + 1);
    }

    public Position getRandomStart() {
        List<Position> positions = getAllPossiblePositions();
        positions = positions.stream().filter(p -> !isBlockedByRobot(p)).collect(Collectors.toList());
        return positions.get(new Random().nextInt(positions.size()));
    }

    private void shuffleAndSortArrivalPoints() {
    	Collections.shuffle(emptiestArrivalPoints);
    	emptiestArrivalPoints.sort(new Comparator<ArrivalPoint>() {
					@Override
					public int compare(ArrivalPoint me, ArrivalPoint you) {
						return Integer.compare(me.getNumberOfExpectedRobots(), you.getNumberOfExpectedRobots());
					}
		});
    }


    public void putRobotInWaitingQueue(SimpleRobot r) {
    	if(!idleRobots.contains(r))
    		idleRobots.add(r);
    }
    
    public void startRobots() {
    	// As long as there are robots and free starting positions...
    	while(hasIdleRobots() && hasEmptyDeparturePoints()) {
    		    		
    		// ...Get robot, ...
    		SimpleRobot robot = idleRobots.remove(0);
    		
    		// ...Get start point, ...
    		DeparturePoint departure = getNextEmptyDeparturePoint();
    		occupiedDeparturePoints.add(departure);
    		
    		// ...Get target and...
    		ArrivalPoint arrival = getNextRandomDestination(departure);
    		
    		// ...connect robot to selected transfer points.
    		departure.addStartingRobot(robot, arrival);
    		arrival.addExpectedRobot(robot);
    		
    	}
    }

    public boolean hasIdleRobots() {
    	return !idleRobots.isEmpty();
    }

    public SimpleRobot getNextIdleRobot() {
    	if(idleRobots.isEmpty())
    		return null;
    	return idleRobots.remove(0);
    }
    
    public boolean hasEmptyDeparturePoints() {
    	return !emptyDeparturePoints.isEmpty();
    }
    
    public DeparturePoint getNextEmptyDeparturePoint() {
    	if(emptyDeparturePoints.isEmpty())
    		return null;
    	return emptyDeparturePoints.remove(new Random().nextInt(emptyDeparturePoints.size()));
    }
    
    public ArrivalPoint getNextRandomDestination() {
    	shuffleAndSortArrivalPoints();
    	int index = (int) (Math.abs(new Random().nextGaussian() * SimpleTrafficLightsConfiguration.getNumberOfTransferPoints() / 4 ));
	    System.out.println(index);
	    return emptiestArrivalPoints.get(index % SimpleTrafficLightsConfiguration.getNumberOfTransferPoints());
    }
    
    public ArrivalPoint getNextRandomDestination(DeparturePoint forbiddenNeighbour) {
    	while(true) {
    		ArrivalPoint target = getNextRandomDestination();
    		if(!target.isNextTo(forbiddenNeighbour)) {
    			return target;
    		}
    	}
    }


}
