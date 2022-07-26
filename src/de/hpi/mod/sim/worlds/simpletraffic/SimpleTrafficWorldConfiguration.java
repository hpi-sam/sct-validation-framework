package de.hpi.mod.sim.worlds.simpletraffic;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_robots.RobotConfiguration;

/**
 * Contains all "magic-numbers" set in the Robots-and-Traffic-Lights-Simulation.
 * Can be set by the view.
 */
public class SimpleTrafficWorldConfiguration extends RobotConfiguration {

	// Types
    public static enum GridMode {
        SINGLE_CROSSROAD, TWO_CROSSROADS, MAXIMUM_CROSSROADS
    }
    
	// Immutable Field Properties (changing would require a complete logic change)
	private static final int CROSSROAD_LENGTH = 2;
	private static final int MINIMUM_NUMBER_OF_STREETS_IN_MAXIMUM_MODE = 2;
	
	// Defaults for (potentially chanable) Field Properties 
	private static final int DEFAULT_FIELD_BORDER_WIDTH = 2;	
    private static final int DEFAULT_FIELD_SIDE_SPACING = 2;
	private static final int DEFAULT_STREET_LENGTH = 5;
	private static final GridMode DEFAULT_CROSSROAD_MODE = GridMode.MAXIMUM_CROSSROADS;
	private static final boolean DEFAUT_SHOW_STATISTICS = false;
 	
	// Defaults for Idle Robot Properties
	private static final Position DEFAULT_IDLE_ROBOTS_POSITION = new Position(-1000,-1000);
	private static final Orientation DEFAULT_IDLE_ROBOTS_ORIENTATION = Orientation.NORTH;
	
	// Defaults for Properties for Departure Point behaviour 
	private static final int DEFAULT_DEPARTURE_POINT_AVERAGE_WAITING_TIME = 1000;
	private static final int DEFAULT_DEPARTURE_POINT_MINIMUM_WAITING_TIME = 500;
	private static final int DEFAULT_DEPARTURE_POINT_MAXIMUM_WAITING_TIME = 3000;
	
	// Defaults for Properties for worobt pathfinding
	private static final int DEFAULT_TARGET_DIRECTION_OFFSET = 3;
	
	// Defaults for Image Files for Robot Indicator lights
	private static final boolean DEFAULT_SHOW_INDICATOR = true;
    public static final String DEFAULT_STRING_PATH_TO_LEFT_INDICATOR_IMAGE = "res/robot-indicator-left.png"; 
    public static final String DEFAULT_STRING_PATH_TO_RIGHT_INDICATOR_IMAGE = "res/robot-indicator-right.png";    
	private static final int DEFAULT_INDICATPR_FLASH_INTERVALL = 200;

	
	// Field properties that are dependant on settings.
	private static int fieldBorderWidth = DEFAULT_FIELD_BORDER_WIDTH;
	private static int streetLength = DEFAULT_STREET_LENGTH;
	private static int fieldSideSpacing = DEFAULT_FIELD_SIDE_SPACING;
	private static int minimalNumberOfStreetsInMaximumMode = MINIMUM_NUMBER_OF_STREETS_IN_MAXIMUM_MODE;
	
	// Field size properties that change depenting on avialbe space and scenario settings.
    private static int availableFieldWidth = 0;
	private static int availableFieldHeight = 0;
    private static int fieldWidth = 0;
	private static int fieldHeight = 0;
	private static int verticalStreets = 0;
	private static int horizontalStreets = 0;
	private static GridMode crossroadsMode = DEFAULT_CROSSROAD_MODE;
	private static boolean showStatistics = DEFAUT_SHOW_STATISTICS;
	
	// Idle Robot Properties
	private static Position idleRobotsPosition = DEFAULT_IDLE_ROBOTS_POSITION;	
	private static Orientation idleRobotsOrientation = DEFAULT_IDLE_ROBOTS_ORIENTATION;
	
	// Properties for Departure Point behaviour 
	private static int departurePointNormalWaitingTime = DEFAULT_DEPARTURE_POINT_AVERAGE_WAITING_TIME;
	private static int departurePointMinimalWaitingTime = DEFAULT_DEPARTURE_POINT_MINIMUM_WAITING_TIME;
	private static int departurePointMaximalWaitingTime = DEFAULT_DEPARTURE_POINT_MAXIMUM_WAITING_TIME;
	
