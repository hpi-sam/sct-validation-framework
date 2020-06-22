package de.hpi.mod.sim.core.simulation;

import java.util.HashMap;
import java.util.Map;
/**
 * Contains all "magic-numbers" set in the Simulation.
 * Can be set by the view.
 */
public class SimulatorConfig {

    public static  final int SCENARIO_PASSING_TIME = 60; //Not in use at the moment
    public static  final String TEST_FILE_NAME = ".testresults";
    public static  final String STRING_PATH_TO_PLAY_ICON = "res/play.png";
    public static  final String STRING_PATH_TO_PAUSE_ICON = "res/pause.png";
    public static  final String STRING_PATH_TO_STOP_ICON = "res/stop.png";
    public static  final String STRING_PATH_TO_RESET_ICON = "res/reset.png";
   
	public static  final int DEFAULT_WAITING_TIME_BEFORE_TEST = 1000; //in ms
	
	public static  final float DEFAULT_BLOCK_SIZE = 20;
	public static  final float DEFAULT_OFFSET_X = 0;
	public static  final float DEFAULT_OFFSET_Y = 0;
	public static  final float DEFAULT_REFRESH_INTERVAL = 25;
	public static  final float MIN_BLOCK_SIZE = 5;
	public static  final float MAX_BLOCK_SIZE = 30;
	
	public static  final float ENTITY_LEVEL_0_SPEED = 0.00025f;
	public static  final float ENTITY_LEVEL_1_SPEED = 0.0005f;
	public static  final float ENTITY_LEVEL_2_SPEED = 0.001f;
	public static  final float ENTITY_LEVEL_3_SPEED = 0.002f;
	public static  final float ENTITY_LEVEL_4_SPEED = 0.003f;
	public static  final float ENTITY_LEVEL_5_SPEED = 0.004f;
	public static  final float ENTITY_LEVEL_6_SPEED = 0.005f;
	public static  final float ENTITY_LEVEL_7_SPEED = 0.006f;
	public static  final float ENTITY_LEVEL_8_SPEED = 0.008f;
	public static  final float ENTITY_LEVEL_9_SPEED = 0.01f;
	public static  final float ENTITY_LEVEL_10_SPEED = 0.012f;
	public static  final int ENTITY_MIN_SPEED_LEVEL = 0;
	public static  final int ENTITY_DEFAULT_SPEED_LEVEL = 5;
	public static  final int ENTITY_MAX_SPEED_LEVEL = 10;
	
	public static  final int DEFAULT_MAP_HEIGHT = 10;
	public static  final int NOT_USED_ROWS = 3;
	public static  final long MESSAGE_DISPLAY_TIME = 3000; //in ms

    private static int scenarioPassingTime = SCENARIO_PASSING_TIME;
    private static String testFileName = TEST_FILE_NAME;
    private static String stringPathToPlayIcon = STRING_PATH_TO_PLAY_ICON;
    private static String stringPathToPauseIcon = STRING_PATH_TO_PAUSE_ICON;
    private static String stringPathToStopIcon = STRING_PATH_TO_STOP_ICON;
    private static String stringPathToResetIcon = STRING_PATH_TO_RESET_ICON;
    
   	private static int defaultWaitingTimeBeforeTest = DEFAULT_WAITING_TIME_BEFORE_TEST;
   	
   	public static int getDefaultWaitingTimeBeforeTest() {
		return defaultWaitingTimeBeforeTest;
	}

	private static float defaultBlockSize = DEFAULT_BLOCK_SIZE;
   	private static float defaultOffsetX = DEFAULT_OFFSET_X;
   	private static float defaultOffsetY = DEFAULT_OFFSET_Y;
   	private static float defaultRefreshInterval = DEFAULT_REFRESH_INTERVAL;
   	private static float minBlockSize = MIN_BLOCK_SIZE;
   	private static float maxBlockSize = MAX_BLOCK_SIZE;
   	
