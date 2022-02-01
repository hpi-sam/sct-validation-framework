package de.hpi.mod.sim.worlds.simpletraffic.entities;

import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;

public class TrafficLightWithStaticColors extends TrafficLight {
	
	    /**
	     * Creates a traffic light without actually starting a statemachine
	     */
	    public TrafficLightWithStaticColors(RelativePosition relative, Position absolute, boolean northGreen, boolean eastGreen, boolean southGreen, boolean westGreen) {
	    	super(relative, absolute);
	    	this.setLightStateNorth(northGreen);
	    	this.setLightStateEast(eastGreen);
	    	this.setLightStateSouth(southGreen);
	    	this.setLightStateWest(westGreen);
	    }
	    
	    public TrafficLightWithStaticColors(RelativePosition relative, Position absolute, boolean allGreen) {
	    	this(relative, absolute, allGreen, allGreen, allGreen, allGreen);
	    }
	    

	    @Override
	    public void update() {
	        /**
	         * Do nothing
	         */
	    }

	}
