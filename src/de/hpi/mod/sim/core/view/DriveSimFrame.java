package de.hpi.mod.sim.core.view;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Simulation;
import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.core.view.panels.*;
import de.hpi.mod.sim.core.view.sim.SimulationWorld;
import de.hpi.mod.sim.core.view.sim.SimulatorView;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DriveSimFrame extends JFrame {

	private static final long serialVersionUID = 4683030810403226266L;
	private Setting setting;
	private SimulatorView simView;
	private EntityInfoPanel robotInfoPanel1;
	private EntityInfoPanel robotInfoPanel2;
	private ScenarioPanel scenarioPanel;
	private TestListPanel testListPanel;
	private JScrollPane testListScrollPane;
	private TestOverviewPanel testOverviewPanel;
	private SimulationPanel simulationPanel;
	private TimerPanel timerPanel;

	private long lastFrame;
	private boolean running = true;

	public static Color MAIN_MENU_COLOR = new Color(0xfff3e2);
	public static Color MENU_GREEN = new Color(0xdcf3d0);
	public static Color MENU_RED = new Color(0xffe1d0);

	public DriveSimFrame(Setting setting) {
		super("Drive System Simulator");
		this.setting = setting;
		// TODO rethink structure of simulationWorld/simulationView/MetaWorld
		SimulationWorld simWorld = new SimulationWorld(setting.getGrid());
		Simulation simulation = new Simulation(setting, simWorld);
		setting.initialize(this, simulation);
		simView = new SimulatorView(setting, simWorld);
		simWorld.initialize(simView);

		setLayout(new GridBagLayout());

		createFileIfNotExist(SimulatorConfig.getTestFileName());
		initializePanels(simWorld);
		loadTestFileContent(SimulatorConfig.getTestFileName());
		addListeners(simWorld);
		setDesignOfSubpanels();
		setDesignOfMainWindow();

		lastFrame = System.currentTimeMillis();
		while (running)
			update();
		close();
	}

	public void displayMessage(String message) {
		displayMessage(message, MAIN_MENU_COLOR);
	}

	public void displayWarningMessage(String message) {
		displayMessage(message, MENU_RED);
	}

	public void displayMessage(String message, Color color) {
		Popup popup = createPopup(message, color);
		popup.show();

		Thread popupHider = new Thread() {
			public void run() {
				try {
					Thread.sleep(SimulatorConfig.getMessageDisplayTime());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				popup.hide();
			}
		};
		popupHider.start();
	}

	public void clearSelections() {
		testListPanel.clearSelections();
		scenarioPanel.clearSelections();
	}

	public void resetSimulationView() {
		setting.resetView();
	}

	public boolean isRunning() {
		return running;
	}

	public ScenarioPanel getScenarioPanel() {
		return scenarioPanel;
	}

	public SimulationPanel getSimulationPanel() {
		return simulationPanel;
	}

	public TestListPanel getTestListPanel() {
		return testListPanel;
	}

	public JScrollPane getTestListScrollPane() {
		return testListScrollPane;
	}

	public TimerPanel getTimerPanel() {
		return timerPanel;
	}

	public static void setSystemLookAndFeel() {
    	try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.put("Button.background", Color.white);
			UIManager.put("TextField.background", Color.white);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.out.println("The look and feel could not be loaded. The Application will work fine but look different.");
		}
    }
    
    private void initializePanels(SimulationWorld simulationWorld) {
		robotInfoPanel1 = new EntityInfoPanel(simulationWorld, false);
        robotInfoPanel2 = new EntityInfoPanel(simulationWorld, true);
        simulationPanel = new SimulationPanel(setting.getSimulation(), setting.getScenarioManager());
        testListPanel = new TestListPanel(setting.getScenarioManager());
        testListScrollPane = new JScrollPane(testListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        testOverviewPanel = new TestOverviewPanel(setting.getScenarioManager(), this);
		timerPanel = new TimerPanel(setting.getSimulation());
        scenarioPanel = new ScenarioPanel(setting.getScenarioManager());
        setJMenuBar(new DriveSimMenu(setting.getSimulation(), simulationWorld));
	}
    
    private void addListeners(SimulationWorld simulationWorld) {
		simulationWorld.addHighlightedListener(robotInfoPanel1);
        simulationWorld.addHighlightedListener(robotInfoPanel2);
        setting.getSimulation().addTimeListener(simulationPanel);
        setting.getScenarioManager().addTestListener(testListPanel);
        setting.getScenarioManager().addTestListener(testOverviewPanel);
	}
    
    private void setDesignOfSubpanels() {
		testOverviewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Tests"));
		testOverviewPanel.setBackground(MAIN_MENU_COLOR);
        
        scenarioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Scenarios"));
        scenarioPanel.setBackground(MAIN_MENU_COLOR);
        
        simulationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Simulation"));
        simulationPanel.setBackground(MAIN_MENU_COLOR);
        
        timerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Timer"));
        timerPanel.setBackground(MAIN_MENU_COLOR);
        
        //info panels work with a different layout than all other panels and need a specified size
        robotInfoPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info left clicked robot"));
        robotInfoPanel1.setMinimumSize(new Dimension(200, 200));
        robotInfoPanel1.setPreferredSize(new Dimension(200, 200));
        robotInfoPanel1.setBackground(MENU_GREEN);
        
        robotInfoPanel2.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info right clicked robot"));
        robotInfoPanel2.setMinimumSize(new Dimension(200, 200));
        robotInfoPanel2.setPreferredSize(new Dimension(200, 200));
        robotInfoPanel2.setBackground(MENU_RED);
        
        testListPanel.setBackground(MAIN_MENU_COLOR);
        
        int testListScrollPanelHeight = testOverviewPanel.getPreferredSize().height +
        		scenarioPanel.getPreferredSize().height +
        		simulationPanel.getPreferredSize().height +
        		timerPanel.getPreferredSize().height +
        		robotInfoPanel1.getPreferredSize().height;
        int testListScrollPanelWidth = testListPanel.getPreferredSize().width + 50;
        testListScrollPane.setPreferredSize(new Dimension(testListScrollPanelWidth, testListScrollPanelHeight));
        testListScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Tests"));
	}

	private void setDesignOfMainWindow() {
		
		// Uses a Grid Bag Layout with the following panel
		// --------------------------------------------------------------
		// |						||	Test Overview	|	Test List	|
		// |						||------------------|				|
		// |						||	Scenarios		|				|
		// |						||------------------|				|
		// |		Simulation		|| 	Speed Config	|				|
		// |						||------------------|				|
		// |						||	Timer			|				|
		// |						||------------------|				|
		// |						||	Info1  |  Info2 |				|
		// --------------------------------------------------------------
		
		//add the different Panels. This is lengthy because lots of GridBagLayout setup takes place here. This gives us a lot of flexibility though
		
		//add the main simulation panel, which gets all of the additional space when the window is resized
		GridBagConstraints simConstraints = new GridBagConstraints();
		simConstraints.gridx = 0;
		simConstraints.gridy = 0;
		simConstraints.fill = GridBagConstraints.BOTH;
		simConstraints.weightx = 1.0;
		simConstraints.weighty = 1.0;
		simConstraints.gridheight = 5;
		add(simView, simConstraints);
		
		//add the gray spacer to the right of the simulation panel
		JPanel spacer = new JPanel();
		spacer.setBackground(Color.DARK_GRAY);
		GridBagConstraints spacerConstraints = new GridBagConstraints();
		spacerConstraints.gridx = 1;
		spacerConstraints.gridy = 0;
		spacerConstraints.gridheight = 5;
		spacerConstraints.fill = GridBagConstraints.VERTICAL;
		add(spacer, spacerConstraints);
		
		GridBagConstraints testOverviewConstraints = new GridBagConstraints();
		testOverviewConstraints.gridx = 2;
		testOverviewConstraints.gridy = 0;
		testOverviewConstraints.gridwidth = 2;
		testOverviewConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(testOverviewPanel, testOverviewConstraints);
		
		GridBagConstraints scenarioConstraints = new GridBagConstraints();
		scenarioConstraints.gridx = 2;
		scenarioConstraints.gridy = 1;
		scenarioConstraints.gridwidth = 2;
		scenarioConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(scenarioPanel, scenarioConstraints);
		
		GridBagConstraints simulationConstraints = new GridBagConstraints();
		simulationConstraints.gridx = 2;
		simulationConstraints.gridy = 2;
		simulationConstraints.gridwidth = 2;
		simulationConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(simulationPanel, simulationConstraints);
		
		GridBagConstraints timerConstraints = new GridBagConstraints();
		timerConstraints.gridx = 2;
		timerConstraints.gridy = 3;
		timerConstraints.gridwidth = 2;
		timerConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(timerPanel, timerConstraints);
		
		GridBagConstraints infoConstraints = new GridBagConstraints();
		infoConstraints.gridx = 2;
		infoConstraints.gridy = 4;
		infoConstraints.fill = GridBagConstraints.HORIZONTAL;
		infoConstraints.anchor = GridBagConstraints.PAGE_START;
		add(robotInfoPanel1, infoConstraints);
		
		GridBagConstraints info2Constraints = new GridBagConstraints();
		info2Constraints.gridx = 3;
		info2Constraints.gridy = 4;
		info2Constraints.fill = GridBagConstraints.HORIZONTAL;
		info2Constraints.anchor = GridBagConstraints.PAGE_START;
		add(robotInfoPanel2, info2Constraints);
		
		GridBagConstraints testListConstraints = new GridBagConstraints();
		testListConstraints.gridx = 4;
		testListConstraints.gridy = 0;
		testListConstraints.gridheight = 5;
		testListConstraints.fill = GridBagConstraints.HORIZONTAL;
		testListConstraints.anchor = GridBagConstraints.PAGE_START;
		add(testListScrollPane, testListConstraints);
		
		//Set up the color and size of the whole window
		getContentPane().setBackground(MAIN_MENU_COLOR);
		setBackground(MAIN_MENU_COLOR);
		setMinimumSize(new Dimension(902, 800));
		setPreferredSize(new Dimension(1212, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // try {
		// 	setIconImage(ImageIO.read(new File(SimulatorConfig.getStringPathToRobotIcon())));
		// } catch (IOException e) {
		// 	System.out.println("hi");
		// 	e.printStackTrace();
		// }
        pack();
        setVisible(true);
	}
    
    private void loadTestFileContent(String fileName) {
    	boolean written = true;
    	TestScenario test;
    	
    	for (int i = 0; i < setting.getScenarioManager().getTests().size(); i++) {
    		test = setting.getScenarioManager().getTests().get(i);
			written = writeLineIfNeeded(test, fileName);
    		if(!written && testPassed(test, fileName)) {
    			testListPanel.onTestCompleted(test);
    		}
    	}
	}

	private boolean testPassed(TestScenario test, String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       String[] parts = line.split("#");
		       if(parts[0].equals(test.getName())) {
		    	   if(parts[1].equals("n")) {
		    		   return false;
		    	   } else if (parts[1].equals("y")) {
		    		   return true;
		    	   }
		       }
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean writeLineIfNeeded(TestScenario test, String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       String[] parts = line.split("#");
		       if(parts[0].equals(test.getName())) {
		    	   return false;
		       }
		    }
		    br.close();
		    BufferedWriter output = new BufferedWriter(new FileWriter(fileName, true));
		    output.append(test.getName() + "#n" + System.lineSeparator());
		    output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void createFileIfNotExist(String fileName) {
    	File file = new File(fileName);
    	try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("Could not write persistence file to file system.");
			e.printStackTrace();
		}
	}

    private void update() {
    	while(System.currentTimeMillis() - lastFrame < SimulatorConfig.getDefaultRefreshInterval()) {
	    	try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
        float delta = System.currentTimeMillis() - lastFrame;
        lastFrame = System.currentTimeMillis();
        
        setting.getSimulation().refresh();
        robotInfoPanel1.onHighlightedChange();
        robotInfoPanel2.onHighlightedChange();
        setting.getScenarioManager().refresh();
        setting.getSimulation().update(delta);

        this.repaint();
    }

	private void close() {
		setting.getRoboterDispatch().close();
        setVisible(false);
        dispose();
        System.exit(0);
    }
    
    //create a new popup with the provided text 
    private Popup createPopup(String message, Color color) {
		JPanel popupPanel = new JPanel(new BorderLayout());
		popupPanel.setPreferredSize(new Dimension(460, 100));
		popupPanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 192));
		String labelText = String.format("<html><div WIDTH=%d text-align: center>%s</div></html>", 350, message);
		JLabel popupLabel = new JLabel(labelText, SwingConstants.CENTER);
		Font original = (Font) UIManager.get("Label.font");
		popupLabel.setFont(original.deriveFont(Font.BOLD, 16));
		popupPanel.add(popupLabel);
		
		PopupFactory pf = PopupFactory.getSharedInstance();
		
		Popup popup = pf.getPopup(this, popupPanel, (int)this.getLocation().getX() + 10, (int)this.getLocation().getY() + 55);
		return popup;
	}
}
