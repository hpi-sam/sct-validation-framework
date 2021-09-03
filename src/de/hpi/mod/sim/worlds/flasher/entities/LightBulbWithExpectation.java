package de.hpi.mod.sim.worlds.flasher.entities;

import de.hpi.mod.sim.worlds.flasher.scenario.TestCaseExpectation;

public class LightBulbWithExpectation extends LightBulb {
	
	private TestCaseExpectation expectation = null;

	public LightBulbWithExpectation(TestCaseExpectation expectation) {
		super();
		this.expectation = expectation;
	}
}
