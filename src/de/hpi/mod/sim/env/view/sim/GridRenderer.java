package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.ServerGridManagement;
import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.model.CellType;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Renders the Grid of the Simulation
 */
public class GridRenderer {
	
	private static float GRID_DARKEN_FACTOR = (float) 0.95;

    private ServerGridManagement grid;
    private SimulationWorld world;
    
    private BufferedImage leftClickedRobotBlocking, rightClickedRobotBlocking;


    public GridRenderer(SimulationWorld world, ServerGridManagement grid) {
        this.world = world;
        this.grid = grid;
        
        loadImages();
    }
    
    private void loadImages() {
        try {
        	leftClickedRobotBlocking = ImageIO.read(new File(SimulatorConfig.getStringPathToLeftClickedRobotBlocking()));
        	rightClickedRobotBlocking = ImageIO.read(new File(SimulatorConfig.getStringPathToRightClickedRobotBlocking()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renders the grid
     * @param graphic the Graphics to render on
     */
    public void render(Graphics graphic) {

        // Shift Viewpoint by offset
        float blocksOffsetX = world.getOffsetX();
        float blocksOffsetY = world.getOffsetY();

        float blockSize = world.getBlockSize();

        // The Size if the Window in Blocks
        int widthInBlocks = (int) (world.getView().getWidth() / blockSize + 2);
        int heightInBlocks = (int) (world.getView().getHeight() / blockSize + 2);

        int stationDepth = SimulatorConfig.QUEUE_SIZE + 1;

        for (int y = -stationDepth + (int) blocksOffsetY; y < heightInBlocks - stationDepth + blocksOffsetY; y++) {
            for (int x = (int) blocksOffsetX; x < widthInBlocks + blocksOffsetX; x++) {
                Position current = new Position(x, y);
                CellType cellType = grid.cellType(current);
                
                // Some cells need a border on the left
                boolean borderLeft = (cellType == CellType.BATTERY || cellType == CellType.QUEUE || cellType == CellType.LOADING);

                // Highlighted Cells are special
                boolean highlight = world.isMousePointing() && world.getMousePointer().equals(current);
                
                // The Zero Zero Cell should be marked
                boolean isZeroZero = current.is(new Position(0, 0));
                
                // Unused stations should be drawn differently
                boolean isUnusedStationBlock = (cellType == CellType.BATTERY || cellType == CellType.STATION || cellType == CellType.LOADING || cellType == CellType.QUEUE) 
                		&& (current.getX() >= SimulatorConfig.getChargingStationsInUse() * 3 || current.getX() < 0);
                
                boolean blockedBy1 = world.isBlockedByHighlightedRobot1(current);
                boolean blockedBy2 = world.isBlockedByHighlightedRobot2(current);

                // Draw the block
                drawBlock(graphic, cellType, world.toDrawPosition(current), borderLeft, highlight, isZeroZero, isUnusedStationBlock, blockedBy1, blockedBy2);
            }
        }
    }

    /**
     * Draws a Block
     * @param graphic Graphics to render to
     * @param cell The type of the cell determines how its rendered
     * @param drawPos The draw-position
     * @param highlight Highlighted?
     */
    private void drawBlock(Graphics graphic, CellType cell, Point2D drawPos, boolean borderLeft, boolean highlight, boolean isZeroZero, boolean isUnusedStationBlock, boolean blockedBy1, boolean blockedBy2) {
        float blockSize = world.getBlockSize();

        if (cell == CellType.BLOCK || isUnusedStationBlock)
            graphic.setColor(Color.DARK_GRAY);
        else if (cell == CellType.WAYPOINT)
            graphic.setColor(Color.WHITE);
        else if (cell == CellType.CROSSROAD)
            graphic.setColor(Color.LIGHT_GRAY);
        else if (cell == CellType.BATTERY)
            graphic.setColor(new Color(0xe0d9f9));
        else if (cell == CellType.LOADING)
            graphic.setColor(new Color(0xc0e8ed));
        else if (cell == CellType.STATION || cell == CellType.QUEUE)
            graphic.setColor(new Color(0xfff3e2));

        graphic.fillRect((int) drawPos.getX(), (int) drawPos.getY(), (int) blockSize, (int) blockSize);
        
        Color color = graphic.getColor();
        color = new Color((int) (color.getRed() * GRID_DARKEN_FACTOR), 
        		(int) (color.getGreen() * GRID_DARKEN_FACTOR), 
        		(int) (color.getBlue() * GRID_DARKEN_FACTOR));
        graphic.setColor(color);
        graphic.drawRect((int) drawPos.getX(), (int) drawPos.getY(), (int) blockSize, (int) blockSize);
        
        if (borderLeft) {
        	graphic.setColor(Color.DARK_GRAY);
        	graphic.drawRect((int) drawPos.getX(), (int) drawPos.getY(), 0, (int) blockSize);
        }
        
        if (isZeroZero) {
        	graphic.setColor(Color.GREEN);
            graphic.fillOval((int) (drawPos.getX() + blockSize / 4),
            		(int) (drawPos.getY() + blockSize / 4),
            		(int) blockSize / 2, 
            		(int) blockSize / 2);
        }

        // Draw Highlight
        if (highlight) {
            graphic.setColor(Color.RED);
            graphic.fillRect((int) (drawPos.getX() + blockSize / 4),
                    (int) (drawPos.getY() + blockSize / 4),
                    (int) (blockSize / 2),
                    (int) (blockSize / 2));
        }
        
        if (blockedBy1) {
        	graphic.drawImage(leftClickedRobotBlocking, (int) drawPos.getX(), (int) drawPos.getY(), (int) blockSize, (int) blockSize, null);
        }
        
        if (blockedBy2) {
        	graphic.drawImage(rightClickedRobotBlocking, (int) drawPos.getX(), (int) drawPos.getY(), (int) blockSize, (int) blockSize, null);
        }
    }
}
