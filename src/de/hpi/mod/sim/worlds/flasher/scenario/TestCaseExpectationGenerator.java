package de.hpi.mod.sim.worlds.flasher.scenario;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.scenario.TestScenario;

public class TestCaseExpectationGenerator {
	
	double startTime;
	double endTime;
	double timeDelta;
	boolean repeat;
	boolean expectedOn;
	
	public TestCaseExpectationGenerator(double time, boolean expectedLightBulbOn) {
		this.startTime = time;
		this.endTime = time;
		this.expectedOn = expectedLightBulbOn;
	}
	
	public TestCaseExpectationGenerator(double startTime, double endTime, double invervalLength, boolean expectedLightBulbOn) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.timeDelta = invervalLength;
		this.expectedOn = expectedLightBulbOn;
	}
	
	public List<TestCaseExpectationEntry> generateEntries(){

        List<TestCaseExpectationEntry> expectationEntries = new ArrayList<>();
        
        if(this.endTime > this.startTime) {
        	double time = this.startTime;
	        while(time < this.endTime) {
	        	expectationEntries.add(new TestCaseExpectationEntry(time, this.expectedOn));
	        	time += this.timeDelta;
	        }        
        }else {
        	expectationEntries.add(new TestCaseExpectationEntry(startTime, this.expectedOn));
        }
        
		return expectationEntries;
		
	}
	
}
