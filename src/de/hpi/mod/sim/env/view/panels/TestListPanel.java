package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.ITestListener;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;

import javax.swing.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TestListPanel extends JPanel implements ITestListener {

    private static Map<TestScenario, JLabel> tests = new HashMap<>();
    private ScenarioManager scenarioManager;
    private TestOverviewPanel testOverview;


    public TestListPanel(ScenarioManager scenarioManager) {
    	this.scenarioManager = scenarioManager;
        setLayout(new GridBagLayout());

        for (TestScenario test : scenarioManager.getTests())
            addTest(test);
    }
    
    public Map<TestScenario, JLabel> getTests() {
    	return tests;
    }
    
    @Override
    public void onTestCompleted(TestScenario test) {
    	//set the background of the menu wrapper of the label to green
        tests.get(test).getParent().setBackground(DriveSimFrame.MENU_GREEN);
        repaint();
    }

	public void resetColors() {
		//set the background off the menu wrappers of all test labels to the generic menu color
		for (JLabel label : tests.values())
            label.getParent().setBackground(DriveSimFrame.MENU_ORANGE);
		repaint();
	}

    private void addTest(TestScenario test) {
    	
    	// --------------------------
    	// | -------------	-------	|
    	// | | Test Name | 	| run |	|
    	// | -------------  ------- |
    	// --------------------------
    	
        JLabel label = new JLabel(test.getName());
        label.setToolTipText("Tooltip");
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = tests.size();
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.LINE_START;
        labelConstraints.weightx = 1.0;
        add(new MenuWrapper(220, 30, DriveSimFrame.MENU_ORANGE, label), labelConstraints);
        
        JButton run = new JButton("run");
        run.setToolTipText("Tooltip");
        run.addActionListener(e -> {
        	testOverview.stopRunAllSequenz();
        	scenarioManager.runScenario(test);
        	select(label);
        	DriveSimFrame.displayMessage("Starting test \"" + test.getName() + "\"");
        });
        GridBagConstraints runConstraints = new GridBagConstraints();
        runConstraints.gridx = 1;
        runConstraints.gridy = tests.size();
        runConstraints.fill = GridBagConstraints.HORIZONTAL;
        runConstraints.insets = new Insets(3, 3, 3, 3);
        add(new MenuWrapper(74, 24, DriveSimFrame.MENU_ORANGE, run), runConstraints);
        
        tests.put(test, label);
    }
    
    /*
     * Gets called when a test is run.
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
		for(JLabel l: tests.values()) {
			Font oldFont = l.getFont();
			Font newFont = new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize());
			l.setFont(newFont);
		}
	}

	public void setTestOverview(TestOverviewPanel testOverview) {
		this.testOverview = testOverview;
	}
}
