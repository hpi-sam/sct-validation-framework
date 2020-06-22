package de.hpi.mod.sim.core.view.sim;

import javax.swing.*;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.Position;
import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.setting.infinitewarehouses.GridRenderer;
import de.hpi.mod.sim.setting.infinitewarehouses.RobotRenderer;
import de.hpi.mod.sim.setting.robot.Robot;

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

    private Setting setting;
    private SimulationWorld simulationWorld;
    
    private int currentHeight;
    private int currentWidth;


    public SimulatorView(Setting setting, SimulationWorld simulationWorld) {
        this.setting = setting;
        this.simulationWorld = simulationWorld;

        addMouseListener(this);
        addMouseMotionListener(this);

        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);

        // Draw Grid
        setting.render(graphic);
        
        //Refresh simulation properties
        refreshSimulationSize();
        setting.refreshSimulationProperties(currentHeight, currentWidth);
    }


	@Override
    public void mouseClicked(MouseEvent e) {
        Position pos = simulationWorld.toGridPosition(e.getX(), e.getY());
        for (Entity entityO : setting.getEntities()) {
            Robot entity = (Robot) entityO; //TODO I bet we want to handle mouseClicks directly on the paintedcomponents
            if (entity.getDriveManager().currentPosition().equals(pos) || entity.getDriveManager().getOldPosition().equals(pos)) {
            	if(e.getButton() == MouseEvent.BUTTON1) {
            		simulationWorld.setHighlighted1(entity);
            	} else if (e.getButton() == MouseEvent.BUTTON3) {
            		simulationWorld.setHighlighted2(entity);
            	} else {
            		simulationWorld.setHighlighted1(entity);
            	}
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    	setting.mousePressed(e);
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

}
