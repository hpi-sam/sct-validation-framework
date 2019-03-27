package de.hpi.mod.sim.env;

import de.hpi.mod.sim.ITimerCallback;
import de.hpi.mod.sim.TimerService;

public class DynamicTimerService extends TimerService {
	
	public void setTimer(final ITimerCallback callback, final int eventID,
			long time, boolean isPeriodic) {
		long dynamicTime = (long) (time / SimulatorConfig.getRobotSpeedFactor());
		super.setTimer(callback, eventID, dynamicTime, isPeriodic);
	}
	
}
