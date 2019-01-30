package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.view.panels.*;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;
import de.hpi.mod.sim.env.view.sim.SimulatorView;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class DriveSimFrame extends JFrame {

    private SimulatorView sim;
    private RobotInfoPanel info;
    private ScenarioPanel scenario;

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
        var config = new ConfigPanel();
        var control = new ControlPanel(world);
        var test = new TestPanel(scenarioManager);
        var timer = new TimerPanel();
        scenario = new ScenarioPanel(scenarioManager, timer);

        TimerPanel.setParent(this);
        setJMenuBar(new DriveSimMenu(world));

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
}
