package de.hpi.mod.sim.setting.infinitewarehouses;

import java.util.HashMap;
import java.util.Map;

import de.hpi.mod.sim.core.model.Position;
import de.hpi.mod.sim.core.simulation.SimulatorConfig;

/**
 * Contains all "magic-numbers" set in the Infinite-Warehouse-Simulation.
 * Can be set by the view.
 */
public class InfiniteWarehouseSimConfig extends SimulatorConfig {

    public static final int DEFAULT_CHARGING_STATIONS_IN_USE = 10;
    public static final int SPACE_BETWEEN_CHARGING_STATIONS = 3;
	private static final float RECOMMENDED_ROBOTS_PER_STATION = 4;
    public static final int MAX_ROBOTS_PER_STATION = 5;
    public static final String STRING_PATH_TO_ROBOT_ICON = "res/robot.png";
    public static final String STRING_PATH_TO_LEFT_CLICKED_ROBOT_ICON = "res/robot-left-clicked.png";
    public static final String STRING_PATH_TO_RIGHT_CLICKED_ROBOT_ICON = "res/robot-right-clicked.png";
    public static final String STRING_PATH_TO_LEFT_CLICKED_ROBOT_BLOCKING = "res/robot-left-blocking.png";
    public static final String STRING_PATH_TO_RIGHT_CLICKED_ROBOT_BLOCKING = "res/robot-right-blocking.png";
    public static final String STRING_PATH_TO_EMPTY_BATTERY = "res/battery_empty.png";
    public static final String STRING_PATH_TO_PACKAGE = "res/package.png";
    public static final String URL_TO_EXPLOSION = "/explosion.gif"; //the gif needs to be loaded using getResource. For this reason the path looks different to the other paths
    public static final float DEFAULT_ROTATION_SPEED = .5f;
	public static final long DEFAULT_UNLOADING_TIME = 1000;
	public static final float BATTERY_FULL = 100;
	public static final float BATTERY_LOW = 30;
	public static final float BATTERY_LOSS = .03f;
	public static final float BATTERY_LOADING_SPEED = .015f;
	public static final float MIN_BATTERY_RATIO = .5f;
	public static final int DEFAULT_UNLOADING_RANGE = 50;
	public static final int DEFAULT_STATION_UNBLOCKING_TIME = 10000; //in ms
	private static final float MIN_WAITING_TIME_BEFORE_LOADING = 1000;
	private static final float MAX_WAITING_TIME_BEFORE_LOADING = 10000;

	
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
	public static final long MESSAGE_DISPLAY_TIME = 3000; //in ms

    private static int defaultChargingStationsInUse = DEFAULT_CHARGING_STATIONS_IN_USE;
    private static int spaceBetweenChargingStations = SPACE_BETWEEN_CHARGING_STATIONS;
	private static float recommendedRobotsPerStation = RECOMMENDED_ROBOTS_PER_STATION;
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
	private static float minWaitingTimeBeforeLoading = MIN_WAITING_TIME_BEFORE_LOADING;
	private static float maxWaitingTimeBeforeLoading = MAX_WAITING_TIME_BEFORE_LOADING;
   	
   	private static int defaultWaitingTimeBeforeTest = DEFAULT_WAITING_TIME_BEFORE_TEST;
   	
	private static int defaultUnloadingRange = DEFAULT_UNLOADING_RANGE;
   	private static int queueSize = QUEUE_SIZE;
   	private static int batteriesPerStation = BATTERIES_PER_STATION;
   	private static int chargingStationsInUse = DEFAULT_CHARGING_STATIONS_IN_USE;
    private static int unloadingRange = DEFAULT_UNLOADING_RANGE;
    
    
    
    public static int getDefaultChargingStationsInUse() {
    	return defaultChargingStationsInUse;
    }
    
    public static int getSpaceBetweenChargingStations() {
    	return spaceBetweenChargingStations;
    }

	public static float getRecommendedRobotsPerStation() {
		return recommendedRobotsPerStation;
	}
    
    public static int getMaxRobotsPerStation() {
    	return maxRobotsPerStation;
    }
    
    public static Position getFirstStationTop() {
    	return new Position(-chargingStationsInUse/2 * 3, -2);
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

	public static float getMinWaitingTimeBeforeLoading() {
		return minWaitingTimeBeforeLoading;
	}

	public static float getMaxWaitingTimeBeforeLoading() {
		return maxWaitingTimeBeforeLoading;
	}
}
