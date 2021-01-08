package de.hpi.mod.sim.core.scenario;

public interface ITestScenarioListener {
    void markTestPassed(TestScenario test);
    void markTestFailed(TestScenario test);
    void resetAllTests();
}
