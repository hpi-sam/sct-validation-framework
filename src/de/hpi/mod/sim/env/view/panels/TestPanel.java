package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.view.model.ITestListener;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPanel extends JPanel implements ITestListener {

    private Map<TestScenario, JPanel> tests = new HashMap<>();


    public TestPanel(ScenarioManager scenarioManager) {
        for (TestScenario test : scenarioManager.getTests())
            addTest(scenarioManager, test);
    }

    private void addTest(ScenarioManager manager, TestScenario test){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(test.getName());
        JButton run = new JButton("run");

        run.addActionListener(e -> manager.runScenario(test));

        panel.add(label);
        panel.add(run);

        tests.put(test, panel);

        add(panel);
    }

    @Override
    public void onTestCompleted(TestScenario test) {
        tests.get(test).setBackground(Color.green);
        repaint();
    }
}
