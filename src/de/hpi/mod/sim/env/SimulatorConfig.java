package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.Position;
/**
 * Contains all "magic-numbers" set in the Simulation.
 * Can be set by the view.
 */
public class SimulatorConfig {

    public static final int CHARGING_STATIONS_IN_USE = 10;
    public static final int SPACE_BETWEEN_CHARGING_STATIONS = 3;
    public static final int MAX_ROBOTS_PER_STATION = 3;
    public static final int SCENARIO_PASSING_TIME = 60; //Not in use at the moment
    public static final Position FIRST_CHARGING_STATION_TOP = new Position(0,-2);
    public static final String TEST_FILE_NAME = "Tests";
    public static final String STRING_PATH_TO_ROBOT_ICON = "simulator/res/robot.png";
    public static final String STRING_PATH_TO_HIGHLIGHT_ROBOT_ICON = "simulator/res/robot-highlight.png";
    public static final String STRING_PATH_TO_EMPTY_BATTERY = "simulator/res/battery_empty.png";
    public static final String STRING_PATH_TO_PLAY_ICON = "simulator/res/play.png";
    public static final String STRING_PATH_TO_PAUSE_ICON = "simulator/res/pause.png";
    public static final float DEFAULT_ROTATION_SPEED = .5f;
	public static final long DEFAULT_UNLOADING_TIME = 1000;
	public static final float BATTERY_FULL = 100;
	public static final float BATTERY_LOW = 20;
	public static final float BATTERY_LOSS = .1f;
	public static final float BATTERY_LOADING_SPEED = .02f;
	public static final int DEFAULT_UNLOADING_RANGE = 30;
	
	public static final float DEFAULT_BLOCK_SIZE = 20;
	public static final float DEFAULT_OFFSET_X = 0;
	public static final float DEFAULT_OFFSET_Y = 0;
	public static final float DEFAULT_SENSOR_REFRESH_INTERVAL = 10;
	public static final float MIN_BLOCK_SIZE = 5;
	public static final float MAX_BLOCK_SIZE = 30;
	
	public static final float ROBOT_LEVEL_1_SPEED = 0.0005f;
	public static final float ROBOT_LEVEL_2_SPEED = 0.001f;
	public static final float ROBOT_LEVEL_3_SPEED = 0.0015f;
	public static final float ROBOT_LEVEL_4_SPEED = 0.0025f;
	public static final float ROBOT_LEVEL_5_SPEED = 0.0035f;
	public static final float ROBOT_LEVEL_6_SPEED = 0.0045f;
	public static final float ROBOT_LEVEL_7_SPEED = 0.006f;
	public static final float ROBOT_LEVEL_8_SPEED = 0.0075f;
	public static final float ROBOT_LEVEL_9_SPEED = 0.009f;
	public static final float ROBOT_LEVEL_10_SPEED = 0.011f;
	public static final int ROBOT_MIN_SPEED_LEVEL = 1;
	public static final int ROBOT_DEFAULT_SPEED_LEVEL = 4;
	public static final int ROBOT_MAX_SPEED_LEVEL = 10;
	public static final float DEFAULT_ROBOT_MOVE_SPEED = ROBOT_LEVEL_4_SPEED;
	
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
	public static final int MAP_HEIGHT = 4;

    private static float robotMoveSpeed = DEFAULT_ROBOT_MOVE_SPEED;
    private static int chargingStationsInUse = CHARGING_STATIONS_IN_USE;
    private static int spaceBetweenChargingStations = SPACE_BETWEEN_CHARGING_STATIONS;
    private static int maxRobotsPerStation = MAX_ROBOTS_PER_STATION;
    private static int scenarioPassingTime = SCENARIO_PASSING_TIME;
    private static Position firstChargingStationTop = FIRST_CHARGING_STATION_TOP;
    private static String testFileName = TEST_FILE_NAME;
    private static String stringPathToRobotIcon = STRING_PATH_TO_ROBOT_ICON;
    private static String stringPathToHighlightRobotIcon = STRING_PATH_TO_HIGHLIGHT_ROBOT_ICON;
    private static String stringPathToEmptyBattery = STRING_PATH_TO_EMPTY_BATTERY;
    private static String stringPathToPlayIcon = STRING_PATH_TO_PLAY_ICON;
    private static String stringPathToPauseIcon = STRING_PATH_TO_PAUSE_ICON;
    private static float defaultRotationSpeed = DEFAULT_ROTATION_SPEED;
   	private static long defaultUnloadingTime = DEFAULT_UNLOADING_TIME;
   	private static float batteryFull = BATTERY_FULL;
   	private static float batteryLow = BATTERY_LOW;
   	private static float batteryLoss = BATTERY_LOSS;
   	private static float batteryLoadingSpeed = BATTERY_LOADING_SPEED;
   	
