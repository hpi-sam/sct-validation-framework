package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.view.KeyManager;
import de.hpi.mod.sim.env.view.model.ITimeListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;

/**
 * Controls the time flow of the simulation.<br>
 * Currently supports:
 * <ul>
 *     <li>Play/Pause</li>
 * </ul>
 *
 * Each control can also be used as a shortcut in {@link KeyManager}.<br>
 * Implements {@link ITimeListener} to get refreshed if other classes change the values
 */
public class ControlPanel extends JPanel implements ITimeListener {

    /**
     * Reference to the world
     */
    private SimulationWorld world;

    /**
     * icons for buttons
     */
    private Icon playIcon, pauseIcon;

    /**
     * Controls the playback of the simulation.
     */
    private JButton playButton;


    /**
     * Initialize the panel and adds the controls
     * @param world the World to be controlled
     */
    public ControlPanel(SimulationWorld world) {
        this.world = world;
        loadIcons();

        playButton = new JButton();
        refresh();  // Refresh to set icon
        playButton.addActionListener(e -> world.toggleRunning());

        add(playButton);
    }

    /**
     * Loads all the icons for the panel
     */
    private void loadIcons() {
        playIcon = new ImageIcon("res/play.png");
        pauseIcon = new ImageIcon("res/pause.png");
    }

    /**
     * Gets called if any value got changed externally
     */
    @Override
    public void refresh() {

        // Set correct icons
        if (world.isRunning()) playButton.setIcon(pauseIcon);
        else playButton.setIcon(playIcon);
    }
}