	// Properties for rorobt pathfinding
	private static int targetDirectionOffset = DEFAULT_TARGET_DIRECTION_OFFSET;

	// Image Files for Robot Indicator lights
    private static boolean showIndicator = DEFAULT_SHOW_INDICATOR;
    private static String stringPathToLeftIndicatorImage = DEFAULT_STRING_PATH_TO_LEFT_INDICATOR_IMAGE;
	private static String stringPathToRightIndicatorImage = DEFAULT_STRING_PATH_TO_RIGHT_INDICATOR_IMAGE;
	private static int indicatorFlashInterval = DEFAULT_INDICATPR_FLASH_INTERVALL;
    
	// Getter Methods
	public static Position getIdleRobotsPosition() {
		return idleRobotsPosition;
	}

   	public static Orientation getIdleRobotsOrientation() {
		return idleRobotsOrientation;
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
		return CROSSROAD_LENGTH;
	}

	public static int getDeparturePointMaximalWaitingTime() {
		return departurePointMaximalWaitingTime;
	}

	public static int getDeparturePointMinimalWaitingTime() {
		return departurePointMinimalWaitingTime;
	}

	public static int getDeparturePointNormalWaitingTime() {
		return departurePointNormalWaitingTime;
	}

	// Setter Methods for properties that may change depending on user or scenario settings.
	
	public static void setCrossroadsMode(GridMode mode) {
		crossroadsMode = mode;
		computeFieldSize();
	}
	public static void setAvailableFieldDimensions(int width, int height) {
		availableFieldWidth = width - (fieldSideSpacing * 2);
		availableFieldHeight = height - (fieldSideSpacing * 2);
		computeFieldSize();
	}

	// Computation Method to adapt settings
	
	private static void computeFieldSize() {
		// Do necessary calculations
		int twoBordersAndOneStreet = (2*fieldBorderWidth) + streetLength;
		int oneStreetAndOneBorder = CROSSROAD_LENGTH + streetLength;
		
		switch(crossroadsMode) {
		case SINGLE_CROSSROAD:
			verticalStreets = horizontalStreets = 1;
			break;
		case TWO_CROSSROADS:
			verticalStreets = horizontalStreets = 2;
			break;
		case MAXIMUM_CROSSROADS:
			verticalStreets = Math.max(minimalNumberOfStreetsInMaximumMode, // Minimum number of crossroads
					(int) ((availableFieldWidth-twoBordersAndOneStreet) / oneStreetAndOneBorder) // OR as many as fit in available space
			);
			horizontalStreets = Math.max(minimalNumberOfStreetsInMaximumMode, // Minimum number of crossroads
					(int) ((availableFieldHeight-twoBordersAndOneStreet) / oneStreetAndOneBorder) // OR as many as fit in available space
			);
			break;
		}
		fieldWidth = twoBordersAndOneStreet + (verticalStreets * oneStreetAndOneBorder);
		fieldHeight = twoBordersAndOneStreet + (horizontalStreets * oneStreetAndOneBorder);
		
		// Set Offset... 
		
		// ...in X direction: 
			// Default: View will autoamtically put 0 in the center. 
			// Problem: However, We have 0 at the left side of the field instead of at the center.
			// Solution: We need to use the offset to shift the field by 1/2 fieldWidth to the left,  so that the field is centered again.
		setOriginOffsetX(-(fieldWidth/2)-1);
		
		// ...in Y direction: 
			// Default: View will autoamtically put 0 directoly on the bottom. 
			// Problem: However, We want to have equal spacing on bottom and top side.
			// Solution: We need to calculate te distace between window and field height and make the offset 1/2 of that to truely center the field.
		setOriginOffsetY( (availableFieldHeight+(fieldSideSpacing * 2)-fieldHeight)/2 );
		
	}

	public static int getTargetDirectionOffset() {
		return targetDirectionOffset;
	}
	
    public static String getStringPathToLeftIndicatorImage() {
		return stringPathToLeftIndicatorImage;
	}

	public static String getStringPathToRightIndicatorImage() {
		return stringPathToRightIndicatorImage;
	}
	
    public static boolean showIndicator() {
		return showIndicator;
	}

	public static int getIndicatorFlashInterval() {
		return indicatorFlashInterval ;
	}

    public static boolean showStatistics() {
		return showStatistics;
	} 
    
	public static void setShowStatistics(boolean statistics) {
		showStatistics = statistics;
	}
	
}
