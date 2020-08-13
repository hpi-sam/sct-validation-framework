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
	private static Map<TestScenario, JLabel> tests = new HashMap<>();
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
	    	addTest(test);
	    	yCoordinate++;
	    }
		
	}

	@Override
    public void onTestCompleted(TestScenario test) {
    	//set the background of the menu wrapper of the label to green
        tests.get(test).getParent().setBackground(SimulatorFrame.MENU_GREEN);
        repaint();
    }
    
    @Override
	public void failTest(TestScenario test) {
    	tests.get(test).getParent().setBackground(SimulatorFrame.MENU_RED);	
    	repaint();
	}

	public void resetColors() {
		//set the background off the menu wrappers of all test labels to the generic menu color
		for (JLabel label : tests.values())
            label.getParent().setBackground(SimulatorFrame.MAIN_MENU_COLOR);
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
        add(new MenuWrapper(180, 30, SimulatorFrame.MAIN_MENU_COLOR, label), labelConstraints);
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
        labelConstraints.gridy = yCoordinate;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.LINE_START;
        labelConstraints.weightx = 1.0;
        add(new MenuWrapper(180, 30, SimulatorFrame.MAIN_MENU_COLOR, label), labelConstraints);
        
        JButton run = newRunButton(test);
        GridBagConstraints runConstraints = new GridBagConstraints();
        runConstraints.gridx = 1;
        runConstraints.gridy = yCoordinate;
        runConstraints.fill = GridBagConstraints.HORIZONTAL;
        runConstraints.insets = new Insets(3, 3, 3, 3);
        add(new MenuWrapper(74, 24, SimulatorFrame.MAIN_MENU_COLOR, run), runConstraints);
        
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
    	((SimulatorFrame) SwingUtilities.windowForComponent(this)).clearSelections();
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
