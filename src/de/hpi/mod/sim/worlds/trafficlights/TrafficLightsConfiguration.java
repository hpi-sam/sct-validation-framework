package de.hpi.mod.sim.worlds.trafficlights;

import de.hpi.mod.sim.worlds.abstract_robots.RobotConfiguration;

/**
 * Contains all "magic-numbers" set in the Infinite-Warehouse-Simulation.
 * Can be set by the view.
 */
public class TrafficLightsConfiguration extends RobotConfiguration {

    public static final int DEFAULT_FIELD_WIDTH = 28;
	public static final int DEFAULT_FIELD_HEIGHT = 28;

    private static int fieldWidth = DEFAULT_FIELD_WIDTH;
	private static int fieldHeight = DEFAULT_FIELD_HEIGHT;

	// TEMPORARILY MOVED TO WORLD INITIALIZATION
//	static {
//		setOriginOffsetX(-TrafficLightsConfiguration.getFieldWidth() / 2 - 1);
//		setOriginOffsetY(2);
//	}

	public static int getDefaultFieldWidth() {
		return DEFAULT_FIELD_WIDTH;
	}

	public static int getDefaultFieldHeight() {
		return DEFAULT_FIELD_HEIGHT;
	}

	public static int getFieldWidth() {
		return fieldWidth;
	}

	public static int getFieldHeight() {
		return fieldHeight;
	}

	public static void setFieldDimensions(int width, int height) {
		TrafficLightsConfiguration.fieldWidth = width;
		TrafficLightsConfiguration.fieldHeight = height;
	}
}
