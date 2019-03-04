package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScenarioPanel extends JPanel {

    private static Map<Scenario, JLabel> scenarios = new HashMap<>();
    private SimulationWorld world;


    public ScenarioPanel(SimulationWorld world, ScenarioManager scenarioManager, TimerPanel timer) {
    	this.world = world;
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
        	world.setIsRunningScenario(true);
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
        add(new MenuWrapper(80, 30, DriveSimFrame.MENU_ORANGE, run), runConstraints);
        
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
