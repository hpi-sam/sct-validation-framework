package de.hpi.mod.sim.env;

import java.util.HashMap;
import java.util.Map;

import de.hpi.mod.sim.env.model.Position;
/**
 * Contains all "magic-numbers" set in the Simulation.
 * Can be set by the view.
 */
public class SimulatorConfig {

    public static final int DEFAULT_CHARGING_STATIONS_IN_USE = 10;
    public static final int SPACE_BETWEEN_CHARGING_STATIONS = 3;
    public static final int MAX_ROBOTS_PER_STATION = 3;
    public static final int SCENARIO_PASSING_TIME = 60; //Not in use at the moment
    public static final String TEST_FILE_NAME = ".tests";
    public static final String STRING_PATH_TO_ROBOT_ICON = "res/robot.png";
    public static final String STRING_PATH_TO_LEFT_CLICKED_ROBOT_ICON = "res/robot-left-clicked.png";
    public static final String STRING_PATH_TO_RIGHT_CLICKED_ROBOT_ICON = "res/robot-right-clicked.png";
    public static final String STRING_PATH_TO_LEFT_CLICKED_ROBOT_BLOCKING = "res/robot-left-blocking.png";
    public static final String STRING_PATH_TO_RIGHT_CLICKED_ROBOT_BLOCKING = "res/robot-right-blocking.png";
    public static final String STRING_PATH_TO_EMPTY_BATTERY = "res/battery_empty.png";
    public static final String STRING_PATH_TO_PACKAGE = "res/package.png";
    public static final String STRING_PATH_TO_PLAY_ICON = "res/play.png";
    public static final String STRING_PATH_TO_PAUSE_ICON = "res/pause.png";
    public static final String STRING_PATH_TO_STOP_ICON = "res/stop.png";
    public static final String STRING_PATH_TO_RESET_ICON = "res/reset.png";
    public static final String URL_TO_EXPLOSION = "/explosion.gif"; //the gif needs to be loaded using getResource. For this reason the path looks different to the other paths
    public static final float DEFAULT_ROTATION_SPEED = .5f;
	public static final long DEFAULT_UNLOADING_TIME = 1000;
	public static final float BATTERY_FULL = 100;
	public static final float BATTERY_LOW = 40;
	public static final float BATTERY_LOSS = .01f;
	public static final float BATTERY_LOADING_SPEED = .015f;
	public static final float MIN_BATTERY_RATIO = .5f;
	public static final int DEFAULT_UNLOADING_RANGE = 50;
	public static final int DEFAULT_STATION_UNBLOCKING_TIME = 10000; //in ms
	
	public static final float DEFAULT_BLOCK_SIZE = 20;
	public static final float DEFAULT_OFFSET_X = 0;
	public static final float DEFAULT_OFFSET_Y = 0;
	public static final float DEFAULT_REFRESH_INTERVAL = 25;
	public static final float MIN_BLOCK_SIZE = 5;
	public static final float MAX_BLOCK_SIZE = 30;
	
	public static final float ROBOT_LEVEL_0_SPEED = 0.00025f;
	public static final float ROBOT_LEVEL_1_SPEED = 0.0005f;
	public static final float ROBOT_LEVEL_2_SPEED = 0.001f;
	public static final float ROBOT_LEVEL_3_SPEED = 0.002f;
	public static final float ROBOT_LEVEL_4_SPEED = 0.003f;
	public static final float ROBOT_LEVEL_5_SPEED = 0.004f;
	public static final float ROBOT_LEVEL_6_SPEED = 0.005f;
	public static final float ROBOT_LEVEL_7_SPEED = 0.006f;
	public static final float ROBOT_LEVEL_8_SPEED = 0.008f;
	public static final float ROBOT_LEVEL_9_SPEED = 0.01f;
	public static final float ROBOT_LEVEL_10_SPEED = 0.012f;
	public static final int ROBOT_MIN_SPEED_LEVEL = 0;
	public static final int ROBOT_DEFAULT_SPEED_LEVEL = 5;
	public static final int ROBOT_MAX_SPEED_LEVEL = 10;
	
	/**
     * The number of cells in a queue.
     * Changes to this value require changes in the implementation of the map too.
     */
	public static final int QUEUE_SIZE = 5;
	/**
     * The number of batteries in a station.
     * Changes to this value require changes in the implementation of the map too.
     */
	public static final int BATTERIES_PER_STATION = 3;
	/**
     * Number of vertical unloading positions.
     */
	public static final int DEFAULT_MAP_HEIGHT = 10;
	public static final int NOT_USED_ROWS = 3;
	public static final long MESSAGE_DISPLAY_TIME = 1000; //in ms

