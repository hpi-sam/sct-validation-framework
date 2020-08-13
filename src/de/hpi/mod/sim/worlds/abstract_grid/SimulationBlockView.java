package de.hpi.mod.sim.worlds.abstract_grid;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.view.panels.AnimationPanel;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class SimulationBlockView extends AnimationPanel {
 
    private static final long serialVersionUID = -990087401929081125L;
    
    /**
     * The scaling of a block
     */
    private float blockSize;
    /**
     * The Position of the mouse in blocks
     */
    private Position mousePointer;

    public SimulationBlockView(World world) {
        super(world);
        blockSize = GridConfiguration.getDefaultBlockSize();
    }

    @Override
    public void zoomIn(float zoom) {
        if (blockSize < GridConfiguration.getMaxBlockSize())
            blockSize += zoom;
    }

    @Override
    public void zoomOut(float zoom) {
        if (blockSize > GridConfiguration.getMinBlockSize())
            blockSize -= zoom;
    }

    @Override
    public void resetZoom() {
        blockSize = GridConfiguration.getDefaultBlockSize();
    }

    public float getBlockSize() {
        return blockSize;
    }

    public void setMousePointer(Position mousePointer) {
        this.mousePointer = mousePointer;
        setMousePointing(true);
    }

    public Position getMousePointer() {
        return mousePointer;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        setMousePointer(toGridPosition(e.getX(), e.getY()));
    }

  
    /**
     * Converts a draw-position to a grid-position
     */
    public Position toGridPosition(int x, int y) {
        y = (int) (getHeight() - y - blockSize / 2);
        int blockX = (int) Math.floor(
                x / blockSize - getWidth() / (2 * blockSize) - GridConfiguration.getOriginOffsetX() + getOffsetX());
        int blockY = (int) Math
                .floor(y / blockSize - GridConfiguration.getOriginOffsetY() + getOffsetY());

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
        float drawX = getWidth() / 2 + (x + GridConfiguration.getOriginOffsetX() - getOffsetX()) * blockSize;
        float drawY = getHeight()
                - (y + GridConfiguration.getOriginOffsetY() + 1.5f - getOffsetY()) * blockSize;
        return new Point2D.Float(drawX, drawY);
    }

}