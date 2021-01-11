package de.hpi.mod.sim.worlds.flasher.entities;

public class FlashTask {

	private int timesToBlink;
	private double waitingTime;

	public FlashTask(int timesToBlink, double waitingTime) {
		super();
		this.timesToBlink = timesToBlink;
		this.waitingTime = waitingTime;
	}

	public int getNumberOfFlashes() {
		return timesToBlink;
	}
	
	public double getWaitingTime() {
		return waitingTime;
	}
	
}
