package de.hpi.mod.sim.worlds.flasher.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TestCaseExpectation {

	private List<Entry> expectations;

	public TestCaseExpectation(List<Entry> expectations) {
		this.expectations = expectations;
		this.sortExpectations();
	}
	

	public TestCaseExpectation(TestCaseExpectation.Generator... generators) {
        List<Entry> expectationEntries = new ArrayList<>();        
        for(TestCaseExpectation.Generator generator:generators) {
        	expectationEntries.addAll(generator.generateEntries());
        }                
	}
	
	private void sortExpectations() {
		this.expectations.sort(new Comparator<Entry>() {
			@Override
			public int compare(Entry lhs, Entry rhs) {
		        return lhs.getTime() > rhs.getTime() ? -1 : (lhs.getTime() < rhs.getTime()) ? 1 : 0;
			}
		});
	}
	
	public List<Entry> getExpectations() {
        this.sortExpectations();
		return this.expectations;
	}
	
	
	public static class Entry{
	
		private double time;
		private boolean expectedOn;
	
		public Entry(double time, boolean expectedLightBulbOn) {
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
	
	public static class Generator {
		
		double startTime;
		double endTime;
		double timeDelta;
		boolean repeat;
		boolean expectedOn;
		
		public Generator(double time, boolean expectedLightBulbOn) {
			this.startTime = time;
			this.endTime = time;
			this.expectedOn = expectedLightBulbOn;
		}
		
		public Generator(double startTime, double endTime, double invervalLength, boolean expectedLightBulbOn) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.timeDelta = invervalLength;
			this.expectedOn = expectedLightBulbOn;
		}

		public List<Entry> generateEntries(){
	        List<Entry> expectationEntries = new ArrayList<>();
	        if(this.endTime > this.startTime) {
	        	double time = this.startTime;
		        while(time < this.endTime) {
		        	expectationEntries.add(new Entry(time, this.expectedOn));
		        	time += this.timeDelta;
		        }        
	        }else {
	        	expectationEntries.add(new Entry(startTime, this.expectedOn));
	        }	        
			return expectationEntries;			
		}		
	}

}