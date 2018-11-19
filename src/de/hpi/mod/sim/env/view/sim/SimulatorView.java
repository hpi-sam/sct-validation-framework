package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Uses {@link GridRenderer} and {@link RobotRenderer} to render the Simulation given by {@link SimulationWorld}.
 * Keeps track of the Mouse
 */
public class SimulatorView extends JPanel implements MouseListener, MouseMotionListener {

    private GridRenderer grid;
    private RobotRenderer robot;

    private SimulationWorld world;


    public SimulatorView() {
        world = new SimulationWorld(this);
        grid = new GridRenderer(world, world.getSimulator().getGrid());
        robot = new RobotRenderer(world);

        addMouseListener(this);
        addMouseMotionListener(this);

        setFocusable(true);
    }

    public SimulationWorld getWorld() {
        return world;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw Grid
        grid.render(g);
        robot.render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Position pos = world.toGridPosition(e.getX(), e.getY());
        for (Robot r : world.getRobots()) {
            if (r.getDriveManager().currentPosition().equals(pos)) {
                world.setHighlightedRobot(r);
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
        world.setMousePointing(false);
    }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {
        world.setMousePointer(world.toGridPosition(e.getX(), e.getY()));
    }
}
