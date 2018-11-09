package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.view.model.Scenario;
import de.hpi.mod.sim.env.view.model.ScenarioManager;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class DriveSimMenu extends JMenuBar {

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

    private SimulationWorld world;


    public DriveSimMenu(SimulationWorld world) {
        this.world = world;

        JMenu scenariosMenu = new JMenu("Scenarios");
        scenariosMenu.setMnemonic(KeyEvent.VK_S);

        fillScenarioMenu(scenariosMenu);

        add(scenariosMenu);
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
}
