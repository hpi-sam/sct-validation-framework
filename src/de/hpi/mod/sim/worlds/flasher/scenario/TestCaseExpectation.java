package de.hpi.mod.sim.worlds.flasher.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TestCaseExpectation {

	private List<Expectation> expectations;
	

	public TestCaseExpectation(TestCaseExpectation.Expectation... expectations) {
		this.expectations = Arrays.asList(expectations);    
		this.sortExpectations();            
	}
	
	private void sortExpectations() {
		if(this.expectations != null && !this.expectations.isEmpty()) {
			this.expectations.sort(new Comparator<Expectation>() {
				@Override
				public int compare(Expectation lhs, Expectation rhs) {
			        return lhs.getStartTime() < rhs.getStartTime() ? -1 : (lhs.getStartTime() > rhs.getStartTime()) ? 1 : 0;
				}
			});
		}
	} 
	
	public List<Expectation> getExpectations() {
		return this.expectations;
	}
	
	public static abstract class Expectation {
		
		private double startTime;
		private double endTime;
		
		public Expectation(double startTime, double endTime) {
			this.startTime = startTime;
			this.endTime = endTime;
		}
		
		public double getStartTime() {
			return this.startTime;
		}
		
		public double getEndTime() {
			return this.endTime;
		}
		
		public abstract List<Boolean> getExpectedValueList();
	}

	public static class SingleValueExpectation extends Expectation {
		
		boolean expectedOn;
		
		public SingleValueExpectation(double time, boolean expectedLightBulbOn) {
			super(time, time);
			this.expectedOn = expectedLightBulbOn;
		}

		@Override
		public List<Boolean> getExpectedValueList() {
			return new ArrayList<>(Arrays.asList(expectedOn));
		}
		
	}

	public static class IntervalExpectation extends Expectation {
		
		boolean expectedOn;
		
		public IntervalExpectation(double startTime, double endTime, boolean expectedLightBulbOn) {
			super(startTime, endTime);
			this.expectedOn = expectedLightBulbOn;
		}

		@Override
		public List<Boolean> getExpectedValueList() {
			return new ArrayList<>(Arrays.asList(expectedOn));
		}
		
	}

	public static class SequenceExpectation extends Expectation {
		
		boolean[] expectedOnSequence;
		
		public SequenceExpectation(double startTime, double endTime, boolean...expectedValues) {
			super(startTime, endTime);
			this.expectedOnSequence = expectedValues;
		}

		@Override
		public List<Boolean> getExpectedValueList() {
			List<Boolean> result = new ArrayList<>();
			
			// Return all values as flattened ArrayList (flattened = no same values behind each other!) 
			for(boolean onValue : expectedOnSequence)
				if(result.isEmpty()|| result.get(result.size() - 1) != onValue)
					result.add(onValue);
			return result;
		}
		
	}

}