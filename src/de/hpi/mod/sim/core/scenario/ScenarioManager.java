package de.hpi.mod.sim.core.scenario;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.view.SimulatorFrame;

public class ScenarioManager {
	private List<Scenario> scenarios;
	private Scenario clear = new EmptyScenario();
	private Scenario currentScenario;
	private Map<String, List<TestScenario>> testGroups;
	private List<ITestScenarioListener> listeners = new ArrayList<>();

	private Queue<TestScenario> testsToRun = new LinkedList<>();
	private boolean runningAllTests = false;

	private boolean currentTestFailed = false;
	private boolean isRunningTest = false;

	private String failReason = "";

	private TestScenario activeTest = null;

	SimulatorFrame frame;
	private List<TestScenario> testList = new ArrayList<>();
	private World world;
	private TestResultDatabase testResults;

	public ScenarioManager(World world) {
		// Important References to World and to Primary Frame
		this.world = world;
		this.frame = world.getFrame();

		// Set up test result database and add it as a test listener.
		this.testResults = new TestResultDatabase(world.getInternalName());
		this.addTestScenarioListener(this.testResults);

		// Load Tests and Scenarios
		this.scenarios = world.getScenarios();
		this.testGroups = world.getTestGroups();
		initializeTestListAndLoadResults();
	}

	private void initializeTestListAndLoadResults() {
		for (String key : testGroups.keySet()) {
			for (TestScenario test : testGroups.get(key)) {
				this.testList.add(test);
				this.testResults.earmarkTest(test.getName());
			}
		}
		this.testResults.loadTestResultsFromFile();
	}

	private void runScenario(Scenario scenario, boolean isTest) {
		world.runScenario(scenario);

		if (isTest) {
			activeTest = (TestScenario) scenario;
			isRunningTest = true;
		} else {
			activeTest = null;
			isRunningTest = false;
		}
	}

	public void runTest(TestScenario test) {
		currentScenario = test;
		testsToRun.clear();
		runningAllTests = false;
		test.resetTest();
		frame.getTestListPanel().select(test);
		frame.getTimerPanel().startNewClock();
		frame.displayMessage("Starting test \"" + test.getName() + "\"");
		runScenario(test, true);
	}

	public void runScenario(Scenario scenario) {
		currentScenario = scenario;
		testsToRun.clear();
		runningAllTests = false;
		frame.getScenarioPanel().select(scenario);
		frame.getTimerPanel().startNewClock();
		frame.displayMessage("Starting scenario \"" + scenario.getName() + "\"");
		runScenario(scenario, false);
	}

	public void runAllTests() {
		testsToRun.clear();
		testsToRun.addAll(testList);
		runningAllTests = true;
		frame.displayMessage("Running all tests.");
		runNextTest();
	}

	private void runNextTest() {
		if (testsToRun.isEmpty()) {
			runningAllTests = false;
		} else {
			TestScenario nextTest = testsToRun.poll();
			nextTest.resetTest();
			frame.getTestListPanel().select(nextTest);
			currentScenario = nextTest;
			frame.getTimerPanel().startNewClock();
			runScenario(nextTest, true);
		}
	}

	public void addTestScenarioListener(ITestScenarioListener listener) {
		listeners.add(listener);
	}

	private void notifyTestScenarioListenersAboutPassedTest(TestScenario test) {
		for (ITestScenarioListener listener : listeners) {
			listener.markTestPassed(test);
		}
	}

	private void notifyTestScenarioListenersAboutFailedTest(TestScenario test) {
		for (ITestScenarioListener listener : listeners) {
			listener.markTestFailed(activeTest);
		}
	}

	private void notifyTestScenarioListenersAboutReset() {
		for (ITestScenarioListener listener : listeners) {
			listener.resetAllTests();
		}
	}

	public void clearScenario() {
		currentScenario = null;
		testsToRun.clear();
		runningAllTests = false;
		frame.clearSelections();
		frame.getTimerPanel().clearTimer();
		runScenario(clear, false);
		
		// Stop detectors
		world.deactivateDetectors();
	}

	public void restartScenario() {
		if (currentScenario != null) {
			frame.getTimerPanel().startNewClock();
			frame.displayMessage("Restarting scenario: \"" + currentScenario.getName() + "\"");
			runScenario(currentScenario, isRunningTest);

		}
	}

	public void refresh() {
		if (isRunningTest) { // Is a test running?

			if (currentTestFailed) { // Was I notified about a test failure?

				// Stop detectors
				world.deactivateDetectors();

				// Nofity UI
				this.notifyTestScenarioListenersAboutFailedTest(activeTest);
				activeTest.notifyFailToUser(frame, failReason);

				// Reset internal state
				currentTestFailed = false;
				isRunningTest = false;
				activeTest = null;

				// Optional: Run next test
				if (runningAllTests)
					runNextTest();

			} else if (activeTest.isPassed()) { // Has the success condition been met yet?
				
				// Stop detectors
				world.deactivateDetectors();

				// Nofity UI
				this.notifyTestScenarioListenersAboutPassedTest(activeTest);
				activeTest.notifySuccessToUser(frame);

				// Reset internal state
				isRunningTest = false;
				activeTest = null;

				// Optional: Run next test
				if (runningAllTests)
					runNextTest();
			}
		}
		this.world.updateDetectors();
	}

	public List<Scenario> getScenarios() {
		return scenarios;
	}

	public List<TestScenario> getTests() {
		return testList;
	}

	private class EmptyScenario extends Scenario {

		public EmptyScenario() {
			name = "Empty";
			resizable = true;
		}

		@Override
		public List<EntitySpecification<?>> getScenarioEntities() {
			return new ArrayList<>();
		}
	}

	public void failCurrentTest(String reason) {
		failReason = reason;
		currentTestFailed = true;
	}

	public boolean isRunningTest() {
		return isRunningTest;
	}

	public Map<String, List<TestScenario>> getTestGroups() {
		return testGroups;
	}

	public boolean isTestPassed(TestScenario test) {
		return this.testResults.getTestResult(test.getName()) == TestResultDatabase.Result.TEST_PASSED;
	}

	public int getNumberOfAvailableTests() {
		return this.testList.size();
		// return this.testResults.getNumberOfTests();
	}

	public int getNumberOfPassedTests() {
		return this.testResults.getNumberOfPassedTests();
	}

	public void resetTests() {
		this.testResults.resetTestResults();
		this.notifyTestScenarioListenersAboutReset();
	}
}
