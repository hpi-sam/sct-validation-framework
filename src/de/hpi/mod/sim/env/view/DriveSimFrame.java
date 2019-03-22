package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.panels.*;
import de.hpi.mod.sim.env.view.sim.CollisionDetector;
import de.hpi.mod.sim.env.view.sim.DeadlockDetector;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;
import de.hpi.mod.sim.env.view.sim.SimulatorView;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DriveSimFrame extends JFrame {

	private SimulationWorld world;
    private SimulatorView sim;
    private DeadlockDetector deadlockDetector;
    private CollisionDetector collisionDetector;
    private RobotInfoPanel robotInfoPanel1;
    private RobotInfoPanel robotInfoPanel2;
    private ScenarioPanel scenarioPanel;
    private TestListPanel testListPanel;
    private TestOverviewPanel testOverviewPanel;
    private ConfigPanel configPanel;
    private TimerPanel timerPanel;

    private ScenarioManager scenarioManager;

    private long lastFrame;
    private boolean running = true;

	public static Color MAIN_MENU_COLOR = new Color(0xfff3e2);
	public static Color MENU_GREEN = new Color(0xdcf3d0);
	public static Color MENU_RED = new Color(0xffe1d0);

    public DriveSimFrame() {
        super("Drive System Simulator");
        setLayout(new GridBagLayout());
        
        createFileIfNotExist(SimulatorConfig.getTestFileName());
        initializeSimulationItems();
        initializePanels();
        loadTestFileContent(SimulatorConfig.getTestFileName());
        addListeners();
        setDesignOfSubpanels();
        setDesignOfMainWindow();
		
        lastFrame = System.currentTimeMillis();
        while (running)
            update();
        close();
    }
    
    public static void make_window() {
        setSystemLookAndFeel();
        new DriveSimFrame();
    }
    
    public void displayMessage(String message) {
		Popup popup = createPopup(message);
		popup.show();
		
		Thread popupHider = new Thread() {
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				popup.hide();
			}
		};
		popupHider.start();
	}

	public void reportCollision(Robot r1, Robot r2) {
		displayMessage("Collision detected!");
		forbidFurhterRunning();
		sim.renderExplosion(r1);
	}
	
	public void forbidFurhterRunning() {
		world.setRunForbidden(true);
	}
	
	public void allowRunning() {
		world.setRunForbidden(false);
	}

	public void clearSelections() {
    	testListPanel.clearSelections();
    	scenarioPanel.clearSelections();
    }
	
	public void resetSimulationView() {
		sim.reset();
	}
    
    public boolean isRunning() {
    	return running;
    }
    
    public ScenarioPanel getScenarioPanel() {
    	return scenarioPanel;
    }
    
    public ConfigPanel getConfigPanel() {
    	return configPanel;
    }
    
    public TestListPanel getTestListPanel() {
    	return testListPanel;
    }
    
    public TimerPanel getTimerPanel() {
    	return timerPanel;
    }
    
    private static void setSystemLookAndFeel() {
    	try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.put("Button.background", Color.white);
			UIManager.put("TextField.background", Color.white);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.out.println("The look and feel could not be loaded. The Application will work fine but look different.");
		}
    }
    
    private void initializeSimulationItems() {
		sim = new SimulatorView();
        world = sim.getWorld();
        scenarioManager = new ScenarioManager(world, collisionDetector, this);
        deadlockDetector = new DeadlockDetector(world, scenarioManager, this);
        collisionDetector = new CollisionDetector(scenarioManager, world, this);
        scenarioManager.setDeadlockDetector(deadlockDetector);
        scenarioManager.setCollisionDetector(collisionDetector);
	}
    
    private void initializePanels() {
		robotInfoPanel1 = new RobotInfoPanel(world, false);
        robotInfoPanel2 = new RobotInfoPanel(world, true);
        configPanel = new ConfigPanel(world);
        testListPanel = new TestListPanel(scenarioManager);
        testOverviewPanel = new TestOverviewPanel(scenarioManager, this);
        timerPanel = new TimerPanel(world, this);
        scenarioPanel = new ScenarioPanel(scenarioManager);
        setJMenuBar(new DriveSimMenu(world));
	}
    
    private void addListeners() {
		world.addHighlightedRobotListener(robotInfoPanel1);
        world.addHighlightedRobotListener2(robotInfoPanel2);
        world.addTimeListener(configPanel);
        scenarioManager.addTestListener(testListPanel);
        scenarioManager.addTestListener(testOverviewPanel);
	}
    
    private void setDesignOfSubpanels() {
		testOverviewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Tests"));
		testOverviewPanel.setBackground(MAIN_MENU_COLOR);
        
        scenarioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Scenarios"));
        scenarioPanel.setBackground(MAIN_MENU_COLOR);
        
        configPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Configuration"));
        configPanel.setBackground(MAIN_MENU_COLOR);
        
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
        
        testListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Tests"));
        testListPanel.setBackground(MAIN_MENU_COLOR);
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
		add(sim, simConstraints);
		
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
		
		GridBagConstraints configConstraints = new GridBagConstraints();
		configConstraints.gridx = 2;
		configConstraints.gridy = 2;
		configConstraints.gridwidth = 2;
		configConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(configPanel, configConstraints);
		
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
		testListPanel.setVisible(false);
		add(testListPanel, testListConstraints);
		
		//Set up the color and size of the whole window
		getContentPane().setBackground(MAIN_MENU_COLOR);
		setBackground(MAIN_MENU_COLOR);
		setPreferredSize(new Dimension(1000, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
	}
    
    private void loadTestFileContent(String fileName) {
    	boolean written = true;
    	TestScenario test;
    	
    	for (int i = 0; i < scenarioManager.getTests().size(); i++) {
    		test = scenarioManager.getTests().get(i);
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
        
        sim.getWorld().refresh();
        robotInfoPanel1.onHighlightedRobotChange();
        robotInfoPanel2.onHighlightedRobotChange();
        scenarioManager.refresh();
        deadlockDetector.update();
        collisionDetector.update();
        sim.getWorld().update(delta);

        this.repaint();
    }

    private void close() {
        sim.getWorld().dispose();
        setVisible(false);
        dispose();
        System.exit(0);
    }
    
    //create a new popup with the provided text 
    private Popup createPopup(String message) {
		JPanel popupPanel = new JPanel(new BorderLayout());
		popupPanel.setPreferredSize(new Dimension(600, 100));
		popupPanel.setBackground(new Color(MAIN_MENU_COLOR.getRed(), MAIN_MENU_COLOR.getGreen(), MAIN_MENU_COLOR.getBlue(), 192));
		JLabel popupLabel = new JLabel(message, SwingConstants.CENTER);
		Font original = (Font) UIManager.get("Label.font");
		popupLabel.setFont(original.deriveFont(Font.BOLD, 16));
		popupPanel.add(popupLabel);
		
		PopupFactory pf = PopupFactory.getSharedInstance();
		
		Popup popup = pf.getPopup(this, popupPanel, (int)this.getLocation().getX()+this.getWidth()/2-300, 
													(int)this.getLocation().getY()+this.getHeight()/2-50);
		return popup;
	}
}
