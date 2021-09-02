package de.hpi.mod.sim.worlds.flasher.scenario;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.scenario.TestScenario;

public class TestCaseExpectationGenerator {
	
	double startTime;
	double endTime;
	double timeDelta;
	boolean expectedOn;
	
	public TestCaseExpectationGenerator(double startTime, double endTime, double invervalLength, boolean expectedLightBulbOn) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.timeDelta = invervalLength;
		this.expectedOn = expectedLightBulbOn;
	}
	
	public List<TestCaseExpectationEntry> generateEntries(){

        List<TestCaseExpectationEntry> expectationEntries = new ArrayList<>();
        
        double time = this.startTime;
        while(time < this.endTime) {
        	expectationEntries.add(new TestCaseExpectationEntry(time, this.expectedOn));
        	time += this.timeDelta;
        }        
        
		return expectationEntries;
		
	}
	
}
