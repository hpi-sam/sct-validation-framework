package de.hpi.mod.sim.worlds.abstract_robots;

import de.hpi.mod.sim.worlds.abstract_grid.GridConfiguration;

/**
 * Contains all "magic-numbers" set in the Infinite-Warehouse-Simulation.
 * Can be set by the view.
 */
public class RobotConfiguration extends GridConfiguration {

    public static final String STRING_PATH_TO_ROBOT_ICON = "res/robot.png";
    public static final String STRING_PATH_TO_LEFT_CLICKED_ROBOT_ICON = "res/robot-left-clicked.png";
    public static final String STRING_PATH_TO_RIGHT_CLICKED_ROBOT_ICON = "res/robot-right-clicked.png";
    public static final String STRING_PATH_TO_LEFT_CLICKED_ROBOT_BLOCKING = "res/robot-left-blocking.png";
    public static final String STRING_PATH_TO_RIGHT_CLICKED_ROBOT_BLOCKING = "res/robot-right-blocking.png";
    public static final String STRING_PATH_TO_EMPTY_BATTERY = "res/battery_empty.png";
    public static final String STRING_PATH_TO_PACKAGE = "res/package.png";
	public static final float DEFAULT_ROTATION_SPEED = .5f;

	public static final float BATTERY_FULL = 100;
	public static final float BATTERY_LOW = 30;
	public static final float BATTERY_LOSS = .03f;
	public static final float BATTERY_LOADING_SPEED = .015f;
	public static final float MIN_BATTERY_RATIO = .5f;
	
    private static String stringPathToRobotIcon = STRING_PATH_TO_ROBOT_ICON;
    private static String stringPathToLeftClickedRobotIcon = STRING_PATH_TO_LEFT_CLICKED_ROBOT_ICON;
    private static String stringPathToRightClickedRobotIcon = STRING_PATH_TO_RIGHT_CLICKED_ROBOT_ICON;
    private static String stringPathToLeftClickedRobotBlocking = STRING_PATH_TO_LEFT_CLICKED_ROBOT_BLOCKING;
    private static String stringPathToRightClickedRobotBlocking = STRING_PATH_TO_RIGHT_CLICKED_ROBOT_BLOCKING;
    private static String stringPathToEmptyBattery = STRING_PATH_TO_EMPTY_BATTERY;
    private static String stringPathToPackage = STRING_PATH_TO_PACKAGE;
    private static float defaultRotationSpeed = DEFAULT_ROTATION_SPEED;
   	private static float batteryFull = BATTERY_FULL;
   	private static float batteryLow = BATTERY_LOW;
   	private static float batteryLoss = BATTERY_LOSS;
   	private static float batteryChargingSpeed = BATTERY_LOADING_SPEED;
   	private static float minBatteryRatio = MIN_BATTERY_RATIO;
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

}
