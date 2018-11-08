package de.hpi.mod.sim.env.view;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class ControlPanel extends JPanel {

    private IRobotController control;
    private Icon playIcon, pauseIcon;

    private JButton playButton;


    public ControlPanel(IRobotController control) {
        this.control = control;
        loadIcons();

        playButton = new JButton();
        updateRunning();
        playButton.addActionListener(e -> control.toggleRunning());

        add(playButton);
    }

    public void updateRunning() {
        if (control.isRunning()) playButton.setIcon(pauseIcon);
        else playButton.setIcon(playIcon);
    }

    private void loadIcons() {
        playIcon = new ImageIcon("res/play.png");
        pauseIcon = new ImageIcon("res/pause.png");
    }
}
