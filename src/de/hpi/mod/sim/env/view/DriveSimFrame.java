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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class DriveSimFrame extends JFrame {

    private SimulatorView sim;
    private RobotInfoPanel info;
    private ScenarioPanel scenario;
    private TestPanel test;
    private ConfigPanel config;

    private ScenarioManager scenarioManager;

    private long lastFrame;
    private long lastRefresh;
    private boolean running = true;


    public DriveSimFrame() {
        super("Drive System Simulator");
        setLayout(new BorderLayout());

        JPanel side = new JPanel();
        side.setLayout(new BorderLayout());

        sim = new SimulatorView();
        SimulationWorld world = sim.getWorld();

        scenarioManager = new ScenarioManager(world);

        info = new RobotInfoPanel(world);
        config = new ConfigPanel();
        var control = new ControlPanel(world);
        test = new TestPanel(scenarioManager);
        var timer = new TimerPanel();
        scenario = new ScenarioPanel(scenarioManager, timer);

        TimerPanel.setParent(this);
        setJMenuBar(new DriveSimMenu(world));
        
        createFileIfNotExist(SimulatorConfig.getTestFileName());
        loadFile(test, SimulatorConfig.getTestFileName());

        world.addHighlightedRobotListener(info);
        world.addTimeListener(control);
        scenarioManager.addTestListener(test);

        info.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info"));
        config.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Configuration"));
        test.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Tests"));
        scenario.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Scenarios"));
        timer.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Timer"));
        side.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));

        JPanel northPanel = new JPanel(new GridLayout(0, 1));


        northPanel.add(test);
        northPanel.add(scenario);
        northPanel.add(config);
        northPanel.add(timer);

        side.add(northPanel, BorderLayout.NORTH);
        side.add(info, BorderLayout.CENTER);
        side.add(control, BorderLayout.SOUTH);

        add(sim, BorderLayout.CENTER);
        add(side, BorderLayout.EAST);

        setPreferredSize(new Dimension(800, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        lastFrame = System.currentTimeMillis();
        lastRefresh = System.currentTimeMillis();
        while (running)
            update();
        close();
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
        float delta = System.currentTimeMillis() - lastFrame;
        lastFrame = System.currentTimeMillis();

        if (System.currentTimeMillis() - lastRefresh > sim.getWorld().getSensorRefreshInterval()) {
            lastRefresh = System.currentTimeMillis();
            sim.getWorld().refresh();
            info.onHighlightedRobotChange();
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
}
