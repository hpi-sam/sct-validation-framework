package de.hpi.mod.sim.env.testing;

import de.hpi.mod.sim.env.testing.tests.TestScenario;

public interface ITestListener {
    void onTestCompleted(TestScenario test);
    void failTest(TestScenario test);
}
