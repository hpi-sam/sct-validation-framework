package de.hpi.mod.sim.worlds.flasher.entities;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.worlds.flasher.scenario.TestCaseExpectation;

public class LightBulbWithExpectation extends LightBulb {
	
	private List<MeasurementPoint> upcomingMeasurements = new ArrayList<MeasurementPoint>();
	private boolean failedTest = false;
	
	private double timer = 0;
	private MeasurementPoint nextMeasurement;

	public LightBulbWithExpectation(TestCaseExpectation expectation) {
		super();
		// Create Test Entries
		for(TestCaseExpectation.Entry entry : expectation.getExpectations()) {
			this.upcomingMeasurements.add(new MeasurementPoint(entry.getTime(), entry.isExpectedOn()));
		}
		// Start with first expected measurement
		this.nextMeasurement = this.getNextMeasurement();
	}
	
	private class MeasurementPoint{
		private double time;
		private boolean result;
		
		public MeasurementPoint(double time, boolean on) {
			this.time = time;
			this.result = on;
		}

		public double getTime() {
			return time;
		}

		public boolean isExpectedOn() {
			return result;
		}
	}

	private MeasurementPoint getNextMeasurement() {

		// Quit if list is empty
		if(this.upcomingMeasurements.isEmpty()) {
			return null;
		}
		
		// Take first element and remove from list
		MeasurementPoint nextTest = this.upcomingMeasurements.get(0);
		this.upcomingMeasurements.remove(0);
		return nextTest;

	}
	
	@Override
	public boolean hasPassedAllTestCriteria() {
		return !failedTest && this.upcomingMeasurements.isEmpty();
	}
	
	public boolean hasFailedTest() {
		return failedTest;
	}

	
	public void update(float delta) {

		// Decrement timer
		this.timer += delta;

		// Is timer reached?
		if(this.nextMeasurement != null && this.timer >= nextMeasurement.getTime()) {
			
			System.out.println("(" + this.timer + ") Expecting: "+nextMeasurement.isExpectedOn()+"  Is:"+this.isOn());
			
			// If yes, check if test is fulfilled...
			if(this.isOn() != nextMeasurement.isExpectedOn()) {
				this.failedTest = true;
			}
			
			// ...and get next test.
			this.nextMeasurement = this.getNextMeasurement();
			
		}
	}

	
	
}
