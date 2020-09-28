package de.hpi.mod.sim;

import java.lang.reflect.Constructor;
import javax.swing.JOptionPane;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.view.SimulatorFrame;

/**
 * 
 * @author Tim Cech, Jonas Kordt, Paul Methfessel, Simon Wietheger
 *         (Hasso-Plattner-Institute chair for software analysis and modeling)
 * @version 1.0.0
 *
 *          This classes starts the execution of the program.
 */
public class App {

    private static final String[] POSSIBLE_WORLDS = {
            "de.hpi.mod.sim.worlds.traffic_light_robots.TrafficLightWorld",
            "de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse",
            "de.hpi.mod.sim.worlds.flasher.FlashWorld"
    		
     };

     private static World selectWorld() {
        String chosenName;
        if (POSSIBLE_WORLDS.length == 1)
            chosenName = POSSIBLE_WORLDS[0];
        else
            chosenName = (String) JOptionPane.showInputDialog(null, "Choose a world ...", "World selection",
                    JOptionPane.QUESTION_MESSAGE, null, POSSIBLE_WORLDS, POSSIBLE_WORLDS[0]);
        try {
            @SuppressWarnings("unchecked")
            Constructor<? extends World> con = (Constructor<? extends World>) Class.forName(chosenName).getConstructors()[0];
            return con.newInstance();
        } catch (Exception e) {
            System.err.println("Found no such subclass of world: " + chosenName);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SimulatorFrame.setSystemLookAndFeel();
        World world = selectWorld();
        new SimulatorFrame(world);
    }
}
