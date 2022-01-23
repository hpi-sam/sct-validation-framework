package de.hpi.mod.sim.worlds.simpletrafficlights;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TrafficLightState;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TransitPoint;

/**
 * Represents the Map and contains all logic which is dependant of the
 * implementation of the map. The Position (0, 0) is the bottom left block of the 
 * whole grid and necessarily a wall.
 */
public class StreetNetworkManager extends RobotGridManager {
	
	private List<TrafficLight> trafficLights;
    private List<ArrivalPoint> arrivalPoints;
    private List<DeparturePoint> departurePoints;
    
    List<SimpleRobot> idleRobots = new CopyOnWriteArrayList<>();

    public StreetNetworkManager() {
        super();
        resetFieldDataStructures();
    }
    
    /**
     * Add a Robot to the list of idle robots.
     */
    public void addIdleRobot(SimpleRobot robot) {
    	if(!this.hasIdleRobot(robot))
    		this.idleRobots.add(robot);
    }

    /**
     * Check if there are any Robots is inside the list of active robots. 
     * 
     * @return true is there is at least one Robot is in the data structure, false otherwise.
     */
    public boolean hasIdleRobots() {
    	return !this.idleRobots.isEmpty();
    }

    /**
     * Check if a Robot is inside the list of active robots. 
     * 
     * @return true is the Robot is in the data structure, false otherwise.
     */
    public boolean hasIdleRobot(SimpleRobot robot) {
    	return this.idleRobots.contains(robot);
    }
    
    /**
     * Remove a Robot from the list of active robots.
     */
    public void removeIdleRobot(SimpleRobot robot) {
    	this.idleRobots.remove(robot);
    }
    
    /**
     * Clears all Robots from the list of active and inactive robots
     */
	@Override
    public void clearRobots() {
		super.clearRobots();
		clearIdleRobots();
    }
    

    /**
     * Clears all Robots from the list of active robots
     */
    public void clearIdleRobots() {
        for (Robot robot : this.idleRobots) {
            robot.close();
        }
        this.idleRobots.clear();
    }


    public void resetFieldDataStructures() {
    	this.arrivalPoints = new CopyOnWriteArrayList();
    	this.departurePoints = new CopyOnWriteArrayList();
    	this.trafficLights = new CopyOnWriteArrayList();
    }
    
