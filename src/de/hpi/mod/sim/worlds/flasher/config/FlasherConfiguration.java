package de.hpi.mod.sim.worlds.flasher.config;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FlasherConfiguration {

	
    private static final String PATH_TO_BULB_ON_IMAGE = "res/bulb_on.png";
    private static final String PATH_TO_BULB_OFF_IMAGE = "res/bulb_off.png";
	
    private static final double LIGHT_BULB_IMAGE_MAXIMUM_RELATIVE_WIDTH = 0.5;
    private static final double LIGHT_BULB_IMAGE_MAXIMUM_RELATIVE_HEIGHT = 0.8;

    private static final int TASK_DISPLAY_HEIGHT = 100;
    private static final int TASK_DISPLAY_SPACING = 30;
    
    private static final double WAITING_TIME_BEFORE_TASK = 2000.0;
    
	private static final Font TASK_DISPLAY_HEADER_FONT = new Font("Monospaced", Font.BOLD, 20);
    private static final Font TASK_DISPLAY_DESCRIPION_FONT = new Font("Monospaced", Font.PLAIN, 14);
    private static final Font TASK_DISPLAY_DESCRIPION_HIGHLIGHT_FONT = new Font("Monospaced", Font.BOLD, 14);
    private static final Font TASK_DISPLAY_NO_TASK_FONT = new Font("Monospaced", Font.BOLD, 30);
    
    private static final DecimalFormat TASK_DISPLAY_TIMER_FORMAT = new DecimalFormat("000.0", DecimalFormatSymbols.getInstance( Locale.ENGLISH ));
    
    private static final Color TASK_DISPLAY_WAITING_FONT_COLOR = new Color(0, 0, 0, 200);
    private static final Color TASK_DISPLAY_TASK_FONT_COLOR = new Color(0, 0, 0, 255);
    private static final Color TASK_DISPLAY_NO_TASK_FONT_COLOR = new Color(0, 0, 0, 150);
    
    private static final Color TASK_DISPLAY_WAITING_BACKGROUND_COLOR = new Color(0, 0, 0, 100);
    private static final Color TASK_DISPLAY_ONGOING_BACKGROUND_COLOR = new Color(255, 255, 0, 100);
    private static final Color TASK_DISPLAY_SUCCESS_BACKGROUND_COLOR = new Color(0, 255, 0, 100);
    private static final Color TASK_DISPLAY_FAILURE_BACKGROUND_COLOR = new Color(255, 0, 0, 100);
    
    
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

	public static int getTaskDisplayHeight() {
		return TASK_DISPLAY_HEIGHT;
	}

	public static int getTaskDisplaySpacing() {
		return TASK_DISPLAY_SPACING;
	}    

	public static int getTaskDisplayReservedHeight() {
		return TASK_DISPLAY_HEIGHT + (2 * TASK_DISPLAY_SPACING);
	}

    public static Font getTaskDisplayHeaderFont() {
		return TASK_DISPLAY_HEADER_FONT;
	}

	public static Font getTaskDisplayDescripionFont() {
		return TASK_DISPLAY_DESCRIPION_FONT;
	}

	public static Font getTaskDisplayNoTaskFont() {
		return TASK_DISPLAY_NO_TASK_FONT;
	}

	public static DecimalFormat getTaskDisplayTimerFormat() {
		return TASK_DISPLAY_TIMER_FORMAT;
	}

	public static Color getTaskDisplayWaitingFontColor() {
		return TASK_DISPLAY_WAITING_FONT_COLOR;
	}

	public static Color getTaskDisplayTaskFontColor() {
		return TASK_DISPLAY_TASK_FONT_COLOR;
	}

	public static Color getTaskDisplayNoTaskFontColor() {
		return TASK_DISPLAY_NO_TASK_FONT_COLOR;
	}

	public static Color getTaskDisplayWaitingBackgroundColor() {
		return TASK_DISPLAY_WAITING_BACKGROUND_COLOR;
	}

	public static Color getTaskDisplayOngoingBackgroundColor() {
		return TASK_DISPLAY_ONGOING_BACKGROUND_COLOR;
	}

	public static Color getTaskDisplaySuccessBackgroundColor() {
		return TASK_DISPLAY_SUCCESS_BACKGROUND_COLOR;
	}

	public static Color getTaskDisplayFailureBackgroundColor() {
		return TASK_DISPLAY_FAILURE_BACKGROUND_COLOR;
	}

	public static Font getTaskDisplayDescripionHighlightFont() {
		return TASK_DISPLAY_DESCRIPION_HIGHLIGHT_FONT;
	}
	
}
