package de.hpi.mod.sim;

import java.lang.reflect.Constructor;
import javax.swing.JOptionPane;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.view.SimulatorFrame;

/**
 * @author Tim Cech, Jonas Kordt, Paul Methfessel, Chiara Schirmer, Simon Wietheger, Christian Zöllner
 *         (System Analysis and Modeling Group @ Hasso-Plattner-Institute, Potsdam University, Germany)
 * @version 0.9.0
 * 
 *          This classes starts the execution of the program.
 */
public class App {

    private static final String[] POSSIBLE_WORLDS = {
            "de.hpi.mod.sim.worlds.flasher.FlashWorld",
    		"de.hpi.mod.sim.worlds.pong.PongWorld",
            "de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse",
            "de.hpi.mod.sim.worlds.traffic_light_robots.TrafficLightWorld",
     };

     private static String selectWorldName() {
    	 // Catch cases where either one one no world is available.
	     if (POSSIBLE_WORLDS.length < 1) {
	    	 return null;
	     }
	     if (POSSIBLE_WORLDS.length == 1) {
	    	 return POSSIBLE_WORLDS[0];
	     }
	     
	     // Get World Name from list
	     String inputResponse = (String) JOptionPane.showInputDialog(null, "Select the world that you want to simulate...", "World Selection",
                    					            				 JOptionPane.QUESTION_MESSAGE, null, 
                    					            				 POSSIBLE_WORLDS, POSSIBLE_WORLDS[0]);
	     return (inputResponse == null || inputResponse.isBlank()) ? null : inputResponse.strip();
     }

     private static World instanciateWorld(String name) {
        try {
            @SuppressWarnings("unchecked")
            Constructor<? extends World> con = (Constructor<? extends World>) Class.forName(name).getConstructors()[0];
            return con.newInstance();
        } catch (Exception e) {
            System.err.println("Found no such subclass of world: " + name);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
    	// Set global system 
        SimulatorFrame.setSystemLookAndFeel();
        
        // Let the User Select a World, exit is nothing is selected
        String worldName = selectWorldName();
        if (worldName == null) System.exit(1);
        
        // Instranciate Simulator World, exit if selected world is invalid
        World world = instanciateWorld(worldName);
        if (world == null) System.exit(1);
        
        // Start Simulator 
	    new SimulatorFrame(world);
    }
}
