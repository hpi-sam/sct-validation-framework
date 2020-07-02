package de.hpi.mod.sim.core.view.panels;

import javax.swing.*;

import de.hpi.mod.sim.core.model.IHighlightable;
import de.hpi.mod.sim.core.model.StateChartEntity;
import de.hpi.mod.sim.core.view.model.IHighlightedListener;
import de.hpi.mod.sim.core.view.sim.SimulationView;

import java.awt.*;

/**
 * Shows informations about the currently highlighted robot
 */
public class EntityInfoPanel extends JPanel implements IHighlightedListener {

	private static final long serialVersionUID = -42067353669036945L;
	private SimulationView world;
    private boolean isRightClickedEntity = false;

    private JLabel[] infoLabels = null;
    private StateTree stateTree;

    /**
     * @param world                We need to ask the simulation for the reference
     *                             to the highlighted Entity constantly to be able
     *                             to react on changes.
     * @param isRightClickedEntity If we monitor the right clicked Entity
     *                             world.highlightedEntity2 is the one to be
     *                             observed otherwise world.highlightedEntity
     */
    public EntityInfoPanel(SimulationView world, boolean isRightClickedEntity) {
        this.world = world;
        this.isRightClickedEntity = isRightClickedEntity;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    private void addStateTree() {
    	JLabel stateTreeLabel = new JLabel();
    	stateTree = new StateTree(stateTreeLabel);
    	add(stateTreeLabel);
    }
    
    private void addInfos(int number) {
        removeAll();
        infoLabels = new JLabel[number];
        for (int i = 0; i < number; i++) {
            JLabel label = new JLabel();
            infoLabels[i] = label;
            label.setFont(label.getFont().deriveFont(Font.PLAIN));

            label.setAlignmentX(LEFT_ALIGNMENT);
            add(label);
        }
        addStateTree();
    }

    /**
     * Gets called if a value got changed and labels need to be updated
     */
    @Override
    public void onHighlightedChange() {
        IHighlightable highlight = isRightClickedEntity ? world.getHighlighted2() : world.getHighlighted1();
         if (highlight == null) {
             if (infoLabels == null || infoLabels.length != 1)
                addInfos(1);
            String prefix = isRightClickedEntity ? "Right" : "Left";
            infoLabels[0].setText(prefix + "-click an entity");
            return;
         }

        java.util.List<String> infos = highlight.getHighlightInfo();

        if (infoLabels == null || infoLabels.length != infos.size()) {
            addInfos(infos.size());
        }
       
        for (int i = 0; i < infos.size(); i++)
            infoLabels[i].setText(infos.get(i));
        
        stateTree.refresh();
        repaint();
    }


    private class StateTree {
    	
    	private JLabel label;
    	private String currentDisplay = "";

		public StateTree(JLabel label) {
			this.label = label;
		}

        public void refresh() {
            IHighlightable highlight = isRightClickedEntity ? world.getHighlighted2() : world.getHighlighted1();
            if (highlight == null || !(highlight instanceof StateChartEntity)) {
                label.setText("<html>State: -");
            } else {
                StateChartEntity entity = (StateChartEntity) highlight;

                if (entity != null) {
                    String state = entity.getMachineState();
                    if (!state.equals(currentDisplay)) {
                        currentDisplay = state;

                        String[] states = splitStates(state, entity.getTopStateName());
                        StringBuilder stringBuilder = new StringBuilder("<html>State:<br/>");
                        for (int i = 0; i < states.length; i++) {
                            for (int j = 0; j < 2 * i + 1; j++)
                                stringBuilder.append("&nbsp;"); //adds a secure space since the JLabel won't render normal spaces liek we want it to
                            stringBuilder.append(states[i]);
                            stringBuilder.append("<br/>");
                        }

                        label.setText(stringBuilder.toString());
                    }
                }
            }
		}
		
		/*
         * This splits the YAKINDU state name into the hierarchy levels.
         * 
         * This only works with the following naming conventions in the state chart: 
         * 1. The top state must be called as specified in {@link
         * StateChartEntity#getTopStateName()}
         * 2. When regions are used they have to either be unnamed or start
         * with an underscore "_". 
         * 3. No other underscores "_" can be used in state
         * names.
         */
        private String[] splitStates(String machineState, String topStateName) {
	    	try {
	    		//Only enabled when the main region is named according to topStateName
	    		if(machineState.startsWith(topStateName + "_")) {
		    		
			    	//remove the top state prefix
		    		String[] hierarchyLevels = machineState.substring(topStateName.length() + 1).split("__");
			    	
			    	//if the state is not in the highest hierarchy it has a region prefix which we want to remove.
			    	for(int i = 1; i < hierarchyLevels.length; i++) {
			    		String region = hierarchyLevels[i].split("_")[0];	    		
			    		if(region.length() + 1 < hierarchyLevels[i].length()) {
			    			hierarchyLevels[i] = hierarchyLevels[i].substring(region.length() + 1);
			    		}
			    	}
			    	
			    	//YAKINDU replaces spaces with underscores. We undo this.
			    	for(int i = 0; i < hierarchyLevels.length; i++) {
			    		hierarchyLevels[i] = hierarchyLevels[i].replace("_", " ");
			    	}
			    	
			    	return hierarchyLevels;
			    	
	    		} else {
	    			return new String[] {"-"};
	    		}
		    	
	    	} catch(Exception e) {	
	    		return new String[] {"-"};
	    	}
	    	
		}
    	
    }
}
