package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.model.IHighlightedRobotListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows informations about the currently highlighted robot
 */
public class RobotInfoPanel extends JPanel implements IHighlightedRobotListener {

    private SimulationWorld world;
    private boolean isRightClickedRobot = false;

    /**
     * List of refreshable information
     */
    private List<LabelRefresher> refresher = new ArrayList<>();

    /**
     * @param world We need to ask the world for the reference to the highlighted Robot constantly to be able to react on changes.
     * @param isLRightClickedRobot If we monitor the right clicked Robot world.highlightedRobot2 is the one to be observed otherwise world.highlightedRobot
     */
    public RobotInfoPanel(SimulationWorld world, boolean isRightClickedRobot) {
        this.world = world;
        this.isRightClickedRobot = isRightClickedRobot;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Add information
        addInfo("ID", robot -> Integer.toString(robot.getID()));
        addInfo("Battery", robot -> Integer.toString((int) robot.getBattery()));
        addInfo("Pos", robot -> stringify(robot.pos()));
        addInfo("Target", robot -> stringify(robot.getTarget()));
        addInfo("Facing", robot -> robot.posOrientation().toString());
        addInfo("Target Direction", robot ->
                robot.isTargetReached() ? "-" : robot.targetDirection().toString());
        addInfo("State", robot -> trimStateName(robot.getMachineState()));
    }

    /*
     * This trims the YAKINDU state name to the lowest hierarchy level.
     * 
     * This only works with the following naming conventions in the state chart:
     * 1. The top state must be called "Drive System" (or any other String with exactly 13 characters).
     * 2. When regions are used they have to either be unnamed or start with an underscore "_".
     * 3. No other underscores "_" can be used in state names.
     */
    private String trimStateName(String machineState) {
    	//remove the top state prefix
    	String[] hierarchyLevels = machineState.substring(13).split("__");
    	
    	//get the lowest hierarchy level (they are separated by a double underscore)
    	String stateWithPossibleRegion = hierarchyLevels[hierarchyLevels.length - 1];
    	
    	//if the state is not in the highest hierarchy it has a region prefix which we want to remove.
    	String stateWithoutRegion;
    	if(hierarchyLevels.length > 1) {
    		//the region prefix is separated by a single underscore
    		String region = stateWithPossibleRegion.split("_")[0];
    		stateWithoutRegion = stateWithPossibleRegion.substring(region.length() + 1);
    	} else {
    		stateWithoutRegion = stateWithPossibleRegion;
    	}
    	//YAKINDU replaces spaces with underscores. We undo this.
    	stateWithoutRegion = stateWithoutRegion.replace("_", " ");
		return stateWithoutRegion;
	}

	/**
     * Adds the information to the panel
     * @param template The name of the value
     * @param refresh The getter for the value
     */
    private void addInfo(String template, RobotInformation refresh) {
        JLabel label = new JLabel();
        label.setFont(label.getFont().deriveFont(Font.PLAIN));

        var labelRefresher = new LabelRefresher(label, template, refresh);
        refresher.add(labelRefresher);
        labelRefresher.refresh();

        add(label);
    }

    /**
     * Gets called if a value got changed and labels need to be updated
     */
    @Override
    public void onHighlightedRobotChange() {
        for (LabelRefresher labelRefresher : refresher)
            labelRefresher.refresh();
        repaint();
    }

    /**
     * converts a Position to a String
     * @param position the position
     * @return a String representation
     */
    private String stringify(Position position) {
        return String.format("(%s, %s)", position.getX(), position.getY());
    }

    /**
     * Stores the information needed to onHighlightedRobotChange a label
     */
    private class LabelRefresher {

        /**
         * The Label that displays the information
         */
        private JLabel label;

        /**
         * The name of the information
         */
        private String template;

        /**
         * Getter for the information
         */
        private RobotInformation runnable;


        public LabelRefresher(JLabel label, String template, RobotInformation runnable) {
            this.label = label;
            this.template = template;
            this.runnable = runnable;
        }

        /**
         * Asks the getter for the value of the highlighted Robot and renders it to the label
         */
        public void refresh() {
        	Robot robot;
        	
        	if(isRightClickedRobot) {
        		robot = world.getHighlightedRobot2();
        	} else {
        		robot = world.getHighlightedRobot1();
        	}
            if (robot == null)
                label.setText(template + ": -");
            else
                label.setText(template + ": " + runnable.run(robot));
        }
    }

    private interface RobotInformation {
        String run(Robot r);
    }
}
