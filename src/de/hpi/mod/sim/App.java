package de.hpi.mod.sim;

import javax.swing.JOptionPane;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.worlds.flasher.FlashWorld;
import de.hpi.mod.sim.worlds.pong.PongWorld;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorld;
import de.hpi.mod.sim.worlds.infinitewarehouse.InfiniteWarehouse;
import de.hpi.mod.sim.core.view.SimulatorFrame;

/**
 * @author Christian Zöllner, Tim Cech, Jonas Kordt, Paul Methfessel, Chiara Schirmer, Simon Wietheger
 *         (System Analysis and Modeling Group @ Hasso-Plattner-Institute, Potsdam University, Germany)
 * @version 0.9.0
 * 
 *          This classes starts the execution of the program.
 */
public class App {

	// List of instances of all available world classes to be used for selection.  
	private static final World[] POSSIBLE_WORLDS = {
			new InfiniteWarehouse(),
			new SimpleTrafficWorld(),
			new FlashWorld(),
			new PongWorld(),
	};

     private static World selectWorld() {
    	 // Catch cases where either one one no world is available.
	     if (POSSIBLE_WORLDS.length < 1) {
	    	 return null;
	     }
	     if (POSSIBLE_WORLDS.length == 1) {
	    	 return POSSIBLE_WORLDS[0];
	     }
	     
	     // Get World Name from list
	     return (World) JOptionPane.showInputDialog(null, "Select a world to simulate...", "World Selection",
                    					            JOptionPane.QUESTION_MESSAGE, null, 
                    					            POSSIBLE_WORLDS, POSSIBLE_WORLDS[0]);
     }

    public static void main(String[] args) {
    	// Set global system 
        SimulatorFrame.setSystemLookAndFeel();
        
        // Instantiate Simulator World, exit if selected world is invalid
        World world = selectWorld();
        if (world == null) System.exit(1);
        
        // Start Simulator 
	    new SimulatorFrame(world);
    }
}
