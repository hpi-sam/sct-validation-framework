package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.ITestListener;
import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestPanel extends JPanel implements ITestListener {

    private static Map<TestScenario, JPanel> tests = new HashMap<>();
    private int currentTestID = 0;
    private boolean isRunningAll = false;
    private ScenarioManager scenarioManager;


    public TestPanel(ScenarioManager manager) {
    	scenarioManager = manager;
        setLayout(new GridLayout(0, 1));

        for (TestScenario test : manager.getTests())
            addTest(manager, test);
        
        addRunAllTestButton();
        addTestResetButton();
    }
    
    public Map<TestScenario, JPanel> getTests() {
    	return tests;
    }
    
    @Override
    public void onTestCompleted(TestScenario test) {
        tests.get(test).setBackground(Color.green);
        repaint();
        try {
			writeTestPassed(test);
		} catch (IOException e) {
			e.printStackTrace();
		}
        test.notifySuccessToUser();
        if(isRunningAll) {
        	runAllTests();
        }
    }
    
    private void addRunAllTestButton() {
    	JPanel panel = new JPanel();
        JLabel label = new JLabel("Run all tests");
        JButton run = new JButton("run");

        panel.setLayout(new BorderLayout());
        run.addActionListener(e -> {
        	if (currentTestID != 0 || isRunningAll) {
        		currentTestID = 0;
        		isRunningAll = false;
        	}
        	runAllTests();
        });

        panel.add(label, BorderLayout.CENTER);
        panel.add(run, BorderLayout.EAST);

        add(panel);
	}

    /**
     * Indirectly run all tests. The method manages in cooperation with the method this.onTestCompleted(test) 
     * a boolean and an ID. With these two things all testCases are run one after another.
     */
	private void runAllTests( ) {
		isRunningAll = true;
		toggleRunScenarioByID();
		currentTestID += 1;
		
		if(currentTestID == scenarioManager.getTests().size()) {
			isRunningAll = false;
			currentTestID = 0;
		}
	}
	
	private void toggleRunScenarioByID() {
		TestScenario test = scenarioManager.getTests().get(currentTestID);
		DriveSimFrame.displayMessage("Starting test \"" + test.getName() + "\"");
		scenarioManager.runScenario(test);
	}
    
    private void writeTestPassed(TestScenario test) throws IOException {
		changeContent(SimulatorConfig.getTestFileName(), test.getName() + "#n", test.getName() + "#y");
	}
    
    private void addTestResetButton( ) {
    	JPanel panel = new JPanel();
        JLabel label = new JLabel("Reset test results");
        JButton run = new JButton("Reset");

        panel.setLayout(new BorderLayout());
        run.addActionListener(e -> resetTestFile(SimulatorConfig.getTestFileName()));

        panel.add(label, BorderLayout.CENTER);
        panel.add(run, BorderLayout.EAST);

        add(panel);
		
	}

	private void resetTestFile(String testFileName) {
		try {
			changeContent(testFileName, "#y", "#n");
			resetColours();
			for (Map.Entry<TestScenario, JPanel> entry : tests.entrySet())
			{
			    TestScenario test = entry.getKey();
			    test.resetTest();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void resetColours( ) {
		for (TestScenario test : scenarioManager.getTests())
            tests.get(test).setBackground(UIManager.getColor("Panel.background"));
		repaint();
	}


	private void changeContent(String fileName, String oldContent, String newContent) throws IOException {
		Path path = Paths.get(fileName);
		Charset charset = StandardCharsets.UTF_8;

		String content = new String(Files.readAllBytes(path), charset);
		content = content.replaceAll(oldContent, newContent);
		Files.write(path, content.getBytes(charset));
	}

    private void addTest(ScenarioManager manager, TestScenario test){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(test.getName());
        JButton run = new JButton("run");

        panel.setLayout(new BorderLayout());
        run.addActionListener(e -> {
        	scenarioManager.getWorld().setIsRunningScenario(false);
        	manager.runScenario(test);
        	DriveSimFrame.resetBorders();
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
