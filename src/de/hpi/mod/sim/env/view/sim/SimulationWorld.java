package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.ServerGridManagement;
import de.hpi.mod.sim.env.Simulator;
import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.model.IHighlightedRobotListener;
import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.panels.RobotInfoPanel;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Stores the values needed to display the Simulation and gives access to the Simulation
 */
public class SimulationWorld {

    /**
     * Default values needed to reset
     */
    private static final float
            DEFAULT_BLOCK_SIZE = 20,
            DEFAULT_OFFSET_X = 0,
            DEFAULT_OFFSET_Y = 0,
            DEFAULT_SENSOR_REFRESH_INTERVAL = 10,
            MIN_BLOCK_SIZE = 5,
            MAX_BLOCK_SIZE = 30;

    /**
     * The scaling of a block
     */
    private float blockSize = DEFAULT_BLOCK_SIZE;

    /**
     * Offset in blocks
     */
    private float offsetX = DEFAULT_OFFSET_X, offsetY = DEFAULT_OFFSET_Y;

    /**
     * Number of milliseconds between sensor refreshes
     */
    private float sensorRefreshInterval = DEFAULT_SENSOR_REFRESH_INTERVAL;

    /**
     * Whether the simulation is running or not
     */
    private static boolean running = false;

    /**
     * The Position of the mouse in blocks
     */
    private Position mousePointer;

    /**
     * Whether the mouse is on screen or nor.
     * If false, {@link SimulationWorld#mousePointer} is garbage
     */
    private boolean isMousePointing;

    /**
     * The highlighted Robot. Null if none
     */
    private Robot highlightedRobot = null;
    private Robot highlightedRobot2 = null;

    private Simulator sim;

    private SimulatorView view;

    /**
     * List of {@link IHighlightedRobotListener}s.
     * Gets called if the highlighted Robot changes
     */
    private List<IHighlightedRobotListener> highlightedRobotListeners = new ArrayList<>();
    private List<IHighlightedRobotListener> highlightedRobotListeners2 = new ArrayList<>();

    /**
     * List of {@link ITimeListener}s.
     * Gets called if the time flow changes.
     */
    private List<ITimeListener> timeListeners = new ArrayList<>();

    /**
     * Used for locking while the List of Robots gets traversed
     */
    private boolean isRefreshing = false, isUpdating = false;


    public SimulationWorld(SimulatorView view) {
        this.view = view;
        sim = new Simulator();
    }

    /**
     * Refreshes the Simulation and sends Sensor-Refreshes to all Robots.
     * Locks the List of robots
     */
    public synchronized void refresh() {
        if (running) {
            isRefreshing = true;
            sim.refresh();
            isRefreshing = false;
            notifyAll();
        }
    }

