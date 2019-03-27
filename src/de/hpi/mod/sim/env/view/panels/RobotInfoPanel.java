package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.IHighlightedRobotListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows informations about the currently highlighted robot
 */
public class RobotInfoPanel extends JPanel implements IHighlightedRobotListener {

    private SimulationWorld world;
    private boolean isRightClickedRobot = false;
    private JTree stateTree;
    private DefaultTreeModel stateTreeModel;

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
        addInfo("Pos", robot -> stringify(robot.pos()));
        addInfo("Target", robot -> stringify(robot.getTarget()));
        addInfo("Facing", robot -> robot.posOrientation().toString());
        addInfo("Target Direction", robot ->
                robot.isTargetReached() ? "-" : robot.targetDirection().toString());
        addStateTree();
        //addInfo("State", robot -> trimStateName(robot.getMachineState()));
    }

    private void addStateTree() {
		
    	DefaultMutableTreeNode topStateNode = new DefaultMutableTreeNode("State:");
	    
    	stateTreeModel = new DefaultTreeModel(topStateNode);
    	
	    stateTree = new JTree(stateTreeModel);
	    stateTree.setShowsRootHandles(false);
	    stateTree.setToolTipText("<html>This feature is experimental. <br>Please see the project documentation for neccessary naming conventions.");
	    
	    refresher.add(new StateTreeRefresher(topStateNode));
	    
	    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        stateTree.setCellRenderer(renderer);
        
        if(isRightClickedRobot)
        	stateTree.setBackground(DriveSimFrame.MENU_RED);
        else
        	stateTree.setBackground(DriveSimFrame.MENU_GREEN);
	    
        stateTree.setAlignmentX(LEFT_ALIGNMENT);
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
    	
    	private DefaultMutableTreeNode topNode;
    	private String currentDisplay = "";

		public StateTreeRefresher(DefaultMutableTreeNode topNode) {
			this.topNode = topNode;
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
        			topNode.removeAllChildren();
                	stateTreeModel.reload();
                	
                	DefaultMutableTreeNode lastNode = topNode;
        		
	        		String[] states = splitStates(robotState);
	        		for(String state : states) {
	        			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(state);
	        			
	        			stateTreeModel.insertNodeInto(newNode, lastNode, 0);
	        			
	        			lastNode = newNode;
	        		}
	        		stateTree.scrollPathToVisible(new TreePath(lastNode.getPath()));
        		}
        		
        	} else {
        		topNode.removeAllChildren();
            	stateTreeModel.reload();
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
