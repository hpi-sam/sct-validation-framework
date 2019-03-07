package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.ITestListener;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.sim.DeadlockDetector;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TestListPanel extends JPanel implements ITestListener {

    private static Map<TestScenario, JLabel> tests = new HashMap<>();
    private ScenarioManager scenarioManager;
    private DeadlockDetector deadlockDetector;


    public TestListPanel(DeadlockDetector deadlockDetector, ScenarioManager scenarioManager) {
    	this.deadlockDetector = deadlockDetector;
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
        tests.get(test).getParent().setBackground(DriveSimFrame.MENU_GREEN);
        endDeadlockDetection();
        repaint();
    }
    
    @Override
	public void failTest(TestScenario test) {
    	tests.get(test).getParent().setBackground(DriveSimFrame.MENU_RED);	
    	repaint();
	}

	public void resetColors( ) {
		for (JLabel label : tests.values())
            label.getParent().setBackground(DriveSimFrame.MENU_ORANGE);
		repaint();
	}

    private void addTest(TestScenario test){
        JLabel label = new JLabel(test.getName());
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = tests.size();
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.LINE_START;
        labelConstraints.weightx = 1.0;
        add(new MenuWrapper(150, 30, DriveSimFrame.MENU_ORANGE, label), labelConstraints);
        
        JButton run = new JButton("run");
        run.addActionListener(e -> {
        	useDeadlockDetection();
        	notifyDeadlockDetectorAboutRunningTest();
        	scenarioManager.runScenario(test);
        	select(label);
        	DriveSimFrame.displayMessage("Starting test \"" + test.getName() + "\"");
        });
        GridBagConstraints runConstraints = new GridBagConstraints();
        runConstraints.gridx = 1;
        runConstraints.gridy = tests.size();
        runConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(new MenuWrapper(80, 30, DriveSimFrame.MENU_ORANGE, run), runConstraints);
        
        tests.put(test, label);
    }
    
    private void select(JLabel label) {
		((DriveSimFrame) SwingUtilities.windowForComponent(this)).clearSelections();
		Font oldFont = label.getFont();
		Font newFont = new Font(oldFont.getName(), Font.ITALIC | Font.BOLD, oldFont.getSize());
		label.setFont(newFont);
		
	}

    public void clearSelections() {
		for(JLabel l: tests.values()) {
			Font oldFont = l.getFont();
			Font newFont = new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize());
			l.setFont(newFont);
		}
	}

	public void useDeadlockDetection() {
		deadlockDetector.reactivate();
	}

	public void endDeadlockDetection() {
		deadlockDetector.deactivate();
	}
	
	public void notifyDeadlockDetectorAboutRunningTest() {
		deadlockDetector.setIsRunningTest(true);
	}
}
