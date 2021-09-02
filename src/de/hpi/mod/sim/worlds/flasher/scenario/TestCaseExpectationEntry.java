package de.hpi.mod.sim.worlds.flasher.scenario;


public class TestCaseExpectationEntry{

	private double time;
	private boolean expectedOn;

	public TestCaseExpectationEntry(double time, boolean expectedLightBulbOn) {
		this.time = time;
		this.expectedOn = expectedLightBulbOn;
	}

	public boolean isExpectedOn() {
		return expectedOn;
	}

	public double getTime() {
		return time;
	}


}