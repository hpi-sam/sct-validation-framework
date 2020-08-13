package de.hpi.mod.sim.worlds.abstract_grid;

import de.hpi.mod.sim.core.Configuration;

public class GridConfiguration extends Configuration {

    public static final float DEFAULT_BLOCK_SIZE = 20;
    public static final float MIN_BLOCK_SIZE = 5;
    public static final float MAX_BLOCK_SIZE = 30;

    public static final int ORIGIN_OFFSET_X = 0;
    public static final int ORIGIN_OFFSET_Y = 0;

    public static final int DEFAULT_MAP_HEIGHT = 10;
    public static final int NOT_USED_ROWS = 3;

    private static float defaultBlockSize = DEFAULT_BLOCK_SIZE;
    private static float minBlockSize = MIN_BLOCK_SIZE;
    private static float maxBlockSize = MAX_BLOCK_SIZE;

    private static int originOffsetX = ORIGIN_OFFSET_X;
    private static int originOffsetY = ORIGIN_OFFSET_Y;

    private static int defaultMapHeight = DEFAULT_MAP_HEIGHT;
    private static int mapHeight = DEFAULT_MAP_HEIGHT;

    private static int notUsedRows = NOT_USED_ROWS;

    public static float getDefaultBlockSize() {
        return defaultBlockSize;
    }

    public static float getMinBlockSize() {
        return minBlockSize;
    }

    public static float getMaxBlockSize() {
        return maxBlockSize;
    }

    public static int getOriginOffsetX() {
        return originOffsetX;
    }

    public static int getOriginOffsetY() {
        return originOffsetY;
    }

    public static int getDefaultMapHeight() {
        return defaultMapHeight;
    }

    public static int getMapHeight() {
        return mapHeight;
    }

    public static int getNotUsedRows() {
        return notUsedRows;
    }

    public static void setMapHeight(int mapHeight) {
        GridConfiguration.mapHeight = mapHeight;
    }

    public static void setOriginOffsetX(int offset) {
        GridConfiguration.originOffsetX = offset;
    }
   
    public static void setOriginOffsetY(int offset) {
        GridConfiguration.originOffsetY = offset;
    }
}
