package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.SimulatorConfig;
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
    private ExplosionRenderer explosion;

    private SimulationWorld world;
    
    private int currentHeight;
    private int currentWidth;


    public SimulatorView() {
        world = new SimulationWorld(this);
        grid = new GridRenderer(world, world.getSimulator().getGrid());
        robot = new RobotRenderer(world);
        explosion = new ExplosionRenderer(world);

        addMouseListener(this);
        addMouseMotionListener(this);

        setFocusable(true);
    }

    public SimulationWorld getWorld() {
        return world;
    }

    @Override
    protected void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);

        // Draw Grid
        grid.render(graphic);
        robot.render(graphic);
        explosion.render(graphic);
        
        //Refresh simulation properties
        refreshSimulationSize();
        refreshSimulationProperties();
    }

    private void refreshSimulationProperties() {
    	float blockSize = SimulatorConfig.getDefaultBlockSize();
    	int heightBlocks = (int) (currentHeight/blockSize);
    	int widthBlocks = (int) (currentWidth/blockSize);
    	
    	SimulatorConfig.setChargingStationsInUse(widthBlocks/SimulatorConfig.getSpaceBetweenChargingStations());
    	//SimulatorConfig.setMapHeight(heightBlocks);
    	int unloadingRange = (widthBlocks/3)*((heightBlocks-SimulatorConfig.getQueueSize())/3);
    	SimulatorConfig.setUnloadingRange(unloadingRange);
    	
	}

	@Override
    public void mouseClicked(MouseEvent e) {
        Position pos = world.toGridPosition(e.getX(), e.getY());
        for (Robot r : world.getRobots()) {
            if (r.getDriveManager().currentPosition().equals(pos)) {
            	if(e.getButton() == MouseEvent.BUTTON1) {
            		world.setHighlightedRobot1(r);
            	} else if (e.getButton() == MouseEvent.BUTTON3) {
            		world.setHighlightedRobot2(r);
            	} else {
            		world.setHighlightedRobot1(r);
            	}
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

    private void refreshSimulationSize() {
    	Rectangle r = this.getBounds();
    	currentHeight = r.height;
    	currentWidth = r.width;
    }

	public void renderExplosion(Robot robot) {
		explosion.showExplosion(robot);
	}

	public void reset() {
		explosion.reset();
	}
}