    /**
     * Updates the Robots each frame.
     * Locks the List of Robots
     * @param delta The time since last frame in milliseconds
     */
    public synchronized void update(float delta) {
        if (running) {
            try {
                isUpdating = true;
                for (Robot r : sim.getRobots()) {
                    r.getDriveManager().update(delta);
                }
                isUpdating = false;
                notifyAll();
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }
    }

    public void addHighlightedRobotListener(IHighlightedRobotListener highlightedRobotListener) {
        highlightedRobotListeners.add(highlightedRobotListener);
    }
    
    public void addHighlightedRobotListener2(IHighlightedRobotListener highlightedRobotListener2) {
    	highlightedRobotListeners2.add(highlightedRobotListener2);
	}

    public void addTimeListener(ITimeListener timeListener) {
        timeListeners.add(timeListener);
    }

    public Simulator getSimulator() {
        return sim;
    }

    public SimulatorView getView() {
        return view;
    }

    /**
     * Returns the Robots of the Simulation.
     * Be careful when using this, since other threads are modifying the List, which can lead
     * to {@link ConcurrentModificationException}
     * @return List of Robots in Simulation
     */
    public List<Robot> getRobots() {
        return sim.getRobots();
    }

    public void zoomIn(float zoom) {
        if (blockSize < MAX_BLOCK_SIZE)
            blockSize += zoom;
    }
    
    public void zoomOut(float zoom) {
        if (blockSize > MIN_BLOCK_SIZE)
            blockSize -= zoom;
    }

    public void resetZoom() {
        blockSize = DEFAULT_BLOCK_SIZE;
    }

    public float getBlockSize() {
        return blockSize;
    }

    public void moveHorizontal(int dir) {
        offsetX += dir;
    }

    public void moveVertical(int dir) {
        offsetY += dir;
        if (offsetY < 0)
            offsetY = 0;
    }

    public void resetOffset() {
        offsetX = DEFAULT_OFFSET_X;
        offsetY = DEFAULT_OFFSET_Y;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public float getSensorRefreshInterval() {
        return sensorRefreshInterval;
    }

    public void setSensorRefreshInterval(float sensorRefreshInterval) {
        this.sensorRefreshInterval = sensorRefreshInterval;
    }

    public synchronized Robot addRobot() {
        return addRobotRunner(() -> sim.addRobot());
    }

    public Robot addRobotAtWaypoint(Position pos, Orientation facing, Position target) {
        return addRobotRunner(() -> sim.addRobotAtWaypoint(pos, facing, target));
    }
    
    public Robot addRobotInScenarioHPI2(Position pos, Orientation facing) {
    	return addRobotRunner(() -> sim.addRobotInScenarioHPI(pos, facing));
    }

    private Robot addRobotRunner(AddRobotRunner runner) {
        while (isRefreshing || isUpdating) {
        	//Do nothing while refreshing or updating
		}

		Robot r = runner.run();
		setHighlightedRobot(r);
		return r;
    }

    /**
     * Clears all Robots, stops the simulation, loads the scenario and plays it
     * @param scenario The Scenario to play
     */
    public void playScenario(Scenario scenario) {
        while (isRefreshing || isUpdating) {
        	//Do nothing while refreshing or updating
		}

		if (isRunning()) toggleRunning();
		sim.getRobots().clear();

		scenario.loadScenario(this);
    }

    public void setMousePointer(Position mousePointer) {
        this.mousePointer = mousePointer;
        isMousePointing = true;
    }
    
    public Position getMousePointer() {
        return mousePointer;
    }

    public void setMousePointing(boolean mousePointing) {
        isMousePointing = mousePointing;
    }

    public boolean isMousePointing() {
        return isMousePointing;
    }

    public void setHighlightedRobot(Robot r) {
        highlightedRobot = r;
        refreshHighlightedRobotListeners();
    }
    
    public void setHighlightedRobot2(Robot r) {
		highlightedRobot2 = r;
		refreshHighlightedRobotListeners();
	}

    public Robot getHighlightedRobot() {
        return highlightedRobot;
    }
    
    public Robot getHighlightedRobot2() {
        return highlightedRobot2;
    }
    
    public void toggleRunning() {
        running = !running;
        refreshTimeListeners();
    }
    
    public static boolean isRunning() {
        return running;
    }


    /**
     * Converts a draw-position to a grid-position
     */
    public Position toGridPosition(int x, int y) {
        y = (int) (view.getHeight() - y - blockSize / 2);
        int blockX = (int) Math.floor(x / blockSize + offsetX);
        int blockY = (int) Math.floor(y / blockSize - ServerGridManagement.QUEUE_SIZE + offsetY);

        return new Position(blockX, blockY);
    }

    /**
     * Converts a grid-position to the draw-position
     */
    public Point2D toDrawPosition(Position pos) {
        return toDrawPosition(pos.getX(), pos.getY());
    }

    /**
     * Converts a grid-position to the draw-position
     */
    public Point2D toDrawPosition(float x, float y) {
        float drawX = (x - offsetX) * blockSize;
        float drawY = view.getHeight() - (y + ServerGridManagement.QUEUE_SIZE + 1.5f - offsetY) * blockSize;
        return new Point2D.Float(drawX, drawY);
    }

    public void dispose() {
        sim.close();
    }

    private void refreshHighlightedRobotListeners() {
        highlightedRobotListeners.forEach(IHighlightedRobotListener::onHighlightedRobotChange);
    }

    private void refreshTimeListeners() {
        timeListeners.forEach(ITimeListener::refresh);
    }

    private interface AddRobotRunner {
        Robot run();
    }
}
