package de.hpi.mod.sim.env.view.panels;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.DriveSimFrame;
import de.hpi.mod.sim.env.view.model.ITestListener;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;

public class TestOverviewPanel extends JPanel implements ITestListener {

	private ScenarioManager scenarioManager;
	
	private JProgressBar progressDisplay;
	private JButton showHideButton;
	private JButton runAllButton;
	private JButton resetButton;
	
	private DriveSimFrame frame;
		
	private boolean listVisible = false;
	
	public TestOverviewPanel(ScenarioManager scenarioManager, DriveSimFrame frame) {
		this.scenarioManager = scenarioManager;
		this.frame = frame;
		
		// -----------------------------------
    	// |  -----------	---------------	 |
    	// |  |Progress |	|  Run All    |	 |
    	// |  -----------	---------------	 |	
    	// |  -----------	---------------	 |					
    	// |  |Show/Hide|	|Reset Results|	 |						
    	// |  -----------	---------------	 |
    	// -----------------------------------
    	
    	//We use a GridBagLayout for flexibility
		setLayout(new GridBagLayout());
		
		addProgressDisplay();
		addShowHideButton();
		addRunAllButton();
		addResetButton();
	}
	
	private void addProgressDisplay() {
		progressDisplay = newProgressDisplay();
		updateProgressDisplay();
		GridBagConstraints progressDisplayConstraints = new GridBagConstraints();
		progressDisplayConstraints.gridx = 0;
		progressDisplayConstraints.gridy = 0;
		progressDisplayConstraints.weightx = 1.0;
		progressDisplayConstraints.weighty = 1.0;
		add(new MenuWrapper(194, 26, DriveSimFrame.MAIN_MENU_COLOR, progressDisplay), progressDisplayConstraints);
	}

	private void addShowHideButton() {
		showHideButton = newShowHideButton();
		GridBagConstraints showHideButtonConstraints = new GridBagConstraints();
		showHideButtonConstraints.gridx = 0;
		showHideButtonConstraints.gridy = 1;
		showHideButtonConstraints.weightx = 1.0;
		showHideButtonConstraints.weighty = 1.0;
		showHideButtonConstraints.insets = new Insets(3, 3, 3, 3);
		add(new MenuWrapper(194, 24, DriveSimFrame.MAIN_MENU_COLOR, showHideButton), showHideButtonConstraints);
	}

	private void addRunAllButton() {
		runAllButton = newRunAllButton();
		GridBagConstraints runAllButtonConstraints = new GridBagConstraints();
		runAllButtonConstraints.gridx = 1;
		runAllButtonConstraints.gridy = 0;
		runAllButtonConstraints.weightx = 1.0;
		runAllButtonConstraints.weighty = 1.0;
		runAllButtonConstraints.insets = new Insets(3, 3, 3, 3);
		add(new MenuWrapper(194, 24, DriveSimFrame.MAIN_MENU_COLOR, runAllButton), runAllButtonConstraints);
	}

	private void addResetButton() {
		resetButton = newResetButton();
		GridBagConstraints resetButtonConstraints = new GridBagConstraints();
		resetButtonConstraints.gridx = 1;
		resetButtonConstraints.gridy = 1;
		resetButtonConstraints.weightx = 1.0;
		resetButtonConstraints.weighty = 1.0;
		resetButtonConstraints.insets = new Insets(3, 3, 3, 3);
		add(new MenuWrapper(194, 24, DriveSimFrame.MAIN_MENU_COLOR, resetButton), resetButtonConstraints);
	}
	
	private JProgressBar newProgressDisplay() {
	/*	JTextField textField = new JTextField("0/0 green");
		textField.setEditable(false);
		return textField;*/
		
		JProgressBar progressBar = new JProgressBar(0, scenarioManager.getTests().size());
		progressBar.setStringPainted(true);
		progressBar.setString("0/0 green");
		return progressBar;
	}
	
	private void updateProgressDisplay() {
		String complete = Integer.toString(getCompletedTestCount());
		String amount = Integer.toString(scenarioManager.getTests().size());
		progressDisplay.setValue(getCompletedTestCount());
		progressDisplay.setString(complete + "/" + amount + " green");
	}
	
	private JButton newShowHideButton() {
		JButton button = new JButton("Show");
		
		button.addActionListener(e -> {
			listVisible = !listVisible;
			updateShowHideButton();
			frame.getTestListPanel().setVisible(listVisible);
			updateFrameSize();
		});
		
		return button;
	}
	
	private void updateFrameSize() {
		int height = frame.getHeight();
		int width = frame.getWidth();
		if (listVisible)
			width += frame.getTestListPanel().getPreferredSize().width;
		else
			width -= frame.getTestListPanel().getPreferredSize().width;
		frame.setPreferredSize(new Dimension(width, height));
		frame.pack();
	}
	
	private void updateShowHideButton() {
		if (listVisible) {
			showHideButton.setText("Hide");
		} else {
			showHideButton.setText("Show");
		}
	}
	
	private JButton newResetButton() {
		JButton button = new JButton("Reset Results");
		
		button.addActionListener(e -> {
			resetTestFile(SimulatorConfig.getTestFileName());
			resetTests();
			updateProgressDisplay();
			frame.getTestListPanel().resetColors();
		});
		
		return button;
	}
	
	private void resetTests() {
		for (TestScenario test : scenarioManager.getTests()) {
			test.resetTest();
		}
	}
	
	private void resetTestFile(String testFileName) {
		try {
			changeContent(testFileName, "#y", "#n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void changeContent(String fileName, String oldContent, String newContent) throws IOException {
		Path path = Paths.get(fileName);
		Charset charset = StandardCharsets.UTF_8;

		String content = new String(Files.readAllBytes(path), charset);
		content = content.replaceAll(oldContent, newContent);
		Files.write(path, content.getBytes(charset));
	}
	
	private JButton newRunAllButton() {
        JButton button = new JButton("Run All");

        button.addActionListener(e -> {
        	scenarioManager.runAllTests();
        });

        return button;
	}
	
	private void writeTestPassed(TestScenario test) throws IOException {
		changeContent(SimulatorConfig.getTestFileName(), test.getName() + "#n", test.getName() + "#y");
	}
	
	private int getCompletedTestCount() {
		Path path = Paths.get(SimulatorConfig.getTestFileName());
		Charset charset = StandardCharsets.UTF_8;
		
		int count = 0;
		try {
			String content;
			content = new String(Files.readAllBytes(path), charset);
			count = content.split("#y", -1).length - 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	@Override
	public void onTestCompleted(TestScenario test) {
		try {
			writeTestPassed(test);
		} catch (IOException e) {
			e.printStackTrace();
		}
        updateProgressDisplay();
	}
	
	@Override
	public void failTest(TestScenario test) {
		
	}
}
