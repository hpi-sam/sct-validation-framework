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

	private static final long serialVersionUID = -42067353669036945L;
	private SimulationWorld world;
    private boolean isRightClickedRobot = false;

    /**
     * List of refreshable information
     */
    private List<InfoRefresher> refresher = new ArrayList<>();

    /**
     * @param world We need to ask the world for the reference to the highlighted Robot constantly to be able to react on changes.
     * @param isLRightClickedRobot If we monitor the right clicked Robot world.highlightedRobot2 is the one to be observed otherwise world.highlightedRobot
     */
    public RobotInfoPanel(SimulationWorld world, boolean isRightClickedRobot) {
        this.world = world;
        this.isRightClickedRobot = isRightClickedRobot;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        //setLayout(new GridLayout(0, 1));
        	
        // Add information
        addInfo("ID", robot -> Integer.toString(robot.getID()));
        addInfo("Battery", robot -> Integer.toString((int) robot.getBattery()));
        addInfo("Pos", robot -> stringify(robot.pos()) + " (" + robot.posType().toString() +")");
        addInfo("Blocked sides", robot -> (robot.blockedFront() ? "front " : "") +
        								  (robot.blockedLeft() ? "left " : "") +
        								  (robot.blockedRight() ? "right " : ""));
        addInfo("Target", robot -> stringify(robot.getTarget()));
        addInfo("Facing", robot -> robot.posOrientation().toString());
        addInfo("Target Direction", robot ->
                robot.isTargetReached() ? "-" : robot.targetDirection().toString());
        addStateTree();
        //addInfo("State", robot -> trimStateName(robot.getMachineState()));
    }

    private void addStateTree() {
    	JLabel stateTree = new JLabel();
    	
    	StateTreeRefresher stateTreeRefresher = new StateTreeRefresher(stateTree);
    	refresher.add(stateTreeRefresher);
    	stateTreeRefresher.refresh();
    	
    	add(stateTree);
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

        label.setAlignmentX(LEFT_ALIGNMENT);
        add(label);
    }

    /**
     * Gets called if a value got changed and labels need to be updated
     */
    @Override
    public void onHighlightedRobotChange() {
        for (InfoRefresher infoRefresher : refresher)
            infoRefresher.refresh();
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
    private class LabelRefresher implements InfoRefresher{

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
        @Override
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
    
    private class StateTreeRefresher implements InfoRefresher {
    	
    	private JLabel label;
    	private String currentDisplay = "";

		public StateTreeRefresher(JLabel label) {
			this.label = label;
		}

		@Override
		public void refresh() {
			Robot robot;
        	
        	if(isRightClickedRobot) {
        		robot = world.getHighlightedRobot2();
        	} else {
        		robot = world.getHighlightedRobot1();
        	}
        	
        	if(robot != null) {
        		String robotState = robot.getMachineState();
        		if (!robotState.equals(currentDisplay)) {
        			currentDisplay = robotState;
        			
        			String[] states = splitStates(robotState);
        			StringBuilder stringBuilder = new StringBuilder("<html>State:<br/>");
        			for(int i = 0; i < states.length; i++) {
        				for(int j = 0; j < 2*i + 1; j++)
        					stringBuilder.append("&nbsp;"); //adds a secure space since the JLabel won't render normal spaces liek we want it to
        				stringBuilder.append(states[i]);
        				stringBuilder.append("<br/>");
        			}
        			
        			label.setText(stringBuilder.toString());
        		}
        		
        	} else {
        		label.setText("<html>State: -");
        	}
        	
		}
		
		/*
	     * This splits the YAKINDU state name into the hierarchy levels.
	     * 
	     * This only works with the following naming conventions in the state chart:
	     * 1. The top state must be called "Drive System" (or any other String with exactly 13 characters).
	     * 2. When regions are used they have to either be unnamed or start with an underscore "_".
	     * 3. No other underscores "_" can be used in state names.
	     */
	    private String[] splitStates(String machineState) {
	    	//remove the top state prefix
	    	String[] hierarchyLevels = machineState.substring(13).split("__");
	    	
	    	//if the state is not in the highest hierarchy it has a region prefix which we want to remove.
	    	for(int i = 1; i < hierarchyLevels.length; i++) {
	    		String region = hierarchyLevels[i].split("_")[0];
	    		hierarchyLevels[i] = hierarchyLevels[i].substring(region.length() + 1);
	    	}
	    	
	    	//YAKINDU replaces spaces with underscores. We undo this.
	    	for(int i = 0; i < hierarchyLevels.length; i++) {
	    		hierarchyLevels[i] = hierarchyLevels[i].replace("_", " ");
	    	}
	    	
			return hierarchyLevels;
		}
    	
    }
    
    private interface InfoRefresher {
    	public void refresh();
    }

    private interface RobotInformation {
        String run(Robot r);
    }
}
