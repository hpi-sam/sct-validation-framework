package de.hpi.mod.sim.worlds.abstract_grid;

import de.hpi.mod.sim.core.Configuration;

public class GridConfiguration extends Configuration {

    public static final float DEFAULT_INITIAL_BLOCK_SIZE = 20;
    public static final float DEFAULT_MIN_BLOCK_SIZE = 5;
    public static final float DEFAULT_MAX_BLOCK_SIZE = 30;

    public static final int DEFAULT_ORIGIN_OFFSET_X = 0;
    public static final int DEFAULT_ORIGIN_OFFSET_Y = 0;

    public static final int DEFAULT_MAP_HEIGHT = 10; //TODO move?
    public static final int NOT_USED_ROWS = 3;// TODO move?

    private static float initialBlockSize = DEFAULT_INITIAL_BLOCK_SIZE;
    private static float minBlockSize = DEFAULT_MIN_BLOCK_SIZE;
    private static float maxBlockSize = DEFAULT_MAX_BLOCK_SIZE;

    private static int originOffsetX = DEFAULT_ORIGIN_OFFSET_X;
    private static int originOffsetY = DEFAULT_ORIGIN_OFFSET_Y;

    private static int defaultMapHeight = DEFAULT_MAP_HEIGHT;
    private static int mapHeight = DEFAULT_MAP_HEIGHT;

    private static int notUsedRows = NOT_USED_ROWS;

    public static float getDefaultBlockSize() {
        return initialBlockSize;
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

    public static void setMapHeight(int height) {
        mapHeight = height;
    }
   
    public static void resetOriginOffset() {
        originOffsetY = DEFAULT_ORIGIN_OFFSET_X;
        originOffsetY = DEFAULT_ORIGIN_OFFSET_Y;
    }

    public static void setOriginOffsetX(int offset) {
        originOffsetX = offset;
    }
   
    public static void setOriginOffsetY(int offset) {
        originOffsetY = offset;
    }
}
