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

    public TestListPanel(ScenarioManager scenarioManager) {
    	this.scenarioManager = scenarioManager;
        setLayout(new GridBagLayout());

        for (TestScenario test : scenarioManager.getTests())
            addTest(test);
    }
    
    @Override
    public void onTestCompleted(TestScenario test) {
    	//set the background of the menu wrapper of the label to green
        tests.get(test).getParent().setBackground(DriveSimFrame.MENU_GREEN);
        repaint();
    }
    
    @Override
	public void failTest(TestScenario test) {
    	tests.get(test).getParent().setBackground(DriveSimFrame.MENU_RED);	
    	repaint();
	}

	public void resetColors() {
		//set the background off the menu wrappers of all test labels to the generic menu color
		for (JLabel label : tests.values())
            label.getParent().setBackground(DriveSimFrame.MAIN_MENU_COLOR);
		repaint();
	}

    private void addTest(TestScenario test) {
    	
    	// --------------------------
    	// | -------------	-------	|
    	// | | Test Name | 	| run |	|
    	// | -------------  ------- |
    	// --------------------------
    	
        JLabel label = newTestLabel(test);
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = tests.size();
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.LINE_START;
        labelConstraints.weightx = 1.0;
        add(new MenuWrapper(180, 30, DriveSimFrame.MAIN_MENU_COLOR, label), labelConstraints);
        
        JButton run = newRunButton(test);
        GridBagConstraints runConstraints = new GridBagConstraints();
        runConstraints.gridx = 1;
        runConstraints.gridy = tests.size();
        runConstraints.fill = GridBagConstraints.HORIZONTAL;
        runConstraints.insets = new Insets(3, 3, 3, 3);
        add(new MenuWrapper(74, 24, DriveSimFrame.MAIN_MENU_COLOR, run), runConstraints);
        
        tests.put(test, label);
    }
    
    private JLabel newTestLabel(TestScenario test) {
    	JLabel label = new JLabel(test.getName());
        label.setToolTipText(test.getDescription());
        return label;
    }
    
    private JButton newRunButton(TestScenario test) {
    	JButton run = new JButton("run");
        run.setToolTipText(test.getDescription());
        run.addActionListener(e -> {
        	scenarioManager.runTest(test);
        });
        return run;
    }
    
    public void select(TestScenario test) {
    	JLabel label = tests.get(test);
    	((DriveSimFrame) SwingUtilities.windowForComponent(this)).clearSelections();
		Font oldFont = label.getFont();
		Font newFont = new Font(oldFont.getName(), Font.ITALIC | Font.BOLD, oldFont.getSize());
		label.setFont(newFont);
    }

    /*
	 * Turns all scenario labels to plain text.
	 */
    public void clearSelections() {
		for(JLabel label: tests.values()) {
			Font oldFont = label.getFont();
			Font newFont = new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize());
			label.setFont(newFont);
		}
	}
}
