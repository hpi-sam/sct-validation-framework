package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.ServerGridManagement;
import de.hpi.mod.sim.env.model.CellType;
import de.hpi.mod.sim.env.model.Position;

import java.awt.*;

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
        float realOffsetX = offsetX - blocksOffsetX * blockSize;
        float realOffsetY = offsetY - blocksOffsetY * blockSize;
        int stationDepth = ServerGridManagement.QUEUE_SIZE + 1;

        for (int y = -stationDepth + blocksOffsetY; y < blocksHeight - stationDepth + blocksOffsetY; y++) {
            for (int x = blocksOffsetX; x < blocksWidth + blocksOffsetX; x++) {
                Position current = new Position(x, y);
                float realX = (x - blocksOffsetX) * blockSize - realOffsetX;
                float realY = (y - blocksOffsetY + stationDepth) * blockSize - realOffsetY;
                boolean highlight = parent.isHighlighted() && parent.getHighlight().equals(current);

                drawBlock(g, grid.cellType(current), realX, realY, highlight);
            }
        }
    }

    private void drawBlock(Graphics g, CellType cell, float x, float y, boolean highlight) {
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

        g.fillRect((int) x, (int) (parent.getHeight() - y - blockSize / 2), (int) blockSize, (int) blockSize);

        if (highlight) {
            g.setColor(Color.RED);
            g.fillRect((int) (x + blockSize / 4),
                    (int) (parent.getHeight() - y - blockSize / 4),
                    (int) (blockSize / 2),
                    (int) (blockSize / 2));
        }
    }
}
