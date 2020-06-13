package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.model.IGrid;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.simulation.robot.Robot;
import de.hpi.mod.sim.env.simulation.SimulatorConfig;
import de.hpi.mod.sim.env.simulation.Simulation;

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

	private static final long serialVersionUID = -361892313938561668L;
	private GridRenderer gridRenderer;
    private RobotRenderer robotRenderer;
    private ExplosionRenderer explosionRenderer;

    private SimulationWorld simulationWorld;
    private Simulation world;
    
    private int currentHeight;
    private int currentWidth;


    public SimulatorView(Simulation world, IGrid grid) {
        this.world = world;
        simulationWorld = new SimulationWorld(this);
        gridRenderer = new GridRenderer(simulationWorld, grid);
        robotRenderer = new RobotRenderer(world, simulationWorld);
        explosionRenderer = new ExplosionRenderer(simulationWorld);

        addMouseListener(this);
        addMouseMotionListener(this);

        setFocusable(true);
    }

    public SimulationWorld getSimulationWorld() {
        return simulationWorld;
    }

    @Override
    protected void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);

        // Draw Grid
        gridRenderer.render(graphic);
        robotRenderer.render(graphic);
        explosionRenderer.render(graphic);
        
        //Refresh simulation properties
        refreshSimulationSize();
        refreshSimulationProperties();
    }

    private void refreshSimulationProperties() {
    	float blockSize = SimulatorConfig.getDefaultBlockSize();
    	int heightBlocks = (int) (currentHeight/blockSize);
    	int widthBlocks = (int) (currentWidth/blockSize);
    	
    	
    	int chargingStations = widthBlocks/SimulatorConfig.getSpaceBetweenChargingStations();
    	if(chargingStations % 2 != 0)
    		chargingStations--;
    	SimulatorConfig.setChargingStationsInUse(chargingStations);
    	int unloadingRange = (widthBlocks/3)*((heightBlocks-SimulatorConfig.getQueueSize())/3);
    	SimulatorConfig.setUnloadingRange(unloadingRange);
    	world.updateSimulator(SimulatorConfig.getChargingStationsInUse());
    	
	}

	@Override
    public void mouseClicked(MouseEvent e) {
        Position pos = simulationWorld.toGridPosition(e.getX(), e.getY());
        for (Robot r : world.getRobots()) {
            if (r.getDriveManager().currentPosition().equals(pos) || r.getDriveManager().getOldPosition().equals(pos)) {
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
    public void mousePressed(MouseEvent e) {
    	explosionRenderer.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {
        grabFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        simulationWorld.setMousePointing(false);
    }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {
        simulationWorld.setMousePointer(simulationWorld.toGridPosition(e.getX(), e.getY()));
    }

    private void refreshSimulationSize() {
    	Rectangle rectangle = this.getBounds();
    	currentHeight = rectangle.height;
    	currentWidth = rectangle.width;
    }

	public void renderExplosion(Robot robot) {
		explosionRenderer.showExplosion(robot);
	}

	public void reset() {
		explosionRenderer.reset();
	}
}
