package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScenarioPanel extends JPanel {

    private Map<Scenario, JPanel> scenarios = new HashMap<>();
    private JPanel scenarioPanel;


    public ScenarioPanel(ScenarioManager scenarioManager, TimerPanel timer) {
        setLayout(new GridLayout(0, 1));

        for (Scenario scenario : scenarioManager.getScenarios())
            addScenario(scenarioManager, scenario, timer);
    }
    
    public void scenarioPassed() {
		// scenarioPanel.setBackground(Color.green);
		// repaint();
	}

    private void addScenario(ScenarioManager manager, Scenario test, TimerPanel timer){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(test.getName());
        JButton run = new JButton("run");

        panel.setLayout(new BorderLayout());
        run.addActionListener(e -> runScenario(manager, test, timer));

        panel.add(label, BorderLayout.CENTER);
        panel.add(run, BorderLayout.EAST);

        scenarios.put(test, panel);

        add(panel);
    }

	private void runScenario(ScenarioManager manager, Scenario test, TimerPanel timer) {
		timer.startNewClock(SimulatorConfig.getScenarioPassingTime());
		manager.runScenario(test);
		scenarioPanel = scenarios.get(test);
	}
}