   	private static int defaultUnloadingRange = DEFAULT_UNLOADING_RANGE;
   	private static float defaultBlockSize = DEFAULT_BLOCK_SIZE;
   	private static float defaultOffsetX = DEFAULT_OFFSET_X;
   	private static float defaultOffsetY = DEFAULT_OFFSET_Y;
   	private static float defaultSensorRefreshInterval = DEFAULT_SENSOR_REFRESH_INTERVAL;
   	private static float minBlockSize = MIN_BLOCK_SIZE;
   	private static float maxBlockSize = MAX_BLOCK_SIZE;
   	
   	private static int robotDefaultSpeedLevel = ROBOT_DEFAULT_SPEED_LEVEL;
   	private static float robotLevel1Speed = ROBOT_LEVEL_1_SPEED;
   	private static float robotLevel2Speed = ROBOT_LEVEL_2_SPEED;
   	private static float robotLevel3Speed = ROBOT_LEVEL_3_SPEED;
   	private static float robotLevel4Speed = ROBOT_LEVEL_4_SPEED;
   	private static float robotLevel5Speed = ROBOT_LEVEL_5_SPEED;
   	private static float robotLevel6Speed = ROBOT_LEVEL_6_SPEED;
   	private static float robotLevel7Speed = ROBOT_LEVEL_7_SPEED;
   	private static float robotLevel8Speed = ROBOT_LEVEL_8_SPEED;
   	private static float robotLevel9Speed = ROBOT_LEVEL_9_SPEED;
   	private static float robotLevel10Speed = ROBOT_LEVEL_10_SPEED;
   	
   	private static int queueSize = QUEUE_SIZE;
   	private static int batteriesPerStation = BATTERIES_PER_STATION;
   	private static int mapHeight = MAP_HEIGHT;
    

    public static float getRobotMoveSpeed() {
        return robotMoveSpeed;
    }

    public static void setRobotMoveSpeed(float robotMoveSpeed) {
        SimulatorConfig.robotMoveSpeed = robotMoveSpeed;
    }
    
    public static int getChargingStationsInUse() {
    	return chargingStationsInUse;
    }
    
    public static int getSpaceBetweenChargingStations() {
    	return spaceBetweenChargingStations;
    }
    
    public static int getMaxRobotsPerStation() {
    	return maxRobotsPerStation;
    }
    
    public static Position getFirstStationTop() {
    	return firstChargingStationTop;
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
	
	public static String getStringPathToHighlightRobotIcon() {
		return stringPathToHighlightRobotIcon;
	}
	
	public static String getStringPathToEmptyBattery() {
		return stringPathToEmptyBattery;
	}
	
	public static String getStringPathToPlayIcon() {
		return stringPathToPlayIcon;
	}
	
	public static String getStringPathToPauseIcon() {
		return stringPathToPauseIcon;
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
	
	public static float getBatteryLoadingSpeed() {
		return batteryLoadingSpeed;
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
   	
   	public static float getDefaultSensorRefreshInterval() {
   		return defaultSensorRefreshInterval;
   	}
   	
   	public static float getMinBlockSize() {
   		return minBlockSize;
   	}
   	
   	public static float getMaxBlockSize() {
   		return maxBlockSize;
   	}
   	
   	public static int getRobotDefaultSpeedLevel() {
		return robotDefaultSpeedLevel;
	}
   	
   	public static float getRobotLevel1Speed() {
   		return robotLevel1Speed;
   	}
   	
   	public static float getRobotLevel2Speed() {
   		return robotLevel2Speed;
   	}
   	
   	public static float getRobotLevel3Speed() {
   		return robotLevel3Speed;
   	}
   	
   	public static float getRobotLevel4Speed() {
   		return robotLevel4Speed;
   	}
   	
   	public static float getRobotLevel5Speed() {
   		return robotLevel5Speed;
   	}
   	
   	public static float getRobotLevel6Speed() {
   		return robotLevel6Speed;
   	}
   	
   	public static float getRobotLevel7Speed() {
   		return robotLevel7Speed;
   	}
   	
   	public static float getRobotLevel8Speed() {
   		return robotLevel8Speed;
   	}
   	
   	public static float getRobotLevel9Speed() {
   		return robotLevel9Speed;
   	}
   	
   	public static float getRobotLevel10Speed() {
   		return robotLevel10Speed;
   	}
   	
   	public static int getQueueSize() {
   		return queueSize;
   	}
   	
   	public static int getBatteriesPerStation() {
   		return batteriesPerStation;
   	}
   	
   	public static int getMapHeight() {
   		return mapHeight;
   	}
}
