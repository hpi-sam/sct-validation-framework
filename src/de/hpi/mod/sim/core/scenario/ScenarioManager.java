package de.hpi.mod.sim.core.scenario;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import de.hpi.mod.sim.core.model.Entity;
import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.view.SimulatorFrame;

public class ScenarioManager {
	private List<Scenario> scenarios;
	private Scenario clear = new EmptyScenario();
	private Scenario currentScenario;
	private Map<String, List<TestScenario>> testGroups;
	private List<ITestListener> listeners = new ArrayList<>();

	private Queue<TestScenario> testsToRun = new LinkedList<>();
	private boolean runningAllTests = false;

	private boolean currentTestFailed = false;
	private boolean isRunningTest = false;

	private String failReason = "";

	private TestScenario activeTest = null;

	SimulatorFrame frame;
	private List<TestScenario> tests = new ArrayList<>();
	private Setting setting;

	public ScenarioManager(Setting setting) {
		this.setting = setting;
		this.frame = setting.getFrame();
		scenarios = setting.getScenarios();
		testGroups = setting.getTestGroups();
		initializeTestList();
	}

	private void initializeTestList() {
		for (String key : testGroups.keySet()) {
			for (TestScenario test : testGroups.get(key)) {
				tests.add(test);
			}
		}
	}

	private void updateDetectors() {
		List<? extends Entity> entities = setting.getEntities();
		for (Detector detector : setting.getDetectors())
			detector.update(entities);
	}

    private void runScenario(Scenario scenario, boolean isTest) {
    	setting.runScenario(scenario);
        
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
    	frame.displayMessage("Starting scenario: \"" + scenario.getName() + "\"");
    	runScenario(scenario, false);
    }
    
    public void runAllTests() {
    	testsToRun.clear();
    	testsToRun.addAll(tests);
    	runningAllTests = true;
    	frame.displayMessage("Running all tests.");
    	runNextTest();
    }

    private void runNextTest() {
    	if(testsToRun.isEmpty()) {
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

	public void addTestListener(ITestListener listener) {
        listeners.add(listener);
    }
	
	public void clearScenario() {
		currentScenario = null;
		testsToRun.clear();
    	runningAllTests = false;
    	frame.clearSelections();
    	frame.getTimerPanel().clearTimer();
    	runScenario(clear, false);
	}
	
	public void restartScenario() {
		if(currentScenario != null) {
			frame.getTimerPanel().startNewClock();
	    	frame.displayMessage("Restarting scenario: \"" + currentScenario.getName() + "\"");
			runScenario(currentScenario, isRunningTest);
			
		}
	}

    public void refresh() {
		if (isRunningTest) {
			if (currentTestFailed) {
				setting.resetDetectors(); //TODO: originally just deadlockDetector.deactivate(); Does it work?
				for (ITestListener listener : listeners) {
					listener.failTest(activeTest);
				}
				activeTest.notifyFailToUser(frame, failReason);
				currentTestFailed = false;
				isRunningTest = false;
				activeTest = null;
				if (runningAllTests)
					runNextTest();
			} else if (activeTest.isPassed()) {
				setting.resetDetectors(); // TODO: originally just deadlockDetector.deactivate(); Does it work?
				for (ITestListener listener : listeners) {
					listener.onTestCompleted(activeTest);
				}
				activeTest.notifySuccessToUser(frame);
				isRunningTest = false;
				activeTest = null;
				if (runningAllTests)
					runNextTest();
			}
		}
		updateDetectors();
    }
    
    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public List<TestScenario> getTests() {
        return tests;
    }
    
    private class EmptyScenario extends Scenario {

        public EmptyScenario() {
            name = "Empty";
            resizable = true;
        }

        @Override
        public List<EntityDescription<?>> initializeScenario() {
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


	public Map<String,List<TestScenario>> getTestGroups() {
		return testGroups;
	}
}
