package de.hpi.mod.sim.core.view.sim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.hpi.mod.sim.core.model.IHighlightable;
import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.core.view.model.IHighlightedListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Stores the values needed to display the Simulation, renders it and keeps track of the mouse
 */
public abstract class SimulationView extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -361892313938561668L;

	
	/**
	 * Whether the mouse is on screen or nor. If false,
	 * {@link SimulationView#mousePointer} is garbage
	 */
	private boolean isMousePointing;

	/**
	 * The highlighted objects. Null if none
	 */
	private IHighlightable highlighted1 = null;
	private IHighlightable highlighted2 = null;

	/**
	 * List of {@link IHighlightedListener}s. Gets called if the highlighted
	 * Entity changes
	 */
	private List<IHighlightedListener> highlightedEntityListeners = new ArrayList<>();
	private List<IHighlightedListener> highlightedEntityListeners2 = new ArrayList<>();

	/**
	 * Offset of viewed part of the simulation. Has to be interpretated by subclass
	 */
	private float offsetX, offsetY;

	private int currentHeight;
	private int currentWidth;
	private Setting setting;



	public SimulationView(Setting setting) {
		this.setting = setting;
		
		offsetX = SimulatorConfig.getDefaultOffsetX();
		offsetY = SimulatorConfig.getDefaultOffsetY();

		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
	}


	public void addHighlightedListener(IHighlightedListener highlightedListener) {
		highlightedEntityListeners.add(highlightedListener);
	}

	public void addHighlightedListener2(IHighlightedListener highlightedListener) {
		highlightedEntityListeners2.add(highlightedListener);
	}

	public boolean isMousePointing() {
		return isMousePointing;
	}

	public void setMousePointing(boolean b) {
		isMousePointing = b;
	}

	public void setHighlighted1(IHighlightable h) {
		highlighted1 = h;
		refreshHighlightedListeners();
	}

	public void setHighlighted2(IHighlightable h) {
		highlighted2 = h;
		refreshHighlightedListeners();
	}

	public IHighlightable getHighlighted1() {
		return highlighted1;
	}

	public IHighlightable getHighlighted2() {
		return highlighted2;
	}
	
	private void refreshHighlightedListeners() {
		highlightedEntityListeners.forEach(IHighlightedListener::onHighlightedChange);
	}

	public void resetHighlightedEntities() {
		highlighted1 = null;
		highlighted2 = null;
	}

	@Override
	protected void paintComponent(Graphics graphic) {
		super.paintComponent(graphic);

		// Draw Grid
		setting.render(graphic);

		// Refresh simulation properties
		refreshSimulationSize();
		setting.refreshSimulationProperties(currentHeight, currentWidth);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		IHighlightable highlight = setting.getHighlightAtPosition(e.getX(), e.getY());
		if (highlight == null)
			return;
		if (e.getButton() == MouseEvent.BUTTON1) {
			setHighlighted1(highlight);
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			setHighlighted2(highlight);
		} else {
			setHighlighted1(highlight);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		setting.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		grabFocus();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		isMousePointing = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	private void refreshSimulationSize() {
		Rectangle rectangle = this.getBounds();
		currentHeight = rectangle.height;
		currentWidth = rectangle.width;
	}

	public void reset() {
		resetZoom();
		resetOffset();
		resetHighlightedEntities();
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
		offsetX = SimulatorConfig.getDefaultOffsetX();
		offsetY = SimulatorConfig.getDefaultOffsetY();
	}

	public float getOffsetX() {
		return offsetX;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public abstract void resetZoom();
	
	public abstract void zoomIn(float zoom);
	
	public abstract void zoomOut(float zoom);

}