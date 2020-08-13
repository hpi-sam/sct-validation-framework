package de.hpi.mod.sim.worlds.abstract_grid;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Renders the Grid of the Simulation
 */
public class GridRenderer {

    private IGrid grid;
    private SimulationBlockView simView;


    public GridRenderer(SimulationBlockView simView, IGrid grid) {
        this.simView = simView;
        this.grid = grid;
    }

    /**
     * Renders the grid
     * @param graphic the Graphics to render on
     */
    public void render(Graphics graphic) {

        // Shift Viewpoint by offset
        float blocksOffsetX = simView.getOffsetX();
        float blocksOffsetY = simView.getOffsetY();

        float blockSize = simView.getBlockSize();

        // The Size if the Window in Blocks
        int widthInBlocks = (int) (simView.getWidth() / blockSize + 2);
        int heightInBlocks = (int) (simView.getHeight() / blockSize + 2);

        int lowestY = GridConfiguration.getOriginOffsetY() + 1;
        int leftertX = GridConfiguration.getOriginOffsetX() + 1;

        for (int y = -lowestY + (int) blocksOffsetY; y < heightInBlocks - lowestY + blocksOffsetY; y++) {
            for (int x = -leftertX + (int) blocksOffsetX - widthInBlocks/2; x < widthInBlocks/2 + blocksOffsetX; x++) {
                Position current = new Position(x, y);
                ICellType cellType = grid.cellType(current);

                // Highlighted Cells are special
                boolean highlight = simView.isMousePointing() && simView.getMousePointer().equals(current);
                
                // The Zero Zero Cell should be marked
                boolean isZeroZero = current.is(new Position(0, 0));
                
                boolean blockedBy1 = grid.affects(simView.getHighlighted1(), current);
                boolean blockedBy2 = grid.affects(simView.getHighlighted2(), current);

                // Draw the block
                drawBlock(graphic, cellType, simView.toDrawPosition(current), cellType.borderLeft(), highlight, isZeroZero, blockedBy1, blockedBy2);
            }
        }
    }

    /**
     * Draws a Block
     * @param graphic Graphics to render to
     * @param cell The type of the cell determines how its rendered
     * @param drawPosition The draw-position
     * @param borderLeft should a border be drawn on the left of the block?
     * @param highlight Highlighted?
     * @param isZeroZero should the zero-zero highlight be drawn?
 	 * @param blockedBy1 is this block blocked by the first highlighted robot?
 	 * @param blockedBy2 is this block blocked by the second highlighted robot?
     */
    private void drawBlock(Graphics graphic, ICellType cell, Point2D drawPosition, boolean borderLeft, boolean highlight, boolean isZeroZero, boolean blockedBy1, boolean blockedBy2) {
        float blockSize = simView.getBlockSize();

        graphic.setColor(cell.getColor());

        //draw the block
        graphic.fillRect((int) drawPosition.getX(), (int) drawPosition.getY(), (int) blockSize, (int) blockSize);
        
        //draw the grid pattern around the block
        graphic.setColor(darken(graphic.getColor()));
        graphic.drawRect((int) drawPosition.getX(), (int) drawPosition.getY(), (int) blockSize, (int) blockSize);
        
        //draw the border to the left of the block
        if (borderLeft) {
        	graphic.setColor(Color.DARK_GRAY);
        	graphic.drawRect((int) drawPosition.getX(), (int) drawPosition.getY(), 0, (int) blockSize);
        }
        
        //draw the zero-zero highlight
        if (isZeroZero) {
        	graphic.setColor(Color.GREEN);
            graphic.fillOval((int) (drawPosition.getX() + blockSize / 4),
            		(int) (drawPosition.getY() + blockSize / 4),
            		(int) blockSize / 2, 
            		(int) blockSize / 2);
        }

        //draw the mouse highlight
        if (highlight) {
            graphic.setColor(Color.RED);
            graphic.fillRect((int) (drawPosition.getX() + blockSize / 4),
                    (int) (drawPosition.getY() + blockSize / 4),
                    (int) (blockSize / 2),
                    (int) (blockSize / 2));
        }
    }
    
    /*
     * darkens a color for the grid pattern
     */
    private Color darken(Color color) {
    	float darken = 0.95f;
    	return new Color((int) (color.getRed() * darken), 
        		(int) (color.getGreen() * darken), 
        		(int) (color.getBlue() * darken));
    }
}
