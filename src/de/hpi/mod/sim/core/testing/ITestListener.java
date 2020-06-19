package de.hpi.mod.sim.core.testing;

import de.hpi.mod.sim.core.testing.tests.TestScenario;

public interface ITestListener {
    void onTestCompleted(TestScenario test);
    void failTest(TestScenario test);
}
