package de.hpi.mod.sim.worlds.flasher.config;

public class FlasherConfiguration {

	
    private static final String PATH_TO_BULB_ON_IMAGE = "res/bulb_on.png";
    private static final String PATH_TO_BULB_OFF_IMAGE = "res/bulb_off.png";
	
    private static final double LIGHT_BULB_IMAGE_MAXIMUM_WIDTH_RATE = 0.5;
    private static final double LIGHT_BULB_IMAGE_MAXIMUM_HEIGHT_RATE = 0.7;
    
    private static final double WAITING_TIME_BEFORE_TASK = 2000.0;
    
    public static String getStringPathBulbOff() {
    	return PATH_TO_BULB_OFF_IMAGE;
    }
    
    public static String getStringPathBulbOn() {
    	return PATH_TO_BULB_ON_IMAGE;
    }

	public static double getLightBulbImageMaximumWidthRate() {
		return LIGHT_BULB_IMAGE_MAXIMUM_WIDTH_RATE;
	}

	public static double getLightBulbImageMaximumHeightRate() {
		return LIGHT_BULB_IMAGE_MAXIMUM_HEIGHT_RATE;
	}

	public static double getWaitingTimeBeforeTask() {
		return WAITING_TIME_BEFORE_TASK;
	}    
    
}
