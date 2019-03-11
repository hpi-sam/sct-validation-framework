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
    private DriveSimFrame frame;

    public ScenarioPanel(DeadlockDetector deadlockDetector, SimulationWorld world, ScenarioManager scenarioManager, TimerPanel timer, TestOverviewPanel testOverview, DriveSimFrame frame) {
    	this.world = world;
    	this.frame = frame;
    	
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
        	((JFrame) SwingUtilities.getWindowAncestor(this)).setResizable(scenario.isResizable());
        	runScenario(manager, scenario, timer);
        	frame.displayMessage("Starting scenario: \"" + scenario.getName() + "\"");
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
    
    public void select(Scenario scenario) {
    	JLabel label = scenarios.get(scenario);
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

	private void runScenario(ScenarioManager manager, Scenario scenario, TimerPanel timer) {
		world.resetZoom();
		world.resetOffset();
		timer.startNewClock(SimulatorConfig.getScenarioPassingTime());
		manager.runScenario(scenario);
	}
}
