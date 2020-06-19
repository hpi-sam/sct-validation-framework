package de.hpi.mod.sim.core.view.sim;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.Position;
import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.core.view.model.IHighlightedEntityListener;
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
	private float blockSize = SimulatorConfig.getDefaultBlockSize();

	/**
	 * Offset in blocks
	 */
	private float offsetX = SimulatorConfig.getDefaultOffsetX(), offsetY = SimulatorConfig.getDefaultOffsetY();

	
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
	 * The highlighted Entity. Null if none
	 */
	private Entity highlightedEntity1 = null;
	private Entity highlightedEntity2 = null;

	private SimulatorView view;

	/**
	 * List of {@link IHighlightedEntityListener}s. Gets called if the highlighted
	 * Entity changes
	 */
	private List<IHighlightedEntityListener> highlightedEntityListeners = new ArrayList<>();
	private List<IHighlightedEntityListener> highlightedEntityListeners2 = new ArrayList<>();



	public SimulationWorld(SimulatorView view) {
		this.view = view;
	}

	public void addHighlightedRobotListener(IHighlightedEntityListener highlightedEntityListener) {
		highlightedEntityListeners.add(highlightedEntityListener);
	}

	public void addHighlightedRobotListener2(IHighlightedEntityListener highlightedEntityListener2) {
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

	public void setHighlightedEntity1(Entity e) {
		highlightedEntity1 = e;
		refreshHighlightedEntityListeners();
	}

	public void setHighlightedEntity2(Entity e) {
		highlightedEntity2 = e;
		refreshHighlightedEntityListeners();
	}

	public Entity getHighlightedEntity1() {
		return highlightedEntity1;
	}

	public Entity getHighlightedEntity2() {
		return highlightedEntity2;
	}

	/**
	 * Converts a draw-position to a grid-position
	 */
	public Position toGridPosition(int x, int y) {
		y = (int) (view.getHeight() - y - blockSize / 2);
		int blockX = (int) Math.floor(x / blockSize - view.getWidth()/(2*blockSize)+ offsetX);
		int blockY = (int) Math.floor(y / blockSize - SimulatorConfig.QUEUE_SIZE + offsetY);

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
		float drawY = view.getHeight() - (y + SimulatorConfig.QUEUE_SIZE + 1.5f - offsetY) * blockSize;
		return new Point2D.Float(drawX, drawY);
	}

	public boolean isBlockedByHighlightedEntity1(Position position) {

		//TODO move somewhere specific
		if (highlightedEntity1 == null || ! (highlightedEntity1 instanceof Robot))
			return false;
		Robot r = (Robot) highlightedEntity1;
		return position.is(r.pos()) || position.is(r.oldPos());
	}

	public boolean isBlockedByHighlightedRobot2(Position position) {
		if (highlightedEntity2 == null || !(highlightedEntity2 instanceof Robot))
			return false;
		Robot r = (Robot) highlightedEntity1;
		return position.is(r.pos()) || position.is(r.oldPos());
	}

	private void refreshHighlightedEntityListeners() {
		highlightedEntityListeners.forEach(IHighlightedEntityListener::onHighlightedEntityChange);
	}

	public void resetHighlightedEntities() {
		highlightedEntity1 = null;
		highlightedEntity2 = null;
	}


}
