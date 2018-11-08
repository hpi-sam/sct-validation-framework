package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.ServerGridManagement;
import de.hpi.mod.sim.env.model.CellType;
import de.hpi.mod.sim.env.model.Position;

import java.awt.*;
import java.awt.geom.Point2D;

public class GridRenderer {

    private ServerGridManagement grid;
    private SimulatorView parent;


    public GridRenderer(SimulatorView parent, ServerGridManagement grid) {
        this.parent = parent;
        this.grid = grid;
    }

    public void update(float delta) {

    }

    public void render(Graphics g) {
        float offsetX = parent.getOffsetX();
        float offsetY = parent.getOffsetY();
        float blockSize = parent.getBlockSize();
        int blocksOffsetX = (int) (offsetX / blockSize) - 1;
        int blocksOffsetY = (int) (offsetY / blockSize);
        int blocksWidth = (int) (parent.getWidth() / blockSize + 2);
        int blocksHeight = (int) (parent.getHeight() / blockSize + 2);
        int stationDepth = ServerGridManagement.QUEUE_SIZE + 1;

        for (int y = -stationDepth + blocksOffsetY; y < blocksHeight - stationDepth + blocksOffsetY; y++) {
            for (int x = blocksOffsetX; x < blocksWidth + blocksOffsetX; x++) {
                Position current = new Position(x, y);
                boolean highlight = parent.isHighlighted() && parent.getHighlight().equals(current);

                drawBlock(g, grid.cellType(current), parent.toDrawPosition(current), highlight);
            }
        }
    }

    private void drawBlock(Graphics g, CellType cell, Point2D drawPos, boolean highlight) {
        float blockSize = parent.getBlockSize();

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
