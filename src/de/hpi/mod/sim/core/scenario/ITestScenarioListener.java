package de.hpi.mod.sim.core.scenario;

public interface ITestScenarioListener {
    void onTestCompleted(TestScenario test);
    void failTest(TestScenario test);
}
