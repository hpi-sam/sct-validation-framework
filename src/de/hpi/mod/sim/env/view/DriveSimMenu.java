package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.model.ScenarioManager;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class DriveSimMenu extends JMenuBar implements ITimeListener {

    private final char[] NUMBER_KEYS = {
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
    };

    private Icon playIcon, pauseIcon;
    private SimulationWorld world;

    private JMenuItem playItem;


    public DriveSimMenu(SimulationWorld world) {
        this.world = world;

        loadIcons();

        JMenu scenariosMenu = new JMenu("Scenarios");
        scenariosMenu.setMnemonic(KeyEvent.VK_S);

        fillScenarioMenu(scenariosMenu);

        JMenu playMenu = new JMenu("Play");
        playItem = new JMenuItem("Play/Pause", playIcon);
        playItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        playItem.addActionListener(e -> world.toggleRunning());
        playMenu.add(playItem);

        JMenu worldMenu = new JMenu("World");
        JMenu zoomMenu = new JMenu("Zoom");
        JMenu moveMenu = new JMenu("Move");
        JMenuItem zoomInItem = new JMenuItem("In");
        JMenuItem zoomOutItem = new JMenuItem("Out");
        JMenuItem zoomResetItem = new JMenuItem("Reset");
        JMenuItem moveLeftItem = new JMenuItem("Left");
        JMenuItem moveRightItem = new JMenuItem("Right");
        JMenuItem moveUpItem = new JMenuItem("Up");
        JMenuItem moveDownItem = new JMenuItem("Down");
        JMenuItem moveResetItem = new JMenuItem("Reset");
        JMenuItem addRobotItem = new JMenuItem("Add Robot");

        addRobotItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));
        zoomInItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK));
        zoomOutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));
        zoomResetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0));
        moveLeftItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        moveRightItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        moveUpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        moveDownItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        moveResetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0));

        addRobotItem.addActionListener(e -> world.addRobot());
        zoomInItem.addActionListener(e -> world.zoomIn(1));
        zoomOutItem.addActionListener(e -> world.zoomOut(1));
        zoomResetItem.addActionListener(e -> world.resetZoom());
        moveLeftItem.addActionListener(e -> world.moveHorizontal(-1));
        moveRightItem.addActionListener(e -> world.moveHorizontal(1));
        moveUpItem.addActionListener(e -> world.moveVertical(1));
        moveDownItem.addActionListener(e -> world.moveVertical(-1));
        moveResetItem.addActionListener(e -> world.resetOffset());

        moveMenu.add(moveLeftItem);
        moveMenu.add(moveRightItem);
        moveMenu.add(moveUpItem);
        moveMenu.add(moveDownItem);
        moveMenu.add(moveResetItem);

        zoomMenu.add(zoomInItem);
        zoomMenu.add(zoomOutItem);
        zoomMenu.add(zoomResetItem);

        worldMenu.add(moveMenu);
        worldMenu.add(zoomMenu);
        worldMenu.addSeparator();
        worldMenu.add(addRobotItem);

        add(worldMenu);
        add(playMenu);
        add(scenariosMenu);

        world.addTimeListener(this);
    }

    private void loadIcons() {
        playIcon = new ImageIcon("res/play.png");
        pauseIcon = new ImageIcon("res/pause.png");
    }

    private void fillScenarioMenu(JMenu menu) {

        for (int i = 0; i < ScenarioManager.scenarios.size(); i++) {
            Scenario scenario = ScenarioManager.scenarios.get(i);

            JMenuItem item = new JMenuItem(scenario.getName());
            item.addActionListener(e -> world.playScenario(scenario));

            if (i < NUMBER_KEYS.length) {
                item.setAccelerator(KeyStroke.getKeyStroke(NUMBER_KEYS[i]));
            }

            menu.add(item);
        }
    }

    @Override
    public void refresh() {
        if (world.isRunning()) playItem.setIcon(pauseIcon);
        else playItem.setIcon(playIcon);
    }
}
