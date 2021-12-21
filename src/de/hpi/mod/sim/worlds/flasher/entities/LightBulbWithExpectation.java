package de.hpi.mod.sim.worlds.flasher.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hpi.mod.sim.worlds.flasher.scenario.TestCaseExpectation;

public class LightBulbWithExpectation extends LightBulb {
	
	private List<LightBulbObservation> upcomingObservations = new ArrayList<LightBulbObservation>();
	private List<LightBulbObservation> activeExpectation = new ArrayList<LightBulbObservation>();
	
	private boolean failedTest = false;
	
	private double timer = 0;
	private LightBulbObservation nextMeasurement;

	public LightBulbWithExpectation(TestCaseExpectation testExpectedation) {
		super();
		
		// Create local Observations to be made
		for(TestCaseExpectation.Expectation expectation : testExpectedation.getExpectations()) {
			List<Boolean> expectedResults = expectation.getExpectedValueList();
			if(!expectedResults.isEmpty())
				this.upcomingObservations.add(new LightBulbObservation(expectation.getStartTime(), expectation.getEndTime(), expectedResults));
		}
	}
	
	private class LightBulbObservation{
		private double startTime;
		private double endTime;
		private List<Boolean> expectedValues;
		private List<Boolean> observedValues;
		
		public LightBulbObservation(double startTime, double endTime, List<Boolean> results) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.expectedValues = results;
			this.observedValues = new ArrayList<>();
		}
		
		public void addObservation(boolean newValue) {
			if(this.observedValues.isEmpty() || this.observedValues.get(this.observedValues.size() - 1) != newValue)
				this.observedValues.add(newValue);
		}
		
		public boolean isIntermediateObservartionValid() {
			return this.expectedValues.get(0) == this.observedValues.get(0) && this.observedValues.size() <= this.expectedValues.size();
		}
		
		public boolean isObservartionValidAndComplete() {
			return this.observedValues.equals(this.expectedValues);
		}

		public double getStartTime() {
			return this.startTime;
		}

		public double getEndTime() {
			return this.endTime;
		}
	}
	
	@Override
	public boolean hasPassedAllTestCriteria() {
		return !failedTest && this.upcomingObservations.isEmpty() && this.activeExpectation.isEmpty();
	}
	
	public boolean hasFailedTest() {
		return failedTest;
	}

	
	public void update(float delta) {

		// Increment local timer
		this.timer += delta;
		
		// REMOVE active observations if end time was passed and perform FINAL CHECK
		//     (by having the remove before the adding and checking of new observations, we ensure that single value expectations are checked once.)
		Iterator<LightBulbObservation> activeObservationsIterator = this.activeExpectation.iterator();
		while (activeObservationsIterator.hasNext()) {
			LightBulbObservation observation = activeObservationsIterator.next();
			if(timer > observation.getEndTime()){
				activeObservationsIterator.remove();
				if(!observation.isObservartionValidAndComplete())
					this.failedTest = true;
			}
		}

		// ADD active observations (from upcoming list) if start time is reached
		Iterator<LightBulbObservation> upcomingObservationsIterator = this.upcomingObservations.iterator();
		while (upcomingObservationsIterator.hasNext()) {
			LightBulbObservation observation = upcomingObservationsIterator.next();
			if(timer >= observation.getStartTime()) {
				upcomingObservationsIterator.remove();
				this.activeExpectation.add(observation);				
			}
		}
		
		// APPEND current value to active  and perform INTERMEDIATE CHECK
		for(LightBulbObservation observartion: this.activeExpectation) {
			observartion.addObservation(this.isOn());
			if(!observartion.isIntermediateObservartionValid())
				this.failedTest = true;
		}
		
	}

	
	
}
