package de.hpi.mod.sim.core.view.sim;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.model.Position;
import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.core.simulation.robot.Robot;
import de.hpi.mod.sim.core.view.model.IHighlightedRobotListener;

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
	 * The highlighted Robot. Null if none
	 */
	private Robot highlightedRobot1 = null;
	private Robot highlightedRobot2 = null;

	private SimulatorView view;

	/**
	 * List of {@link IHighlightedRobotListener}s. Gets called if the highlighted
	 * Robot changes
	 */
	private List<IHighlightedRobotListener> highlightedRobotListeners = new ArrayList<>();
	private List<IHighlightedRobotListener> highlightedRobotListeners2 = new ArrayList<>();



	public SimulationWorld(SimulatorView view) {
		this.view = view;
	}

	public void addHighlightedRobotListener(IHighlightedRobotListener highlightedRobotListener) {
		highlightedRobotListeners.add(highlightedRobotListener);
	}

	public void addHighlightedRobotListener2(IHighlightedRobotListener highlightedRobotListener2) {
		highlightedRobotListeners2.add(highlightedRobotListener2);
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

	public void setHighlightedRobot1(Robot r) {
		highlightedRobot1 = r;
		refreshHighlightedRobotListeners();
	}

	public void setHighlightedRobot2(Robot r) {
		highlightedRobot2 = r;
		refreshHighlightedRobotListeners();
	}

	public Robot getHighlightedRobot1() {
		return highlightedRobot1;
	}

	public Robot getHighlightedRobot2() {
		return highlightedRobot2;
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

	public boolean isBlockedByHighlightedRobot1(Position position) {
		if (highlightedRobot1 == null)
			return false;
		return position.is(highlightedRobot1.pos()) || position.is(highlightedRobot1.oldPos());
	}

	public boolean isBlockedByHighlightedRobot2(Position position) {
		if (highlightedRobot2 == null)
			return false;
		return position.is(highlightedRobot2.pos()) || position.is(highlightedRobot2.oldPos());
	}

	private void refreshHighlightedRobotListeners() {
		highlightedRobotListeners.forEach(IHighlightedRobotListener::onHighlightedRobotChange);
	}

	public void resetHighlightedRobots() {
		highlightedRobot1 = null;
		highlightedRobot2 = null;
	}


}
