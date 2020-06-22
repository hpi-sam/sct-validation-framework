package de.hpi.mod.sim.core.view.sim;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.model.IHighlightable;
import de.hpi.mod.sim.core.model.Position;
import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.core.view.model.IHighlightedListener;
import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteWarehouseSimConfig;
import de.hpi.mod.sim.setting.robot.Robot;

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



	public SimulationWorld() {
		blockSize = SimulatorConfig.getDefaultBlockSize();
		offsetX = SimulatorConfig.getDefaultOffsetX();
		offsetY = SimulatorConfig.getDefaultOffsetY();
	}

	public void initialize(SimulatorView view) {
		this.view = view;
	}

	public void addHighlightedRobotListener(IHighlightedListener highlightedEntityListener) {
		highlightedEntityListeners.add(highlightedEntityListener);
	}

	public void addHighlightedRobotListener2(IHighlightedListener highlightedEntityListener2) {
		highlightedEntityListeners2.add(highlightedEntityListener2);
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
		refreshHighlightedEntityListeners();
	}

	public void setHighlighted2(IHighlightable h) {
		highlighted2 = h;
		refreshHighlightedEntityListeners();
	}

	public IHighlightable getHighlighted1() {
		return highlighted1;
	}

	public IHighlightable getHighlighted2() {
		return highlighted2;
	}

	/**
	 * Converts a draw-position to a grid-position
	 */
	public Position toGridPosition(int x, int y) {
		y = (int) (view.getHeight() - y - blockSize / 2);
		int blockX = (int) Math.floor(x / blockSize - view.getWidth() / (2 * blockSize) + offsetX);
		int blockY = (int) Math.floor(y / blockSize - InfiniteWarehouseSimConfig.getQueueSize() + offsetY);  //TODO move specific or make more generic

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
		float drawX = view.getWidth()/2 + (x - offsetX) * blockSize;
		float drawY = view.getHeight() - (y + InfiniteWarehouseSimConfig.getQueueSize() + 1.5f - offsetY) * blockSize; //TODO move specific or make general
		return new Point2D.Float(drawX, drawY);
	}

	public boolean isBlockedByHighlightedEntity1(Position position) {

		//TODO move somewhere specific
		if (highlighted1 == null || ! (highlighted1 instanceof Robot))
			return false;
		Robot r = (Robot) highlighted1;
		return position.is(r.pos()) || position.is(r.oldPos());
	}

	public boolean isBlockedByHighlightedRobot2(Position position) {
		if (highlighted2 == null || !(highlighted2 instanceof Robot))
			return false;
		Robot r = (Robot) highlighted1;
		return position.is(r.pos()) || position.is(r.oldPos());
	}

	private void refreshHighlightedEntityListeners() {
		highlightedEntityListeners.forEach(IHighlightedListener::onHighlightedChange);
	}

	public void resetHighlightedEntities() {
		highlighted1 = null;
		highlighted2 = null;
	}


}
