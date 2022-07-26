package de.hpi.mod.sim.worlds.abstract_robots;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.worlds.abstract_grid.GridManager;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.interfaces.IRobotController;


public abstract class RobotGridManager extends GridManager implements IRobotController{ 

    private List<Robot> robots = new CopyOnWriteArrayList<>();
    
    /**
     * Add a Robot to the list of active robots.
     */
    public Robot addRobot(Robot robot) {
    	if(!this.hasRobot(robot))
    		this.robots.add(robot);
        return robot;
    }

    /**
     * Check if a Robot is inside the list of active robots. 
     * 
     * @return true is the Robot is in the data structure, false otherwise.
     */
    public boolean hasRobot(Robot robot) {
    	return this.robots.contains(robot);
    }
    
    /**
     * Remove a Robot from the list of active robots.
     */
    public void removeRobot(Robot robot) {
    	this.robots.remove(robot);
    }

    /**
     * Refreshes the Robots.
     */
    public void refreshRobots() {
        for (Robot robot : this.robots) {
        	robot.refresh();
        }
    }

    /**
     * Clears all Robots from the list of active robots
     */
    public void clearRobots() {
        for (Robot robot : robots) {
            robot.close();
        }
    	robots.clear();
    }

    /**
     * Whether there is a Robot on the given Position
     * @param position The Position to check
     * @return true if there is a Robot on the Position
     */
    public boolean isBlockedByRobot(Position position) {
        for (Robot robot : robots) {
            if (robot.pos().equals(position) || robot.oldPos().equals(position))
                return true;
        }
        return false;
    }

    /**
     * Returns the Robots of the Simulation. Be careful when using this, since other
     * threads are modifying the List, which can lead to
     * {@link ConcurrentModificationException}
     * 
     * @return List of Robots in Simulation
     */
    public List<Robot> getRobots() {
        return robots;
    }
    
    public void closeRobots() {
        for (Robot robot : robots)
            robot.close();
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
    public boolean[] blocked(Orientation facing, Position position) {

        // A boolean storing all directions
        boolean[] blocked = new boolean[4];
        Position[] neighbors = getNeighbors(facing, position);
        // If a neighbor is blocked, its corresponding boolean is set to true
        for (int i = 0; i < blocked.length; i++) {
            blocked[i] = isBlockedByMap(neighbors[i]) || isBlockedByRobot(neighbors[i]);
        }
        return blocked;
    }

    /**
     * Whether the robot can stand on this position
     * 
     * @param pos The position of the Robot
     */
    public boolean isBlockedByMap(Position pos) {
        return false;
    }
    
    @Override
    public boolean affects(IHighlightable highlight, Position position) {
        if (highlight == null || !(highlight instanceof Robot))
            return false;
        Robot r = (Robot) highlight;
        return position.is(r.pos()) || position.is(r.oldPos());
    }
}
