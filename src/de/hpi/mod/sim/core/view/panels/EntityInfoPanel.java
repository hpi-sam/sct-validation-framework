package de.hpi.mod.sim.core.view.panels;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.view.IHighlightedListener;

/**
 * A JPanel to that displays information about a currently highlighted entity
 * 
 * 
 */
public class EntityInfoPanel extends JPanel implements IHighlightedListener {

	private static final long serialVersionUID = -42067353669036945L;
	private AnimationPanel animationPanel;
	private boolean isRightClickedEntity = false;

	private JLabel[] textLabels = null;
	private StateTreeLabel treeLabel;

	private static interface InfoPanelType {

	}

	/**
	 * @param animationPanel       We need to ask the simulation for the reference
	 *                             to the highlighted Entity constantly to be able
	 *                             to react on changes.
	 * @param isRightClickedEntity If we monitor the right clicked Entity
	 *                             world.highlightedEntity2 is the one to be
	 *                             observed otherwise world.highlightedEntity
	 */
	public EntityInfoPanel(AnimationPanel animationPanel, boolean isRightClickedEntity) {
		this.animationPanel = animationPanel;
		this.isRightClickedEntity = isRightClickedEntity;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	private void addStateTree() {
		treeLabel = new StateTreeLabel();
		add(treeLabel);
	}

	private void clearInfoLabels() {
		removeAll();
	}

	private void createInfoLabels(int number) {
		clearInfoLabels();
		textLabels = new JLabel[number];
		for (int i = 0; i < number; i++) {
			JLabel label = new JLabel();
			textLabels[i] = label;
			label.setFont(label.getFont().deriveFont(Font.PLAIN));

			label.setAlignmentX(LEFT_ALIGNMENT);
			add(label);
		}
		addStateTree();
	}
	
	private IHighlightable getEntity() {
		return isRightClickedEntity ? animationPanel.getHighlighted2() : animationPanel.getHighlighted1();
	}

	@Override
	public void onValueUpdate() {
		
		IHighlightable highlight = getEntity();
		
		if (highlight == null) return;
		
		java.util.List<String> infos = highlight.getHighlightInfo();
		if (textLabels == null || textLabels.length != infos.size()) {
			createInfoLabels(infos.size());
		}
		for (int i = 0; i < infos.size(); i++)
			textLabels[i].setText(infos.get(i));
		
		if(highlight instanceof StateChartEntity) {
			treeLabel.showStateOf((StateChartEntity) highlight);
		}
		
		repaint();
	}

	@Override
	public void onHighlightedEntitySelection() {

		IHighlightable highlight = getEntity();
		
		if (highlight == null) {
			if (textLabels == null || textLabels.length != 1)
				createInfoLabels(1);
//			String prefix = isRightClickedEntity ? "Right" : "Left";
			textLabels[0].setText((isRightClickedEntity ? "Right" : "Left") + "-click an entity");
			treeLabel.showDefaultText();
			return;
		}
		
	}

	private class StateTreeLabel extends JLabel {

		private static final long serialVersionUID = -42067353669036945L;

		private String lastDisplayedStateString = "";

		public void showDefaultText() {
			setText("<html>State: -");
		}

		public void showStateOf(StateChartEntity entity) {

			// Ensure a proper entity was provided
			if (entity == null) return;
			
			// Ensure  that the state String has changed since last time 
			String stateString = entity.getMachineState();
			if (stateString.equals(lastDisplayedStateString)) return;
			
			System.out.println(stateString);
			
			lastDisplayedStateString = stateString;

			String[] states = splitStates(stateString, entity.getTopStateName());
			
			StringBuilder stringBuilder = new StringBuilder("<html>State:<br/>");
			for (int i = 0; i < states.length; i++) {
				for (int j = 0; j < 2 * i + 1; j++)
					stringBuilder.append("&nbsp;"); // adds a secure space since the JLabel won't render normal
													// spaces liek we want it to
				stringBuilder.append(states[i]);
				stringBuilder.append("<br/>");
			}

			setText(stringBuilder.toString());

		}

		/*
		 * This splits the YAKINDU state name into the hierarchy levels.
		 * 
		 * This only works with the following naming conventions in the state chart: 1.
		 * The top state must be called as specified in {@link
		 * StateChartEntity#getTopStateName()} 2. When regions are used they have to
		 * either be unnamed or start with an underscore "_". 3. No other underscores
		 * "_" can be used in state names.
		 */
		private String[] splitStates(String chartState, String topStateName) {
			chartState = chartState.toLowerCase();
			try {
				// Only enabled when the main region is named according to topStateName
				if (chartState.startsWith(topStateName + "_")) {

					// remove the top state prefix
					String[] hierarchyLevels = chartState.substring(topStateName.length() + 1).split("__");

					// if the state is not in the highest hierarchy it has a region prefix which we
					// want to remove.
					for (int i = 1; i < hierarchyLevels.length; i++) {
						String region = hierarchyLevels[i].split("_")[0];
						if (region.length() + 1 < hierarchyLevels[i].length()) {
							hierarchyLevels[i] = hierarchyLevels[i].substring(region.length() + 1);
						}
					}

					// YAKINDU replaces spaces with underscores. We undo this.
					for (int i = 0; i < hierarchyLevels.length; i++) {
						hierarchyLevels[i] = hierarchyLevels[i].replace("_", " ");
					}

					return hierarchyLevels;

				} else {
					return new String[] { "-" };
				}

			} catch (Exception e) {
				return new String[] { "-" };
			}

		}

	}
}
