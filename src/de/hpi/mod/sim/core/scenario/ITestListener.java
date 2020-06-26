package de.hpi.mod.sim.core.scenario;

public interface ITestListener {
    void onTestCompleted(TestScenario test);
    void failTest(TestScenario test);
}
