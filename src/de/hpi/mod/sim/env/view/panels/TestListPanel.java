package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.ITestListener;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TestListPanel extends JPanel implements ITestListener {

    private static Map<TestScenario, JPanel> tests = new HashMap<>();
    private ScenarioManager scenarioManager;


    public TestListPanel(ScenarioManager scenarioManager) {
    	this.scenarioManager = scenarioManager;
        setLayout(new GridLayout(0, 1));

        for (TestScenario test : scenarioManager.getTests())
            addTest(test);
    }
    
    public Map<TestScenario, JPanel> getTests() {
    	return tests;
    }
    
    @Override
    public void onTestCompleted(TestScenario test) {
        tests.get(test).setBackground(DriveSimFrame.MENU_GREEN);
        repaint();
    }

	public void resetColors( ) {
		for (TestScenario test : scenarioManager.getTests())
            tests.get(test).setBackground(UIManager.getColor("Panel.background"));
		repaint();
	}

    private void addTest(TestScenario test){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(test.getName());
        JButton run = new JButton("run");

        panel.setLayout(new BorderLayout());
        run.addActionListener(e -> {
        	scenarioManager.runScenario(test);
        	((DriveSimFrame) SwingUtilities.windowForComponent(this)).clearSelections();
        	Border blackline = BorderFactory.createLineBorder(Color.black);
        	panel.setBorder(blackline);
        	DriveSimFrame.displayMessage("Starting test \"" + test.getName() + "\"");
        });

        panel.add(label, BorderLayout.CENTER);
        panel.add(run, BorderLayout.EAST);

        tests.put(test, panel);

        add(panel);
    }

	public static void resetAllBorders() {
		for (Map.Entry<TestScenario, JPanel> entry : tests.entrySet())
		{
		    JPanel scenarioPanel = entry.getValue();
		    Border empty = BorderFactory.createEmptyBorder();
		    scenarioPanel.setBorder(empty);
		}
	}
}
