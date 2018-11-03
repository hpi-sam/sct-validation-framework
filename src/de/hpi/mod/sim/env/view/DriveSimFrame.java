package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.robot.Robot;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class DriveSimFrame extends JFrame implements IInspector {

    private SimulatorView sim;
    private RobotInfoView info;
    private ConfigPanel config;

    private long lastFrame;
    private long lastRefresh;
    private boolean running = true;


    public DriveSimFrame() {
        super("Drive System Simulator");
        setLayout(new BorderLayout());

        JPanel side = new JPanel();
        side.setLayout(new BorderLayout());

        sim = new SimulatorView(this);
        info = new RobotInfoView();
        config = new ConfigPanel();

        info.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info"));
        config.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Configuration"));
        side.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));

        side.add(config, BorderLayout.NORTH);
        side.add(info, BorderLayout.CENTER);

        add(sim, BorderLayout.CENTER);
        add(side, BorderLayout.EAST);

        sim.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK), "zoom in");
        sim.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK), "zoom out");
        sim.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_LEFT, 0), "move left");
        sim.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_RIGHT, 0), "move right");
        sim.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_UP, 0), "move up");
        sim.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_DOWN, 0), "move down");
        sim.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, 0), "add robot");
        sim.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE, 0), "exit");

        sim.getActionMap().put("zoom in", addAction(e -> sim.zoomIn()));
        sim.getActionMap().put("zoom out", addAction(e -> sim.zoomOut()));
        sim.getActionMap().put("move left", addAction(e -> sim.moveHorizontal(-1)));
        sim.getActionMap().put("move right", addAction(e -> sim.moveHorizontal(1)));
        sim.getActionMap().put("move up", addAction(e -> sim.moveVertical(1)));
        sim.getActionMap().put("move down", addAction(e -> sim.moveVertical(-1)));
        sim.getActionMap().put("add robot", addAction(e -> sim.addRobot()));
        sim.getActionMap().put("exit", addAction(e -> running = false));

        setSystemLookAndFeel();

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

        if (System.currentTimeMillis() - lastRefresh > sim.getSensorRefreshInterval()) {
            lastRefresh = System.currentTimeMillis();
            sim.refresh();
            info.refresh();
        }

        sim.update(delta);

        this.repaint();
    }

    private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, "Could not switch to System Look-And-Feel",
                    "UI Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void close() {
        sim.close();
        setVisible(false);
        dispose();
        System.exit(0);
    }

    private AbstractAction addAction(ActionTemplate template) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                template.action(e);
            }
        };
    }

    @Override
    public void showInfo(Robot r) {
        info.setRobot(r);
    }

    private interface ActionTemplate {
        void action(ActionEvent e);
    }

    public static void main(String[] args) {
        new DriveSimFrame();
    }
}
