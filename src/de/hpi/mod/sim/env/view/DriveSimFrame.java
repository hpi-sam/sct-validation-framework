package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.model.TestScenario;
import de.hpi.mod.sim.env.view.panels.*;
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

    private SimulatorView sim;
    private RobotInfoPanel info;
    private RobotInfoPanel info2;
    private ScenarioPanel scenario;
    private TestPanel test;
    private ConfigPanel config;

    private ScenarioManager scenarioManager;

    private long lastFrame;
    private long lastRefresh;
    private boolean running = true;
	// private ControlPanel control;
	private TimerPanel timer;
	private JPanel side;
	private SimulationWorld world;
	private static JPanel messagePanel;
	private static long messageTime = System.currentTimeMillis();
	
	


    public DriveSimFrame() {
        super("Drive System Simulator");
        setLayout(new BorderLayout());

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

	private void setDesignOfMainWindow() {
		JPanel northPanel = new JPanel((new GridLayout(0,1)));
		addPanelsToNorthPanel(northPanel);
        addPanelsToSidePanel(northPanel);
        
        messagePanel = new JPanel();
        //text.setOpaque(false);
        JLabel textField = new JLabel("");
        messagePanel.add(textField);
        messagePanel.setBackground(Color.WHITE);
        messagePanel.setVisible(false);
        add(messagePanel, BorderLayout.NORTH);
        
        add(sim, BorderLayout.CENTER);
        add(side, BorderLayout.EAST);
        setPreferredSize(new Dimension(800, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
	}

	private void loadTestFileContent() {
		createFileIfNotExist(SimulatorConfig.getTestFileName());
        loadFile(test, SimulatorConfig.getTestFileName());
	}

	private void initializeSimulationItems() {
		sim = new SimulatorView();
        world = sim.getWorld();
        scenarioManager = new ScenarioManager(world);
	}

	private void addPanelsToSidePanel(JPanel northPanel) {
		side.add(northPanel, BorderLayout.NORTH);
		// side.add(control, BorderLayout.SOUTH);
        side.add(info, BorderLayout.WEST);
        side.add(info2, BorderLayout.EAST);
	}

	private void addPanelsToNorthPanel(JPanel northPanel) {
		northPanel.add(test);
        northPanel.add(scenario);
        northPanel.add(config);
        northPanel.add(timer);
	}

	private void setDesignOfSubpanels() {
		side.setLayout(new BorderLayout());
        info.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info left clicked robot"));
        info2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info right clicked robot"));
        config.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Configuration"));
        config.setPreferredSize(new Dimension(10,10));
        test.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Tests"));
        scenario.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Scenarios"));
        timer.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Timer"));
        side.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
	}

	private void addListeners() {
		world.addHighlightedRobotListener(info);
        world.addHighlightedRobotListener2(info2);
        world.addTimeListener(config);
        scenarioManager.addTestListener(test);
	}

	private void initializePanels() {
		side = new JPanel();
		info = new RobotInfoPanel(world, false);
        info2 = new RobotInfoPanel(world, true);
        config = new ConfigPanel(world);
        // control = new ControlPanel(world);
        test = new TestPanel(scenarioManager);
        timer = new TimerPanel();
        scenario = new ScenarioPanel(world, scenarioManager, timer);
        TimerPanel.setParent(this);
        setJMenuBar(new DriveSimMenu(world));
	}
    
    private void loadFile(TestPanel testPanel, String fileName) {
    	boolean written = true;
    	TestScenario test;
    	
    	for (int i=0; i<scenarioManager.getTests().size(); i++) {
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

	public static void make_window() {
        setSystemLookAndFeel();
        new DriveSimFrame();
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

    private void update() {
    	if(messageTime + 5000 <= System.currentTimeMillis()) {
    		messagePanel.setVisible(false);
    	}
    	
        float delta = System.currentTimeMillis() - lastFrame;
        lastFrame = System.currentTimeMillis();

        if (System.currentTimeMillis() - lastRefresh > sim.getWorld().getSensorRefreshInterval()) {
            lastRefresh = System.currentTimeMillis();
            sim.getWorld().refresh();
            info.onHighlightedRobotChange();
            info2.onHighlightedRobotChange();
            scenarioManager.refresh();
        }

        sim.getWorld().update(delta);

        this.repaint();
    }

    private void close() {
        sim.getWorld().dispose();
        setVisible(false);
        dispose();
        System.exit(0);
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            if (UIManager.getSystemLookAndFeelClassName().equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) {
                Font original = (Font) UIManager.get("MenuItem.acceleratorFont");
                UIManager.put("MenuItem.acceleratorFont", original.deriveFont(Font.PLAIN, 10));
                UIManager.put("MenuItem.acceleratorForeground", new Color(100, 150, 255));
            }
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, "Could not switch to System Look-And-Feel",
                    "UI Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void resetBorders() {
    	TestPanel.resetAllBorders();
    	ScenarioPanel.resetAllBorders();
    }

	public static void displayMessage(String string) {
		if(messagePanel != null) {
			for (Component jc : messagePanel.getComponents()) {
			    if ( jc instanceof JLabel) {
			        ((JLabel) jc).setText(string);
			    }
			}
			messagePanel.setVisible(true);
			messageTime  = System.currentTimeMillis();
		}
	}
}
