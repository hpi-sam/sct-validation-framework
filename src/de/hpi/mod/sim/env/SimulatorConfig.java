package de.hpi.mod.sim.env;

import de.hpi.mod.sim.env.model.Position;
/**
 * TODO: Configuration data should be removed from Simulator and added here
 * TODO: Should be an Instance and not static
 *
 * Contains all "magic-numbers" set in the Simulation.
 * Can be set by the view.
 */
public class SimulatorConfig {

    public static final float DEFAULT_ROBOT_MOVE_SPEED = .005f;
    public static final int CHARGING_STATIONS_IN_USE = 10;
    public static final int SPACE_BETWEEN_CHARGING_STATIONS = 3;
    public static final int MAX_ROBOTS_PER_STATION = 3;
    public static final int SCENARIO_PASSING_TIME = 60;
    public static final Position FIRST_CHARGING_STATION_TOP = new Position(0,-2);
    public static final String TEST_FILE_NAME = "Tests";

    private static float robotMoveSpeed = DEFAULT_ROBOT_MOVE_SPEED;
    private static int chargingStationsInUse = CHARGING_STATIONS_IN_USE;
    private static int spaceBetweenChargingStations = SPACE_BETWEEN_CHARGING_STATIONS;
    private static int maxRobotsPerStation = MAX_ROBOTS_PER_STATION;
    private static int scenarioPassingTime = SCENARIO_PASSING_TIME;
    private static Position firstChargingStationTop = FIRST_CHARGING_STATION_TOP;
    private static String testFileName = TEST_FILE_NAME;
    

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
}
