package de.hpi.mod.sim.worlds.flasher.config;

public class FlasherConfiguration {

	
    private static final String PATH_TO_BULB_ON_IMAGE = "res/bulb_on.png";
    private static final String PATH_TO_BULB_OFF_IMAGE = "res/bulb_off.png";
	
    private static final double LIGHT_BULB_IMAGE_MAXIMUM_RELATIVE_WIDTH = 0.5;
    private static final double LIGHT_BULB_IMAGE_MAXIMUM_RELATIVE_HEIGHT = 0.8;

    private static final double TASK_DISPLAY_HEIGHT = 100;
    private static final double TASK_DISPLAY_PADDING = 10;
    private static final double TASK_DISPLAY_SPACING = 50;
    
    private static final double WAITING_TIME_BEFORE_TASK = 2000.0;
    
    public static String getStringPathBulbOff() {
    	return PATH_TO_BULB_OFF_IMAGE;
    }
    
    public static String getStringPathBulbOn() {
    	return PATH_TO_BULB_ON_IMAGE;
    }

	public static double getLightBulbImageMaximumRelativeWidth() {
		return LIGHT_BULB_IMAGE_MAXIMUM_RELATIVE_WIDTH;
	}

	public static double getLightBulbImageMaximumRelativeHeight() {
		return LIGHT_BULB_IMAGE_MAXIMUM_RELATIVE_HEIGHT;
	}

	public static double getWaitingTimeBeforeTask() {
		return WAITING_TIME_BEFORE_TASK;
	}

	public static double getTaskDisplayHeight() {
		return TASK_DISPLAY_HEIGHT;
	}

	public static double getTaskDisplayPadding() {
		return TASK_DISPLAY_PADDING;
	}

	public static double getTaskDisplaySpacing() {
		return TASK_DISPLAY_SPACING;
	}    

	public static double getTaskDisplayReservedHeight() {
		return TASK_DISPLAY_HEIGHT + (2 * TASK_DISPLAY_SPACING);
	}
    
}