    private static int defaultChargingStationsInUse = DEFAULT_CHARGING_STATIONS_IN_USE;
    private static int spaceBetweenChargingStations = SPACE_BETWEEN_CHARGING_STATIONS;
    private static int maxRobotsPerStation = MAX_ROBOTS_PER_STATION;
    private static int scenarioPassingTime = SCENARIO_PASSING_TIME;
    private static String testFileName = TEST_FILE_NAME;
    private static String stringPathToRobotIcon = STRING_PATH_TO_ROBOT_ICON;
    private static String stringPathToLeftClickedRobotIcon = STRING_PATH_TO_LEFT_CLICKED_ROBOT_ICON;
    private static String stringPathToRightClickedRobotIcon = STRING_PATH_TO_RIGHT_CLICKED_ROBOT_ICON;
    private static String stringPathToLeftClickedRobotBlocking = STRING_PATH_TO_LEFT_CLICKED_ROBOT_BLOCKING;
    private static String stringPathToRightClickedRobotBlocking = STRING_PATH_TO_RIGHT_CLICKED_ROBOT_BLOCKING;
    private static String stringPathToEmptyBattery = STRING_PATH_TO_EMPTY_BATTERY;
    private static String stringPathToPackage = STRING_PATH_TO_PACKAGE;
    private static String stringPathToPlayIcon = STRING_PATH_TO_PLAY_ICON;
    private static String stringPathToPauseIcon = STRING_PATH_TO_PAUSE_ICON;
    private static String stringPathToStopIcon = STRING_PATH_TO_STOP_ICON;
    private static String stringPathToResetIcon = STRING_PATH_TO_RESET_ICON;
    private static String urlToExplosion = URL_TO_EXPLOSION;
    private static float defaultRotationSpeed = DEFAULT_ROTATION_SPEED;
   	private static long defaultUnloadingTime = DEFAULT_UNLOADING_TIME;
   	private static float batteryFull = BATTERY_FULL;
   	private static float batteryLow = BATTERY_LOW;
   	private static float batteryLoss = BATTERY_LOSS;
   	private static float batteryChargingSpeed = BATTERY_LOADING_SPEED;
   	private static float minBatteryRatio = MIN_BATTERY_RATIO;
   	private static int defaultStationUnblockingTime = DEFAULT_STATION_UNBLOCKING_TIME;
   	
   	private static int defaultUnloadingRange = DEFAULT_UNLOADING_RANGE;
   	private static float defaultBlockSize = DEFAULT_BLOCK_SIZE;
   	private static float defaultOffsetX = DEFAULT_OFFSET_X;
   	private static float defaultOffsetY = DEFAULT_OFFSET_Y;
   	private static float defaultRefreshInterval = DEFAULT_REFRESH_INTERVAL;
   	private static float minBlockSize = MIN_BLOCK_SIZE;
   	private static float maxBlockSize = MAX_BLOCK_SIZE;
   	
   	private static int robotSpeedLevel = ROBOT_DEFAULT_SPEED_LEVEL;
   	private static int defaultRobotSpeedLevel = ROBOT_DEFAULT_SPEED_LEVEL;
   	
   	private static int queueSize = QUEUE_SIZE;
   	private static int batteriesPerStation = BATTERIES_PER_STATION;
   	private static int defaultMapHeight = DEFAULT_MAP_HEIGHT;
   	private static int chargingStationsInUse = DEFAULT_CHARGING_STATIONS_IN_USE;
    private static int mapHeight = DEFAULT_MAP_HEIGHT;
    private static int unloadingRange = DEFAULT_UNLOADING_RANGE;
    private static Map<Integer,Float> robotSpeedsMap = null;
	private static int notUsedRows = NOT_USED_ROWS;
	private static long messageDisplayTime = MESSAGE_DISPLAY_TIME;
	
    static {
    	robotSpeedsMap = new HashMap<Integer,Float>();
    	robotSpeedsMap.put(0, ROBOT_LEVEL_0_SPEED);
    	robotSpeedsMap.put(1, ROBOT_LEVEL_1_SPEED);
    	robotSpeedsMap.put(2, ROBOT_LEVEL_2_SPEED);
    	robotSpeedsMap.put(3, ROBOT_LEVEL_3_SPEED);
    	robotSpeedsMap.put(4, ROBOT_LEVEL_4_SPEED);
    	robotSpeedsMap.put(5, ROBOT_LEVEL_5_SPEED);
    	robotSpeedsMap.put(6, ROBOT_LEVEL_6_SPEED);
    	robotSpeedsMap.put(7, ROBOT_LEVEL_7_SPEED);
    	robotSpeedsMap.put(8, ROBOT_LEVEL_8_SPEED);
    	robotSpeedsMap.put(9, ROBOT_LEVEL_9_SPEED);
    	robotSpeedsMap.put(10, ROBOT_LEVEL_10_SPEED);
    }
    
