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
    private RobotInfoPanel info;
    private RobotInfoPanel info2;
    private ScenarioPanel scenario;
    private TestListPanel testList;
    private TestOverviewPanel testOverview;
    private ConfigPanel config;
    private TimerPanel timer;

    private ScenarioManager scenarioManager;

    private long lastFrame;
    private long lastRefresh;
    private boolean running = true;

	public static Color MENU_ORANGE = new Color(0xfff3e2);
	public static Color MENU_GREEN = new Color(0xdcf3d0);
	public static Color MENU_RED = new Color(0xffe1d0);

    public DriveSimFrame() {
        super("Drive System Simulator");
        setLayout(new GridBagLayout());
        
        initializeSimulationItems();
        initializePanels();
        loadTestFileContent();
        addListeners();
        setDesignOfSubpanels();
        setDesignOfMainWindow();
		
        lastFrame = System.currentTimeMillis();
        lastRefresh = System.currentTimeMillis();
        while (running)
            update();
        close();
    }
    
    public static void make_window() {
        setSystemLookAndFeel();
        new DriveSimFrame();
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
		info = new RobotInfoPanel(world, false);
        info2 = new RobotInfoPanel(world, true);
        config = new ConfigPanel(world);
        testList = new TestListPanel(scenarioManager);
        testOverview = new TestOverviewPanel(scenarioManager, this);
        timer = new TimerPanel(world, this);
        scenario = new ScenarioPanel(scenarioManager);
        setJMenuBar(new DriveSimMenu(world));
	}
    
    private void loadTestFileContent() {
		createFileIfNotExist(SimulatorConfig.getTestFileName());
        loadFile(testList, SimulatorConfig.getTestFileName());
	}
    
    private void addListeners() {
		world.addHighlightedRobotListener(info);
        world.addHighlightedRobotListener2(info2);
        world.addTimeListener(config);
        scenarioManager.addTestListener(testList);
        scenarioManager.addTestListener(testOverview);
	}
    
    private void setDesignOfSubpanels() {
		testOverview.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Tests"));
		testOverview.setBackground(MENU_ORANGE);
        
        scenario.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Scenarios"));
        scenario.setBackground(MENU_ORANGE);
        
        config.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Configuration"));
        config.setBackground(MENU_ORANGE);
        
        timer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Timer"));
        timer.setBackground(MENU_ORANGE);
        
        //info panels work with a different layout than all other panels and need a specified size
        info.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info left clicked robot"));
        info.setMinimumSize(new Dimension(200, 130));
        info.setPreferredSize(new Dimension(200, 130));
        info.setBackground(MENU_GREEN);
        
        info2.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info right clicked robot"));
        info2.setMinimumSize(new Dimension(200, 130));
        info2.setPreferredSize(new Dimension(200, 130));
        info2.setBackground(MENU_RED);
        
        testList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Tests"));
        testList.setBackground(MENU_ORANGE);
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
		
		//add the gray spacer to the right of the simulation panel (TODO: consider moving this into the simulation renderer)
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
		add(testOverview, testOverviewConstraints);
		
		GridBagConstraints scenarioConstraints = new GridBagConstraints();
		scenarioConstraints.gridx = 2;
		scenarioConstraints.gridy = 1;
		scenarioConstraints.gridwidth = 2;
		scenarioConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(scenario, scenarioConstraints);
		
		GridBagConstraints configConstraints = new GridBagConstraints();
		configConstraints.gridx = 2;
		configConstraints.gridy = 2;
		configConstraints.gridwidth = 2;
		configConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(config, configConstraints);
		
		GridBagConstraints timerConstraints = new GridBagConstraints();
		timerConstraints.gridx = 2;
		timerConstraints.gridy = 3;
		timerConstraints.gridwidth = 2;
		timerConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(timer, timerConstraints);
		
		GridBagConstraints infoConstraints = new GridBagConstraints();
		infoConstraints.gridx = 2;
		infoConstraints.gridy = 4;
		infoConstraints.fill = GridBagConstraints.HORIZONTAL;
		infoConstraints.anchor = GridBagConstraints.PAGE_START;
		add(info, infoConstraints);
		
		GridBagConstraints info2Constraints = new GridBagConstraints();
		info2Constraints.gridx = 3;
		info2Constraints.gridy = 4;
		info2Constraints.fill = GridBagConstraints.HORIZONTAL;
		info2Constraints.anchor = GridBagConstraints.PAGE_START;
		add(info2, info2Constraints);
		
		GridBagConstraints testListConstraints = new GridBagConstraints();
		testListConstraints.gridx = 4;
		testListConstraints.gridy = 0;
		testListConstraints.gridheight = 5;
		testListConstraints.fill = GridBagConstraints.HORIZONTAL;
		testListConstraints.anchor = GridBagConstraints.PAGE_START;
		testList.setVisible(false);
		add(testList, testListConstraints);
		
		//Set up the color and size of the whole window
		getContentPane().setBackground(MENU_ORANGE);
		setBackground(MENU_ORANGE);
		setPreferredSize(new Dimension(1000, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
	}
    
    private void loadFile(TestListPanel testPanel, String fileName) {
    	boolean written = true;
    	TestScenario test;
    	
    	for (int i = 0; i < scenarioManager.getTests().size(); i++) {
    		test = scenarioManager.getTests().get(i);
			written = writeLineIfNeeded(test, fileName);
    		if(!written && testPassed(test, fileName)) {
    			testPanel.onTestCompleted(test);
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
    
    public boolean isRunning() {
    	return running;
    }
    
    public ScenarioPanel getScenarioPanel() {
    	return scenario;
    }
    
    public ConfigPanel getConfigPanel() {
    	return config;
    }
    
    public TestListPanel getTestListPanel() {
    	return testList;
    }
    
    public TimerPanel getTimerPanel() {
    	return timer;
    }

    private void update() {
        float delta = System.currentTimeMillis() - lastFrame;
        lastFrame = System.currentTimeMillis();

        if (System.currentTimeMillis() - lastRefresh > sim.getWorld().getSensorRefreshInterval()) {
            lastRefresh = System.currentTimeMillis();
            sim.getWorld().refresh();
            info.onHighlightedRobotChange();
            info2.onHighlightedRobotChange();
            scenarioManager.refresh();
        }

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
    
    public void clearSelections() {
    	testList.clearSelections();
    	scenario.clearSelections();
    }
    
    //create a new popup with the provided text 
    private Popup createPopup(String message) {
		JPanel popupPanel = new JPanel(new BorderLayout());
		popupPanel.setPreferredSize(new Dimension(600, 100));
		popupPanel.setBackground(new Color(MENU_ORANGE.getRed(), MENU_ORANGE.getGreen(), MENU_ORANGE.getBlue(), 192));
		JLabel popupLabel = new JLabel(message, JLabel.CENTER);
		Font original = (Font) UIManager.get("Label.font");
		popupLabel.setFont(original.deriveFont(Font.BOLD, 16));
		popupPanel.add(popupLabel);
		
		PopupFactory pf = PopupFactory.getSharedInstance();
		Popup popup = pf.getPopup(this, popupPanel, 150, 300);
		return popup;
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
		sim.renderExplosion(r1);
	}
	
	public void resetSimulationView() {
		sim.reset();
	}
}
