package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScenarioPanel extends JPanel {

    private Map<Scenario, JPanel> scenarios = new HashMap<>();


    public ScenarioPanel(ScenarioManager scenarioManager) {
        setLayout(new GridLayout(0, 1));

        for (Scenario scenario : scenarioManager.getScenarios())
            addScenario(scenarioManager, scenario);
    }

    private void addScenario(ScenarioManager manager, Scenario test){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(test.getName());
        JButton run = new JButton("run");

        panel.setLayout(new BorderLayout());
        run.addActionListener(e -> manager.runScenario(test));

        panel.add(label, BorderLayout.CENTER);
        panel.add(run, BorderLayout.EAST);

        scenarios.put(test, panel);

        add(panel);
    }
}
