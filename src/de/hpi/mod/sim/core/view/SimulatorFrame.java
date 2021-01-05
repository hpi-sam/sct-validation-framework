package de.hpi.mod.sim.core.view;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.SimulationRunner;
import de.hpi.mod.sim.core.view.panels.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SimulatorFrame extends JFrame {

	private static final long serialVersionUID = 4683030810403226266L;
	private World world;
	private AnimationPanel animationPanel;
	private EntityInfoPanel entityInfoPanel1;
	private EntityInfoPanel entityInfoPanel2;
	private ScenarioPanel scenarioPanel;
	private TestListPanel testListPanel;
	private JScrollPane testListScrollPane;
	private TestOverviewPanel testOverviewPanel;
	private SimulationControlPanel simulationControlPanel;
	private TimerPanel timerPanel;

	private long lastFrame;
	private boolean running = true;

	public static Color MAIN_MENU_COLOR = new Color(0xfff3e2);
	public static Color MENU_GREEN = new Color(0xdcf3d0);
	public static Color MENU_RED = new Color(0xffe1d0);
	public static Color MENU_WHITE = Color.WHITE;

	public SimulatorFrame(World world) {
		super("Statechart Simulator");
		this.world = world;
		animationPanel = world.createAnimationPanel();
		SimulationRunner simulationRunner = new SimulationRunner(world, animationPanel);
		world.initialize(this, simulationRunner);

		setLayout(new GridBagLayout());

		createFileIfNotExist(Configuration.getTestFileName());
		initializePanels(animationPanel);
		loadTestFileContent(Configuration.getTestFileName());
		addListeners(animationPanel);
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
					Thread.sleep(Configuration.getMessageDisplayTime());
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

	public void resetAnimationPanel() {
		world.resetAnimationPanel();
	}

	public boolean isRunning() {
		return running;
	}

	public ScenarioPanel getScenarioPanel() {
		return scenarioPanel;
	}

	public SimulationControlPanel getSimulationControlPanel() {
		return simulationControlPanel;
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
    
    private void initializePanels(AnimationPanel animationPanel) {
		entityInfoPanel1 = new EntityInfoPanel(animationPanel, false);
        entityInfoPanel2 = new EntityInfoPanel(animationPanel, true);
        simulationControlPanel = new SimulationControlPanel(world.getSimulationRunner(), world.getScenarioManager());
        testListPanel = new TestListPanel(world.getScenarioManager());
        testListScrollPane = new JScrollPane(testListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        testOverviewPanel = new TestOverviewPanel(world.getScenarioManager(), this);
		timerPanel = new TimerPanel(world.getSimulationRunner());
        scenarioPanel = new ScenarioPanel(world.getScenarioManager());
        setJMenuBar(new DriveSimMenuBar(world.getSimulationRunner(), animationPanel));
	}
    
    private void addListeners(AnimationPanel animationPanel) {
		animationPanel.addHighlightedListener(entityInfoPanel1);
        animationPanel.addHighlightedListener(entityInfoPanel2);
        world.getSimulationRunner().addTimeListener(simulationControlPanel);
        world.getScenarioManager().addTestScenarioListener(testListPanel);
        world.getScenarioManager().addTestScenarioListener(testOverviewPanel);
	}
    
    private void setDesignOfSubpanels() {
		testOverviewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Tests"));
		testOverviewPanel.setBackground(MAIN_MENU_COLOR);
        
        scenarioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Scenarios"));
        scenarioPanel.setBackground(MAIN_MENU_COLOR);
        
        simulationControlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Simulation"));
        simulationControlPanel.setBackground(MAIN_MENU_COLOR);
        
        timerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Timer"));
        timerPanel.setBackground(MAIN_MENU_COLOR);
        
        //info panels work with a different layout than all other panels and need a specified size
        entityInfoPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info left clicked entity"));
        entityInfoPanel1.setMinimumSize(new Dimension(200, 200));
        entityInfoPanel1.setPreferredSize(new Dimension(200, 200));
        entityInfoPanel1.setBackground(MENU_GREEN);
        
        entityInfoPanel2.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info right clicked entity"));
        entityInfoPanel2.setMinimumSize(new Dimension(200, 200));
        entityInfoPanel2.setPreferredSize(new Dimension(200, 200));
        entityInfoPanel2.setBackground(MENU_RED);
        
        testListPanel.setBackground(MAIN_MENU_COLOR);
        
        int testListScrollPanelHeight = testOverviewPanel.getPreferredSize().height +
        		scenarioPanel.getPreferredSize().height +
        		simulationControlPanel.getPreferredSize().height +
        		timerPanel.getPreferredSize().height +
        		entityInfoPanel1.getPreferredSize().height;
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
		// |		Animation		|| 	Speed Config	|				|
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
		add(animationPanel, simConstraints);
		
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
		add(simulationControlPanel, simulationConstraints);
		
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
		add(entityInfoPanel1, infoConstraints);
		
		GridBagConstraints info2Constraints = new GridBagConstraints();
		info2Constraints.gridx = 3;
		info2Constraints.gridy = 4;
		info2Constraints.fill = GridBagConstraints.HORIZONTAL;
		info2Constraints.anchor = GridBagConstraints.PAGE_START;
		add(entityInfoPanel2, info2Constraints);
		
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
		
        pack();
        setVisible(true);
	}
    
    private void loadTestFileContent(String fileName) {
    	boolean written = true;
    	TestScenario test;
    	
    	for (int i = 0; i < world.getScenarioManager().getTests().size(); i++) {
    		test = world.getScenarioManager().getTests().get(i);
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
		while (System.currentTimeMillis() - lastFrame < Configuration.getDefaultRefreshInterval()) {
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        float delta = System.currentTimeMillis() - lastFrame;
        lastFrame = System.currentTimeMillis();
        
		world.getSimulationRunner().refresh();
		entityInfoPanel1.onValueUpdate();
		entityInfoPanel2.onValueUpdate();
		world.getScenarioManager().refresh();
		world.getSimulationRunner().update(delta);

        this.repaint();
    }

	private void close() {
		world.close();
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

		Popup popup = pf.getPopup(this, popupPanel, (int) this.getLocation().getX() + 10,
				(int) this.getLocation().getY() + 55);
		return popup;
	}
	
	public static <C extends Component> C setComponentDesign(int width, int height, Color color, C component) {
		// By default, JLabels are not opaque so the background color is not displayed
		if (component instanceof JLabel)
			((JLabel) component).setOpaque(true);

		component.setBackground(color);

		// set both sizes because some Layout Managers look at different size attributes
		component.setMinimumSize(new Dimension(width, height));
		component.setPreferredSize(new Dimension(width, height));
		return component;
	}
}
