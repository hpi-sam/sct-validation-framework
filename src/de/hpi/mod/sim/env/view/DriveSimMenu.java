package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.model.NewRobot;
import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DriveSimMenu extends JMenuBar implements ITimeListener {

    private Icon playIcon, pauseIcon;
    private SimulationWorld world;

    private JMenuItem playItem, zoomInItem, zoomOutItem, zoomResetItem, moveLeftItem,
            moveRightItem, moveUpItem, moveDownItem, moveResetItem, addRobotItem, resetSimItem, keyItem;

    private KeyManager keyManager;


    public DriveSimMenu(SimulationWorld world) {
        this.world = world;

        loadIcons();
        keyManager = new KeyManager();

        JMenu playMenu = new JMenu("Play");
        JMenu optionsMenu = new JMenu("Options");
        JMenu worldMenu = new JMenu("World");
        JMenu zoomMenu = new JMenu("Zoom");
        JMenu moveMenu = new JMenu("Move");

        zoomInItem = new JMenuItem("In");
        zoomOutItem = new JMenuItem("Out");
        zoomResetItem = new JMenuItem("Reset");
        moveLeftItem = new JMenuItem("Left");
        moveRightItem = new JMenuItem("Right");
        moveUpItem = new JMenuItem("Up");
        moveDownItem = new JMenuItem("Down");
        moveResetItem = new JMenuItem("Reset");
        addRobotItem = new JMenuItem("Add Robot");
        playItem = new JMenuItem("Play/Pause", playIcon);
        resetSimItem = new JMenuItem("Reset Simulation");
        keyItem = new JMenuItem("Keystrokes");

        updateKeystrokes();

        Scenario resetSimulationScenario = new Scenario() {
            @Override
            protected List<NewRobot> initializeScenario() {
                return new ArrayList<>();
            }
        };

        addRobotItem.addActionListener(e -> world.addRobot());
        zoomInItem.addActionListener(e -> world.zoomIn(1));
        zoomOutItem.addActionListener(e -> world.zoomOut(1));
        zoomResetItem.addActionListener(e -> world.resetZoom());
        moveLeftItem.addActionListener(e -> world.moveHorizontal(-1));
        moveRightItem.addActionListener(e -> world.moveHorizontal(1));
        moveUpItem.addActionListener(e -> world.moveVertical(1));
        moveDownItem.addActionListener(e -> world.moveVertical(-1));
        moveResetItem.addActionListener(e -> world.resetOffset());
        playItem.addActionListener(e -> world.toggleRunning());
        resetSimItem.addActionListener(e -> world.playScenario(resetSimulationScenario));
        keyItem.addActionListener(e -> openKeyDialog());

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
        worldMenu.add(resetSimItem);

        playMenu.add(playItem);

        optionsMenu.add(keyItem);

        add(worldMenu);
        add(playMenu);
        add(optionsMenu);

        world.addTimeListener(this);
    }

    private void loadIcons() {
        playIcon = new ImageIcon(SimulatorConfig.getStringPathToPlayIcon());
        pauseIcon = new ImageIcon(SimulatorConfig.getStringPathToPauseIcon());
    }

    private void openKeyDialog() {
        new KeyDialog(keyManager);
        updateKeystrokes();
    }

    private void updateKeystrokes() {
        playItem.setAccelerator(keyManager.getKey("Pause/Play"));
        addRobotItem.setAccelerator(keyManager.getKey("Add Robot"));
        zoomInItem.setAccelerator(keyManager.getKey("Zoom In"));
        zoomOutItem.setAccelerator(keyManager.getKey("Zoom Out"));
        zoomResetItem.setAccelerator(keyManager.getKey("Reset Zoom"));
        moveLeftItem.setAccelerator(keyManager.getKey("Move Left"));
        moveRightItem.setAccelerator(keyManager.getKey("Move Right"));
        moveUpItem.setAccelerator(keyManager.getKey("Move Up"));
        moveDownItem.setAccelerator(keyManager.getKey("Move Down"));
        moveResetItem.setAccelerator(keyManager.getKey("Reset Move"));
        resetSimItem.setAccelerator(keyManager.getKey("Reset Simulation"));
    }

    @Override
    public void refresh() {
        if (world.isRunning()) playItem.setIcon(pauseIcon);
        else playItem.setIcon(playIcon);
    }
}