	public static int getDefaultRobotSpeedLevel() {
		return defaultRobotSpeedLevel;
	}
	
	public static float getDefaultRobotSpeed() {
		return robotSpeedsMap.get(getDefaultRobotSpeedLevel());
	}
    
    public static float getRobotSpeed() {
        return robotSpeedsMap.get(getRobotSpeedLevel());
    }
    
    public static int getRobotSpeedLevel() {
    	return robotSpeedLevel;
    }

    public static void setRobotSpeedLevel(int robotSpeedLevel) {
        SimulatorConfig.robotSpeedLevel = robotSpeedLevel;
    }
    
    public static float getRobotSpeedFactor() {
    	return getRobotSpeed() / getDefaultRobotSpeed();
    }
    
    public static int getDefaultChargingStationsInUse() {
    	return defaultChargingStationsInUse;
    }
    
    public static int getSpaceBetweenChargingStations() {
    	return spaceBetweenChargingStations;
    }
    
    public static int getMaxRobotsPerStation() {
    	return maxRobotsPerStation;
    }
    
    public static Position getFirstStationTop() {
    	return new Position(-chargingStationsInUse/2 * 3, -2);
    }

	public static int getScenarioPassingTime() {
		return scenarioPassingTime;
	}
	
	public static String getTestFileName() {
		return testFileName;
	}
	
	public static String getStringPathToRobotIcon(){
		return stringPathToRobotIcon;
	}
	
	public static String getStringPathToLeftClickedRobotIcon() {
		return stringPathToLeftClickedRobotIcon;
	}
	
	public static String getStringPathToRightClickedRobotIcon() {
		return stringPathToRightClickedRobotIcon;
	}
	
	public static String getStringPathToLeftClickedRobotBlocking() {
		return stringPathToLeftClickedRobotBlocking;
	}
	
	public static String getStringPathToRightClickedRobotBlocking() {
		return stringPathToRightClickedRobotBlocking;
	}
	
	public static String getStringPathToEmptyBattery() {
		return stringPathToEmptyBattery;
	}
	
	public static String getStringPathToPackage() {
		return stringPathToPackage;
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
	public static String getURLToExplosion() {
		return urlToExplosion;
	}
	
	public static float getDefaultRotationSpeed() {
		return defaultRotationSpeed;
	}
	
	public static long getDefaultUnloadingTime() {
		return defaultUnloadingTime;
	}
	
	public static float getBatteryFull() {
		return batteryFull;
	}
	
	public static float getBatteryLow() {
		return batteryLow;
	}
	
	public static float getBatteryLoss() {
		return batteryLoss;
	}
	
	public static float getMinBatteryRatio (){
		return minBatteryRatio;
	}
	
	public static float getBatteryChargingSpeed() {
		return batteryChargingSpeed;
	}
	
	public static int getDefaultUnloadingRange() {
		return defaultUnloadingRange;
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
   	
   	public static int getQueueSize() {
   		return queueSize;
   	}
   	
   	public static int getBatteriesPerStation() {
   		return batteriesPerStation;
   	}
   	
   	public static int getDefaultMapHeight() {
   		return defaultMapHeight;
   	}

	public static int getMapHeight() {
		return mapHeight;
	}
	
	public static int getDefaultStationUnblockingTime() {
		return defaultStationUnblockingTime;
	}

	public static void setMapHeight(int mapHeight) {
		SimulatorConfig.mapHeight = mapHeight;
	}

	public static int getUnloadingRange() {
		return unloadingRange;
	}

	public static void setUnloadingRange(int unloadingRange) {
		SimulatorConfig.unloadingRange = unloadingRange;
	}

	public static int getChargingStationsInUse() {
		return chargingStationsInUse;
	}

	public static void setChargingStationsInUse(int chargingStationsInUse) {
		SimulatorConfig.chargingStationsInUse = chargingStationsInUse;
	}

	public static int getNotUsedRows() {
		return notUsedRows;
	}

	public static long getMessageDisplayTime() {
		return messageDisplayTime ;
	}
}
