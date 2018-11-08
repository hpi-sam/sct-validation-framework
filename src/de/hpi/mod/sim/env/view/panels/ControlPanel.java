package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;

public class ControlPanel extends JPanel implements ITimeListener {

    private SimulationWorld world;
    private Icon playIcon, pauseIcon;

    private JButton playButton;


    public ControlPanel(SimulationWorld world) {
        this.world = world;
        loadIcons();

        playButton = new JButton();
        refresh();
        playButton.addActionListener(e -> world.toggleRunning());

        add(playButton);
    }

    private void loadIcons() {
        playIcon = new ImageIcon("res/play.png");
        pauseIcon = new ImageIcon("res/pause.png");
    }

    @Override
    public void refresh() {
        if (world.isRunning()) playButton.setIcon(pauseIcon);
        else playButton.setIcon(playIcon);
    }
}
