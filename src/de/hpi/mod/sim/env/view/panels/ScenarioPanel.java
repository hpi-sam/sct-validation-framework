package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.sim.DeadlockDetector;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScenarioPanel extends JPanel {

    private static Map<Scenario, JLabel> scenarios = new HashMap<>();
    private SimulationWorld world;
    private DeadlockDetector deadlockDetector;
    private TestOverviewDetector testOverview

    public ScenarioPanel(DeadlockDetector deadlockDetector, SimulationWorld world, ScenarioManager scenarioManager, TimerPanel timer) {
    	this.deadlockDetector = deadlockDetector;
	this.world = world;
    	this.testOverview = testOverview;
    	
        setLayout(new GridBagLayout());

        for (Scenario scenario : scenarioManager.getScenarios())
            addScenario(scenarioManager, scenario, timer);
    }
    
    public void scenarioPassed() {

	}

    private void addScenario(ScenarioManager manager, Scenario scenario, TimerPanel timer){
    	
    	// ------------------------------
    	// | -----------------	-------	|
    	// | | Scenario Name | 	| run |	|
    	// | -----------------  ------- |
    	// ------------------------------
    	
        JLabel label = new JLabel(scenario.getName());
        JButton run = new JButton("run");

        run.addActionListener(e -> {
        	testOverview.stopRunAllSequenz();
        	((JFrame) SwingUtilities.getWindowAncestor(this)).setResizable(scenario.isResizable());
        	deadlockDetector.reactivate();
        	deadlockDetector.setIsRunningTest(false);
        	runScenario(manager, scenario, timer);
        	select(label);
        	DriveSimFrame.displayMessage("Starting scenario: \"" + scenario.getName() + "\"");
        });
        
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = scenarios.size();
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.weightx = 1.0;
        add(new MenuWrapper(300, 30, DriveSimFrame.MENU_ORANGE, label), labelConstraints);

        GridBagConstraints runConstraints = new GridBagConstraints();
        runConstraints.gridx = 1;
        runConstraints.gridy = scenarios.size();
        runConstraints.fill = GridBagConstraints.HORIZONTAL;
        runConstraints.insets = new Insets(3, 3, 3, 3);
        add(new MenuWrapper(74, 24, DriveSimFrame.MENU_ORANGE, run), runConstraints);
        
        scenarios.put(scenario, label);
    }

    /*
     * Gets called when a scenario is run.
     * Turns the text of the label italic and bold.
     */
	private void select(JLabel label) {
		((DriveSimFrame) SwingUtilities.windowForComponent(this)).clearSelections();
		Font oldFont = label.getFont();
		Font newFont = new Font(oldFont.getName(), Font.ITALIC | Font.BOLD, oldFont.getSize());
		label.setFont(newFont);
		
	}
	
	/*
	 * Turns all scenario labels to plain text.
	 */
	public void clearSelections() {
		for(JLabel l: scenarios.values()) {
			Font oldFont = l.getFont();
			Font newFont = new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize());
			l.setFont(newFont);
		}
	}

	private void runScenario(ScenarioManager manager, Scenario test, TimerPanel timer) {
		world.resetZoom();
		world.resetOffset();
		timer.startNewClock(SimulatorConfig.getScenarioPassingTime());
		manager.runScenario(test);
	}
}
