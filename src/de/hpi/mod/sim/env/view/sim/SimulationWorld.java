package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.ServerGridManagement;
import de.hpi.mod.sim.env.Simulator;
import de.hpi.mod.sim.env.model.Orientation;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.model.IHighlightedRobotListener;
import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.model.Scenario;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimulationWorld {

    private static final float
            DEFAULT_BLOCK_SIZE = 20,
            DEFAULT_OFFSET_X = 0,
            DEFAULT_OFFSET_Y = 0,
            DEFAULT_SENSOR_REFRESH_INTERVAL = 10,
            MIN_BLOCK_SIZE = 5,
            MAX_BLOCK_SIZE = 30;

    private float blockSize = DEFAULT_BLOCK_SIZE;
    private float offsetX = DEFAULT_OFFSET_X;
    private float offsetY = DEFAULT_OFFSET_Y;
    private float sensorRefreshInterval = DEFAULT_SENSOR_REFRESH_INTERVAL;

    private boolean running = false;

    private Position mousePointer;
    private boolean isMousePointing;

    private Robot highlightedRobot = null;

    private Simulator sim;

    private SimulatorView view;
    private List<IHighlightedRobotListener> highlightedRobotListeners = new ArrayList<>();
    private List<ITimeListener> timeListeners = new ArrayList<>();

    boolean isRefreshing = false, isUpdating = false;


    public SimulationWorld(SimulatorView view) {
        this.view = view;
        sim = new Simulator();
    }

    public synchronized void refresh() {
        if (running) {
            isRefreshing = true;
            sim.refresh();
            isRefreshing = false;
            notifyAll();
        }
    }

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

    public void addTimeListener(ITimeListener timeListener) {
        timeListeners.add(timeListener);
    }

    public Simulator getSimulator() {
        return sim;
    }

    public SimulatorView getView() {
        return view;
    }

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
    
    public float getZoom() {
        return blockSize;
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

    public Robot addRobot(int stationID) {
        return addRobotRunner(() -> sim.addRobot(stationID));
    }

    public Robot addRobotAtWaypoint(Position pos, Orientation facing, Position target) {
        return addRobotRunner(() -> sim.addRobotAtWaypoint(pos, facing, target));
    }

    private Robot addRobotRunner(AddRobotRunner runner) {
        try {
            while (isRefreshing || isUpdating) {
                wait();
            }

            Robot r = runner.run();
            setHighlightedRobot(r);
            return r;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void playScenario(Scenario scenario) {
        if (isRunning()) toggleRunning();
        sim.getRobots().clear();

        scenario.clear();
        scenario.loadScenario();
        scenario.playScenario(this);
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

    public Robot getHighlightedRobot() {
        return highlightedRobot;
    }
    
    public void toggleRunning() {
        running = !running;
        refreshTimeListeners();
    }
    
    public boolean isRunning() {
        return running;
    }

    public Position toGridPosition(int x, int y) {
        y = (int) (view.getHeight() - y - blockSize / 2);
        int blockX = (int) Math.floor(x / blockSize + offsetX);
        int blockY = (int) Math.floor(y / blockSize - ServerGridManagement.QUEUE_SIZE + offsetY);

        return new Position(blockX, blockY);
    }

    public Point2D toDrawPosition(Position pos) {
        return toDrawPosition(pos.getX(), pos.getY());
    }

    public Point2D toDrawPosition(float x, float y) {
        float drawX = (x - offsetX) * blockSize;
        float drawY = view.getHeight() - (y + ServerGridManagement.QUEUE_SIZE + 1.5f - offsetY) * blockSize;
        return new Point2D.Float(drawX, drawY);
    }

    public void dispose() {
        sim.close();
    }

    private void refreshHighlightedRobotListeners() {
        highlightedRobotListeners.forEach(IHighlightedRobotListener::refresh);
    }

    private void refreshTimeListeners() {
        timeListeners.forEach(ITimeListener::refresh);
    }

    private interface AddRobotRunner {
        Robot run();
    }
}
