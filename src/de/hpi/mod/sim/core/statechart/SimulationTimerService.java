package de.hpi.mod.sim.core.statechart;

import java.util.ArrayList;
import java.util.List;

import com.yakindu.core.ITimerService;
import com.yakindu.core.ITimed;
import de.hpi.mod.sim.core.Configuration;

/**
 * Can be used to (un)set timers considering the simulation time
 */
public class SimulationTimerService implements ITimerService {

	// Timers in real-world system time
	private long lastUpdateSystemTime;
	
	private List<SimulationTimer> timerList = new ArrayList<SimulationTimer>();
    
    private class SimulationTimer{
    	
    	// Event data
		private ITimed callback;
		private int eventID;
		private boolean repeat;
		private boolean cancelled = false;
		
		// Timers in simulation time
		private long totalSimulationTime;
		private long simulationTimeRemaining;
    	
    	public SimulationTimer(final ITimed callback, final int eventId, long time, boolean repeat) {
    		
    		// Set event information
    		this.callback = callback; 
    		this.eventID = eventId;   
    		this.repeat = repeat; 		
        	
    		// Set remaining simulation time 
        	this.totalSimulationTime = time;
        	this.simulationTimeRemaining = time;
		}
    	

    	public SimulationTimer(final ITimed callback, final int eventId) {
    		this.callback = callback; 
    		this.eventID = eventId;  
		}
    	
    	public void reduceTime(long simulationTimeSinceLastUpdate) {
    		this.simulationTimeRemaining -= simulationTimeSinceLastUpdate;    		
    	}
    	
    	public boolean timeElapsed() {
    		return this.simulationTimeRemaining <= 0;
    	}
    	
    	public boolean isPeriodic() {
    		return this.repeat;
    	}
    	
    	public void restart() {
    		this.simulationTimeRemaining = this.totalSimulationTime;
    	}
    	
    	public void run() {
    		this.callback.raiseTimeEvent(this.eventID);
    	}
    	
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof SimulationTimer) {
				return ((SimulationTimer) obj).callback.equals(callback)
						&& ((SimulationTimer) obj).eventID == eventID;
			}
			return super.equals(obj);
		}


		public void cancel() {
			this.cancelled = true; 
		}


		public boolean isCancelled() {
			return this.cancelled; 
		}
    	
    	
  
    }

	public SimulationTimerService() {
		// Init system timers with current system time
		this.lastUpdateSystemTime = System.currentTimeMillis();
	}
	
	@Override
	public void setTimer(final ITimed callback, final int eventID, long time, boolean isPeriodic) {
		
		timerList.add(new SimulationTimer(callback, eventID, time, isPeriodic));
		
		// long dynamicTime = (long) (time / SimulatorConfig.getRobotSpeedFactor());
		// super.setTimer(callback, eventID, dynamicTime, isPeriodic);
	}

	@Override
	public void unsetTimer(ITimed callback, int eventID) {
		int index = this.timerList.indexOf(new SimulationTimer(callback, eventID));
		if (index != -1) {
			this.timerList.get(index).cancel();
			this.timerList.remove(index);
		}		
	}
	
	public void update() {
		
		// Calculate time passed since last update
		long now = System.currentTimeMillis();
    	long systemTimeSinceLastUpdate =  now - this.lastUpdateSystemTime;
    	
    	// If at least 1/4 second passed....
    	if(systemTimeSinceLastUpdate < 250) { return; }
    		
		// Overwrite last reset time
		this.lastUpdateSystemTime = now;
		
		// Calculate simulation time...
		long simulationTimeSinceLastUpdate =  (long) Math.floor(systemTimeSinceLastUpdate * Configuration.getEntitySpeedFactor());
		
		List<SimulationTimer> copyList = new ArrayList<SimulationTimer>(this.timerList);
		for(SimulationTimer timer: copyList) {
			if(!timer.isCancelled()){
	        	// Reduct timer
				timer.reduceTime(simulationTimeSinceLastUpdate);
				
	    		// Check if timer finished
	    		if(timer.timeElapsed()) {
	    			// If so, execute:
	    			timer.run();
	    			
	    			if(timer.isPeriodic()) {
	    				// When timer is periodic, restart:
	    				timer.restart();
	    			}else {
	    				// Otherwise, cancel:
	    				timer.cancel();
	    			}
    			
	    		}
					
			}
    		
    	}

	}
	
	public void cancel() {
		for(SimulationTimer timer : this.timerList ) {
			timer.cancel();
		}
		this.timerList.clear();
	}
	
}