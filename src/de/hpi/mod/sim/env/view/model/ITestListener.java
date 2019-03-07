package de.hpi.mod.sim.env.view.model;

public interface ITestListener {
    void onTestCompleted(TestScenario test);
    void failTest(TestScenario test);
}
