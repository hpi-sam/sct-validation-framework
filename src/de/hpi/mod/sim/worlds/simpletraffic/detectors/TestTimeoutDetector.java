package de.hpi.mod.sim.worlds.simpletraffic.detectors;

import java.util.List;
import java.util.stream.Collectors;

import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.detectors.RobotDetector;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorld;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;

public class TestTimeoutDetector extends Detector {

	private long timeSinceTestStarted = 0;
	private long lastUpdateSystemTime = System.currentTimeMillis();
	
	public TestTimeoutDetector(SimpleTrafficWorld world) {
		super(world, true, false);
	}

    @Override
    public void update(List<? extends Entity> entities) {
		if(!isEnabled())
			return;

		updateTimer();
		
		if(timerFinished()) {
			reportDetectedProblem("test did not finished within 30 seconds.");
		}
			
    }

	private void updateTimer() {
			
		// Calculate delta
	    float systemTimeDelta = System.currentTimeMillis() - this.lastUpdateSystemTime;
	    this.lastUpdateSystemTime = System.currentTimeMillis();
	    float simulationTimeDelta =  (float) Math.floor(systemTimeDelta * SimpleTrafficWorldConfiguration.getEntitySpeedFactor());

	    // Update Wait Time
	    this.timeSinceTestStarted += simulationTimeDelta;
	}

	private boolean timerFinished() {
		return timeSinceTestStarted >= 30000;
	}
	
	public void reset() {
		this.timeSinceTestStarted = 0;
		this.lastUpdateSystemTime = System.currentTimeMillis();
	};


}