    public TrafficLight getLightForCrossroad(int x, int y) {
    	return trafficLights.stream().filter(light -> light.getRelativePosition().is(new Position(x,y))).findFirst().get();
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

    /**
     * Whether the robot can stand on this position
     * 
     * @param position The position of the Robot
     */
    @Override
    public boolean isBlockedByMap(Position position) {
        return cellType(position) == CellType.WALL;
    }
    
    
	public List<TrafficLight> getTraffigLights() {
		return trafficLights;
	}
	
    public List<Entity> getNonRobotEntitiesForDetectors() {    	
    	return Collections.emptyList();
//        return Stream.of(
//        		Arrays.asList(trafficLights),
//        		Arrays.asList(arrivalPoints),
//        		Arrays.asList(departurePoints)
//        		).filter(x -> x != null).flatMap(Collection::stream).collect(Collectors.toList());
    }

	public void clearNonRobotEntities() {		
		// Stop TrafficLight Statecharts
		for(TrafficLight trafficLight : this.trafficLights)
			if(trafficLight != null)
				trafficLight.stop();
		
		// Trigger reset of datastructures (removed traffic lights and arrival/departure points) 
		this.resetFieldDataStructures();
	}
    
	public void updateNonRobotEntities(float delta) {
		// Start Robots
		startRobots();
		
		// Update Departure Points to trigger timed robot starts.
		for(DeparturePoint point : this.departurePoints)
			point.update(delta);

		// Update Arrival Points to trigger timed removal of robots from field.
		for(ArrivalPoint point : this.arrivalPoints)
			point.update(delta);
		
		// Update Traffic Lights
        for (TrafficLight trafficLight : this.trafficLights) {
        	if(trafficLight != null)
        		trafficLight.updateTimer();
        }
	}


    public void addTrafficLight(TrafficLight trafficLight) {
        trafficLights.add(trafficLight);
    }


	public void addArrivalPoint(ArrivalPoint point) {		
        arrivalPoints.add(point);	
	}   

	public void addDeparturePoint(DeparturePoint point) {	
		departurePoints.add(point);
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

    public void makeRobotIdle(SimpleRobot r) {
    	this.removeRobot(r);
    	this.addIdleRobot(r);
    }

    public void makeRobotActive(SimpleRobot r) {
    	this.removeIdleRobot(r);
    	this.addRobot(r);
    }
    
    public void startRobots() {
    	// As long as there are robots and free starting positions...
    	while(hasIdleRobots() && hasEmptyDeparturePoints()) {
    	    		    		
    		// ...Get robot, ...
    		SimpleRobot robot = getNextIdleRobot();
    		
    		// ...Make robot active, ...
    		makeRobotActive(robot);
    		
    		// ...Get start point, ...
    		DeparturePoint departure = getNextEmptyDeparturePoint();
    		
    		// ...Get target and...
    		ArrivalPoint arrival = getNextRandomDestination(Collections.singletonList(departure));
    		
    		// ...connect robot to selected transfer points.
    		departure.addStartingRobot(robot, arrival);
    		arrival.addExpectedRobot(robot);
    		
    	}
    }
    
    public SimpleRobot getNextIdleRobot() {
    	if(idleRobots.isEmpty())
    		return null;
    	return idleRobots.get(0);
    }
    
    public boolean hasEmptyDeparturePoints() {
    	return departurePoints.stream().anyMatch(departurePoint -> departurePoint.isNotOccupied());
    }
    
    public DeparturePoint getNextEmptyDeparturePoint() {
    	List<DeparturePoint> emtpyDeparturePoints = departurePoints.stream().filter(departurePoint -> departurePoint.isNotOccupied()).collect(Collectors.toList());;
    	return emtpyDeparturePoints.get(ThreadLocalRandom.current().nextInt(emtpyDeparturePoints.size()));
    }
    
    public ArrivalPoint getNextRandomDestination(List<TransitPoint> forbiddenNeighbours) {
    	// Get all potential destinations
    	List<ArrivalPoint> emptiestArrivalPoints = arrivalPoints.stream()
    			.filter(arrivalPoint -> forbiddenNeighbours.stream().noneMatch(neighbour -> neighbour.isNextTo(arrivalPoint)))
    			.collect(Collectors.toList());
    	    	
    	// Sort and randomize if values are identical
    	Collections.shuffle(emptiestArrivalPoints);
    	emptiestArrivalPoints.sort(new Comparator<ArrivalPoint>() {
    						@Override
    						public int compare(ArrivalPoint me, ArrivalPoint you) {
    							return Integer.compare(me.getNumberOfExpectedRobots(), you.getNumberOfExpectedRobots());
    						}
    					});
    	
    	// Randomly select
    	int index = (int) (Math.abs(ThreadLocalRandom.current().nextGaussian() * SimpleTrafficLightsConfiguration.getNumberOfTransferPoints() / 4 ));    	
	    return emptiestArrivalPoints.get(index % arrivalPoints.size());
    }
    
    public ArrivalPoint getNextRandomDestination() {
    	return getNextRandomDestination(Collections.emptyList());
    }

    public ArrivalPoint getNextRandomDestination(TransitPoint forbiddenNeighbour) {
    	return getNextRandomDestination(Collections.singletonList(forbiddenNeighbour));
    }

	public boolean isBlocked(Position p) {
		return isBlockedByMap(p) || isBlockedByRobot(p);
	}

	public TrafficLightState queryTrafficLight(Position pos) {
		Optional<TrafficLight> fittingTrafficLight = trafficLights.stream().filter(tl -> tl.isWaitingPosition(pos)).findAny();
		if(fittingTrafficLight.isEmpty())
			return TrafficLightState.NO_TRAFFIC_LIGHT;
		return fittingTrafficLight.get().getTrafficLightState(pos);
	}

	public List<Direction> getTargetDirections(Position robotPosition, Orientation robotOrientation, Position targetPosition) {
		
        List<Orientation> targetOrientations = new CopyOnWriteArrayList<>();
        
        Position robotPositionOneAhead = robotPosition.getModified(SimpleTrafficLightsConfiguration.getStreetLength()+SimpleTrafficLightsConfiguration.getCrossroadLength(), robotOrientation);

    	if(Math.abs(targetPosition.getY() - robotPosition.getY()) <= SimpleTrafficLightsConfiguration.getTargetDirectionOffset() ||
    			targetPosition.getX() != robotPositionOneAhead.getX()){
    		// Robot Y and Target Y are (almost) equal => Robot is on same East-West-street as target
    		// If Robot drove one crossroad ahead, X would still not be almost qual to target => Robot is NOT in danger of driving too far ahead
    		// Either condition suffices to consider WEST or EAST as Target Direction
    		    		
    		if (targetPosition.getX() < robotPosition.getX() - SimpleTrafficLightsConfiguration.getTargetDirectionOffset()) {
    			// Target X is smaller than Robot X (-offset) => Target might be WEST
    			targetOrientations.add(Orientation.WEST);
        	}else if (targetPosition.getX() > robotPosition.getX() + SimpleTrafficLightsConfiguration.getTargetDirectionOffset()) {
        		// Target X is larger than Robot X (+offset) => Target might be EAST
        		targetOrientations.add(Orientation.EAST);
        	}
        }
	
    	if(Math.abs(targetPosition.getX() - robotPosition.getX()) <= SimpleTrafficLightsConfiguration.getTargetDirectionOffset() ||
    			targetPosition.getY() != robotPositionOneAhead.getY()){
    		// Robot X and Target X are (almost) equal => Robot is on same East-West-street as target
    		// If Robot drove one crossroad ahead, Y would still not be almost qual to target => Robot is NOT in danger of driving too far ahead
    		// Either condition suffices to consider NORTH or SOUTH as Target Direction
    		    		
    		if (targetPosition.getY() < robotPosition.getY() - SimpleTrafficLightsConfiguration.getTargetDirectionOffset()) {
    			// Target Y is smaller than Robot Y (-offset) => Target might be SOUTH
    			targetOrientations.add(Orientation.SOUTH);
        	}else if (targetPosition.getY() > robotPosition.getY() + SimpleTrafficLightsConfiguration.getTargetDirectionOffset()) {
        		// Target Y is larger than Robot X (+offset) => Target might be NORTH
        		targetOrientations.add(Orientation.NORTH);
        	}
        }
        
    	// Turn Orientations to Directions
    	List<Direction> directions = targetOrientations.stream().map(orientation -> orientationToDirection(robotOrientation, orientation)).distinct().collect(Collectors.toList());
    	System.out.println(targetOrientations + " => "+ directions);
    	return directions;
	}
	
	@Override
    public Direction orientationToDirection(Orientation facing, Orientation target) {
		
		// Initialize direction variable with the assumption that robot is facing north
		Direction direction;
		switch (target) {
		case NORTH:
			direction = Direction.AHEAD;
			break;
		case EAST:
			direction = Direction.RIGHT;
			break;
		case SOUTH:
			direction = Direction.BEHIND;
			break;
		default: // WEST
			direction = Direction.LEFT;
			break;
		}
		
		// If actually facing NORTH then return direction.
		if(facing == Orientation.NORTH)
			return direction;

		// Turn right. If facing EAST then return direction.
		direction = direction.getTurnedRight();
		if(facing == Orientation.EAST)
			return direction;
		
		// Turn right. If facing SOUTH then return direction.
		direction = direction.getTurnedRight();
		if(facing == Orientation.SOUTH)
			return direction;

		// Turn right. Only WEST leftover, so return direction anyway.
		return direction.getTurnedRight();
    }
    
	public boolean isInFrontOfTrafficLight(Position pos) {
		return trafficLights.stream().anyMatch(tl -> tl.isWaitingPosition(pos));
	}

}
