package de.hpi.mod.sim.worlds.flasher.entities;

import de.hpi.mod.sim.worlds.flasher.config.FlasherConfiguration;

public class FlashTask {

	private int timesToBlink;
	private double waitingTimeBeforeTask;
	private double waitingTimeDuringTask;

	public FlashTask(int timesToBlink, double waitingTimeBeforeTask, double waitingTimeDuringTask) {
		super();
		this.timesToBlink = timesToBlink;
		this.waitingTimeBeforeTask = waitingTimeBeforeTask;
		this.waitingTimeDuringTask = waitingTimeDuringTask;
	}

	public FlashTask(int timesToBlink, double waitingTime) {
		this(timesToBlink, FlasherConfiguration.getWaitingTimeBeforeTask(), waitingTime);
	}

	public int getNumberOfFlashes() {
		return timesToBlink;
	}
	
	public double getPreTaskWaitingTime() {
		return waitingTimeBeforeTask;
	}
	
	public double getTaskTime() {
		return waitingTimeDuringTask;
	}
	
	@Override
	public String toString() {
		return Integer.toString(timesToBlink);
	}
	
}
