package de.hpi.mod.sim.core.view;

import javax.swing.*;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.SimulationRunner;
import de.hpi.mod.sim.core.view.model.ITimeListener;
import de.hpi.mod.sim.core.view.panels.AnimationPanel;

import java.util.ArrayList;
import java.util.List;

public class DriveSimMenuBar extends JMenuBar implements ITimeListener {

	private static final long serialVersionUID = -1260633775659938837L;
	private Icon playIcon, pauseIcon;
    private SimulationRunner simulationRunner;

    private JMenuItem playItem, zoomInItem, zoomOutItem, zoomResetItem, moveLeftItem,
            moveRightItem, moveUpItem, moveDownItem, moveResetItem, resetSimItem, keyItem;

    private KeyManager keyManager;


    public DriveSimMenuBar(SimulationRunner simulationRunner, AnimationPanel animationPanel) {
        this.simulationRunner = simulationRunner;

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
        playItem = new JMenuItem("Play/Pause", playIcon);
        resetSimItem = new JMenuItem("Reset Simulation");
        keyItem = new JMenuItem("Keystrokes");

        updateKeystrokes();

        Scenario resetSimulationScenario = new Scenario() {
            @Override
            protected List<EntitySpecification<? extends Entity>> initializeScenario() {
                return new ArrayList<>();
            }
        };

        zoomInItem.addActionListener(e -> animationPanel.zoomIn(1));
        zoomOutItem.addActionListener(e -> animationPanel.zoomOut(1));
        zoomResetItem.addActionListener(e -> animationPanel.resetZoom());
        moveLeftItem.addActionListener(e -> animationPanel.moveHorizontal(-1));
        moveRightItem.addActionListener(e -> animationPanel.moveHorizontal(1));
        moveUpItem.addActionListener(e -> animationPanel.moveVertical(1));
        moveDownItem.addActionListener(e -> animationPanel.moveVertical(-1));
        moveResetItem.addActionListener(e -> animationPanel.resetOffset());
        playItem.addActionListener(e -> simulationRunner.toggleRunning());
        resetSimItem.addActionListener(e -> simulationRunner.playScenario(resetSimulationScenario));
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
        worldMenu.add(resetSimItem);

        playMenu.add(playItem);

        optionsMenu.add(keyItem);

        add(worldMenu);
        add(playMenu);
        add(optionsMenu);

        simulationRunner.addTimeListener(this);
    }

    private void loadIcons() {
        playIcon = new ImageIcon(Configuration.getStringPathToPlayIcon());
        pauseIcon = new ImageIcon(Configuration.getStringPathToPauseIcon());
    }

    private void openKeyDialog() {
        new KeyDialog(keyManager);
        updateKeystrokes();
    }

    private void updateKeystrokes() {
        playItem.setAccelerator(keyManager.getKey("Pause/Play"));
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
        if (simulationRunner.isRunning()) playItem.setIcon(pauseIcon);
        else playItem.setIcon(playIcon);
    }
}
