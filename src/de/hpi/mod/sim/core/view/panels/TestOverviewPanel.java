package de.hpi.mod.sim.core.view.panels;

import javax.swing.*;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.core.scenario.ITestScenarioListener;
import de.hpi.mod.sim.core.scenario.ScenarioManager;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.view.SimulatorFrame;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestOverviewPanel extends JPanel implements ITestScenarioListener {

	private static final long serialVersionUID = -3210286617906504416L;
	private ScenarioManager scenarioManager;
	
	private JProgressBar progressDisplay;
	private JButton showHideButton;
	private JButton runAllButton;
	private JButton resetButton;
	
	private SimulatorFrame frame;
		
	private boolean listVisible = true;
	
	public TestOverviewPanel(ScenarioManager scenarioManager, SimulatorFrame frame) {
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
		add(SimulatorFrame.setComponentDesign(194, 26, SimulatorFrame.MAIN_MENU_COLOR, progressDisplay), progressDisplayConstraints);
	}

	private void addShowHideButton() {
		showHideButton = newShowHideButton();
		GridBagConstraints showHideButtonConstraints = new GridBagConstraints();
		showHideButtonConstraints.gridx = 0;
		showHideButtonConstraints.gridy = 1;
		showHideButtonConstraints.weightx = 1.0;
		showHideButtonConstraints.weighty = 1.0;
		showHideButtonConstraints.insets = new Insets(3, 3, 3, 3);
		add(SimulatorFrame.setComponentDesign(194, 24, SimulatorFrame.MAIN_MENU_COLOR, showHideButton), showHideButtonConstraints);
	}

	private void addRunAllButton() {
		runAllButton = newRunAllButton();
		GridBagConstraints runAllButtonConstraints = new GridBagConstraints();
		runAllButtonConstraints.gridx = 1;
		runAllButtonConstraints.gridy = 0;
		runAllButtonConstraints.weightx = 1.0;
		runAllButtonConstraints.weighty = 1.0;
		runAllButtonConstraints.insets = new Insets(3, 3, 3, 3);
		add(SimulatorFrame.setComponentDesign(194, 24, SimulatorFrame.MAIN_MENU_COLOR, runAllButton), runAllButtonConstraints);
	}

	private void addResetButton() {
		resetButton = newResetButton();
		GridBagConstraints resetButtonConstraints = new GridBagConstraints();
		resetButtonConstraints.gridx = 1;
		resetButtonConstraints.gridy = 1;
		resetButtonConstraints.weightx = 1.0;
		resetButtonConstraints.weighty = 1.0;
		resetButtonConstraints.insets = new Insets(3, 3, 3, 3);
		add(SimulatorFrame.setComponentDesign(194, 24, SimulatorFrame.MAIN_MENU_COLOR, resetButton), resetButtonConstraints);
	}
	
	private JProgressBar newProgressDisplay() {
		JProgressBar progressBar = new JProgressBar(0, this.scenarioManager.getNumberOfAvailableTests());
		progressBar.setStringPainted(true);
		progressBar.setString("No available tests");
		return progressBar;
	}
	
	private void updateProgressDisplay() {
		int passed = this.scenarioManager.getNumberOfPassedTests();
		int total = this.scenarioManager.getNumberOfAvailableTests();
		if(total > 0) {
			progressDisplay.setValue(passed);
			progressDisplay.setString(Integer.toString(passed) + "/" + Integer.toString(total) + " passed");
		}
	}
	
	private JButton newShowHideButton() {
		JButton button = new JButton("Hide");
		
		button.addActionListener(e -> {
			listVisible = !listVisible;
			updateShowHideButton();
			frame.getTestListScrollPane().setVisible(listVisible);
			updateFrameSize();
		});
		
		return button;
	}
	
	private void updateFrameSize() {
		int height = frame.getHeight();
		int width = frame.getWidth();
		if (listVisible)
			width += frame.getTestListScrollPane().getPreferredSize().width;
		else
			width -= frame.getTestListScrollPane().getPreferredSize().width;
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
			this.scenarioManager.resetTests();
			this.updateProgressDisplay();
		});
		
		return button;
	}
	
	private JButton newRunAllButton() {
        JButton button = new JButton("Run All");

        button.addActionListener(e -> {
        	scenarioManager.runAllTests();
        });

        return button;
	}
	
	@Override
	public void markTestPassed(TestScenario test) {
        updateProgressDisplay();
	}
	
	@Override
	public void markTestFailed(TestScenario test) {
		updateProgressDisplay();
	}
	
	@Override
	public void resetAllTests() {
        updateProgressDisplay();
	}
}
