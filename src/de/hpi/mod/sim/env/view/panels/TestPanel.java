package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.model.ITestListener;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPanel extends JPanel implements ITestListener {

    private Map<TestScenario, JPanel> tests = new HashMap<>();


    public TestPanel(ScenarioManager scenarioManager) {
        setLayout(new GridLayout(0, 1));

        for (TestScenario test : scenarioManager.getTests())
            addTest(scenarioManager, test);
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
    }
    
    private void writeTestPassed(TestScenario test) throws IOException {
		changeLine(SimulatorConfig.getTestFileName(), test.getName() + "#n", test.getName() + "#y");
	}

	private void changeLine(String fileName, String oldLine, String newLine) throws IOException {
		Path path = Paths.get(fileName);
		Charset charset = StandardCharsets.UTF_8;

		String content = new String(Files.readAllBytes(path), charset);
		content = content.replaceAll(oldLine, newLine);
		Files.write(path, content.getBytes(charset));
	}

	public Map<TestScenario, JPanel> getTests() {
    	return tests;
    }

    private void addTest(ScenarioManager manager, TestScenario test){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(test.getName());
        JButton run = new JButton("run");

        panel.setLayout(new BorderLayout());
        run.addActionListener(e -> manager.runScenario(test));

        panel.add(label, BorderLayout.CENTER);
        panel.add(run, BorderLayout.EAST);

        tests.put(test, panel);

        add(panel);
    }
}
