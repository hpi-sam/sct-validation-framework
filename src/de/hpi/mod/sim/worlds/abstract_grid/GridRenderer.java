package de.hpi.mod.sim.worlds.abstract_grid;

import java.awt.*;
import java.awt.geom.Point2D;
/**
 * Renders the Grid of the Simulation
 */
public class GridRenderer {

    private GridManager grid;
    private SimulationBlockView simView;


    public GridRenderer(SimulationBlockView simView, GridManager grid) {
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
        int leftestX = GridConfiguration.getOriginOffsetX() + 1;
        
        //Draw cells
        for (int y = -lowestY + (int) blocksOffsetY; y < heightInBlocks - lowestY + blocksOffsetY; y++) {
            for (int x = -leftestX + (int) blocksOffsetX - widthInBlocks / 2; x < widthInBlocks / 2
                    - leftestX + blocksOffsetX; x++) {
                Position current = new Position(x, y);
                ICellType cellType = grid.cellType(current);

                // Highlighted Cells are special
                boolean highlight = simView.isMousePointing() && simView.getMousePointer().equals(current);

                // The Zero Zero Cell should be marked
                boolean isZeroZero = current.is(new Position(0, 0));

                boolean blockedBy1 = grid.affects(simView.getHighlighted1(), current);
                boolean blockedBy2 = grid.affects(simView.getHighlighted2(), current);

                // Draw the block
                drawBlock(graphic, cellType, simView.toDrawPosition(current), highlight, isZeroZero, blockedBy1,
                        blockedBy2);
            }
        }
        //Draw borders
        for (int y = -lowestY + (int) blocksOffsetY; y < heightInBlocks - lowestY + blocksOffsetY; y++) {
            for (int x = -leftestX + (int) blocksOffsetX - widthInBlocks / 2; x < widthInBlocks / 2 - leftestX
                    + blocksOffsetX; x++) {
                Position current = new Position(x, y);
                ICellType cellType = grid.cellType(current);
                drawBorders(graphic, cellType, simView.toDrawPosition(current));
            }
        }

        
    }

    /**
     * Draws a Block
     * @param graphic Graphics to render to
     * @param cell The type of the cell determines how its rendered
     * @param drawPosition The draw-position
     * @param highlight Highlighted?
     * @param isZeroZero should the zero-zero highlight be drawn?
 	 * @param blockedBy1 is this block blocked by the first highlighted entity?
 	 * @param blockedBy2 is this block blocked by the second highlighted entity?
     */
    protected void drawBlock(Graphics graphic, ICellType cell, Point2D drawPosition, boolean highlight,
            boolean isZeroZero, boolean blockedBy1, boolean blockedBy2) {
        float blockSize = simView.getBlockSize();

        int x = (int) drawPosition.getX();
        int y = (int) drawPosition.getY();

        graphic.setColor(cell.getColor());

        //draw the block
        graphic.fillRect(x, y, (int) blockSize, (int) blockSize);

        //draw the grid pattern around the block
        graphic.setColor(darken(graphic.getColor()));
        graphic.drawLine(x, y, x + (int) blockSize, y);
        graphic.drawLine(x, y, x, y + (int) blockSize);
        graphic.drawLine(x + (int) blockSize, y, x + (int) blockSize, y + (int) blockSize);
        graphic.drawLine(x, y + (int) blockSize, x + (int) blockSize, y + (int) blockSize);

        //draw the zero-zero highlight
        if (isZeroZero) {
            graphic.setColor(Color.GREEN);
            graphic.fillOval((int) (drawPosition.getX() + blockSize / 4), (int) (drawPosition.getY() + blockSize / 4),
                    (int) blockSize / 2, (int) blockSize / 2);
        }

        //draw the mouse highlight
        if (highlight) {
            graphic.setColor(Color.RED);
            graphic.fillRect((int) (drawPosition.getX() + blockSize / 4), (int) (drawPosition.getY() + blockSize / 4),
                    (int) (blockSize / 2), (int) (blockSize / 2));
        }
    }
    
    private void drawBorders(Graphics graphic, ICellType cell, Point2D drawPosition) {
        for (Orientation orientation : Orientation.values()) {
            if (cell.hasBorder(orientation))
                drawBorder(graphic, cell, orientation, drawPosition);
        }            
    }
    
    private void drawBorder(Graphics graphic, ICellType cell, Orientation orientation, Point2D drawPosition) {
        graphic.setColor(cell.borderColor(orientation));
        Graphics2D graphics2D = (Graphics2D) graphic;
        graphics2D.setStroke(new BasicStroke(cell.borderWidth(orientation)));
        
        float blockSize = simView.getBlockSize();
        int x = (int) drawPosition.getX();
        int y = (int) drawPosition.getY();
        
        switch (orientation) {
            case EAST:
                graphic.drawLine(x + (int) blockSize, y, x + (int) blockSize, y + (int) blockSize);
                break;
            case NORTH:
                graphic.drawLine(x, y, x + (int) blockSize, y);
                break;
            case SOUTH:
            graphic.drawLine(x, y + (int) blockSize, x + (int) blockSize, y + (int) blockSize);
                break;
            case WEST:
                graphics2D.drawLine(x, y, x, y + (int) blockSize);
                break;
            default:
                throw new IllegalArgumentException();
            
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
