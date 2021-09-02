package de.hpi.mod.sim.worlds.flasher.scenario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestCaseExpectation {

	private List<TestCaseExpectationEntry> expectations;

	public TestCaseExpectation(List<TestCaseExpectationEntry> expectations) {
		this.expectations = expectations;
		this.expectations.sort(new Comparator<TestCaseExpectationEntry>() {
			@Override
			public int compare(TestCaseExpectationEntry lhs, TestCaseExpectationEntry rhs) {
		        return lhs.getTime() > rhs.getTime() ? -1 : (lhs.getTime() < rhs.getTime()) ? 1 : 0;
			}
		});
	}
	
	public static TestCaseExpectation createExpectationFromGenerators(List<TestCaseExpectationGenerator> generators) {
        List<TestCaseExpectationEntry> expectationEntries = new ArrayList<>();        
        for(TestCaseExpectationGenerator generator:generators) {
        	expectationEntries.addAll(generator.generateEntries());
        }                
		return new TestCaseExpectation(expectationEntries);
	}
	
	public List<TestCaseExpectationEntry> getExpectations() {
		return this.expectations;
	}

}