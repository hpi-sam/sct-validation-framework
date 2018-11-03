package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.ServerGridManagement;
import de.hpi.mod.sim.env.Simulator;
import de.hpi.mod.sim.env.model.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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
        sim.refresh();
    }

    public void update(float delta) {
        grid.update(delta);
        robot.update(delta);
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

    private Position toPosition(int x, int y) {
        y = (int) (getHeight() - y - blockSize / 2);
        int blockX = (int) Math.floor((x + offsetX) / blockSize);
        int blockY = (int) Math.floor((y + offsetY) / blockSize - ServerGridManagement.QUEUE_SIZE);

        return new Position(blockX, blockY);
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
        Position pos = toPosition(e.getX(), e.getY());
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
        highlight = toPosition(e.getX(), e.getY());
        isHighlighted = true;
    }
}
