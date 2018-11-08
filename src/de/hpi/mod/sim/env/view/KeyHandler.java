package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.view.model.IWindow;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class KeyHandler {

    public static void registerKeyEventsOn(SimulationWorld sim, IWindow win, JPanel panel) {
        panel.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK), "zoom in");
        panel.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK), "zoom out");
        panel.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_LEFT, 0), "move left");
        panel.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_RIGHT, 0), "move right");
        panel.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_UP, 0), "move up");
        panel.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_DOWN, 0), "move down");
        panel.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, 0), "add robot");
        panel.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_SPACE, 0), "toggle running");
        panel.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE, 0), "exit");

        panel.getActionMap().put("zoom in", addAction(e -> sim.zoomIn()));
        panel.getActionMap().put("zoom out", addAction(e -> sim.zoomOut()));
        panel.getActionMap().put("move left", addAction(e -> sim.moveHorizontal(-1)));
        panel.getActionMap().put("move right", addAction(e -> sim.moveHorizontal(1)));
        panel.getActionMap().put("move up", addAction(e -> sim.moveVertical(1)));
        panel.getActionMap().put("move down", addAction(e -> sim.moveVertical(-1)));
        panel.getActionMap().put("add robot", addAction(e -> sim.addRobot()));
        panel.getActionMap().put("toggle running", addAction(e -> sim.toggleRunning()));
        panel.getActionMap().put("exit", addAction(e -> win.exit()));
    }

    private static AbstractAction addAction(ActionTemplate template) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                template.action(e);
            }
        };
    }

    private interface ActionTemplate {
        void action(ActionEvent e);
    }
}
