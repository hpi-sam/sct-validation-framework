package de.hpi.mod.sim.worlds.infinitewarehouse;

import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.RobotConfiguration;

/**
 * Contains all "magic-numbers" set in the Infinite-Warehouse-Simulation.
 * Can be set by the view.
 */
public class InfiniteWarehouseConfiguration extends RobotConfiguration {

    public static final int DEFAULT_CHARGING_STATIONS_IN_USE = 10;
    public static final int SPACE_BETWEEN_CHARGING_STATIONS = 3;
	private static final float RECOMMENDED_ROBOTS_PER_STATION = 4;
    public static final int MAX_ROBOTS_PER_STATION = 5;
    public static final long DEFAULT_UNLOADING_TIME = 1000;
	public static final int DEFAULT_UNLOADING_RANGE = 50;
	public static final int DEFAULT_STATION_UNBLOCKING_TIME = 10000; //in ms
	private static final float MIN_WAITING_TIME_BEFORE_LOADING = 1000;
	private static final float MAX_WAITING_TIME_BEFORE_LOADING = 10000;

	
	/**
     * The number of cells in a queue.
     * Changes to this value require changes in the implementation of the map too.
     */
	public static final int QUEUE_SIZE = 5;

	// TEMPORARILY MOVED TO WORLD INITIALIZATION
//	static {
//		setOriginOffsetY(QUEUE_SIZE);
//	}


	/**
     * The number of chargers in a station.
     * Changes to this value require changes in the implementation of the map too.
     */
	public static final int CHARGERS_PER_STATION = 3;
	/**
     * Number of vertical unloading positions.
     */
	public static final int DEFAULT_MAP_HEIGHT = 10;
	public static final int NOT_USED_ROWS = 3;

    private static int defaultChargingStationsInUse = DEFAULT_CHARGING_STATIONS_IN_USE;
    private static int spaceBetweenChargingStations = SPACE_BETWEEN_CHARGING_STATIONS;
	private static float recommendedRobotsPerStation = RECOMMENDED_ROBOTS_PER_STATION;
    private static int maxRobotsPerStation = MAX_ROBOTS_PER_STATION;
    private static String stringPathToRobotIcon = STRING_PATH_TO_ROBOT_ICON;
    private static String stringPathToLeftClickedRobotIcon = STRING_PATH_TO_LEFT_CLICKED_ROBOT_ICON;
    private static String stringPathToRightClickedRobotIcon = STRING_PATH_TO_RIGHT_CLICKED_ROBOT_ICON;
    private static String stringPathToLeftClickedRobotBlocking = STRING_PATH_TO_LEFT_CLICKED_AREA_HIGHLIGHT;
    private static String stringPathToRightClickedRobotBlocking = STRING_PATH_TO_RIGHT_CLICKED_AREA_HIGHLIGHT;
    private static String stringPathToEmptyBattery = STRING_PATH_TO_EMPTY_BATTERY;
    private static String stringPathToPackage = STRING_PATH_TO_PACKAGE;
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
   	
  	private static int defaultUnloadingRange = DEFAULT_UNLOADING_RANGE;
   	private static int queueSize = QUEUE_SIZE;
   	private static int chargersPerStation = CHARGERS_PER_STATION;
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
		return new Position(-chargingStationsInUse / 2 * 3, -2);
	}

	public static String getStringPathToRobotIcon() {
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

	public static float getMinBatteryRatio() {
		return minBatteryRatio;
	}

	public static float getBatteryChargingSpeed() {
		return batteryChargingSpeed;
	}

	public static int getDefaultUnloadingRange() {
		return defaultUnloadingRange;
	}

	public static int getQueueSize() {
		return queueSize;
	}

	public static int getChargersPerStation() {
		return chargersPerStation;
	}

	public static int getDefaultStationUnblockingTime() {
		return defaultStationUnblockingTime;
	}

	public static int getUnloadingRange() {
		return unloadingRange;
	}

	public static void setUnloadingRange(int unloadingRange) {
		InfiniteWarehouseConfiguration.unloadingRange = unloadingRange;
	}

	public static int getChargingStationsInUse() {
		return chargingStationsInUse;
	}

	public static void setChargingStationsInUse(int chargingStationsInUse) {
		InfiniteWarehouseConfiguration.chargingStationsInUse = chargingStationsInUse;
	}

	public static float getMinWaitingTimeBeforeLoading() {
		return minWaitingTimeBeforeLoading;
	}

	public static float getMaxWaitingTimeBeforeLoading() {
		return maxWaitingTimeBeforeLoading;
	}
}
