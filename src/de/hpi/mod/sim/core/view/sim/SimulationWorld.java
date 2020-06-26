package de.hpi.mod.sim.core.view.sim;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.model.IGrid;
import de.hpi.mod.sim.core.model.IHighlightable;
import de.hpi.mod.sim.core.model.Position;
import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.core.view.model.IHighlightedListener;

/**
 * Stores the values needed to display the Simulation and gives access to the
 * Simulation
 */
public class SimulationWorld {

	/**
	 * Default values needed to reset
	 */

	/**
	 * The scaling of a block
	 */
	private float blockSize;

	/**
	 * Offset in blocks
	 */
	private float offsetX, offsetY;

	
	/**
	 * The Position of the mouse in blocks
	 */
	private Position mousePointer;

	/**
	 * Whether the mouse is on screen or nor. If false,
	 * {@link SimulationWorld#mousePointer} is garbage
	 */
	private boolean isMousePointing;

	/**
	 * The highlighted objects. Null if none
	 */
	private IHighlightable highlighted1 = null;
	private IHighlightable highlighted2 = null;

	private SimulatorView view;

	/**
	 * List of {@link IHighlightedListener}s. Gets called if the highlighted
	 * Entity changes
	 */
	private List<IHighlightedListener> highlightedEntityListeners = new ArrayList<>();
	private List<IHighlightedListener> highlightedEntityListeners2 = new ArrayList<>();

	private IGrid grid;



	public SimulationWorld(IGrid grid) {
		blockSize = SimulatorConfig.getDefaultBlockSize();
		offsetX = SimulatorConfig.getDefaultOffsetX();
		offsetY = SimulatorConfig.getDefaultOffsetY();
		this.grid = grid;
	}

	public void initialize(SimulatorView view) {
		this.view = view;
	}

	public void addHighlightedListener(IHighlightedListener highlightedListener) {
		highlightedEntityListeners.add(highlightedListener);
	}

	public void addHighlightedListener2(IHighlightedListener highlightedListener) {
		highlightedEntityListeners2.add(highlightedListener);
	}

	public SimulatorView getView() {
		return view;
	}


	public void zoomIn(float zoom) {
		if (blockSize < SimulatorConfig.getMaxBlockSize())
			blockSize += zoom;
	}

	public void zoomOut(float zoom) {
		if (blockSize > SimulatorConfig.getMinBlockSize())
			blockSize -= zoom;
	}

	public void resetZoom() {
		blockSize = SimulatorConfig.getDefaultBlockSize();
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
		offsetX = SimulatorConfig.getDefaultOffsetX();
		offsetY = SimulatorConfig.getDefaultOffsetY();
	}

	public float getOffsetX() {
		return offsetX;
	}

	public float getOffsetY() {
		return offsetY;
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

	/**
	 * Converts a draw-position to a grid-position
	 */
	public Position toGridPosition(int x, int y) {
		return grid.toGridPosition(x, y, this);
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
		return grid.toDrawPosition(x, y, this);
	}


}
