package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.ServerGridManagement;
import de.hpi.mod.sim.env.model.CellType;
import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import java.awt.*;
import java.awt.geom.Point2D;

public class GridRenderer {

    private ServerGridManagement grid;
    private SimulationWorld world;


    public GridRenderer(SimulationWorld world, ServerGridManagement grid) {
        this.world = world;
        this.grid = grid;
    }

    public void render(Graphics g) {
        float blocksOffsetX = world.getOffsetX();
        float blocksOffsetY = world.getOffsetY();
        float blockSize = world.getBlockSize();
        int widthInBlocks = (int) (world.getView().getWidth() / blockSize + 2);
        int heightInBlocks = (int) (world.getView().getHeight() / blockSize + 2);
        int stationDepth = ServerGridManagement.QUEUE_SIZE + 1;

        for (int y = -stationDepth + (int) blocksOffsetY; y < heightInBlocks - stationDepth + blocksOffsetY; y++) {
            for (int x = (int) blocksOffsetX; x < widthInBlocks + blocksOffsetX; x++) {
                Position current = new Position(x, y);
                boolean highlight = world.isMousePointing() && world.getMousePointer().equals(current);

                drawBlock(g, grid.cellType(current), world.toDrawPosition(current), highlight);
            }
        }
    }

    private void drawBlock(Graphics g, CellType cell, Point2D drawPos, boolean highlight) {
        float blockSize = world.getBlockSize();

        if (cell == CellType.BLOCK)
            g.setColor(Color.BLACK);
        if (cell == CellType.WAYPOINT)
            g.setColor(Color.WHITE);
        if (cell == CellType.CROSSROAD)
            g.setColor(Color.LIGHT_GRAY);
        if (cell == CellType.BATTERY)
            g.setColor(Color.YELLOW);
        if (cell == CellType.LOADING)
            g.setColor(Color.BLUE);
        if (cell == CellType.STATION)
            g.setColor(Color.GRAY);

        g.fillRect((int) drawPos.getX(), (int) drawPos.getY(), (int) blockSize, (int) blockSize);

        if (highlight) {
            g.setColor(Color.RED);
            g.fillRect((int) (drawPos.getX() + blockSize / 4),
                    (int) (drawPos.getY() + blockSize / 4),
                    (int) (blockSize / 2),
                    (int) (blockSize / 2));
        }
    }
}
