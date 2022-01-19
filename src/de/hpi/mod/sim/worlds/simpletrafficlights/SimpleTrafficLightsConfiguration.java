package de.hpi.mod.sim.worlds.simpletrafficlights;

import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.RobotConfiguration;

/**
 * Contains all "magic-numbers" set in the Robots-and-Traffic-Lights-Simulation.
 * Can be set by the view.
 */
public class SimpleTrafficLightsConfiguration extends RobotConfiguration {
 	
	private static final Position DEFAULT_IDLE_ROBOTS_POSITION = new Position(-1000,-1000);
	
    public static enum GridMode {
        SINGLE_CROSSROAD, TWO_CROSSROADS, MAXIMUM_CROSSROADS
    }

    private static final int FIELD_SIDE_SPACING = 2;
	private static final int STREET_LENGTH = 5;
	private static final int CROSSROAD_LENGTH = 2;
	private static final int FIELD_BORDER_WIDTH = 2;
	private static final int MINIMUM_CROSSROADS_IN_MAXIMUM_MODE = 2;
	private static final GridMode DEFAULT_CROSSROAD_MODE = GridMode.MAXIMUM_CROSSROADS;
	
	private static final int DEFAULT_FIELD_WIDTH = 30;
    private static final int DEFAULT_FIELD_HEIGHT = 30;
        
	private static int fieldBorderWidth = FIELD_BORDER_WIDTH;
    private static int streetLength = STREET_LENGTH;
    private static int crossroadLength = CROSSROAD_LENGTH;

    private static int availableFieldWidth = DEFAULT_FIELD_WIDTH;
	private static int availableFieldHeight = DEFAULT_FIELD_HEIGHT;

    private static int fieldWidth = 0;
	private static int fieldHeight = 0;

	private static int verticalStreets = 0;
	private static int horizontalStreets = 0;
	
	private static GridMode crossroadsMode = DEFAULT_CROSSROAD_MODE;

	private static Position idleRobotsPosition = DEFAULT_IDLE_ROBOTS_POSITION;
	
   	public static Position getIdleRobotsPosition() {
		return idleRobotsPosition;
	}
   	
	public static int getFieldWidth() {
		return fieldWidth;
	}

	public static int getFieldHeight() {
		return fieldHeight;
	}

	public static GridMode getCrossroadsMode() {
		return crossroadsMode;
	}

	public static void setAvailableFieldDimensions(int width, int height) {
		availableFieldWidth = width - (FIELD_SIDE_SPACING * 2);
		availableFieldHeight = height - (FIELD_SIDE_SPACING * 2);
		computeFieldSize();
	}

	public static void setCrossroadsMode(GridMode mode) {
		crossroadsMode = mode;
		computeFieldSize();
	}
	
	public static int getVerticalStreets() {
		return verticalStreets;
	}

	public static int getHorizontalStreets() {
		return horizontalStreets;
	}


	public static int getNumberOfTransferPoints() {
		return (verticalStreets + horizontalStreets)*2;
	}

	public static int getNumberOfCrossroads() {
		return verticalStreets * horizontalStreets;
	}

	public static int getFieldBorderWidth() {
		return fieldBorderWidth;
	}

	public static int getStreetLength() {
		return streetLength;
	}

	public static int getCrossroadLength() {
		return crossroadLength;
	}


	private static void computeFieldSize() {
		// Do necessary calculations
		int twoBordersAndOneStreet = (2*FIELD_BORDER_WIDTH) + STREET_LENGTH;
		int oneStreetAndOneBorder = CROSSROAD_LENGTH + STREET_LENGTH;
		
		switch(crossroadsMode) {
		case SINGLE_CROSSROAD:
			verticalStreets = horizontalStreets = 1;
			break;
		case TWO_CROSSROADS:
			verticalStreets = horizontalStreets = 2;
			break;
		case MAXIMUM_CROSSROADS:
			verticalStreets = Math.max(MINIMUM_CROSSROADS_IN_MAXIMUM_MODE, // Minimum number of crossroads
					(int) ((availableFieldWidth-twoBordersAndOneStreet) / oneStreetAndOneBorder) // OR as many as fit in available space
			);
			horizontalStreets = Math.max(MINIMUM_CROSSROADS_IN_MAXIMUM_MODE, // Minimum number of crossroads
					(int) ((availableFieldHeight-twoBordersAndOneStreet) / oneStreetAndOneBorder) // OR as many as fit in available space
			);
			break;
		}
		fieldWidth = twoBordersAndOneStreet + (verticalStreets * oneStreetAndOneBorder);
		fieldHeight = twoBordersAndOneStreet + (horizontalStreets * oneStreetAndOneBorder);
	}
}
