package de.hpi.mod.sim.worlds.flasher.entities;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.worlds.flasher.scenario.TestCaseExpectation;

public class LightBulbWithExpectation extends LightBulb {
	
	private List<TestResult> upcomingTests = new ArrayList<TestResult>();
	private boolean failedTest = false;
	
	private double timer = 0;
	private TestResult nextTest;

	public LightBulbWithExpectation(TestCaseExpectation expectation) {
		super();
		// Create Test Entries
		for(TestCaseExpectation.Entry entry : expectation.getExpectations()) {
			upcomingTests.add(new TestResult(entry.getTime(), entry.isExpectedOn()));
		}
	}
	
	private class TestResult{
		private double time;
		private boolean result;
		
		public TestResult(double time, boolean on) {
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

	private TestResult getNextTest() {

		// Quit if list ist empty
		if(this.upcomingTests.isEmpty()) {
			return null;
		}
		
		// Take first element and remove from list
		TestResult nextTest = this.upcomingTests.get(0);
		this.upcomingTests.remove(0);
		return nextTest;

	}
	
	@Override
	public boolean hasPassedAllTestCriteria() {
		return !failedTest && this.upcomingTests.isEmpty();
	}
	
	public boolean hasFailedTest() {
		return failedTest;
	}

	
	public void update(float delta) {

		// Decrement timer
		this.timer += delta;

		// Is timer reached?
		if(this.nextTest != null && this.timer >= nextTest.getTime()) {
			
			System.out.println(this.timer + "TEST EVENT " + this.upcomingTests.size());
			
			// If yes, check if test is fullfilled...
			if(this.isOn() != nextTest.isExpectedOn()) {
				this.failedTest = true;
			}
			
			// ...and get next test.
			this.nextTest = this.getNextTest();
			
		}
	}

	
	
}
