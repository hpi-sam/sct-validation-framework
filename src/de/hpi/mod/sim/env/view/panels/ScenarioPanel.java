package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScenarioPanel extends JPanel {

    private static Map<Scenario, JPanel> scenarios = new HashMap<>();
    private JPanel scenarioPanel;
    private SimulationWorld world;


    public ScenarioPanel(SimulationWorld world, ScenarioManager scenarioManager, TimerPanel timer) {
    	this.world = world;
        setLayout(new GridLayout(0, 1));

        for (Scenario scenario : scenarioManager.getScenarios())
            addScenario(scenarioManager, scenario, timer);
    }
    
    public void scenarioPassed() {
		// scenarioPanel.setBackground(Color.green);
		// repaint();
	}

    private void addScenario(ScenarioManager manager, Scenario scenario, TimerPanel timer){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(scenario.getName());
        JButton run = new JButton("run");

        panel.setLayout(new BorderLayout());
        run.addActionListener(e -> {
        	runScenario(manager, scenario, timer);
        	DriveSimFrame.resetBorders();
        	Border blackline = BorderFactory.createLineBorder(Color.black);
        	panel.setBorder(blackline);
        	DriveSimFrame.displayMessage("Starting scenario: \"" + scenario.getName() + "\"");
        });

        panel.add(label, BorderLayout.CENTER);
        panel.add(run, BorderLayout.EAST);

        scenarios.put(scenario, panel);

        add(panel);
    }

	public static void resetAllBorders() {
		for (Map.Entry<Scenario, JPanel> entry : scenarios.entrySet())
		{
		    JPanel scenarioPanel = entry.getValue();
		    Border empty = BorderFactory.createEmptyBorder();
		    scenarioPanel.setBorder(empty);
		}
		TestPanel.resetAllBorders();
	}

	private void runScenario(ScenarioManager manager, Scenario test, TimerPanel timer) {
		world.resetZoom();
		world.resetOffset();
		timer.startNewClock(SimulatorConfig.getScenarioPassingTime());
		manager.runScenario(test);
		scenarioPanel = scenarios.get(test);
	}
}
