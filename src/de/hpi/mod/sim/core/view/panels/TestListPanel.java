package de.hpi.mod.sim.core.view.panels;

import javax.swing.*;

import de.hpi.mod.sim.core.scenario.ITestScenarioListener;
import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.view.SimulatorFrame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class TestListPanel extends JPanel implements ITestScenarioListener {

	private static final long serialVersionUID = 7429792932382048548L;
	private static Map<TestScenario, JLabel> testLabels = new HashMap<>();
    private ScenarioManager scenarioManager;
    private int yCoordinate = 0;

    public TestListPanel(ScenarioManager scenarioManager) {
    	this.scenarioManager = scenarioManager;
        setLayout(new GridBagLayout());
        
        Map<String, List<TestScenario>> testGroups = scenarioManager.getTestGroups();
        Set<String> keys = testGroups.keySet();
        for(String key : keys) {
        	addTestGroup(key, testGroups.get(key));
        }   
    }
    
    private void addTestGroup(String key, List<TestScenario> list) {
	    addGroupLabel(key);
	    yCoordinate++;
	    for (TestScenario test : list) {
	    	addTestLabelAndButton(test);
	    	if(this.scenarioManager.isTestPassed(test))
	    		markTestPassed(test);
	    	yCoordinate++;
	    }
		
	}

	@Override
    public void markTestPassed(TestScenario test) {
        //set the background of the label to green
        testLabels.get(test).setBackground(SimulatorFrame.MENU_GREEN);
        repaint();
    }
    
    @Override
	public void markTestFailed(TestScenario test) {
    	testLabels.get(test).setBackground(SimulatorFrame.MENU_RED);	
    	repaint();
	}

	@Override
	public void resetAllTests() {
		for (JLabel label : testLabels.values())
            label.setBackground(SimulatorFrame.MAIN_MENU_COLOR);
		repaint();
	}

	public void resetColors() {
		//set the background of all test labels to the generic menu color
		for (JLabel label : testLabels.values())
            label.setBackground(SimulatorFrame.MAIN_MENU_COLOR);
		repaint();
	}

	private void addGroupLabel(String key) {
		Font text = new Font(Font.MONOSPACED, Font.BOLD, 14);
		JLabel label = new JLabel(key);
		label.setFont(text);
		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.gridx = 0;
        labelConstraints.gridy = yCoordinate;
        labelConstraints.gridwidth = 2;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.LINE_START;
        labelConstraints.weightx = 1.0;
        add(SimulatorFrame.setComponentDesign(180, 30, SimulatorFrame.MAIN_MENU_COLOR, label), labelConstraints);
	}
	
    private void addTestLabelAndButton(TestScenario test) {
    	
    	// --------------------------
    	// | -------------	-------	|
    	// | | Test Name | 	| run |	|
    	// | -------------  ------- |
    	// --------------------------
    	
        JLabel label = createTestLabel(test);
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = yCoordinate;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.LINE_START;
        labelConstraints.weightx = 1.0;
        add(SimulatorFrame.setComponentDesign(180, 30, SimulatorFrame.MAIN_MENU_COLOR, label), labelConstraints);
        
        JButton run = createRunButton(test);
        GridBagConstraints runConstraints = new GridBagConstraints();
        runConstraints.gridx = 1;
        runConstraints.gridy = yCoordinate;
        runConstraints.fill = GridBagConstraints.HORIZONTAL;
        runConstraints.insets = new Insets(3, 3, 3, 3);
        add(SimulatorFrame.setComponentDesign(74, 24, SimulatorFrame.MAIN_MENU_COLOR, run), runConstraints);
        
        testLabels.put(test, label);
    }
    
    private JLabel createTestLabel(TestScenario test) {
    	JLabel label = new JLabel(test.getName());
        label.setToolTipText(test.getDescription());
        return label;
    }
    
    private JButton createRunButton(TestScenario test) {
    	JButton run = new JButton("run");
        run.setToolTipText(test.getDescription());
        run.addActionListener(e -> {
        	scenarioManager.runTest(test);
        });
        return run;
    }
    
    public void select(TestScenario test) {
    	JLabel label = testLabels.get(test);
    	((SimulatorFrame) SwingUtilities.windowForComponent(this)).clearSelections();
		Font oldFont = label.getFont();
		Font newFont = new Font(oldFont.getName(), Font.ITALIC | Font.BOLD, oldFont.getSize());
		label.setFont(newFont);
    }

    /*
	 * Turns all scenario labels to plain text.
	 */
    public void clearSelections() {
		for(JLabel label: testLabels.values()) {
			Font oldFont = label.getFont();
			Font newFont = new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize());
			label.setFont(newFont);
		}
	}
}
