package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.view.panels.ConfigPanel;
import de.hpi.mod.sim.env.view.panels.ControlPanel;
import de.hpi.mod.sim.env.view.panels.RobotInfoPanel;
import de.hpi.mod.sim.env.view.panels.TestPanel;
import de.hpi.mod.sim.env.view.sim.ScenarioManager;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;
import de.hpi.mod.sim.env.view.sim.SimulatorView;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class DriveSimFrame extends JFrame {

    private SimulatorView sim;
    private RobotInfoPanel info;

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
        side.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));

        JPanel northPanel = new JPanel(new GridLayout(0, 1));


        northPanel.add(test);
        northPanel.add(config);

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

    public static void main(String[] args) {
        setSystemLookAndFeel();
        new DriveSimFrame();
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