   	private static int entitySpeedLevel = ENTITY_DEFAULT_SPEED_LEVEL;
   	private static int defaultEntitySpeedLevel = ENTITY_DEFAULT_SPEED_LEVEL;
   	
   	private static int defaultMapHeight = DEFAULT_MAP_HEIGHT;
   	private static int mapHeight = DEFAULT_MAP_HEIGHT;
    private static Map<Integer,Float> entitySpeedsMap = null;
	private static int notUsedRows = NOT_USED_ROWS;
	private static long messageDisplayTime = MESSAGE_DISPLAY_TIME;
	
    static {
    	entitySpeedsMap = new HashMap<Integer,Float>();
    	entitySpeedsMap.put(0, ENTITY_LEVEL_0_SPEED);
    	entitySpeedsMap.put(1, ENTITY_LEVEL_1_SPEED);
    	entitySpeedsMap.put(2, ENTITY_LEVEL_2_SPEED);
    	entitySpeedsMap.put(3, ENTITY_LEVEL_3_SPEED);
    	entitySpeedsMap.put(4, ENTITY_LEVEL_4_SPEED);
    	entitySpeedsMap.put(5, ENTITY_LEVEL_5_SPEED);
    	entitySpeedsMap.put(6, ENTITY_LEVEL_6_SPEED);
    	entitySpeedsMap.put(7, ENTITY_LEVEL_7_SPEED);
    	entitySpeedsMap.put(8, ENTITY_LEVEL_8_SPEED);
    	entitySpeedsMap.put(9, ENTITY_LEVEL_9_SPEED);
    	entitySpeedsMap.put(10, ENTITY_LEVEL_10_SPEED);
    }
    
	public static int getDefaultEntitySpeedLevel() {
		return defaultEntitySpeedLevel;
	}
	
	public static float getDefaultEntitySpeed() {
		return entitySpeedsMap.get(getDefaultEntitySpeedLevel());
	}
    
    public static float getEntitySpeed() {
        return entitySpeedsMap.get(getEntitySpeedLevel());
    }
    
    public static int getEntitySpeedLevel() {
    	return entitySpeedLevel;
    }

    public static void setEntitySpeedLevel(int entitySpeedLevel) {
        SimulatorConfig.entitySpeedLevel = entitySpeedLevel;
    }
    
    public static float getEntitySpeedFactor() {
    	return getEntitySpeed() / getDefaultEntitySpeed();
    }

	public static int getScenarioPassingTime() {
		return scenarioPassingTime;
	}
	
	public static String getTestFileName() {
		return testFileName;
	}
	
	public static String getStringPathToPlayIcon() {
		return stringPathToPlayIcon;
	}
	
	public static String getStringPathToPauseIcon() {
		return stringPathToPauseIcon;
	}
	
	public static String getStringPathToStopIcon() {
		return stringPathToStopIcon;
	}

	
	public static String getStringPathToResetIcon() {
		return stringPathToResetIcon;
	}
	
	public static float getDefaultBlockSize() {
		return defaultBlockSize;
	}
	
   	public static float getDefaultOffsetX() {
   		return defaultOffsetX;
   	}
   	
   	public static float getDefaultOffsetY() {
   		return defaultOffsetY;
   	}
   	
   	public static float getDefaultRefreshInterval() {
   		return defaultRefreshInterval;
   	}
   	
   	public static float getMinBlockSize() {
   		return minBlockSize;
   	}
   	
   	public static float getMaxBlockSize() {
   		return maxBlockSize;
   	}
   	
   	public static int getDefaultMapHeight() {
   		return defaultMapHeight;
   	}

	public static int getMapHeight() {
		return mapHeight;
	}
	
	public static void setMapHeight(int mapHeight) {
		SimulatorConfig.mapHeight = mapHeight;
	}

	public static int getNotUsedRows() {
		return notUsedRows;
	}

	public static long getMessageDisplayTime() {
		return messageDisplayTime ;
	}
}
