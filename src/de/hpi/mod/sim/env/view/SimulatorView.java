package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.ServerGridManagement;
import de.hpi.mod.sim.env.Simulator;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

public class SimulatorView extends JPanel implements MouseListener, MouseMotionListener {

    public static final float
            DEFAULT_BLOCK_SIZE = 20,
            DEFAULT_OFFSET_X = 0,
            DEFAULT_OFFSET_Y = 0,
            DEFAULT_SENSOR_REFRESH_INTERVAL = 10,
            MIN_BLOCK_SIZE = 5,
            MAX_BLOCK_SIZE = 30,
            MOVE_SPEED = 1;

    private float blockSize = DEFAULT_BLOCK_SIZE;
    private float offsetX = DEFAULT_OFFSET_X;
    private float offsetY = DEFAULT_OFFSET_Y;
    private float sensorRefreshInterval = DEFAULT_SENSOR_REFRESH_INTERVAL;

    private boolean running = false;

    private Position highlight;
    private boolean isHighlighted;

    private Simulator sim;

    private GridRenderer grid;
    private RobotRenderer robot;
    private IInspector inspector;


    public SimulatorView(IInspector inspector) {
        sim = new Simulator();
        grid = new GridRenderer(this, sim.getGrid());
        robot = new RobotRenderer(this);
        this.inspector = inspector;

        addMouseListener(this);
        addMouseMotionListener(this);

        setFocusable(true);
    }

    public void refresh() {
        if (running) {
            sim.refresh();
        }
    }

    public void update(float delta) {
        if (running) {
            grid.update(delta);
            robot.update(delta);
        }
    }

    public void zoomIn() {
        if (blockSize < MAX_BLOCK_SIZE)
            blockSize++;
    }

    public void zoomOut() {
        if (blockSize > MIN_BLOCK_SIZE)
            blockSize--;
    }

    public float getZoom() {
        return blockSize;
    }

    public void moveHorizontal(int dir) {
        offsetX += dir * MOVE_SPEED;
    }

    public void moveVertical(int dir) {
        offsetY += dir * MOVE_SPEED;
        if (offsetY < 0)
            offsetY = 0;
    }

    public float getSensorRefreshInterval() {
        return sensorRefreshInterval;
    }

    public void setSensorRefreshInterval(float sensorRefreshInterval) {
        this.sensorRefreshInterval = sensorRefreshInterval;
    }

    public void addRobot() {
        inspector.showInfo(sim.addRobot());
    }

    public Position getHighlight() {
        return highlight;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public Robot highlightedRobot() {
        return inspector.getRobot();
    }

    public void toggleRunning() {
        running = !running;
    }

    public boolean isRunning() {
        return running;
    }

    Position toGridPosition(int x, int y) {
        y = (int) (getHeight() - y - blockSize / 2);
        int blockX = (int) Math.floor((x + offsetX) / blockSize);
        int blockY = (int) Math.floor((y + offsetY) / blockSize - ServerGridManagement.QUEUE_SIZE);

        return new Position(blockX, blockY);
    }

    Point2D toDrawPosition(Position pos) {
        return toDrawPosition(pos.getX(), pos.getY());
    }

    Point2D toDrawPosition(float x, float y) {
        float drawX = x * blockSize - offsetX;
        float drawY = getHeight() - (y + ServerGridManagement.QUEUE_SIZE + 1.5f) * blockSize - offsetY;
        return new Point2D.Float(drawX, drawY);
    }

    public void close() {
        sim.close();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw Grid
        grid.render(g);
        robot.render(g);
    }

    public float getBlockSize() {
        return blockSize;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public Simulator getSim() {
        return sim;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Position pos = toGridPosition(e.getX(), e.getY());
        for (de.hpi.mod.sim.env.robot.Robot r : sim.getRobots()) {
            if (r.getDriveManager().currentPosition().equals(pos)) {
                inspector.showInfo(r);
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {
        grabFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isHighlighted = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {
        highlight = toGridPosition(e.getX(), e.getY());
        isHighlighted = true;
    }
}
